package com.odk.basedomain.domain;

import com.odk.base.dos.BaseDO;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserBaseDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_base")
@EntityListeners(AuditingEntityListener.class)
public class UserBaseDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 8720143544161713478L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户类型
     * {@link UserTypeEnum}
     */
    private String userType;

    /**
     * 用户状态
     * {@link UserStatusEnum}
     */
    private String userStatus;

}
