package com.odk.basedomain.domain.user;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
        @Index(name = "idx_type_id", columnList = "token_type,token_value", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class UserAccessTokenDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -3008078711003604352L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 主键ID
     *
     */
    @Column(name = "token_id", unique = true)
    private String tokenId;

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
