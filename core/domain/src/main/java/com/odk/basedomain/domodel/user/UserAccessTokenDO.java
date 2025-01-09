package com.odk.basedomain.domodel.user;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserAccessTokenDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_access_token", indexes = {
        @Index(name = "idx_type_id", columnList = "token_value,token_type", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class UserAccessTokenDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -3008078711003604352L;

    @Id
    @GeneratedValue(generator = "user-uuid")
    @GenericGenerator(name = "user-uuid", strategy = "com.odk.basedomain.idgenerate.CustomIDGenerator")
    private String id;

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
