# 教科研模块集成说明

此目录包含用于在 JeecgBoot 中集成“教科研管理模块”的数据库建表脚本与基础后端样例。

集成步骤简要：
1. 在 release_v3 分支下创建或将本模块合并到子模块 `jeecg-boot-module-teaching`。
2. 将 SQL 导入到 Jeecg 使用的数据库（与 sys_ 表处于同一 schema）。
3. 将 Java 源文件放到相应模块目录，修改 package 为项目统一包名。
4. 在 jeecg-boot-vue 中加入前端页面并在菜单中注册路径。
5. 在 Jeecg 流程设计器导入 docs/bpmn/teaching_project_workflow.bpmn

# 注意
- 推荐使用 Elasticsearch 做成果全文检索；若短期验证可用 MySQL LIKE 作为替代。
- 文件存储建议使用 MinIO/S3，file_attachment 表仅为元数据索引。
