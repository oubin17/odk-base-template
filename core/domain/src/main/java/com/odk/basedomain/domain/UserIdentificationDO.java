package com.odk.basedomain.domain;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserIdentificationDO
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_identification")
@EntityListeners(AuditingEntityListener.class)
public class UserIdentificationDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = -7115218095274721902L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 主键ID
     */
    private String identifyId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 认证类型
     * {@link com.odk.base.enums.user.IdentificationTypeEnum}
     */
    private String identifyType;

    /**
     * 认证值
     */
    private String identifyValue;

}
