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

    USER_LIST_QUERY("USER_LIST_QUERY", "用户列表查询"),

    USER_PROFILE_UPDATE("USER_PROFILE_UPDATE", "用户信息更新"),

    USER_PERMISSION_QUERY("USER_PERMISSION_QUERY", "用户权限查询"),


    ROLE_LIST("ROLE_LIST", "角色列表"),

    USER_ROLE_ADD("USER_ROLE_ADD", "添加用户角色"),

    USER_ROLE_DELETE("USER_ROLE_DELETE", "删除用户角色"),

    ROLE_RELA_ADD("ROLE_RELA_ADD", "用户添加角色"),

    ROLE_RELA_DELETE("ROLE_RELA_DELETE", "用户删除角色"),

    USER_PERMISSION_ADD("USER_PERMISSION_ADD", "添加角色权限"),

    PERMISSION_LIST("PERMISSION_LIST", "权限列表"),

    PASSWORD_UPDATE("PASSWORD_UPDATE", "修改密码"),

    ENCODE_DATA("ENCODE_DATA", "加密"),

    VERIFICATION_CODE_GENERATE("VERIFICATION_CODE_GENERATE", "生成验证码"),

    VERIFICATION_CODE_COMPARE("VERIFICATION_CODE_COMPARE", "验证码校验"),

    EXCEL_UPLOAD("EXCEL_UPLOAD", "上传Excel"),

    REDISSON_LOCK_LOCK("REDISSON_LOCK_LOCK", "分布式锁"),


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
