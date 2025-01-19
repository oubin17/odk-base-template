package com.odk.basedomain.domain.impl;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.baseutil.entity.AccessTokenEntity;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.entity.UserProfileEntity;
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
      return getUserInfo(userId);
    }


    @Override
    public UserEntity queryBySession() {
        return getUserInfo(SessionContext.getLoginIdAsString());
    }

    /**
     * 检查用户状态
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryByUserIdAndCheck(String userId) {
        UserEntity userInfo = getUserInfo(userId);
        AssertUtil.notNull(userInfo, BizErrorCode.USER_NOT_EXIST, "用户不存在，用户ID:" + userId);
        AssertUtil.equal(UserStatusEnum.NORMAL.getCode(), userInfo.getUserStatus(), BizErrorCode.USER_STATUS_ERROR, "用户状态异常，用户ID:" + userId);
        return userInfo;
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

    /**
     * 通用获取用户信息方法
     *
     * @param userId
     * @return
     */
    private UserEntity getUserInfo(String userId) {
        Optional<UserBaseDO> userBaseDOOptional = userBaseRepository.findById(userId);
        if (userBaseDOOptional.isEmpty()) {
            log.error("找不到用户，用户ID={}", userId);
            return null;
        }
        //1.用户id
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userBaseDOOptional.get(), userEntity);
        userEntity.setUserId(userBaseDOOptional.get().getId());

        //2.账号信息
        UserAccessTokenDO accessTokenDO = accessTokenRepository.findByUserId(userId);
        AccessTokenEntity accessToken = new AccessTokenEntity();
        BeanUtils.copyProperties(accessTokenDO, accessToken);
        userEntity.setAccessToken(accessToken);

        //3.用户画像
        UserProfileDO userProfileDO = userProfileRepository.findByUserId(userId);
        if (null != userProfileDO) {
            UserProfileEntity userProfile = new UserProfileEntity();
            BeanUtils.copyProperties(userProfileDO, userProfile);
            userEntity.setUserProfile(userProfile);
        }
        return userEntity;
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
