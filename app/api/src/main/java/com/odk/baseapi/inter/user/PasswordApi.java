package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;
import com.odk.baseutil.request.password.PasswordSetRequest;
import com.odk.baseutil.request.password.PasswordUpdateRequest;

/**
 * PasswordApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/3/7
 */
public interface PasswordApi {

    /**
     * 设置密码
     *
     * @param passwordSetRequest
     * @return
     */
    ServiceResponse<Boolean> setPassword(PasswordSetRequest passwordSetRequest);

    /**
     * 重置密码
     *
     * @param passwordSetRequest
     * @return
     */
    ServiceResponse<Boolean> reSetPassword(PasswordSetRequest passwordSetRequest);

    /**
     * 更新密码
     *
     * @param passwordUpdateRequest
     * @return
     */
    ServiceResponse<Boolean> passwordUpdate(PasswordUpdateRequest passwordUpdateRequest);

}
