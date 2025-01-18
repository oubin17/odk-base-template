package com.odk.basedomain.model.user;

import com.odk.base.dos.BaseDO;
import com.odk.base.enums.user.UserStatusEnum;
import com.odk.base.enums.user.UserTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
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
     * 用户名称
     */
    @Column(name = "user_name")
    @Comment("用户姓名")
    private String userName;

    /**
     * 用户类型
     * {@link UserTypeEnum}
     */
    @Column(name = "user_type")
    private String userType;

    /**
     * 用户状态
     * {@link UserStatusEnum}
     */
    @Column(name = "user_status")
    private String userStatus;

}
