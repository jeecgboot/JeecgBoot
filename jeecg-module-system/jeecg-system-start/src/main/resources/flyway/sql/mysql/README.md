# SQL文件命名规则
`V[年月日]_[序号]__[模块名缩写]_[操作类型]_[业务描述].sql`

例如：
```
V20240104_1__easyoa_add_field_attendance.sql
R__202402_drag_update_template.sql
```

### SQL命名规则说明
- 1.仅需要执行一次的，以大写“V”开头
- 2.需要执行多次的，以大写“R”开头，命名如R__clean.sql，R的脚本只要改变了就会执行
- 3.V开头的比R开头的优先级要高。


### 命名规则示例
参考博客：
https://blog.csdn.net/Jiao1225/article/details/129590660