package com.odk.basemanager.api.user;

import com.odk.baseutil.dto.user.PasswordUpdateDTO;
import com.odk.baseutil.request.password.PasswordSetRequest;

/**
 * IPasswordManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/8/1
 */
public interface IPasswordManager {

    /**
     * 密码更新
     *
     * @param passwordUpdateDTO
     * @return
     */
    boolean updatePassword(PasswordUpdateDTO passwordUpdateDTO);

    /**
     * 设置密码
     *
     * @param passwordSetRequest
     * @return
     */
    boolean setPassword(PasswordSetRequest passwordSetRequest);

    /**
     * 重置密码
     *
     * @param passwordSetRequest
     * @return
     */
    boolean reSetPassword(PasswordSetRequest passwordSetRequest);
}
