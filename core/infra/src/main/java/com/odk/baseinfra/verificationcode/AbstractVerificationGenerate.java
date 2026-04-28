package com.odk.baseinfra.verificationcode;

import com.odk.base.context.TenantIdContext;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.JacksonUtil;
import com.odk.baseutil.context.ServiceContextHolder;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.baseutil.enums.VerifySceneEnum;
import com.odk.redisspringbootstarter.DistributedLockService;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AbstractVerificationGenerate
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Slf4j
public abstract class AbstractVerificationGenerate implements IVerificationGenerate {

    private RedisUtil redisUtil;

    private DistributedLockService lockService;

    private static final String VERIFY_CODE_KEY_PREFIX = "sms:";

    @Override
    public VerificationCodeEntity generate(VerificationCodeDTO verificationCodeDTO) {
        VerifySceneEnum verifyScene = verificationCodeDTO.getVerifyScene();
        String key = generateKey(verificationCodeDTO);
        VerificationCodeEntity entity = (VerificationCodeEntity) redisUtil.get(key);
        AssertUtil.isNull(entity, BizErrorCode.VERIFY_CODE_EXISTED);

        String lockKey = key + "_lock";
        //这里需要防止并发调用
        boolean acquired = lockService.tryLock(lockKey, 1, 10);
        if (acquired) {
            try {
                entity = (VerificationCodeEntity) redisUtil.get(key);
                AssertUtil.isNull(entity, BizErrorCode.VERIFY_CODE_EXISTED);
                //判断24小时内，验证码发送次数是否达到上限
                String maxVerifyTimesKey = key + "_max_verify_times";
                if (redisUtil.exists(maxVerifyTimesKey)) {
                    int maxVerifyTimes = (int) redisUtil.get(maxVerifyTimesKey);
                    if (maxVerifyTimes > verifyScene.getMaxSendPerDay()) {
                        throw new BizException(BizErrorCode.VERIFY_CODE_SEND_MAX_TIMES);
                    }
                    redisUtil.incrBy(maxVerifyTimesKey, 1);
                } else {
                    redisUtil.set(maxVerifyTimesKey, 1, 12, TimeUnit.HOURS);

                }
                VerificationCodeEntity verificationCodeEntity = generateVerificationCodeEntity(verifyScene, verificationCodeDTO.getVerifyKey());
                if (redisUtil.setIfAbsent(key, verificationCodeEntity, verifyScene.getExpireTime(), TimeUnit.SECONDS)) {
                    log.info("验证码生成成功：key:{}, value:{}", key, JacksonUtil.toJsonString(entity));
                    verificationCodeEntity.setCode(null);
                    return verificationCodeEntity;
                }
            } finally {
                lockService.unlock(lockKey);
            }
        } else {
            log.error("生成验证码获取锁失败 {} [thread: {}]", lockKey, Thread.currentThread().getName());
            throw new BizException(BizErrorCode.CONCURRENT_REQUEST);
        }
        return null;
    }


    @Override
    public boolean compareUniqueId(VerificationCodeDTO verificationCodeDTO) {
        VerificationCodeEntity entity = getCodeOrThrow(generateKey(verificationCodeDTO));
        AssertUtil.equal(entity.getUniqueId(), verificationCodeDTO.getUniqueId(), BizErrorCode.VERIFY_CODE_UNIQUE_ERROR);
        return true;
    }

    @Override
    public boolean compareAndIncr(VerificationCodeDTO verificationCodeDTO) {
        String key = generateKey(verificationCodeDTO);
        VerificationCodeEntity entity = getCodeOrThrow(key);
        AssertUtil.equal(entity.getUniqueId(), verificationCodeDTO.getUniqueId(), BizErrorCode.VERIFY_CODE_UNIQUE_ERROR, "验证码唯一键不匹配");
        if (StringUtils.equals(verificationCodeDTO.getVerifyCode(), entity.getCode())) {
            //验证成功
            log.info("验证码验证成功：key:{}, value:{}", key, JacksonUtil.toJsonString(entity));
            redisUtil.delete(key);
            return true;
        }
        //验证失败
        entity.setVerifyTimes(entity.getVerifyTimes() + 1);
        if (entity.getVerifyTimes() >= verificationCodeDTO.getVerifyScene().getMaxVerifyTimes()) {
            //超过最大验证次数
            redisUtil.delete(key);
            throw new BizException(BizErrorCode.VERIFY_CODE_COMPARE_MAX_TIMES);
        }
        //计算剩余时间
        int leftSeconds = leftSeconds(entity.getCreateTime(), verificationCodeDTO.getVerifyScene());
        redisUtil.set(key, entity, leftSeconds, TimeUnit.SECONDS);
        entity.setCode(null);
        ServiceContextHolder.setErrorContext(entity);
        throw new BizException(BizErrorCode.VERIFY_CODE_UNMATCHED);
    }

    /**
     * 生成验证码
     *
     * @return
     */
    abstract protected String generateVerificationCode(String phoneNumber);


    /**
     * 从缓存中获取验证码
     *
     * @param key
     * @return
     */
    private VerificationCodeEntity getCodeOrThrow(String key) {

        VerificationCodeEntity entity = (VerificationCodeEntity) redisUtil.get(key);
        AssertUtil.notNull(entity, BizErrorCode.VERIFY_CODE_NOT_EXIST);
        return entity;
    }

    /**
     * 生成缓存 key
     *
     * @param verificationCodeDTO
     * @return
     */
    private String generateKey(VerificationCodeDTO verificationCodeDTO) {
        return VERIFY_CODE_KEY_PREFIX + verificationCodeDTO.getVerifyScene().getCode() + "_" + verificationCodeDTO.getVerifyType() + "_" + verificationCodeDTO.getVerifyKey() + "_" + TenantIdContext.getTenantId();
    }

    /**
     * 生成验证码对象
     *
     * @return
     */
    private VerificationCodeEntity generateVerificationCodeEntity(VerifySceneEnum verifyScene, String phoneNumber) {
        String code = generateVerificationCode(phoneNumber);
        VerificationCodeEntity verificationCodeEntity = new VerificationCodeEntity();
        verificationCodeEntity.setCode(code);
        verificationCodeEntity.setVerifyTimes(0);
        verificationCodeEntity.setMaxVerifyTimes(verifyScene.getMaxVerifyTimes());
        verificationCodeEntity.setExpireTime(verifyScene.getExpireTime());
        verificationCodeEntity.setCreateTime(LocalDateTime.now());
        verificationCodeEntity.setUniqueId(UUID.randomUUID().toString());
        return verificationCodeEntity;
    }

    /**
     * 计算验证码剩余过期时间
     *
     * @param startTime
     * @return
     */
    private int leftSeconds(LocalDateTime startTime, VerifySceneEnum verifySceneEnum) {
        LocalDateTime endTime = startTime.plusSeconds(verifySceneEnum.getExpireTime());
        LocalDateTime now = LocalDateTime.now();
        // 计算剩余秒数（如果已过期可能返回负数）
        long remainingSeconds = Duration.between(now, endTime).getSeconds();

        // 实际业务中通常返回非负数
        return (int) Math.max(remainingSeconds, 0);

    }

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setLockService(DistributedLockService lockService) {
        this.lockService = lockService;
    }
}
