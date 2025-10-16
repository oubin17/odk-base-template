package com.odk.basedomain.domain.criteria;

import com.odk.baseutil.enums.UserQueryTypeEnum;
import lombok.Builder;
import lombok.Getter;

/**
 * UserQueryCriteria
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/28
 */
@Getter
@Builder
public class UserQueryCriteria {

    /**
     * 查询类型:不为空
     */
    private UserQueryTypeEnum queryType;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 登录 ID
     */
    private String loginId;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 是否允许为空: 默认为 false，也就是强制用户存在，不存在报错
     */
    private boolean nullAllowed;

    /**
     * 是否检查状态: 默认为 true，也就是强制检查用户状态，用户被冻结则报错
     */
    private boolean statusCheck;
}
