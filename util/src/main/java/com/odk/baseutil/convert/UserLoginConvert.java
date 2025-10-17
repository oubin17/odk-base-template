package com.odk.baseutil.convert;

import com.odk.baseutil.dto.user.UserLoginDTO;
import com.odk.baseutil.entity.UserEntity;
import com.odk.baseutil.request.UserLoginRequest;
import com.odk.baseutil.response.UserLoginResponse;
import org.mapstruct.Mapper;

/**
 * UserLoginMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/23
 */
@Mapper(componentModel = "spring")
public interface UserLoginConvert {

    UserLoginDTO toDTO(UserLoginRequest userLoginRequest);

    UserLoginResponse toResponse(UserEntity userEntity);
}
