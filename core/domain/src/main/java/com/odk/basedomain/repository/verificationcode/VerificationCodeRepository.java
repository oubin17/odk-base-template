package com.odk.basedomain.repository.verificationcode;

import com.odk.basedomain.model.verificationcode.VerificationCodeDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * VerificationCodeRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/6/27
 */
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeDO, String> {

    /**
     * 更新认证状态
     *
     * @param uniqueId
     * @param status
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE t_verification_code t SET t.verify_times = t.verify_times + 1, t.status = :status, t.update_by = :updateBy, t.update_time = :updateTime where t.unique_id = :uniqueId", nativeQuery = true)

    void updateStatusByUniqueId(@Param("uniqueId")String uniqueId, @Param("status")String status, @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 验证失败，验证次数+1
     * @param uniqueId
     * @param updateBy
     * @param updateTime
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE t_verification_code t SET t.verify_times = t.verify_times + 1, t.update_by = :updateBy, t.update_time = :updateTime where t.unique_id = :uniqueId", nativeQuery = true)
    void updateVerifyTimes(@Param("uniqueId")String uniqueId, @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);
}
