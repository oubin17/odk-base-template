package com.odk.basedomain.repository;

import com.odk.basedomain.domain.UserIdentificationDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserIdentificationRepository
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/4
 */
public interface UserIdentificationRepository extends JpaRepository<UserIdentificationDO, Long> {

    /**
     * 查找密码
     *
     * @param userId
     * @param identifyType
     * @return
     */
    UserIdentificationDO findByUserIdAndIdentifyType(String userId, String identifyType);
}
