
## TreeSoft数据库管理系统
### TreeSoft数据库管理系统使用JAVA开发，采用稳定通用的springMVC +JDBC架构，实现基于WEB方式对 MySQL，Oracle，PostgreSQL, SQL Server,mongoDB,Hive 数据库进行维护管理操作。
  ### 该分支为V2.3.1 简化版本，包含基础的功能，只需创建3个表
  ![截图](https://i0.hdslb.com/bfs/article/31823be12b39817e531c5c3d646b960d499881722.png)
 
  ### Docker安装部署
  - apache-tomcat-8.5；treeNMS-1.7.2；TreeDMS V2.2.7
  - 下载docker镜像：docker pull registry.cn-hangzhou.aliyuncs.com/masilong/treesoft
  - 启动容器：docker run -d -p 127.0.0.1:18080:8080 registry.cn-hangzhou.aliyuncs.com/masilong/treesoft
  - 项目访问：http://127.0.0.1:18080/treenms 、  http://127.0.0.1:18080/treedms
  - 默认用户名及密码：treesoft
  
  
