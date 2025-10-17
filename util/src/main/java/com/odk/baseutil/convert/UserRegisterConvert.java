package com.odk.baseutil.convert;

import com.odk.baseutil.dto.user.UserRegisterDTO;
import com.odk.baseutil.request.UserRegisterRequest;
import org.mapstruct.Mapper;

/**
 * UserRegisterMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/23
 */
@Mapper(componentModel = "spring")
public interface UserRegisterConvert {

    UserRegisterDTO toDTO(UserRegisterRequest userRegisterRequest);
}
