package com.odk.basemanager.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.model.user.UserIdentificationDO;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.baseinfra.security.IEncryption;
import com.odk.basemanager.api.user.IPasswordManager;
import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.UserQueryTypeEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * PasswordManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/17
 */
@Service
@Slf4j
@AllArgsConstructor
public class PasswordManager implements IPasswordManager {

    private IEncryption encryption;

    private UserQueryDomain userQueryDomain;

    private UserIdentificationRepository identificationRepository;

    /**
     * 登录态下
     *
     * @return
     */
    @Override
    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        //1.判断密码是否通过公钥加密
        String oldPassword = encryption.rsaDecode(passwordUpdateDTO.getOldIdentifyValue());
        String newPassword = encryption.rsaDecode(passwordUpdateDTO.getNewIdentifyValue());

        //2.新旧密码不一致
        AssertUtil.notEqual(oldPassword, newPassword, BizErrorCode.IDENTIFICATION_SAME);
        //3.比对旧密码
        UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.SESSION).build());
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(userEntity.getUserId(), passwordUpdateDTO.getIdentifyType(), TenantIdContext.getTenantId());
        AssertUtil.isTrue(encryption.bcryptMatches(oldPassword, userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);
        //4.新密码加密
        String encode = encryption.bcryptEncode(newPassword);
        //5.设置新密码
        int count = this.identificationRepository.updatePassword(userIdentificationDO.getId(), passwordUpdateDTO.getIdentifyType(), encode, userEntity.getUserId(), LocalDateTime.now(), TenantIdContext.getTenantId());
        return count > 0;

    }

}
