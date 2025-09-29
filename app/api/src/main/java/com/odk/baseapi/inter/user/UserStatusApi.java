package com.odk.baseapi.inter.user;

import com.odk.base.vo.response.ServiceResponse;

/**
 * UserStatusApi
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
public interface UserStatusApi {

    ServiceResponse<Boolean> freezeUser(String userId);

    ServiceResponse<Boolean> unfreezeUser(String userId);
}
