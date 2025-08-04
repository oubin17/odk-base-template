package com.odk.basedomain.mapper;

import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.baseutil.dto.user.UserProfileDTO;
import org.mapstruct.Mapper;

/**
 * UserProfileMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileDO toDO(UserProfileDTO userProfileDTO);
}
