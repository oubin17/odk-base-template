# odk-base-template

![GitHub](https://img.shields.io/badge/license-MIT-blue) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen) ![Java](https://img.shields.io/badge/Java-17-blueviolet)

> **一个现代化、模块化、高性能的 Spring Boot 3 项目基础框架，助你快速构建应用！**

---

## 🌟 项目简介

`odk-base-template` 是一个基于 **Spring Boot 3** 的基础框架模板，根据maven archetype脚手架指令可快速生成项目，旨在为开发者提供一套开箱即用解决方案。无论你是构建微服务架构还是单体应用，这个模板都能为你节省大量时间，专注于业务逻辑的实现。

---

## 🚀 主要特性

- **现代化技术栈**：基于 Spring Boot 3 和 Java 17，支持最新的 LTS 版本。
- **模块化设计**：清晰的模块划分，便于扩展和维护。
- **高拓展性**：项目融合微服务的思想，非常方便由单体应用拓展到微服务应用。
- **开箱即用**：内置常用功能模块（如日志管理、异常处理、统一返回格式等）。
- **高性能**：优化的配置和依赖管理，确保运行效率。
- **易于集成**：支持与主流中间件（如 Redis、MySQL、RabbitMQ 等）无缝集成。项目采用配置分离的方式，常用中间件的集成由工程[odk-common-util](https://github.com/oubin17/odk-common-util)实现，本项目只需引入 odk-common-util 对应模块即可。
- **完善的文档**：详细的代码注释和使用说明，降低学习成本。

---

## ✈️ 功能模块
- **web**：提供 http 接口
- **rpc**：预留
- **api**：服务统一对外功能接口，调用方可以是 http，也可以是 rpc
- **service**：服务处理模板封装，参数校验、参数转换、异常处理、日志打印
- **manager**：核心实现模块，业务逻辑处理
- **domain**：领域模块，领域模型封装
- **infra**：基础设施模块，外部系统集成
- **util**：提供常用工具类
- **bootstrap**：启动模块

---

## 🔧 技术栈

以下是本项目使用的核心技术和工具：

- **后端框架**：
  - [Spring Boot 3](https://spring.io/projects/spring-boot)
  - [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
  - [Sa-Token](https://sa-token.cc/index.html)
  - Spring Boot 全家桶，如 Spring MVC、 Redis Starter等。
- **数据库**：
  - MySQL / Oracle
  - Redis（缓存支持）
  - ElasticSearch（倒排索引）
- **消息队列**：
  - RabbitMQ / Kafka（可选）
- **安全框架**：
  - Sa-Token（单点登录、权限认证）
  - 验证码注册、登录
- **构建工具**：
  - Maven
- **其他**：
  - 多租户支持（核心注册登录功能支持多租户，无需部署多服务）
  - 统一异常处理和日志打印
  - Lombok（简化代码）
  - Snowflake ID 生成器（分布式唯一 ID 生成）
  - Tracer (链路追踪)
  - MapStruct（对象映射）
  - Jackson（JSON 处理）
  - Redisson（分布式锁）

---

## 📦 快速开始

### 前置条件

- JDK 17+
- Maven 3.8+
- MySQL / Oracle 数据库
- Redis

### 安装步骤

1. 克隆项目到本地：
> git clone https://github.com/oubin17/odk-base-template.git
2. 修改配置文件：
   在 `src/main/resources/application.yml` 和 `src/main/resources/application-dev.yml` 中填写你的数据库连接信息和其他配置。
3. 执行 SQL 初始化脚本：https://github.com/oubin17/base-template/blob/main/z-non-project-file/odk_template.sql
4. 启动项目：
> mvn spring-boot:run

## 📚 目录结构
![odk-base-template](https://github.com/oubin17/base-template/blob/main/z-non-project-file/odk-module.png?raw=true)


---

## 🤝 如何贡献

非常欢迎任何形式的贡献！如果你对项目有任何改进建议或发现了问题，请随时提交 Issue 或 PR。

1. Fork 本仓库。
2. 创建你的分支：`git checkout -b feature/your-feature-name`
3. 提交更改：`git commit -m "Add some awesome features"`
4. 推送到分支：`git push origin feature/your-feature-name`
5. 提交 Pull Request。


---

## 🌐 联系方式

如果你有任何问题或建议，可以通过以下方式联系我：

- Email: oub8685@163.com
- GitHub: [@oubin17](https://github.com/oubin17)

---

**🌟 Star 本项目，支持我们做得更好！**
