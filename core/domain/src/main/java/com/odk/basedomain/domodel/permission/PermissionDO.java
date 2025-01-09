package com.odk.basedomain.domodel.permission;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * RolePermissionDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_permission", indexes = {
        @Index(name = "idx_permission_id", columnList = "permission_code", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class PermissionDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1983417677692607399L;

    @Id
    @GeneratedValue(generator = "user-uuid")
    @GenericGenerator(name = "user-uuid", strategy = "com.odk.basedomain.idgenerate.CustomIDGenerator")
    private String id;

    /**
     * 权限码
     */
    @Column(name = "permission_code")
    private String permissionCode;

    /**
     * 权限名称
     */
    @Column(name = "permission_name")
    private String permissionName;

    /**
     * 权限状态
     */
    @Column(name = "status")
    private String status;

}
