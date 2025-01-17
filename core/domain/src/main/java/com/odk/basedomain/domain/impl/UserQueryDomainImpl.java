package com.odk.basedomain.domain.impl;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.baseutil.entity.UserEntity;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * UserQueryDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
@Slf4j
@Service
public class UserQueryDomainImpl implements UserQueryDomain {

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private UserProfileRepository userProfileRepository;

    /**
     * 根据userId查找用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryByUserId(String userId) {
        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setUserId(userId);
        userProfileRepository.save(userProfileDO);




        Optional<UserBaseDO> userBaseDOOptional = userBaseRepository.findById(userId);
        if (userBaseDOOptional.isEmpty()) {
            log.error("找不到用户，用户ID={}", userId);
            return null;
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userBaseDOOptional.get(), userEntity);
        userEntity.setUserId(userBaseDOOptional.get().getId());
        return userEntity;
    }

    @Override
    public UserEntity queryBySession() {
        return queryByUserId(SessionContext.getLoginIdAsString());
    }

    /**
     * 检查用户状态
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryByUserIdAndCheck(String userId) {
        Optional<UserBaseDO> optionalUserBaseDO = userBaseRepository.findById(userId);
        AssertUtil.isTrue(optionalUserBaseDO.isPresent(), BizErrorCode.USER_NOT_EXIST, "用户不存在，用户ID:" + userId);
        if (UserStatusEnum.NORMAL != UserStatusEnum.getByCode(optionalUserBaseDO.get().getUserStatus())) {
            throw new BizException(BizErrorCode.USER_STATUS_ERROR, "用户状态异常，用户ID:" + userId);
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(optionalUserBaseDO.get(), userEntity);
        userEntity.setUserId(optionalUserBaseDO.get().getId());
        return userEntity;
    }

    @Override
    public UserEntity queryBySessionAndCheck() {
        return queryByUserIdAndCheck(SessionContext.getLoginIdAsString());
    }

    @Override
    public UserEntity queryByLoginTypeAndLoginId(String tokenType, String tokenValue) {
        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValue(tokenType, tokenValue);
        if (null == userAccessTokenDO) {
            return null;
        }
        return queryByUserIdAndCheck(userAccessTokenDO.getUserId());
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
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }
}
