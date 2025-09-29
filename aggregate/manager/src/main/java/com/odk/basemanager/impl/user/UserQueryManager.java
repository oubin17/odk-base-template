package com.odk.basemanager.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.vo.response.PageResponse;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basemanager.api.user.IUserQueryManager;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.request.UserListQueryRequest;
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
public class UserQueryManager implements IUserQueryManager {

    private UserAccessTokenRepository accessTokenRepository;

    private UserQueryDomain userQueryDomain;

    /**
     * 根据userId查找用户
     *
     * @param userId
     * @return
     */
    @Override
    public UserEntity queryByUserId(String userId) {
        return this.userQueryDomain.queryUser(
                UserQueryCriteria.builder()
                .queryType(UserQueryTypeEnum.USER_ID)
                .userId(userId)
                .build()
        );
    }

    /**
     * 从 session 中获取用户信息
     *
     * @return
     */
    @Override
    public UserEntity queryBySession() {
        return this.userQueryDomain.queryUser(
                UserQueryCriteria.builder()
                .queryType(UserQueryTypeEnum.SESSION)
                .build()
        );
    }

    /**
     * 根据登录凭证查找用户
     *
     * @param tokenType
     * @param tokenValue
     * @return
     */
    @Override
    public UserEntity queryByAccessToken(String tokenType, String tokenValue) {

        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(tokenType, tokenValue, TenantIdContext.getTenantId());
        if (null == userAccessTokenDO) {
            return null;
        }
        UserQueryCriteria build = UserQueryCriteria.builder()
                .queryType(UserQueryTypeEnum.USER_ID)
                .userId(userAccessTokenDO.getUserId())
                .build();
        return this.userQueryDomain.queryUser(build);
    }

    @Override
    public PageResponse<UserEntity> queryUserPageList(UserListQueryRequest pageRequest) {
        return this.userQueryDomain.queryUserPageList(pageRequest);
    }

    @Autowired
    public void setAccessTokenRepository(UserAccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }
}
