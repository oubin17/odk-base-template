package com.odk.basemanager.deal.user;

import com.odk.basedomain.domain.UserDomain;
import com.odk.basedomain.entity.UserEntity;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserQueryManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/5
 */
@Slf4j
@Service
public class UserQueryManager {

    private UserAccessTokenRepository accessTokenRepository;

    private UserDomain userDomain;

    /**
     * 根据userId查找用户
     *
     * @param userId
     * @return
     */
    public UserEntity queryByUserId(Long userId) {
        return this.userDomain.queryByUserId(userId);
    }

    /**
     * 检查用户状态
     *
     * @param userId
     * @return
     */
    public UserEntity queryByUserIdAndCheck(Long userId) {
        return this.userDomain.queryByUserIdAndCheck(userId);
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
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setUserDomain(UserDomain userDomain) {
        this.userDomain = userDomain;
    }
}
