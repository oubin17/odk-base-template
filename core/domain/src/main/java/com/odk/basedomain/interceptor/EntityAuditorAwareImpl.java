package com.odk.basedomain.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditorAwareImpl
 * 当通过 JPA 的 save() 方法保存一个新实体时，Spring Data JPA 会自动触发，设置创建者和更新者
 * @CreatedBy
 * @LastModifiedBy
 *
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/18
 */
@Component
public class EntityAuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return StpUtil.isLogin() ? Optional.ofNullable((Long)StpUtil.getSession().getLoginId()) : Optional.empty();
    }
}
