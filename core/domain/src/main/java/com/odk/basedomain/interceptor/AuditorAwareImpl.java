package com.odk.basedomain.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditorAwareImpl
 * 当通过 JPA 的 save() 方法保存一个新实体时，Spring Data JPA 会自动触发
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2024/11/18
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable((String) StpUtil.getSession().getLoginId());
    }
}
