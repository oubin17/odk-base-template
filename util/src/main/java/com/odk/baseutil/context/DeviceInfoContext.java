package com.odk.baseutil.context;

import lombok.Data;

/**
 * DeviceInfo
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2026/4/29
 */
public class DeviceInfoContext {

    private static final ThreadLocal<DeviceInfo> LOCAL = new ThreadLocal<>();

    @Data
    public static class DeviceInfo {
        // 设备类型：android / ios
        private String deviceType;
        // 设备唯一ID
        private String deviceUid;
    }

    // 设置
    public static void set(DeviceInfo deviceUid) {
        LOCAL.set(deviceUid);
    }

    // 获取
    public static DeviceInfo get() {
        return LOCAL.get();
    }

    // 清除
    public static void remove() {
        LOCAL.remove();
    }
}
