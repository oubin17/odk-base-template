package com.odk.basedomain.model.user;

import com.odk.base.dos.BaseDO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;

/**
 * UserProfile
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/1/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_user_profile", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class UserProfileDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 7347190046809610357L;

    @Column(name = "user_id")
    @Comment("用户ID")
    private String userId;

    @Column(name = "user_name")
    @Comment("用户姓名")
    private String userName;

    @Column(name = "gender")
    @Comment("性别")
    private String gender;

    @Column(name = "birthday")
    @Comment("生日")
    private String birthDay;


}
