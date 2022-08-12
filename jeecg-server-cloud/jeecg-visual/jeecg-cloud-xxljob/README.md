- 初始化脚本(mysql)

  db\tables_xxl_job.sql
  
- 修改数据库连接

  jeecg-cloud-xxljob\src\main\resources\application.yml
  
- 启动项目

  jeecg-cloud-xxljob\src\main\java\com\xxl\job\admin\XxlJobAdminApplication.java
  
 - 访问项目
  http://127.0.0.1:9080/xxl-job-admin/toLogin
  admin/123456
  
 - docker方式安装
 
   https://my.oschina.net/jeecg/blog/4729020
   
   
   
 概念说明
 1、手工创建执行器，AppName对应服务名字  比如： jeecg-demo
 2、手工创建定时任务，选择执行器（服务）、JobHandler对应XxlJob的值