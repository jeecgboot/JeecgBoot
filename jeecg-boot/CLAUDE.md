# CLAUDE.md

> You should always answer questions in Simplified Chinese first, unless the user explicitly requests another language.

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JeecgBoot 3.9.3 — a Java low-code development platform built on **Spring Boot 3.5.5**, **Java 17** (also supports 21, 24). It runs as a monolithic app by default, with an optional Spring Cloud microservices mode. Uses `jakarta` namespace (not `javax`) throughout.

## Build & Run Commands

```bash
# Full build (tests are skipped by default via surefire config)
mvn clean package

# Build with tests
mvn clean package -DskipTests=false

# Run the standalone application (port 8080, context-path: /jeecg-boot)
cd jeecg-module-system/jeecg-system-start
mvn spring-boot:run

# Build a specific module (with dependencies)
mvn clean package -pl jeecg-boot-base-core -am

# Run a single test class
mvn test -DskipTests=false -pl <module> -Dtest=<TestClassName>

# Build with microservices modules included
mvn clean package -P SpringCloud

# Docker startup
./start-docker-compose.sh   # or start-docker-compose.bat on Windows
```

## Module Architecture

```
jeecg-boot-parent (root pom)
├── jeecg-boot-base-core          # Core framework: Shiro/JWT auth, MyBatis-Plus config,
│                                  # common utilities, AOP aspects, base controllers
├── jeecg-module-system            # System management (users, roles, permissions, dicts, menus)
│   ├── jeecg-system-api           # API interfaces (local-api vs cloud-api for mono/micro switch)
│   │   ├── jeecg-system-local-api # Direct method calls (monolithic)
│   │   └── jeecg-system-cloud-api # Feign clients (microservices)
│   ├── jeecg-system-biz           # Business logic, entities, mappers, services
│   └── jeecg-system-start         # Main entry point (JeecgSystemApplication), all configs
├── jeecg-boot-module              # Business feature modules
│   ├── jeecg-module-demo          # Demo/example code
│   ├── jeecg-boot-module-airag    # AI/RAG integration
│   ├── jeecg-boot-module-easyoa   # Simple OA module
│   ├── jeecg-boot-module-joa-flowable  # OA + Flowable workflow
│   ├── jeecg-boot-module-pay      # Payment module
│   └── jeecg-boot-module-wps      # WPS document integration
└── jeecg-boot-platform            # Low-code platform modules
    ├── jeecg-boot-module-bpm-flowable       # BPM workflow engine
    ├── jeecg-boot-module-airag-flow         # AI RAG flow
    ├── jeecg-boot-module-bigscreen          # Big screen/dashboard designer
    ├── jeecg-boot-module-desform            # Form designer
    ├── jeecg-boot-module-drag               # Drag-and-drop report designer
    ├── jeecg-boot-module-lowapp             # Low-code application engine
    ├── jeecg-boot-module-mindesflow-flowable # Simple flow designer
    └── jeecg-boot-module-online             # Online code generator & forms
```

Optional microservices modules (activated via `-P SpringCloud`):
- `jeecg-server-cloud/` — Gateway (port 9999), Nacos (8848), cloud service starters, monitoring (9111), XXL-Job (9080), Sentinel (9000)

## Key Technology Stack

| Layer | Technology |
|-------|-----------|
| ORM | MyBatis-Plus 3.5.12 (`BaseMapper<T>`, `ServiceImpl<M,T>`) |
| Auth | Apache Shiro 2.0.5 + JWT 4.5.0, Redis-backed sessions |
| DB Pool | Druid 1.2.24 with dynamic datasource support |
| DB Migration | Flyway (scripts in `jeecg-system-start/src/main/resources/flyway/sql/mysql/`) |
| JSON | FastJSON 2 |
| Excel | AutoPoi (`autopoi-spring-boot-3-starter`) |
| API Docs | Knife4j 4.5.0 (OpenAPI v3, `@Schema` annotations) |
| Scheduled Jobs | Quartz (JDBC store, clustered) |
| File Storage | MinIO / Aliyun OSS / Qiniu (controlled by `jeecg.uploadType` config) |
| Microservices | Spring Cloud 2025.0.0 + Alibaba (Nacos, Gateway, Sentinel) |

## Code Conventions & Patterns

**Package structure:** `org.jeecg.modules.<module-name>.{controller,entity,mapper,mapper.xml,service,service.impl,vo}`

**Naming conventions:**
- Entities: `Sys` prefix for system entities (e.g., `SysUser`, `SysRole`). Use `@TableName`, `@TableId(type = IdType.ASSIGN_ID)`
- Controllers: `<Entity>Controller extends JeecgController<Entity, IService>` — base class provides standard CRUD + Excel import/export
- Services: Interface `I<Entity>Service extends IService<Entity>`, impl `<Entity>ServiceImpl extends ServiceImpl<Mapper, Entity>`
- Mappers: `<Entity>Mapper extends BaseMapper<Entity>`, with XML in `mapper/xml/`

**Common annotations on entities:** `@Data`, `@EqualsAndHashCode(callSuper = false)`, `@Accessors(chain = true)`, `@TableName`

**API response wrapper:** `Result<T>` (from `org.jeecg.common.api.vo.Result`) — use `Result.OK(data)`, `Result.OK(msg, data)`, `Result.error(msg)`. The `result` field holds data, `success`/`code`/`message` hold status.

**Auto query building:** `QueryGenerator.initQueryWrapper(entity, request.getParameterMap())` auto-builds `QueryWrapper` from HTTP request params, supporting fuzzy match, range queries, etc.

**Monolithic ↔ Microservices switch:** The `jeecg-system-api` module has two implementations (`local-api` for direct calls, `cloud-api` for Feign). Switching is done by changing the dependency in the startup module, not by modifying business code.

**代码修改痕迹日志：** 所有新增或修改的代码块必须用 `update-begin` / `update-end` 注释包裹，格式如下：

```java
//update-begin---author:作者 ---date:YYYY-MM-DD  for：【bug号/需求号】修改说明-----------
// 新增或修改的代码
//update-end---author:作者 ---date:YYYY-MM-DD  for：【bug号/需求号】修改说明-----------
```

规则：
- `author` 填实际修改人，`date` 填修改日期（格式 `YYYY-MM-DD`），`for` 填 bug 号或需求号 + 简要说明
- 新增方法：`update-begin` 放在方法声明前，`update-end` 放在方法结束 `}` 后
- 修改已有方法中的代码：`update-begin` / `update-end` 只包裹被修改的代码段，不包裹整个方法
- 用户未提供 bug 号时，需要主动询问

## Database

**Supported:** MySQL 8.0+ (default), PostgreSQL, Oracle 11g+, SQL Server 2017+, MariaDB, DM8 (达梦), KingBase ES. Database-specific configs are in `application-{dbtype}.yml` profiles.

**Initial setup:** Import `db/jeecgboot-mysql-5.7.sql` for the base schema. Flyway handles incremental migrations (scripts organized by date folders like `202512/`).

**Flyway note:** In dev mode, `spring.main.lazy-initialization=true` is enabled for startup speed, which can interfere with Flyway auto-config. Flyway auto-config is explicitly excluded and managed separately.

## Configuration

Main config files are in `jeecg-module-system/jeecg-system-start/src/main/resources/`:
- `application.yml` — profile selector (active profile set by Maven: dev/test/prod/docker)
- `application-dev.yml` — development config (port 8080, lazy-init enabled)
- Dev environment requires: MySQL, Redis. Optional: MongoDB, RabbitMQ

Key config namespace: `jeecg.*` in YAML controls platform features (upload type, firewall settings, AI config, MinIO, shiro excludes, etc.).

## Docker Services (docker-compose.yml)

MySQL (port 13306), Redis, PostgreSQL+pgvector, MongoDB, and the application container (port 8080).

## Online 低代码模块 (jeecg-boot-module-online)

Online 模块采用**元数据驱动**架构，通过数据库配置表（`onl_cgform_*`）实现运行时 CRUD，无需生成代码。配置存在数据库中而非文件系统，Claude Code 无法直接读取具体表单配置，需用户提供 JSON 导出或截图。

**完整的配置 Schema、控件类型、默认值语法、增强机制等详见: [online-form-schema.md](online-form-schema.md)**
