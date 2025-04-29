package com.odk.baseutil.mapper;

import com.odk.baseutil.dto.verificationcode.VerificationCodeDTO;
import com.odk.baseutil.request.VerificationCodeRequest;
import org.mapstruct.Mapper;

/**
 * VerificationCodeMapper
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/29
 */
@Mapper(componentModel = "spring")
public interface VerificationCodeMapper {

    VerificationCodeDTO toDTO(VerificationCodeRequest verificationCodeRequest);
}
