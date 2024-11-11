package com.odk.baseweb.interceptor.listener;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * SaTokenListener
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/11
 */
@Component
public class CustomerTokenListener implements SaTokenListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomerTokenListener.class);


    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
        logger.info("登录监听：loginType:{}, loginId:{}, 设备类型:{} 登录成功，tokenValue={}", loginType, loginId, loginModel, tokenValue);
    }

    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        logger.info("登出监听：loginType:{}, loginId:{} 登出成功，tokenValue={}", loginType, loginId, tokenValue);
    }

    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {

    }

    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {

    }

    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {

    }

    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {

    }

    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {

    }

    @Override
    public void doCreateSession(String id) {

    }

    @Override
    public void doLogoutSession(String id) {

    }

    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

    }
}
