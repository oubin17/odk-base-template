package com.odk.basemanager.api.privacy;

/**
 * IUserPrivacyManager
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/28
 */
public interface IUserPrivacyManager {


    /**
     * 注册后签署
     *
     * @param userId
     * @param version
     * @return
     */
    void registerAgree(String userId, String version);

    /**
     * 同意隐私协议
     * 同意用户协议
     * @param version
     * @return
     */
    boolean agree(String version);

    /**
     * 撤销隐私协议
     * 撤销用户协议
     * @param version
     * @return
     */
    boolean revoke(String version);
}
