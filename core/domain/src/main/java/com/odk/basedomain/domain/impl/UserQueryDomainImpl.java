package com.odk.basedomain.domain.impl;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.util.PageUtil;
import com.odk.base.vo.response.PageResponse;
import com.odk.basedomain.domain.*;
import com.odk.basedomain.domain.criteria.UserListQueryCriteria;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.baseutil.entity.UserBaseEntity;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.request.UserListQueryRequest;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserQueryDomainImpl
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/29
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserQueryDomainImpl implements UserQueryDomain {

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private RoleDomain roleDomain;

    private UserProfileDomain userProfileDomain;

    private UserBaseDomain userBaseDomain;

    private AccessTokenDomain accessTokenDomain;

    @Override
    public UserEntity queryUser(UserQueryCriteria criteria) {

        AssertUtil.notNull(criteria.getQueryType(), BizErrorCode.PARAM_ILLEGAL, "userQueryTypeEnum is null.");
        UserEntity userEntity = switch (criteria.getQueryType()) {
            case USER_ID -> getUserInfo(criteria.getUserId());
            case LOGIN_ID -> queryByLoginTypeAndLoginId(
                    criteria.getLoginType(),
                    criteria.getLoginId()
            );
            case SESSION -> queryBySession();

            case USER_ID_LIST -> null;
        };
        if (!criteria.isNullAllowed()) {
            AssertUtil.notNull(userEntity, BizErrorCode.USER_NOT_EXIST);
        }
        if (criteria.isStatusCheck()) {
            AssertUtil.notNull(userEntity, BizErrorCode.USER_NOT_EXIST);
            AssertUtil.isTrue(UserStatusEnum.NORMAL.getCode().equals(userEntity.getUserStatus()), BizErrorCode.USER_STATUS_ERROR);
        }
        return userEntity;
    }

    @Override
    public List<UserEntity> queryUserList(UserListQueryCriteria criteria) {
        AssertUtil.notNull(criteria.getQueryType(), BizErrorCode.PARAM_ILLEGAL, "userQueryTypeEnum is null.");
        return switch (criteria.getQueryType()) {
            case USER_ID, SESSION, LOGIN_ID -> new ArrayList<>();
            case USER_ID_LIST -> queryByUserIdList(criteria.getUserIds());
        };
    }

    /**
     * 根据userIdList查找用户
     *
     * @param userIds
     * @return
     */
    private List<UserEntity> queryByUserIdList(List<String> userIds) {
        return userIds.stream()
                .map(this::getUserInfo)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<UserEntity> queryUserPageList(UserListQueryRequest pageParamRequest) {
//        Page<String> strings = customerRepository.queryUserPageList(pageParamRequest);
//        if (strings.isEmpty()) {
//            return PageResponse.ofEmpty();
//        }
//        Page<UserEntity> byTenantId = strings.map(this::getUserInfo);
//        List<UserEntity> userEntities = new ArrayList<>(byTenantId.getContent());
//        return PageResponse.of(userEntities, (int) byTenantId.getTotalElements());


        Page<UserBaseDO> pageList = userBaseRepository.findPageList(pageParamRequest, TenantIdContext.getTenantId(), PageUtil.convertToPageRequest(pageParamRequest));
        List<UserEntity> collect = pageList.stream().map(userBaseDO -> getUserInfo(userBaseDO.getId())).toList();
        return PageResponse.of(collect, (int) pageList.getTotalElements());
    }


    /**
     * 根据tokenType和tokenValue查找用户
     *
     * @param tokenType
     * @param tokenValue
     * @return
     */
    private UserEntity queryByLoginTypeAndLoginId(String tokenType, String tokenValue) {
        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(tokenType, tokenValue, TenantIdContext.getTenantId());
        if (null == userAccessTokenDO) {
            return null;
        }
        return getUserInfo(userAccessTokenDO.getUserId());
    }

    /**
     * 根据session查找用户
     *
     * @return
     */
    private UserEntity queryBySession() {
        return getUserInfo(SessionContext.getLoginIdWithCheck());
    }

    /**
     * 通用获取用户信息方法
     *
     * @param userId
     * @return
     */
    private UserEntity getUserInfo(String userId) {

        UserEntity userEntity = new UserEntity();
        UserBaseEntity userBaseEntity = userBaseDomain.getUserBaseInfo(userId);
        userEntity.setUserId(userBaseEntity.getId());
        userEntity.setUserType(userBaseEntity.getUserType());
        userEntity.setUserStatus(userBaseEntity.getUserStatus());
        userEntity.setAccessToken(accessTokenDomain.getAccessToken(userId));
        userEntity.setRoles(roleDomain.getRoleListByUserId(userId));
        userEntity.setUserProfile(userProfileDomain.getUserProfileByUserId(userId));

        return userEntity;

    }

}
