package com.odk.basedomain.model.permission;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * RolePermissionRelDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_role_permission_rel", indexes = {
        @Index(name = "idx_permission_id", columnList = "role_id,permission_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class RolePermissionRelDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 6132591681856111018L;

    @Id
    @GeneratedValue(generator = "user-uuid")
    @GenericGenerator(name = "user-uuid", strategy = "com.odk.basedomain.idgenerate.CustomIDGenerator")
    private String id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private String permissionId;
}
