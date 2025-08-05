package com.odk.basemanager.impl.user;

import com.odk.base.context.TenantIdContext;
import com.odk.basedomain.cache.UserCache;
import com.odk.basedomain.domain.UserQueryDomain;
import com.odk.basedomain.domain.criteria.UserQueryCriteria;
import com.odk.basedomain.mapper.UserDomainMapper;
import com.odk.basedomain.mapper.UserProfileMapper;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.basedomain.repository.user.UserProfileRepository;
import com.odk.basemanager.api.user.IUserProfileManager;
import com.odk.baseutil.dto.user.UserProfileDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.enums.UserQueryTypeEnum;
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

    private UserQueryDomain userQueryDomain;
    private UserProfileRepository userProfileRepository;

    private UserProfileMapper userProfileMapper;

    private UserDomainMapper userDomainMapper;

    private UserCache userCache;


    @Override
    public boolean updateUserProfile(UserProfileDTO userProfileDTO) {
        UserProfileDO userProfileDO = userProfileRepository.findByUserIdAndTenantId(userProfileDTO.getUserId(), TenantIdContext.getTenantId());
        userProfileMapper.merge(userProfileDTO, userProfileDO);
        userProfileRepository.save(userProfileDO);
        UserEntity userEntity = userQueryDomain.queryUser(UserQueryCriteria.builder().queryType(UserQueryTypeEnum.SESSION).build());
        userEntity.setUserProfile(userDomainMapper.toEntity(userProfileDO));
        userCache.putUserToCache(userEntity);
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
    public void setUserQueryDomain(UserQueryDomain userQueryDomain) {
        this.userQueryDomain = userQueryDomain;
    }

    @Autowired
    public void setUserDomainMapper(UserDomainMapper userDomainMapper) {
        this.userDomainMapper = userDomainMapper;
    }

    @Autowired
    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }
}
