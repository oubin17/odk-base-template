package com.odk.basedomain.convert;

import com.odk.basedomain.model.user.UserProfileDO;
import com.odk.baseutil.dto.user.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * UserProfileMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/4
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    void merge(UserProfileDTO userProfileDTO, @MappingTarget UserProfileDO userProfileDO);

}
