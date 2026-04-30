package com.odk.basedomain.model.sys;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * SysProfileDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_sys_global_config", indexes = {
        @Index(name = "idx_config_key", columnList = "config_key,tenant_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class SysGlobalConfigDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -2031254087332132537L;

    @Comment("配置键")
    @Column(name = "config_key")
    private String configKey;

    @Comment("配置值")
    @Column(name = "config_value", length = 2048)
    private String configValue;

    @Comment("配置描述")
    @Column(name = "config_desc")
    private String configDesc;
}
