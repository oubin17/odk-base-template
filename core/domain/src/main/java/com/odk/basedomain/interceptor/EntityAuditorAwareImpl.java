package com.odk.basedomain.interceptor;

import com.odk.baseutil.userinfo.SessionContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditorAwareImpl
 * 当通过 JPA 的 save() 方法保存一个新实体时，Spring Data JPA 会自动触发，设置创建者和更新者
 *
 * 有些是定时任务触发，获取不到登录用户，则返回系统用户
 * @CreatedBy
 * @LastModifiedBy
 *
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/18
 */
@Component
public class EntityAuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            // 直接尝试获取用户ID，避免先调用 isLogin()
            String userId = SessionContext.getLoginIdWithCheck();
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            // 在非Web上下文或未登录时，返回系统用户
            return Optional.of("system");
        }
    }
}
