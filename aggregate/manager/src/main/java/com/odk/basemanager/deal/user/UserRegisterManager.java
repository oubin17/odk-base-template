package com.odk.basemanager.deal.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.util.JacksonUtil;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.model.user.UserIdentificationDO;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.baseinfra.security.IEncryption;
import com.odk.basemanager.api.user.IUserRegisterManager;
import com.odk.baseutil.dto.user.UserRegisterDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

/**
 * UserRegisterManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserRegisterManager implements IUserRegisterManager {

    private IEncryption encryptionService;

    private TransactionTemplate transactionTemplate;

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private UserIdentificationRepository identificationRepository;

    private UserProfileRepository userProfileRepository;


    /**
     * 这里校验密码是否通过公钥加密
     *
     * @param userRegisterDTO
     * @return
     */
    @Override
    public String registerUser(UserRegisterDTO userRegisterDTO) {
        String password = userRegisterDTO.getIdentifyValue();
        String decrypt = encryptionService.rsaDecode(password);
        userRegisterDTO.setIdentifyValue(encryptionService.bcryptEncode(decrypt));

        UserAccessTokenDO byTokenTypeAndTokenValue = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(userRegisterDTO.getLoginType(), userRegisterDTO.getLoginId(), TenantIdContext.getTenantId());
        AssertUtil.isNull(byTokenTypeAndTokenValue, BizErrorCode.USER_HAS_EXISTED, "用户已经存在，类型：" + userRegisterDTO.getLoginType() + "，登录ID：" + userRegisterDTO.getLoginId());
        String userId;
        try {
            userId = transactionTemplate.execute(status -> {
                //1.生成用户id
                String userId1 = addUserBase();
                //2.添加登录信息
                addAccessToken(userId1, userRegisterDTO);
                //3.添加密码信息 密码加密
                addIdentification(userId1, userRegisterDTO);
                //4.添加用户画像
                addUserProfile(userId1, userRegisterDTO.getUserName());
                return userId1;
            });
        } catch (DataIntegrityViolationException exception) {
            log.error("注册发生唯一键冲突：{}, 异常原因：", JacksonUtil.toJsonString(userRegisterDTO), exception);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (DuplicateKeyException duplicateKeyException) {
            log.error("注册发生主键冲突：{}, 异常原因：", JacksonUtil.toJsonString(userRegisterDTO), duplicateKeyException);
            throw new BizException(BizErrorCode.LOGIN_ID_DUPLICATE);
        } catch (BizException bizException) {
            throw bizException;
        } catch (Exception e) {
            log.error("注册发生未知异常，注册信息：{}, 异常原因：", JacksonUtil.toJsonString(userRegisterDTO), e);
            throw new BizException(BizErrorCode.SYSTEM_ERROR);
        }

        return userId;

    }

    /**
     * 添加基础信息
     */
    private String addUserBase() {
        UserBaseDO userBase = new UserBaseDO();
        userBase.setUserType(UserTypeEnum.INDIVIDUAL.getCode());
        userBase.setUserStatus(UserStatusEnum.NORMAL.getCode());
        UserBaseDO save = userBaseRepository.save(userBase);
        return save.getId();
    }

    /**
     * 添加登录手机号
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addAccessToken(String userId, UserRegisterDTO userRegisterDTO) {
        UserAccessTokenDO accessToken = new UserAccessTokenDO();
        accessToken.setUserId(userId);
        accessToken.setTokenType(userRegisterDTO.getLoginType());
        accessToken.setTokenValue(userRegisterDTO.getLoginId());
        accessTokenRepository.save(accessToken);
    }

    /**
     * 添加密码
     *
     * @param userId
     * @param userRegisterDTO
     */
    private void addIdentification(String userId, UserRegisterDTO userRegisterDTO) {
        UserIdentificationDO identification = new UserIdentificationDO();
        identification.setUserId(userId);
        identification.setIdentifyType(userRegisterDTO.getIdentifyType());
        identification.setIdentifyValue(userRegisterDTO.getIdentifyValue());
        identificationRepository.save(identification);
    }

    /**
     * 添加用户画像
     *
     * @param userId
     * @param userName
     */
    private void addUserProfile(String userId, String userName) {
        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setUserId(userId);
        userProfileDO.setUserName(userName);
        userProfileRepository.save(userProfileDO);
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    @Autowired
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setIdentificationRepository(UserIdentificationRepository identificationRepository) {
        this.identificationRepository = identificationRepository;
    }

    @Autowired
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
}
