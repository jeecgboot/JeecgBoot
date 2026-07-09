# JeecgBoot 3.9.3 升级日志

> 发布日期：2026-07-07

## 概述

本次升级聚焦于基础设施现代化，将核心框架从 Spring Boot 3 迁移至 Spring Boot 4，同步升级 Spring Cloud、XXL-JOB、Nacos、Shiro、Sentinel 等关键组件，全面提升平台的技术栈基线。

---

## 一、Spring Boot 3 → 4 升级（3.5.5 → 4.1.0）

### 1.1 框架版本

| 组件 | 旧版本 | 新版本 |
|------|--------|--------|
| Spring Boot | 3.5.5 | **4.1.0** |
| Spring Framework | 6.x | **7.0** |
| Spring Cloud | 2024.x | **2025.1.2** |
| Spring Cloud Alibaba | 2024.x | **2025.1.0.0** |

### 1.2 兼容性修复

**Jackson 3 迁移**

Spring Boot 4 使用 Jackson 3（`tools.jackson`），替代 Jackson 2（`com.fasterxml.jackson`）。移除 `jackson-module-kotlin`（Jackson 2 专有，与 Jackson 3 不兼容，会导致 `ClassCastException`）。

```xml
<!-- jeecg-boot-base-core/pom.xml -->
<!-- 注释掉 jackson-module-kotlin，Jackson 2 与 Jackson 3 不兼容 -->
<!-- <dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-kotlin</artifactId>
</dependency> -->
```

**UriComponentsBuilder API 变更**

Spring Framework 7 移除了 `UriComponentsBuilder.fromHttpUrl()`，改用 `fromUriString()`：

```java
// OpenApiController.java
// 旧: UriComponentsBuilder.fromHttpUrl(url)
// 新: UriComponentsBuilder.fromUriString(url)
UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
```

**RedisMessageListenerContainer Bean 冲突**

Spring Boot 4.1 自动创建 `redisMessageListenerContainer`，与 JeecgBoot 自定义的 `redisContainer` 冲突，`DragWebSocket` 注入时出现二义性。

修复方案：新增 `RedisContainerPrimaryConfig`，通过 `BeanDefinitionRegistryPostProcessor` 将 `redisContainer` 标记为 `@Primary`：

```java
@Component
public class RedisContainerPrimaryConfig implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        if (registry.containsBeanDefinition("redisContainer")) {
            registry.getBeanDefinition("redisContainer").setPrimary(true);
        }
    }
}
```

### 1.3 其他适配

- Shiro 配置适配 Spring Framework 7 API 变更
- Druid 配置适配新版本 Spring Boot 自动装配
- Swagger3 / Knife4j 配置适配 Spring Boot 4
- 单元测试框架适配

---

## 二、XXL-JOB 2.4.1 → 3.4.2 升级

### 2.1 升级内容

| 组件 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| xxl-job-core | 2.4.1 | **3.4.2** | 核心调度引擎 |
| xxl-sso-core | — | **2.4.0** | 新增，3.x 管理平台登录认证 |
| xxl-tool | — | **2.5.0** | 工具库 |
| mybatis-spring-boot-starter | 3.0.3 | **4.0.1** | MyBatis 适配 Spring Boot 4 |

### 2.2 主要变更

**源码结构重组**
- 管理平台 controller 从 `com.xxl.job.admin.controller` 迁移到 `com.xxl.job.admin.business.controller`
- 调度器从 `com.xxl.job.admin.core.scheduler` 迁移到 `com.xxl.job.admin.business.scheduler`
- model/mapper 从 `core` 迁移到 `business` 包

**API 迁移：ReturnT → Response**

XXL-JOB 3.x 全面使用 `Response<T>` 替代 `ReturnT<T>`：

```java
// 旧
ReturnT<String> execute(String param) { return ReturnT.SUCCESS; }

// 新
Response<String> execute(String param) { return Response.SUCCESS; }
```

**认证方式变更**
- 2.x：数据库 `xxl_job_user` 表 MD5 密码
- 3.x：`xxl-sso` 单点登录框架，SHA-256 密码，默认账号 `admin/123456`

**执行器注册机制**
- 3.x 引入了新的执行器注册和发现机制
- `JeecgXxlJobSpringExecutor` 做了适配以支持 `@Bean` 方法上的 `@XxlJob` 注解扫描

**配置文件格式**
- application.yml → application.properties（解决 `xxl-sso.token.key` 等带点号/横线的 key 解析问题）

**数据库**
- 新数据库名 `xxl_job`，表结构变化需重新导入 `db/tables_xxl_job.sql`

### 2.3 保留的 JeecgBoot 定制

- `XxlJobAdminApplication` 启动日志
- `jeecg.xxljob` 配置前缀支持
- 邮件告警配置集成 JeecgBoot 邮件配置
- Nacos 服务发现适配

---

## 三、Nacos 2.3.2 → 3.2.2 升级

### 3.1 升级策略

使用重新打包的 Nacos 3.2.2，解决官方 Nacos 依赖兼容性问题。

**去掉 pom 中 springboot3 命名空间**：Nacos 模块不再继承 `jeecg-boot-parent`（`org.jeecgframework.boot3`），改为直接以 `spring-boot-starter-parent 4.1.0` 为 parent，退出主项目的 springboot3 版本体系，独立管理依赖。

### 3.2 启动模式

从单阶段启动改为**三阶段启动**：

```java
// 阶段1: Core Context（无 Web）
NacosServerBasicApplication → coreContext
// 阶段2: Server Web Context
NacosServerWebApplication → parent(coreContext)
// 阶段3: Console Context
NacosConsole → parent(coreContext)
```

启动类：`JeecgNacosApplication`

### 3.3 依赖管理

**Hessian 版本统一**

Nacos 3.x 多个依赖引入不同版本 Hessian（`com.caucho.hessian`、`com.alipay.sofa.hessian`），存在 API 冲突。排除所有传递 Hessian，统一使用 `com.caucho:hessian:4.0.66`。

```xml
<!-- Nacos Server、Console、JRaft 均需排除 hessian 传递依赖 -->
```

### 3.4 配置变更

- `application.yml` → `application.properties`
- 数据库名：`nacos`（新库，建表脚本 `db/tables_nacos.sql`）
- 控制台端口：`18080`，上下文路径：`/nacos`
- 部署模式：`merged`（三合一）
- 关闭 AI 扩展和广告开关
- 新增 `logback-spring.xml`
- **删除 springboot3/springboot2 命名空间切换规则**：不再支持 springboot2，新库默认只提供 springboot3/4 的配置

### 3.5 JVM 参数

```bash
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.io=ALL-UNNAMED
```

---

## 四、其他组件升级

| 组件 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| MyBatis-Plus | 3.5.14 | **3.5.16** | ORM 框架 |
| Druid | — | **1.2.28** | 数据库连接池 |
| JSqlParser | — | **5.0** | SQL 解析器 |
| Shiro | < 2.0 | **3.0.0** | 安全框架 |
| shiro-redis | — | **4.1.0** | Shiro Redis 支持（切换到 `io.github.weir2010`） |
| Sentinel Dashboard | 1.8.3 | **1.8.10** | 流量控制（支持 JDK17） |
| MinIO | 8.5.7 | **9.0.3** | 对象存储客户端 |
| commonmark | — | **0.28.0** | Markdown 解析库 |
| AutoPoi | — | **2.0.5** | Excel/Word 工具类（Spring Boot 3/4 专版） |
| commons-io | — | **2.21.0** | Apache Commons IO 工具库 |
| LangChain4j | 1.12.2 | **1.17.2** | AI/LLM 集成框架（正式稳定版） |
| LangChain4j Community BOM | 1.12.2-beta9 | **1.17.2-beta27** | 社区模型（通义/文心/智谱等） |
| Apache Tika | — | **3.3.1** | 文档内容解析器（PDF/HTML/Office）|

### 4.2 AI RAG 模块：LangChain4j 1.12.2 → 1.17.2

**langchain4j-pgvector 切回官方版本**

1.17.2 官方正式支持 pgvector 向量维度超过 1000（最高 2000），无需再依赖自定义 fork 版本。直接通过 `langchain4j-bom` 管理版本即可：

```xml
<!-- jeecg-boot-module-airag/pom.xml -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-bom</artifactId>
    <version>1.17.2</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<!-- pgvector 依赖直接从 BOM 继承官方 1.17.2 版本 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-pgvector</artifactId>
</dependency>
```

官方 pgvector 源码参考：https://github.com/langchain4j/langchain4j/tree/main/langchain4j-pgvector

**主要升级亮点（1.12.2 → 1.17.2）**

- `langchain4j-pgvector` 正式版（不再是 beta），支持 HNSW 索引（`IndexType.HNSW`），向量检索精度和性能优于 IVFFlat
- 新增 `schemaName()` 支持自定义 PostgreSQL schema
- 修复向量维度超过 1000（最高 2000）的限制问题
- `EmbeddingStore` API 趋于稳定，`search(EmbeddingSearchRequest)` 完整支持 Filter 过滤

### 4.1 新增 jeecg-boot-starter-sentinel（按需引入）

> ⚠️ **不兼容变更**：旧版 Sentinel 功能内置在 starter-cloud 中自动生效，新版改为**按需引入**，必须显式添加 `jeecg-boot-starter-sentinel` 依赖后才能使用 Sentinel 限流熔断功能。

本次封装了独立的 **jeecg-boot-starter-sentinel** 模块，将 Sentinel 限流降级能力抽离为公共 starter，与 `starter-cloud` 解耦。

**模块结构：**

```
jeecg-boot-starter-sentinel/
├── JeecgSentinelAutoConfiguration.java    # 自动配置（注册 Bean）
├── handler/
│   └── JeecgUrlBlockHandler.java          # 限流降级处理器（HTTP 429）
├── parser/
│   └── JeecgHeaderRequestOriginParser.java # 请求来源解析器（IP/参数）
└── feign/
    ├── JeecgSentinelFeign.java            # 自定义 Feign Builder
    └── JeecgSentinelInvocationHandler.java # Feign 调用拦截处理器
```

**功能要点：**
- Feign 熔断：支持 `@FeignClient` 的 `fallback`/`fallbackFactory` 自动注入
- 限流降级响应：被 Sentinel 限流时返回 HTTP 429 + 统一 JSON
- 请求来源解析：基于请求参数 `origin` 或 `X-Forwarded-For` 头
- 与 `starter-cloud` 解耦，按需引入

```xml
<dependency>
    <groupId>org.jeecgframework.boot3</groupId>
    <artifactId>jeecg-boot-starter-sentinel</artifactId>
</dependency>
```

---

## 五、数据库变更

| 数据库 | 说明 |
|--------|------|
| `xxl-job` | XXL-JOB 3.4.2 新库，需导入 `db/tables_xxl_job.sql` |
| `nacos` | Nacos 3.2.2 新库，建表脚本 `db/tables_nacos.sql` |

> ⚠️ Nacos 3.x 和 XXL-JOB 3.x 不会自动建表，需手动执行 `db/tables_nacos.sql` 和 `db/tables_xxl_job.sql`。

---

## 六、⚠️ 不兼容升级提醒

**Nacos 和 XXL-JOB 需要重新初始化数据库！**

- Nacos 2.x → 3.x、XXL-JOB 2.x → 3.x 均为**大版本升级**，表结构不兼容，需用新库重新建表
- **升级前请备份老库**，然后将老库中的配置（Nacos 配置项、XXL-JOB 任务等）手工迁移到新库
- 建表脚本：
  - Nacos：`db/tables_nacos.sql`
  - XXL-JOB：`db/tables_xxl_job.sql`

**访问地址变更**

| 服务 | 旧地址 | 新地址 |
|------|--------|--------|
| Nacos 控制台 | `http://localhost:8848/nacos` | `http://localhost:18080/nacos` |
| XXL-JOB 管理 | `http://localhost:9080/xxl-job-admin` | `http://localhost:9080/` |

> Nacos 端口从 8848 改为 18080；XXL-JOB 去掉了 `/xxl-job-admin` 项目后缀。

---

## 七、升级注意事项

1. **Java 版本**：仍需 Java 17+，Spring Boot 4 同时支持 Java 17/21/24
2. **XXL-JOB 执行器**：确保 `jeecg-boot-starter-job` 模块排除旧版 `xxl-job-core`，统一使用 3.4.2
3. **Nacos**：初次启动前需手动导入数据库表，三阶段启动完成后访问 `http://localhost:18080/nacos`
4. **Spring Boot 4**：注意 `jakarta` 命名空间（无变化，3.x 已采用）；Jackson 3 替代 Jackson 2
5. **线上部署**：微服务模块的 Dockerfile 中 jar 包名已更新为 `3.9.3`
6. **IDE 编码**：项目使用 Tab 缩进，properties/YAML 文件使用 UTF-8 编码
7. **jeecg-boot-starter 3.9.4 未上传至 Maven Central**：`jeecg-boot-starter` 3.9.4 版本**未上传至 Maven Central 官方仓库**，需在 `pom.xml` 中配置 jeecg 私有仓库方可下载：

```xml
<repositories>
    <repository>
        <id>jeecg</id>
        <name>jeecg Repository</name>
        <url>https://maven.jeecg.org/nexus/content/repositories/jeecg</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```

---

## 八、版本号对照

```
JeecgBoot:        3.9.2  → 3.9.3
Spring Boot:      3.5.5  → 4.1.0
Spring Cloud:     2024.x → 2025.1.2
SC Alibaba:       2024.x → 2025.1.0.0
XXL-JOB:          2.4.1  → 3.4.2
Nacos Server:     2.3.2  → 3.2.2
Shiro:            <2.0   → 3.0.0
shiro-redis:      —      → 4.1.0
MyBatis-Plus:     3.5.14 → 3.5.16
MyBatis-Starter:  3.0.3  → 4.0.1
Druid:            —      → 1.2.28
JSqlParser:       —      → 5.0
Sentinel:         1.8.3  → 1.8.10
MinIO:            8.5.7  → 9.0.3
LangChain4j:      1.12.2 → 1.17.2
LC4j Community:   1.12.2-beta9 → 1.17.2-beta27
Apache Tika:      —      → 3.3.1
commonmark:       —      → 0.28.0
AutoPoi:          —      → 2.0.5
commons-io:       —      → 2.21.0
```
