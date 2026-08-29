# Xquik 推文搜索插件

Xquik 插件让 AIRAG 应用搜索公开 X（Twitter）推文。

它支持关键词、用户和 X 搜索语法。预设只读取公开数据。

Xquik is an independent third-party service. Not affiliated with X Corp. "Twitter" and "X" are trademarks of X Corp.

## 配置

1. 创建 Xquik API 密钥。
2. 为后端进程设置 `JEECG_PLUGIN_XQUIK_API_KEY` 环境变量。
3. 运行 Flyway 数据库迁移。
4. 在 AI 应用中添加 `Xquik 推文搜索` 插件。

不要把 API 密钥写入插件 JSON 或数据库。

插件在调用时解析 `${JEECG_PLUGIN_XQUIK_API_KEY}`。缺少变量时，请求不会发送。

请求头只能引用 `JEECG_PLUGIN_` 前缀的变量。此限制防止插件读取其他服务密钥。

## 使用

工具名为 `search_x_tweets`。参数如下：

- `q`：关键词、用户名或 X 搜索表达式。
- `queryType`：`Latest` 或 `Top`。默认使用 `Latest`。
- `limit`：结果上限。建议使用 1 至 100。
- `cursor`：上一页返回的游标。

例如，`from:jeecgboot AI` 会搜索相关公开推文。

接口按返回结果计费。仅当 `has_next_page` 为 `true` 时传递 `next_cursor`。

接口合同见 [Xquik OpenAPI 文档](https://docs.xquik.com/openapi.yaml)。
