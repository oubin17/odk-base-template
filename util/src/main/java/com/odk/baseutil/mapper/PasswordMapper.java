package com.odk.baseutil.mapper;

import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import com.odk.baseutil.request.password.PasswordUpdateRequest;
import org.mapstruct.Mapper;

/**
 * PasswordMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/23
 */
@Mapper(componentModel = "spring")
public interface PasswordMapper {

    PasswordUpdateDTO toDTO(PasswordUpdateRequest passwordUpdateRequest);
}
