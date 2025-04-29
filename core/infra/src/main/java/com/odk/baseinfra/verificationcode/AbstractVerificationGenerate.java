package com.odk.baseinfra.verificationcode;

import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.baseutil.context.ServiceContextHolder;
import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.entity.VerificationCodeEntity;
import com.odk.redisspringbootstarter.DistributedLockService;
import com.odk.redisspringbootstarter.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;
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

    /**
     * 过期时间：秒
     */
    private static final int EXPIRE_TIME = 3 * 60;

    private static final int MAX_VERIFY_TIMES = 3;

    private RedisUtil redisUtil;

    private DistributedLockService lockService;

    @Override
    public VerificationCodeEntity generateVerificationCode(VerificationCodeDTO verificationCodeDTO) {
        String key = generateKey(verificationCodeDTO);
        VerificationCodeEntity entity = (VerificationCodeEntity) redisUtil.get(key);
        AssertUtil.isNull(entity, BizErrorCode.VERIFY_CODE_EXISTED, "验证码已发送，请稍后重试。");

        String lockKey = "lock_" + key;
        //这里需要防止并发调用
        boolean acquired = lockService.tryLock(lockKey, 2, 10);
        if (acquired) {
            try {
                entity = (VerificationCodeEntity) redisUtil.get(key);
                AssertUtil.isNull(entity, BizErrorCode.VERIFY_CODE_EXISTED, "验证码已发送，请稍后重试。");
                VerificationCodeEntity verificationCodeEntity = generateVerificationCodeEntity();
                if (redisUtil.setIfAbsent(key, verificationCodeEntity, EXPIRE_TIME, TimeUnit.SECONDS)) {
                    return verificationCodeEntity;
                }
            } finally {
                lockService.unlock(lockKey);
            }
        } else {
            log.error("获取锁失败 {} [thread: {}]", lockKey, Thread.currentThread().getName());
            throw new BizException(BizErrorCode.CONCURRENT_REQUEST);
        }
        return null;
    }


    @Override
    public boolean checkVerificationCode(VerificationCodeDTO verificationCodeDTO) {
        String key = generateKey(verificationCodeDTO);
        VerificationCodeEntity entity = (VerificationCodeEntity) redisUtil.get(key);
        AssertUtil.notNull(entity, BizErrorCode.VERIFY_CODE_EXPIRED);
        if (StringUtils.equals(verificationCodeDTO.getVerifyCode(), entity.getCode())) {
            //验证成功
            redisUtil.delete(key);
            return true;
        }
        //验证失败
        entity.setVerifyTimes(entity.getVerifyTimes() + 1);
        if (entity.getVerifyTimes() >= MAX_VERIFY_TIMES) {
            //超过最大验证次数
            redisUtil.delete(key);
            throw new BizException(BizErrorCode.VERIFY_CODE_MAX_TIMES, "验证码已超过最大次数，请重新获取。");
        }
        //计算剩余时间
        int leftSeconds = leftSeconds(entity.getCreateTime());
        redisUtil.set(key, entity, leftSeconds, TimeUnit.SECONDS);
        ServiceContextHolder.setServiceContext(entity);
        return false;
    }

    /**
     * 生成验证码
     *
     * @return
     */
    abstract protected String generateVerificationCode();

    /**
     * 生成缓存 key
     *
     * @param verificationCodeDTO
     * @return
     */
    private String generateKey(VerificationCodeDTO verificationCodeDTO) {
        return verificationCodeDTO.getVerifyScene() + "_" + verificationCodeDTO.getVerifyType() + "_" + verificationCodeDTO.getVerifyKey();
    }

    /**
     * 生成验证码对象
     *
     * @return
     */
    private VerificationCodeEntity generateVerificationCodeEntity() {
        String code = generateVerificationCode();
        VerificationCodeEntity verificationCodeEntity = new VerificationCodeEntity();
        verificationCodeEntity.setCode(code);
        verificationCodeEntity.setVerifyTimes(0);
        verificationCodeEntity.setExpireTime(EXPIRE_TIME);
        verificationCodeEntity.setCreateTime(LocalDateTime.now());
        return verificationCodeEntity;
    }

    /**
     * 计算验证码剩余过期时间
     *
     * @param startTime
     * @return
     */
    private int leftSeconds(LocalDateTime startTime) {
        LocalDateTime endTime = startTime.plusSeconds(EXPIRE_TIME);
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
