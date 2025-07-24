package com.odk.basemanager.deal.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.exception.AssertUtil;
import com.odk.base.exception.BizErrorCode;
import com.odk.basedomain.dataobject.user.UserIdentificationDO;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.repository.user.UserIdentificationRepository;
import com.odk.baseinfra.security.IDecrypt;
import com.odk.baseinfra.security.IEncrypt;
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
public class PasswordManager {

    private IDecrypt decrypt;

    private IEncrypt iEncrypt;

    private UserQueryDomain userQueryDomain;

    private UserIdentificationRepository identificationRepository;

    /**
     * 登录态下
     *
     * @return
     */
    public boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        //1.判断密码是否通过公钥加密
        String oldPassword = decrypt.decrypt(passwordUpdateDTO.getOldIdentifyValue());
        String newPassword = decrypt.decrypt(passwordUpdateDTO.getNewIdentifyValue());

        //2.新旧密码不一致
        AssertUtil.notEqual(oldPassword, newPassword, BizErrorCode.IDENTIFICATION_SAME);
        //3.比对旧密码
        UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.SESSION).build());
        UserIdentificationDO userIdentificationDO = identificationRepository.findByUserIdAndIdentifyTypeAndTenantId(userEntity.getUserId(), passwordUpdateDTO.getIdentifyType(), TenantIdContext.getTenantId());
        AssertUtil.isTrue(iEncrypt.matches(oldPassword, userIdentificationDO.getIdentifyValue()), BizErrorCode.IDENTIFICATION_NOT_MATCH);
        //4.新密码加密
        String encode = iEncrypt.encrypt(newPassword);
        //5.设置新密码
        int count = this.identificationRepository.updatePassword(userIdentificationDO.getId(), passwordUpdateDTO.getIdentifyType(), encode, userEntity.getUserId(), LocalDateTime.now());
        return count > 0;

    }

}
