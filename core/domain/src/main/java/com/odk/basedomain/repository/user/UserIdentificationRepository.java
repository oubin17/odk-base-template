package com.odk.basedomain.repository.user;

import com.odk.basedomain.model.user.UserIdentificationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * UserIdentificationRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface UserIdentificationRepository extends JpaRepository<UserIdentificationDO, String> {

    /**
     * 查找密码
     *
     * @param userId
     * @param identifyType
     * @return
     */
    UserIdentificationDO findByUserIdAndIdentifyType(String userId, String identifyType);

    /**
     * 更新密码
     *
     * @param id
     * @param identifyType
     * @param identifyValue
     * @param updateBy
     * @param updateTime
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE t_user_identification t  SET t.identify_value = :identifyType, t.update_by = :updateBy, t.update_time = : updateTime where t.id = :id and t.identify_type = :identifyType", nativeQuery = true)
    int updatePassword(@Param("id") String id, @Param("id")String identifyType, @Param("id") String identifyValue, @Param("id") String updateBy, @Param("id") LocalDateTime updateTime);
}
