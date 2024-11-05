package com.odk.basedomain.domain;

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
@Table(name = "t_user_access_token")
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
    private String tokenId;
    /**
     * 用户id
     */
    private String userId;

    /**
     * token 类型
     * {@link com.odk.base.enums.user.TokenTypeEnum}
     */
    private String tokenType;

    /**
     * token值
     */
    private String tokenValue;

}
