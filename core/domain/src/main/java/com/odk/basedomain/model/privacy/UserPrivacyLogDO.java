package com.odk.basedomain.model.privacy;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserPrivacyLog
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_privacy_log", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id,tenant_id")
})
@EntityListeners(AuditingEntityListener.class)
public class UserPrivacyLogDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 4484207385413144247L;


    @Column(name = "user_id")
    @Comment("用户ID")
    private String userId;

    /**
     *
     * '0=未同意 1=已同意 2=已撤回'
     *
     */
    @Column(name = "operation")
    @Comment("操作：0=未同意 1=已同意 2=已撤回")
    private Integer operation;

    @Column(name = "agree_privacy_version")
    @Comment("隐私协议版本")
    private String agreePrivacyVersion;

    @Column(name = "user_agreement_version")
    @Comment("用户协议版本")
    private String userAgreementVersion;
}
