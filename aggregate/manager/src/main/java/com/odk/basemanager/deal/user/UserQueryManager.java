package com.odk.basemanager.deal.user;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.user.UserAccessTokenDO;
import com.odk.basedomain.domain.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basemanager.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public UserEntity queryByUserId(Long userId) {
        Optional<UserBaseDO> userBaseDO = baseRepository.findById(userId);
        if (userBaseDO.isEmpty()) {
            logger.error("找不到用户，用户ID={}", userId);
            return null;
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userBaseDO, userEntity);
        return userEntity;
    }

    /**
     * 检查用户状态
     *
     * @param userId
     * @return
     */
    public UserEntity queryByUserIdAndCheck(Long userId) {
        Optional<UserBaseDO> optionalUserBaseDO = baseRepository.findById(userId);
        if (optionalUserBaseDO.isEmpty()) {
            logger.error("找不到用户，用户ID={}", userId);
            return null;
        }
        if (UserStatusEnum.NORMAL != UserStatusEnum.getByCode(optionalUserBaseDO.get().getUserStatus())) {
            throw new BizException(BizErrorCode.USER_STATUS_ERROR);
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(optionalUserBaseDO.get(), userEntity);
        userEntity.setUserId(optionalUserBaseDO.get().getId());
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
