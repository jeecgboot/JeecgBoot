# LangChain4j 升级日志： 1.17.2

> 适用模块：`jeecg-boot-module-airag`
> 升级日期：2026-07-09
> 官方仓库：https://github.com/langchain4j/langchain4j/tree/main/langchain4j-pgvector

---

## 一、版本概览

| 组件 | 旧版本 | 新版本 |
|------|--------|--------|
| `langchain4j-bom` | 1.3.0 | **1.17.2** |
| `langchain4j-pgvector` | **1.3.0-beta9**（独立 beta） | **1.17.2**（正式稳定版） |
| `langchain4j-community-bom` | 1.3.0-beta9 | **1.17.2-beta27** |
| `com.pgvector:pgvector` 驱动 | ~0.1.4 | **0.1.6** |
| `org.postgresql:postgresql` | ~42.6.x | **42.7.11** |

> **重要**：`langchain4j-pgvector` 在 1.3.0 周期中仍是独立 beta 版本（`1.3.0-beta9`），到 1.17.2 正式毕业为稳定版（GA），API 不再有 breaking change 风险。

---

## 二、核心变更：pgvector 向量维度限制修复

### 问题背景

旧版 `com.pgvector:pgvector` Java 驱动（~0.1.4）在创建 `PGvector` 对象时存在维度上限约束，导致使用超过 1000 维的嵌入模型（如 `text-embedding-3-large` 的 1536 维、部分 3072 维模型）时报错。

JeecgBoot 为此维护了一份**自定义 fork**以绕过该限制。

### 修复结果

升级到官方 `com.pgvector:pgvector:0.1.6` 后，配合 pgvector PostgreSQL 扩展 0.7.0+，**最大支持维度提升至 2000**，可直接使用官方版本，无需 fork。

```xml
<!-- 切回官方版本，通过 BOM 统一管理 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-bom</artifactId>
    <version>1.17.2</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>

<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-pgvector</artifactId>
    <!-- 无需指定 version，由 BOM 管理 -->
</dependency>
```

---

## 三、新功能

### 3.1 混合检索（Hybrid Search）

> **PR #4288，2026-02-03**

新增 `SearchMode` 枚举，支持**向量检索 + 全文检索**融合排名（Reciprocal Rank Fusion，RRF）。

#### 新增 Builder 参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `searchMode` | `SearchMode` | `VECTOR` | 检索模式 |
| `textSearchConfig` | `String` | `"simple"` | PostgreSQL 全文搜索配置 |
| `rrfK` | `Integer` | `60` | RRF 融合算法的 k 参数 |

```java
// SearchMode 枚举值
public enum SearchMode {
    VECTOR,   // 纯向量检索（默认，与旧版行为一致）
    HYBRID    // 向量 + 全文检索，RRF 融合排名
}
```

#### 使用示例

```java
// 纯向量检索（默认，保持向后兼容）
PgVectorEmbeddingStore store = PgVectorEmbeddingStore.builder()
    .host("localhost").port(5432)
    .user("postgres").password("xxx").database("mydb")
    .table("embeddings")
    .dimension(1536)
    .useIndex(true).indexListSize(100)
    .build();

// 混合检索（新功能）
PgVectorEmbeddingStore store = PgVectorEmbeddingStore.builder()
    .host("localhost").port(5432)
    .user("postgres").password("xxx").database("mydb")
    .table("embeddings")
    .dimension(1536)
    .useIndex(true).indexListSize(100)
    .searchMode(SearchMode.HYBRID)   // 开启混合检索
    .textSearchConfig("simple")       // PG 全文配置，中文推荐 "simple"，英文可用 "english"
    .rrfK(60)                         // RRF k 参数，值越大重排范围越大
    .build();
```

> **HYBRID 模式原理**：同时执行向量 ANN 检索和 PostgreSQL `tsvector` 全文检索，然后通过 RRF 算法合并两组结果的排名，提升稀疏词语（专有名词、代码片段）的召回率。

---

### 3.2 DatasourceBuilder — 外部 DataSource 注入

> 较新版本新增

新增 `PgVectorEmbeddingStore.datasourceBuilder()` 工厂方法，适用于已有 Spring 连接池的场景，避免重复创建数据库连接。

```java
// 旧版：只能由 pgvector 内部创建 DataSource
PgVectorEmbeddingStore.builder()
    .host("...").port(5432)...

// 新版：直接注入 Spring 管理的 DataSource
@Autowired
DataSource dataSource;   // Spring 的主数据源或专用 DataSource

EmbeddingStore<TextSegment> store = PgVectorEmbeddingStore.datasourceBuilder()
    .datasource(dataSource)              // 注入外部 DataSource
    .table("embeddings")
    .dimension(1536)
    .createTable(true)
    .searchMode(SearchMode.VECTOR)
    .build();
```

> **注意**：`datasourceBuilder()` 和 `builder()` 支持相同的配置参数（`searchMode`、`textSearchConfig`、`rrfK`、`skipCreateVectorExtension` 等）。

---

### 3.3 `skipCreateVectorExtension` — 跳过扩展创建

> **PR #4576，2026-02-12**

```java
.skipCreateVectorExtension(true)  // 默认 false
```

**适用场景**：云数据库（AWS RDS、阿里云 PolarDB）或受管 PostgreSQL 中，数据库用户**无 superuser 权限**，无法执行 `CREATE EXTENSION IF NOT EXISTS vector`。由 DBA 预装 pgvector 扩展后，应用侧设置此参数跳过。

---

### 3.4 `addAll` 支持预分配 ID

> **PR #2156，2024-12-04**

新增 `EmbeddingStore` 接口方法重载，支持外部指定 ID 写入（幂等写入、增量同步场景）：

```java
// 旧版：ID 由 pgvector 自动生成（UUID）
List<String> ids = store.addAll(embeddings, segments);

// 新版：支持外部预分配 ID
List<String> customIds = List.of("doc-001", "doc-002", "doc-003");
store.addAll(customIds, embeddings, segments);
```

---

## 四、Bug 修复 & 性能优化

### 4.1 搜索 SQL 重写，索引真正生效（PR #2485，2025-02-04）

**问题**：旧版 `1.3.0-beta9` 的查询 SQL 写法导致 IVFFlat 向量索引**实际失效**，数据量大时全表扫描，响应慢。

**修复**：重写搜索 SQL，`useIndex(true)` 设置后索引真正被使用，高数据量场景性能显著提升。

> ⚠️ **升级后建议**：若生产环境已建立 IVFFlat 索引，升级后可用 `EXPLAIN ANALYZE` 验证查询是否走了索引。

---

### 4.2 减少不必要的数据库连接（PR #4422，2026-01-14）

旧版在 Store 初始化时会创建额外的 DB 连接用于检测，新版优化了连接获取逻辑，减少连接池压力，启动速度更快。

---

### 4.3 Schema 名称处理修复（PR #4592，2026-02-18）

**问题**：当 `table` 参数包含 schema 前缀（如 `"myschema.embeddings"`）时，创建索引的 SQL 会错误地将整个字符串当作表名，导致索引名含非法字符。

**修复**：建索引时自动从表名中剥离 schema 前缀，仅用纯表名生成索引名。

---

## 五、工程改进

### 5.1 去 Lombok（PR #2751，2025-03-21）

移除了 Lombok 注解（`@Builder`、`@Data`、`@Getter` 等），改为手写 Builder 和 getter 代码（Delombok）。

- 减少编译期注解处理器依赖
- 更好地兼容 Java 17+ / GraalVM 原生编译
- IDE 代码提示更准确

### 5.2 依赖升级

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| `com.pgvector:pgvector` | ~0.1.4 | **0.1.6** | 最大维度 1000 → 2000 |
| `org.postgresql:postgresql` | ~42.6.x | **42.7.11** | 安全修复、性能优化 |

---

## 六、1.3.0-beta9 已有、未变更的功能

以下功能在 1.3.0-beta9 中已存在，1.17.2 保持兼容，**无 breaking change**：

| 功能 | 说明 |
|------|------|
| `MetadataStorageConfig` | JSON / JSONB / COMBINED_JSONB / COLUMN_PER_KEY 四种元数据存储模式 |
| `removeAll(Collection<String> ids)` | 按 ID 批量删除向量 |
| `removeAll(Filter filter)` | 按 Filter 条件删除向量 |
| `search(EmbeddingSearchRequest)` | 带 Filter 的向量检索 |
| `useIndex` / `indexListSize` | IVFFlat 索引（注意：旧版索引实际失效，4.1 已修复） |
| `createTable` / `dropTableFirst` | 表生命周期管理 |
| `host/port/user/password/database/table/dimension` | 基础连接配置 |

---

## 七、JeecgBoot 迁移说明

### 7.1 pom.xml 配置

```xml
<!-- jeecg-boot-module-airag/pom.xml -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-bom</artifactId>
            <version>1.17.2</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-community-bom</artifactId>
            <version>1.17.2-beta27</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- pgvector 切回官方版本，由 BOM 管理 -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-pgvector</artifactId>
    </dependency>
</dependencies>
```

### 7.2 代码兼容性

`EmbeddingHandler.java` 中现有的 Builder 调用**无需修改**，1.17.2 完全向后兼容：

```java
// 现有代码，升级后继续有效
EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
    .host(embedStoreConfigBean.getHost())
    .port(embedStoreConfigBean.getPort())
    .database(embedStoreConfigBean.getDatabase())
    .user(embedStoreConfigBean.getUser())
    .password(embedStoreConfigBean.getPassword())
    .table(tableName)
    .dimension(embeddingModel.dimension())
    .useIndex(true)
    .indexListSize(100)
    .createTable(true)
    .dropTableFirst(false)
    .build();
```

### 7.3 可选：开启混合检索提升效果

```java
// 如需开启混合检索，在现有 builder 后追加即可
EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
    .host(embedStoreConfigBean.getHost())
    // ... 其他参数保持不变 ...
    .searchMode(SearchMode.HYBRID)   // 新增
    .textSearchConfig("simple")       // 新增，中文场景推荐 "simple"
    .build();
```

> **注意**：`HYBRID` 模式的 `EmbeddingSearchRequest.query()` 需要包含文本内容（非空），否则全文检索部分会报错。

---

## 八、升级影响总结

| 变更项 | 影响级别 | 是否需要改代码 |
|--------|----------|----------------|
| 维度限制修复（2000 维） | 🔴 **高**（核心原因） | 否，透明修复 |
| 搜索 SQL 重写（索引生效） | 🔴 **高**（性能改善） | 否，透明修复 |
| Hybrid Search | 🟡 中（可选新功能） | 按需添加 `.searchMode()` |
| DatasourceBuilder | 🟡 中（可选优化） | 按需迁移 |
| skipCreateVectorExtension | 🟢 低（云环境按需） | 按需添加 |
| addAll with IDs | 🟢 低（可选新 API） | 按需使用 |
| 去 Lombok | 🟢 低（透明） | 否 |
| 依赖版本升级 | 🟢 低 | 否（BOM 自动管理） |
