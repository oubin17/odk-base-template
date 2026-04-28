package com.odk.baseservice.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.exception.BizException;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserStatusApi;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserAccessTokenHisDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserAccessTokenHisRepository;
import com.odk.basedomain.repository.user.UserAccessTokenRepository;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.baseutil.annotation.BizProcess;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * UserStatusService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserStatusService implements UserStatusApi {

    private UserQueryDomain userQueryDomain;

    private UserBaseRepository userBaseRepository;

    private UserAccessTokenRepository userAccessTokenRepository;

    private UserAccessTokenHisRepository userAccessTokenHisRepository;

    private TransactionTemplate transactionTemplate;

    @Override
    @BizProcess(bizScene = BizScene.ACCOUNT_DELETION)
    public ServiceResponse<Void> deletion() {

        UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.SESSION).nullAllowed(false).build());
        AssertUtil.notNull(userEntity.getAccessToken(), BizErrorCode.USER_NOT_EXIST);

        UserBaseDO userBaseDO = userBaseRepository.findById(userEntity.getUserId())
                .orElseThrow(() -> new BizException(BizErrorCode.USER_NOT_EXIST));
        AssertUtil.isTrue(UserStatusEnum.isNormal(userBaseDO.getUserStatus()), BizErrorCode.USER_STATUS_ERROR);

        //1.查出旧的 access token
        UserAccessTokenDO byTokenTypeAndTokenValueAndTenantId = userAccessTokenRepository.findByTokenTypeAndTokenValueAndTenantId(userEntity.getAccessToken().getTokenType(), userEntity.getAccessToken().getTokenValue(), TenantIdContext.getTenantId());
        UserAccessTokenHisDO userAccessTokenHisDO = getUserAccessTokenHisDO(byTokenTypeAndTokenValueAndTenantId);

        transactionTemplate.execute(status -> {
            //1. 删除旧的 access token
            userAccessTokenRepository.delete(byTokenTypeAndTokenValueAndTenantId);
            //2. 保存新的 access token
            userAccessTokenHisRepository.save(userAccessTokenHisDO);

            userBaseDO.setUserStatus(UserStatusEnum.CLOSED.getCode());
            userBaseRepository.save(userBaseDO);

            return userEntity.getUserId();
        });
        SessionContext.logOut();
        return ServiceResponse.valueOfSuccess();

    }

    @Override
    @BizProcess(bizScene = BizScene.USER_FREEZE)
    public ServiceResponse<Boolean> freezeUser(String userId) {
        UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.USER_ID).userId(userId).nullAllowed(false).build());
        UserBaseDO userBaseDO = userBaseRepository.findById(userEntity.getUserId())
                .orElseThrow(() -> new BizException(BizErrorCode.USER_NOT_EXIST));
        AssertUtil.isTrue(UserStatusEnum.isNormal(userBaseDO.getUserStatus()), BizErrorCode.USER_STATUS_ERROR);
        userBaseDO.setUserStatus(UserStatusEnum.FROZEN.getCode());
        userBaseRepository.save(userBaseDO);
        SessionContext.kickOut(userId);
        return ServiceResponse.valueOfSuccess();
    }

    @Override
    @BizProcess(bizScene = BizScene.USER_UNFREEZE)
    public ServiceResponse<Boolean> unfreezeUser(String userId) {
        UserBaseDO userBaseDO = userBaseRepository.findById(userId)
                .orElseThrow(() -> new BizException(BizErrorCode.USER_NOT_EXIST));
        userBaseDO.setUserStatus(UserStatusEnum.NORMAL.getCode());
        userBaseRepository.save(userBaseDO);
        return ServiceResponse.valueOfSuccess();
    }


    @NotNull
    private static UserAccessTokenHisDO getUserAccessTokenHisDO(UserAccessTokenDO byTokenTypeAndTokenValueAndTenantId) {
        UserAccessTokenHisDO userAccessTokenHisDO = new UserAccessTokenHisDO();
        userAccessTokenHisDO.setId(byTokenTypeAndTokenValueAndTenantId.getId());
        userAccessTokenHisDO.setUserId(byTokenTypeAndTokenValueAndTenantId.getUserId());
        userAccessTokenHisDO.setTokenType(byTokenTypeAndTokenValueAndTenantId.getTokenType());
        userAccessTokenHisDO.setTokenValue(byTokenTypeAndTokenValueAndTenantId.getTokenValue());
        userAccessTokenHisDO.setTenantId(byTokenTypeAndTokenValueAndTenantId.getTenantId());
        return userAccessTokenHisDO;
    }
}
