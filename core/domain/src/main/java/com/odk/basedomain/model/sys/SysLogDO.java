package com.odk.basedomain.model.sys;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * SysLogDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_sys_log", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id,tenant_id")
})
@EntityListeners(AuditingEntityListener.class)
public class SysLogDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 6082497400487504846L;

    @Comment("用户ID")
    @Column(name = "user_id")
    private String userId;

    /**
     * 登录日志
     * 操作日志
     */
    @Comment("日志类型")
    @Column(name = "operation")
    private String operation;

    @Comment("设备类型")
    @Column(name = "device_type", length = 2048)
    private String deviceType;

    @Comment("设备 id")
    @Column(name = "device_d", length = 2048)
    private String deviceId;

    @Comment("内容")
    @Column(name = "content", length = 2048)
    private String content;
}
