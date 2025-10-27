package com.odk.baseservice.impl.user;

import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseapi.inter.user.UserStatusApi;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.repository.user.UserBaseRepository;
import com.odk.baseservice.template.AbstractApiImpl;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.BizScene;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import com.odk.baseutil.userinfo.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * UserStatusService
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
@Slf4j
@Service
public class UserStatusService extends AbstractApiImpl implements UserStatusApi {

    private UserQueryDomain userQueryDomain;

    private UserBaseRepository userBaseRepository;

    @Override
    public ServiceResponse<Boolean> freezeUser(String userId) {
        return super.bizProcess(BizScene.USER_FREEZE, userId, new ApiCallBack<Boolean, Boolean>() {

            @Override
            protected void checkParams(Object request) {
                UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.USER_ID).userId(userId).nullAllowed(false).build());

                //long count = userEntity.getRoles().stream().filter(role -> role.getRoleCode().equals(InnerRoleEnum.ADMIN_ROLE)).count();
            }

            @Override
            protected Boolean doProcess(Object args) {

                UserBaseDO userBaseDO = userBaseRepository.findById(userId).orElse(null);
                AssertUtil.notNull(userBaseDO, BizErrorCode.USER_NOT_EXIST);
                userBaseDO.setUserStatus(UserStatusEnum.FROZEN.getCode());
                userBaseRepository.save(userBaseDO);
                SessionContext.kickOut(userId);
                return true;
            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }
        });
    }

    @Override
    public ServiceResponse<Boolean> unfreezeUser(String userId) {
        return super.bizProcess(BizScene.USER_UNFREEZE, userId, new ApiCallBack<Boolean, Boolean>() {

            @Override
            protected void checkParams(Object request) {
                userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.USER_ID).userId(userId).nullAllowed(false).build());
            }
            @Override
            protected Boolean doProcess(Object args) {

                UserBaseDO userBaseDO = userBaseRepository.findById(userId).orElse(null);
                AssertUtil.notNull(userBaseDO, BizErrorCode.USER_NOT_EXIST);
                userBaseDO.setUserStatus(UserStatusEnum.NORMAL.getCode());
                userBaseRepository.save(userBaseDO);
                return true;

            }

            @Override
            protected Boolean convertResult(Boolean apiResult) {
                return apiResult;
            }
        });
    }

    @Autowired
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }

    @Autowired
    public void setUserBaseRepository(UserBaseRepository userBaseRepository) {
        this.userBaseRepository = userBaseRepository;
    }
}
