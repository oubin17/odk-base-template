package com.odk.basedomain.mapper;

import com.odk.basedomain.model.permission.UserRoleDO;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.entity.AccessTokenEntity;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.entity.UserProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * UserDomainMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/23
 */
@Mapper(componentModel = "spring")
public interface UserDomainMapper {

    @Mapping(source = "id", target = "userId")
    UserEntity toEntity(UserBaseDO baseDO);

    AccessTokenEntity toEntity(UserAccessTokenDO userAccessTokenDO);

    UserProfileEntity toEntity(UserProfileDO userProfileDO);

    UserRoleDTO toDTO(UserRoleDO userRoleDO);
}
