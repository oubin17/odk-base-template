package com.odk.basemanager.api.user;

/**
 * IUserStatusManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/9/29
 */
public interface IUserStatusManager {

    Boolean freezeUser(String userId);

    Boolean unfreezeUser(String userId);
}
