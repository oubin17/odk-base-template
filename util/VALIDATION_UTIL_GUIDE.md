# ValidationUtil 使用指南

## 📋 概述

`ValidationUtil` 是一个通用的数据验证工具类，提供常用的格式验证方法，包括：
- ✅ 手机号验证
- ✅ 邮箱验证  
- ✅ 身份证验证
- ✅ 数据脱敏

位置：`com.odk.baseutil.validate.ValidationUtil`

---

## 📱 手机号验证

### 支持的号段

中国大陆手机号，支持以下号段：
- 13x, 14x, 15x, 16x, 17x, 18x, 19x

### 使用方法

#### 1. 简单验证（返回 boolean）

```java
boolean valid = ValidationUtil.isValidMobile("13800138000"); // true
boolean invalid = ValidationUtil.isValidMobile("12345678901"); // false
```

#### 2. 验证并抛出默认异常

```java
// 如果手机号无效，抛出 BizException(PARAM_ILLEGAL, "手机号格式不正确")
ValidationUtil.validateMobile("13800138000"); 
```

#### 3. 验证并抛出自定义异常

```java
// 自定义错误码和错误消息
ValidationUtil.validateMobile(
    phoneNumber, 
    BizErrorCode.USER_PHONE_INVALID, 
    "用户手机号格式错误"
);
```

### 实际应用场景

#### 场景 1：验证码生成时校验手机号

```java
@Override
@BizProcess(bizScene = BizScene.VERIFICATION_CODE_GENERATE)
public ServiceResponse<VerificationCodeEntity> generateCode(VerificationCodeRequest request) {
    // 未登录场景：verifyKey 必须是手机号
    if (SCENE_LIST.contains(dto.getVerifyScene())) {
        AssertUtil.notNull(request.getVerifyKey(), BizErrorCode.PARAM_ILLEGAL);
        ValidationUtil.validateMobile(request.getVerifyKey()); // ✅ 校验手机号
    }
    
    // 业务处理...
}
```

#### 场景 2：用户注册时校验手机号

```java
public ServiceResponse<String> register(UserRegisterRequest request) {
    // 校验手机号格式
    ValidationUtil.validateMobile(request.getMobile());
    
    // 检查手机号是否已注册
    UserDO existUser = userRepository.findByMobile(request.getMobile());
    AssertUtil.isNull(existUser, BizErrorCode.USER_HAS_EXISTED);
    
    // 继续注册流程...
}
```

#### 场景 3：修改手机号时校验

```java
public ServiceResponse<Boolean> updateMobile(UpdateMobileRequest request) {
    String userId = SessionContext.getLoginIdWithCheck();
    
    // 校验新手机号格式
    ValidationUtil.validateMobile(request.getNewMobile());
    
    // 检查新手机号是否已被其他用户使用
    UserDO existUser = userRepository.findByMobile(request.getNewMobile());
    if (existUser != null && !userId.equals(existUser.getId())) {
        throw new BizException(BizErrorCode.USER_PHONE_EXISTS);
    }
    
    // 更新手机号...
}
```

---

## 📧 邮箱验证

### 使用方法

```java
// 1. 简单验证
boolean valid = ValidationUtil.isValidEmail("test@example.com"); // true

// 2. 验证并抛出异常
ValidationUtil.validateEmail("test@example.com");
```

### 实际应用场景

```java
public ServiceResponse<Boolean> bindEmail(BindEmailRequest request) {
    // 校验邮箱格式
    ValidationUtil.validateEmail(request.getEmail());
    
    // 检查邮箱是否已被绑定
    UserDO existUser = userRepository.findByEmail(request.getEmail());
    AssertUtil.isNull(existUser, BizErrorCode.EMAIL_ALREADY_BOUND);
    
    // 绑定邮箱...
}
```

---

## 🆔 身份证验证

### 使用方法

```java
// 1. 简单验证（18位身份证号）
boolean valid = ValidationUtil.isValidIdCard("110101199001011234"); // true

// 2. 验证并抛出异常
ValidationUtil.validateIdCard("110101199001011234");
```

### 实际应用场景

```java
public ServiceResponse<Boolean> realNameAuth(RealNameAuthRequest request) {
    // 校验身份证号格式
    ValidationUtil.validateIdCard(request.getIdCard());
    
    // 实名认证逻辑...
}
```

---

## 🔒 数据脱敏

### 手机号脱敏

```java
String masked = ValidationUtil.maskMobile("13800138000"); 
// 结果：138****1234
```

### 邮箱脱敏

```java
String masked = ValidationUtil.maskEmail("test@example.com"); 
// 结果：t***@example.com
```

### 实际应用场景

#### 场景 1：日志中脱敏

```java
log.info("发送验证码到手机号: {}", ValidationUtil.maskMobile(phoneNumber));
// 输出：发送验证码到手机号: 138****1234
```

#### 场景 2：返回给前端时脱敏

```java
public ServiceResponse<UserInfo> getUserInfo(String userId) {
    UserDO user = userRepository.findById(userId).orElse(null);
    
    UserInfo info = userConvert.toEntity(user);
    // 脱敏手机号
    info.setMobile(ValidationUtil.maskMobile(user.getMobile()));
    
    return ServiceResponse.valueOfSuccess(info);
}
```

---

## 💡 最佳实践

### ✅ 推荐做法

1. **在入口处校验**
   ```java
   // Controller 层或 Service 层入口就校验
   ValidationUtil.validateMobile(request.getMobile());
   ```

2. **结合 AssertUtil 使用**
   ```java
   AssertUtil.notNull(request.getMobile(), BizErrorCode.PARAM_ILLEGAL);
   ValidationUtil.validateMobile(request.getMobile());
   ```

3. **日志中脱敏**
   ```java
   log.info("用户手机号: {}", ValidationUtil.maskMobile(mobile));
   ```

4. **返回前端时脱敏**
   ```java
   userInfo.setMobile(ValidationUtil.maskMobile(user.getMobile()));
   ```

### ❌ 避免做法

1. **不要重复校验**
   ```java
   // ❌ 错误：多次校验
   ValidationUtil.validateMobile(mobile);
   ValidationUtil.validateMobile(mobile); // 重复了
   
   // ✅ 正确：只校验一次
   ValidationUtil.validateMobile(mobile);
   ```

2. **不要在循环中校验**
   ```java
   // ❌ 错误
   for (String mobile : mobileList) {
       ValidationUtil.validateMobile(mobile); // 性能差
   }
   
   // ✅ 正确：批量校验或使用其他方式
   ```

3. **不要忘记脱敏**
   ```java
   // ❌ 错误：日志中暴露完整手机号
   log.info("手机号: {}", mobile);
   
   // ✅ 正确：脱敏后记录
   log.info("手机号: {}", ValidationUtil.maskMobile(mobile));
   ```

---

## 📊 验证规则说明

### 手机号规则

```regex
^1[3-9]\d{9}$
```

- 必须以 1 开头
- 第二位是 3-9
- 总共 11 位数字

**有效示例：**
- ✅ 13800138000
- ✅ 15912345678
- ✅ 18612345678

**无效示例：**
- ❌ 12345678901（第二位不是 3-9）
- ❌ 1380013800（只有 10 位）
- ❌ 138001380001（12 位）

### 邮箱规则

```regex
^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$
```

**有效示例：**
- ✅ test@example.com
- ✅ user_name@domain.co.cn

**无效示例：**
- ❌ @example.com（缺少用户名）
- ❌ test@（缺少域名）

### 身份证规则

```regex
^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$
```

- 18 位
- 前 6 位：地区码
- 中间 8 位：出生日期（YYYYMMDD）
- 后 4 位：顺序码 + 校验码

---

## 🔧 扩展建议

如果需要添加更多验证规则，可以：

1. **添加新的正则表达式**
   ```java
   private static final Pattern XXX_PATTERN = Pattern.compile("...");
   ```

2. **添加验证方法**
   ```java
   public static boolean isValidXXX(String value) {
       if (StringUtils.isBlank(value)) {
           return false;
       }
       return XXX_PATTERN.matcher(value).matches();
   }
   
   public static void validateXXX(String value) {
       if (!isValidXXX(value)) {
           throw new BizException(BizErrorCode.PARAM_ILLEGAL, "XXX格式不正确");
       }
   }
   ```

3. **添加脱敏方法**
   ```java
   public static String maskXXX(String value) {
       // 脱敏逻辑
   }
   ```

---

## 📝 总结

| 功能 | 方法 | 说明 |
|------|------|------|
| 手机号验证 | `isValidMobile()` | 返回 boolean |
| 手机号校验 | `validateMobile()` | 无效时抛异常 |
| 邮箱验证 | `isValidEmail()` | 返回 boolean |
| 邮箱校验 | `validateEmail()` | 无效时抛异常 |
| 身份证验证 | `isValidIdCard()` | 返回 boolean |
| 身份证校验 | `validateIdCard()` | 无效时抛异常 |
| 手机号脱敏 | `maskMobile()` | 138****1234 |
| 邮箱脱敏 | `maskEmail()` | t***@example.com |

**核心原则：**
- ✅ 入口校验，尽早失败
- ✅ 日志脱敏，保护隐私
- ✅ 前端脱敏，安全第一
