package com.odk.basemanager.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.cache.pointcut.UserCacheClean;
import com.odk.basedomain.mapper.UserProfileMapper;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.basemanager.api.user.IUserProfileManager;
import com.odk.baseutil.dto.user.UserProfileDTO;
import com.odk.baseutil.enums.UserCacheSceneEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* UserProfileManager
*   
* @description:    
* @version:        1.0
* @author:         oubin on 2025/8/4
*/
@Slf4j
@Service
public class UserProfileManager implements IUserProfileManager {

    private UserProfileRepository userProfileRepository;

    private UserProfileMapper userProfileMapper;

    @Override
    @UserCacheClean(scene = UserCacheSceneEnum.USER_PROFILE)
    public boolean updateUserProfile(String userId, UserProfileDTO userProfileDTO) {
        UserProfileDO userProfileDO = userProfileRepository.findByUserIdAndTenantId(userProfileDTO.getUserId(), TenantIdContext.getTenantId());
        userProfileMapper.merge(userProfileDTO, userProfileDO);
        userProfileRepository.save(userProfileDO);

        return true;
    }

    @Autowired
    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Autowired
    public void setUserProfileMapper(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

}
