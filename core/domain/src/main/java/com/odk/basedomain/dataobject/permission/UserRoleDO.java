package com.odk.basedomain.dataobject.permission;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserRoleDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_role", indexes = {
        @Index(name = "idx_role_code", columnList = "role_code", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class UserRoleDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -1342391596496714969L;

    /**
     * 角色码
     */
    @Column(name = "role_code")
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色状态
     */
    @Column(name = "status")
    private String status;

}
