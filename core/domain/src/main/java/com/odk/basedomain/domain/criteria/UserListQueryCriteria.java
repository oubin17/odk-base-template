package com.odk.basedomain.domain.criteria;

import com.odk.baseutil.enums.UserQueryTypeEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * UserListQueryCriteria
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2025/4/28
 */
@Getter
@Builder
public class UserListQueryCriteria {

    /**
     * 查询类型
     */
    private UserQueryTypeEnum queryType;

    /**
     * 用户 ID 列表
     */
    private List<String> userIds;
}
