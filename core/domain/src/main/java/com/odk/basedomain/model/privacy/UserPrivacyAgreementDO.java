package com.odk.basedomain.model.privacy;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * UserPrivacyAgreement
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_privacy_agreement", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id,tenant_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class UserPrivacyAgreementDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -8477622520686169737L;

    @Column(name = "user_id")
    @Comment("用户ID")
    private String userId;

    /**
     *
     * '0=未同意 1=已同意 2=已撤回'
     *
     */
    @Column(name = "agree_privacy")
    @Comment("隐私协议状态")
    private Integer agreePrivacy;

    @Column(name = "agree_privacy_version")
    @Comment("隐私协议版本")
    private String agreePrivacyVersion;

    @Column(name = "privacy_revoke_time")
    @Comment("隐私协议撤回时间")
    private LocalDateTime privacyRevokeTime;

    /**
     *
     * '0=未同意 1=已同意 2=已撤回'
     *
     */
    @Column(name = "user_agreement")
    @Comment("用户协议状态")
    private Integer userAgreement;

    @Column(name = "user_agreement_version")
    @Comment("用户协议版本")
    private String userAgreementVersion;

    @Column(name = "user_agreement_revoke_time")
    @Comment("用户协议撤回时间")
    private LocalDateTime userAgreementRevokeTime;
}
