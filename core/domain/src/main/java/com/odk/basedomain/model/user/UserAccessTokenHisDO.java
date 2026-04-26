package com.odk.basedomain.model.user;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserAccessTokenHisDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_access_token_his", indexes = {
        @Index(name = "idx_type_id", columnList = "token_value,token_type,tenant_id"),
        @Index(name = "idx_user_id", columnList = "user_id,tenant_id")
})
@EntityListeners(AuditingEntityListener.class)
public class UserAccessTokenHisDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -5778192315197389734L;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * token 类型
     * {@link com.odk.base.enums.user.TokenTypeEnum}
     */
    @Column(name = "token_type")
    private String tokenType;

    /**
     * token值
     */
    @Column(name = "token_value")
    private String tokenValue;

}
