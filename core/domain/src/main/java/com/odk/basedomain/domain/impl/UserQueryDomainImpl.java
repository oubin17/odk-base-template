package com.odk.basedomain.domain.impl;

import com.odk.base.context.TenantIdContext;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.cache.UserCache;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserListQueryCriteria;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.mapper.UserDomainMapper;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.baseutil.constants.UserInfoConstants;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.userinfo.SessionContext;
import com.odk.redisspringbootstarter.DistributedLockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class UserQueryDomainImpl implements UserQueryDomain {

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository accessTokenRepository;

    private UserProfileRepository userProfileRepository;

    private UserDomainMapper userDomainMapper;

    private UserCache userCache;

    private DistributedLockService distributedLockService;

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

    /**
     * 根据tokenType和tokenValue查找用户
     *
     * @param tokenType
     * @param tokenValue
     * @return
     */
    private UserEntity queryByLoginTypeAndLoginId(String tokenType, String tokenValue) {
        UserAccessTokenDO userAccessTokenDO = accessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(tokenType, tokenValue,  TenantIdContext.getTenantId());
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

        // 1. 首先尝试从缓存中获取用户信息
        UserEntity userInfoFromCache = userCache.getUserFromCache(userId);
        if (null != userInfoFromCache) {
            return userInfoFromCache;
        }

        // 2. 使用带重试机制的分布式锁
        String lockKey = UserInfoConstants.USER_LOCK_PREFIX + userId;
        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            boolean acquired = distributedLockService.tryLock(lockKey, 1, 3); // 等待1秒，持有3秒
            if (acquired) {
                try {
                    // 双重检查缓存
                    userInfoFromCache = userCache.getUserFromCache(userId);
                    if (null != userInfoFromCache) {
                        return userInfoFromCache;
                    }
                    // 查询数据库
                    return queryUserFromDatabase(userId);
                } finally {
                    distributedLockService.unlock(lockKey);
                }
            } else {
                retryCount++;
                if (retryCount < maxRetries) {
                    // 短暂等待后重试
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // 所有重试都失败了，降级处理
        log.warn("获取用户信息失败，userId={}, retryCount={}", userId, retryCount);
        return queryUserFromDatabase(userId); // 直接查询数据库，接受缓存击穿风险
    }

    private UserEntity queryUserFromDatabase(String userId) {
        try {
            Optional<UserBaseDO> userBaseDOOptional = userBaseRepository.findByIdAndTenantId(userId, TenantIdContext.getTenantId());
            if (userBaseDOOptional.isEmpty()) {
                log.error("找不到用户，用户ID={}", userId);
                return null;
            }

            // 构建UserEntity对象
            UserEntity userEntity = this.userDomainMapper.toEntity(userBaseDOOptional.get());

            // 查询并设置账号信息
            UserAccessTokenDO accessTokenDO = accessTokenRepository.findByUserIdAndTenantId(userId, TenantIdContext.getTenantId());
            userEntity.setAccessToken(this.userDomainMapper.toEntity(accessTokenDO));

            // 查询并设置用户画像信息
            UserProfileDO userProfileDO = userProfileRepository.findByUserIdAndTenantId(userId, TenantIdContext.getTenantId());
            if (null != userProfileDO) {
                userEntity.setUserProfile(this.userDomainMapper.toEntity(userProfileDO));
            }

            // 存入缓存
            userCache.putUserToCache(userEntity);
            return userEntity;
        } catch (Exception e) {
            log.error("查询用户信息失败，userId={}", userId, e);
            throw new RuntimeException("查询用户信息失败", e);
        }
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

    @Autowired
    public void setUserDomainMapper(UserDomainMapper userDomainMapper) {
        this.userDomainMapper = userDomainMapper;
    }

    @Autowired
    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    @Autowired
    public void setDistributedLockService(DistributedLockService distributedLockService) {
        this.distributedLockService = distributedLockService;
    }
}
