package com.odk.basedomain.repository.user;

import com.odk.basedomain.domain.user.UserAccessTokenDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserAccessTokenRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface UserAccessTokenRepository extends JpaRepository<UserAccessTokenDO, Long> {

    /**
     * 查找手机号是否存在
     *
     * @param tokenType
     * @param tokenValue
     * @return
     */
    UserAccessTokenDO findByTokenTypeAndTokenValue(String tokenType, String tokenValue);
}
