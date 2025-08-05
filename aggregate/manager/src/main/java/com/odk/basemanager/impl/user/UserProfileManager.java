package com.odk.basemanager.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.base.enums.cache.CacheActionEnum;
import com.odk.basedomain.mapper.UserProfileMapper;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.basemanager.api.common.IEventPublish;
import com.odk.basemanager.api.user.IUserProfileManager;
import com.odk.baseutil.dto.user.UserProfileDTO;
import com.odk.baseutil.event.UserCacheCleanEvent;
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

    private IEventPublish eventPublish;


    @Override
    public boolean updateUserProfile(UserProfileDTO userProfileDTO) {
        UserProfileDO userProfileDO = userProfileRepository.findByUserIdAndTenantId(userProfileDTO.getUserId(), TenantIdContext.getTenantId());
        userProfileMapper.merge(userProfileDTO, userProfileDO);
        userProfileRepository.save(userProfileDO);
        eventPublish.publish(new UserCacheCleanEvent(userProfileDTO.getUserId(), CacheActionEnum.UPDATE));

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

    @Autowired
    public void setEventPublish(IEventPublish eventPublish) {
        this.eventPublish = eventPublish;
    }
}
