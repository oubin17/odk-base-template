package com.odk.baseutil.enums;

/**
 * BizScene
 *
 * @description:
 * @version: 1.0
 * @author: oubin on 2023/11/11
 */
public enum BizScene {
    HELLO_WORLD("HELLO_WORLD", "HELLO_WORLD"),

    USER_REGISTER("USER_REGISTER", "用户注册"),

    USER_LOGIN("USER_LOGIN", "用户登录"),

    USER_LOGOUT("USER_LOGOUT", "注销登录"),

    USER_QUERY("USER_QUERY", "用户查询"),

    USER_PERMISSION_QUERY("USER_PERMISSION_QUERY", "用户权限查询"),

    USER_ROLE_ADD("USER_ROLE_ADD", "添加用户角色"),

    USER_ROLE_DELETE("USER_ROLE_DELETE", "删除用户角色"),

    ROLE_RELA_ADD("ROLE_RELA_ADD", "用户添加角色"),

    USER_PERMISSION_ADD("USER_PERMISSION_ADD", "添加角色权限"),

    PASSWORD_UPDATE("PASSWORD_UPDATE", "修改密码"),




    ;


    private final String code;

    private final String description;

    BizScene(String code, String description) {

        this.code = code;
        this.description = description;
    }


    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
