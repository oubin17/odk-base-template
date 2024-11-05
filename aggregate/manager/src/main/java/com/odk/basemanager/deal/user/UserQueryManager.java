package com.odk.basemanager.deal.user;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import com.odk.basemanager.entity.UserEntity;
import com.odk.basedomain.domain.UserAccessTokenDO;
import com.odk.basedomain.domain.UserBaseDO;
import com.odk.basedomain.repository.UserAccessTokenRepository;
import com.odk.basedomain.repository.UserBaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserQueryManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Service
public class UserQueryManager {

    private static final Logger logger = LoggerFactory.getLogger(UserQueryManager.class);

    private UserBaseRepository baseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    /**
     * 根据userId查找用户
     *
     * @param userId
     * @return
     */
    public UserEntity queryByUserId(String userId) {
        UserBaseDO userBaseDO = baseRepository.findByUserId(userId);
        if (null == userBaseDO) {
            logger.error("找不到用户，用户ID={}", userId);
            return null;
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userBaseDO, userEntity);
        userEntity.setUserType(UserTypeEnum.getByCode(userBaseDO.getUserType()));
        userEntity.setUserStatus(UserStatusEnum.getByCode(userBaseDO.getUserStatus()));
        return userEntity;
    }

    /**
     * 根据登录凭证查找用户
     *
     * @param tokenType
     * @param tokenValue
     * @return
     */
    public UserEntity queryByAccessToken(String tokenType, String tokenValue) {

        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValue(tokenType, tokenValue);
        if (null == userAccessTokenDO) {
            return null;
        }
        return queryByUserId(userAccessTokenDO.getUserId());
    }



    @Autowired
    public void setBaseRepository(UserBaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    @Autowired
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }
}
