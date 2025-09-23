package com.odk.basedomain.mapper;

import com.odk.basedomain.model.permission.UserRoleDO;
import com.odk.basedomain.model.user.UserAccessTokenDO;
import com.odk.basedomain.model.user.UserBaseDO;
import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.baseutil.dto.permission.UserRoleDTO;
import com.odk.baseutil.entity.AccessTokenEntity;
import com.odk.baseutil.entity.RoleEntity;
import com.odk.baseutil.entity.UserBaseEntity;
import com.odk.baseutil.entity.UserProfileEntity;
import org.mapstruct.Mapper;

/**
 * UserDomainMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/23
 */
@Mapper(componentModel = "spring")
public interface UserDomainMapper {

//    @Mapping(source = "id", target = "userId")
//    UserEntity toEntity(UserBaseDO baseDO);

    UserBaseEntity toEntity(UserBaseDO userBaseDO);

    AccessTokenEntity toEntity(UserAccessTokenDO userAccessTokenDO);

    UserProfileEntity toEntity(UserProfileDO userProfileDO);

    RoleEntity toEntity(UserRoleDO userRoleDO);

    UserRoleDTO toDTO(UserRoleDO userRoleDO);

}
