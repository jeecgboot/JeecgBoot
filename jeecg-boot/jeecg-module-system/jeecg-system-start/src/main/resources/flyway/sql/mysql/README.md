# SQL文件命名规则
`V[年月日]_[序号]__[模块名缩写]_[操作类型]_[业务描述].sql`

例如：
```
V20240104_1__easyoa_add_field_attendance.sql
R__202402_drag_update_template.sql
```

### 一、SQL命名规则说明
- 1.仅需要执行一次的，以大写“V”开头
- 2.需要执行多次的，以大写“R”开头，命名如R__clean.sql，R的脚本只要改变了就会执行
- 3.V开头的比R开头的优先级要高。
- 4.参考博客：https://blog.csdn.net/Jiao1225/article/details/129590660

### 二、归档增量SQL
- 1.将目录下的所有SQL文件压缩归档至`backup`目录下
```
  目录：`jeecg-system-start\src\main\resources\flyway\sql\mysql`
```
- 2.执行SQL
```
-- 删除历史增量执行日志
delete from flyway_schema_history where installed_rank > 1;
```
- 3.这样就清爽了，可以开启项目新起点