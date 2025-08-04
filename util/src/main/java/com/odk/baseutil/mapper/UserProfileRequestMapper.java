package com.odk.baseutil.mapper;

import com.odk.baseutil.dto.user.UserProfileDTO;
import com.odk.baseutil.request.UserProfileRequest;
import org.mapstruct.Mapper;

/**
 * UserProfileMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@Mapper(componentModel = "spring")
public interface UserProfileRequestMapper {

    UserProfileDTO toDTO(UserProfileRequest profileRequest);
}
