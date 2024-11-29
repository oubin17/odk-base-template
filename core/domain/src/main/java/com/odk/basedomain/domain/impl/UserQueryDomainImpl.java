package com.odk.basedomain.domain.impl;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.entity.UserEntity;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
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

    /**
     * 根据userId查找用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryByUserId(Long userId) {
        Optional<UserBaseDO> userBaseDO = userBaseRepository.findById(userId);
        if (userBaseDO.isEmpty()) {
            log.error("找不到用户，用户ID={}", userId);
            return null;
        }
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userBaseDO.get(), userEntity);
        userEntity.setUserId(userBaseDO.get().getId());
        return userEntity;
    }

    @Override
    public UserEntity queryByLoginTypeAndLoginId(String tokenType, String tokenValue) {
        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValue(tokenType, tokenValue);
        if (null == userAccessTokenDO) {
            return null;
        }
        return queryByUserId(userAccessTokenDO.getUserId());
    }

    /**
     * 检查用户状态
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryByUserIdAndCheck(Long userId) {
        Optional<UserBaseDO> optionalUserBaseDO = userBaseRepository.findById(userId);
        if (optionalUserBaseDO.isEmpty()) {
            log.error("找不到用户，用户ID={}", userId);
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

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }

    @Autowired
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

}
