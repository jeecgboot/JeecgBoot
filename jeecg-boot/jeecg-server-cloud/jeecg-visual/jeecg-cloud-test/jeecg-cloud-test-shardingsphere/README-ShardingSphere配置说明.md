# JeecgBoot ShardingSphere配置使用说明

## 项目中的ShardingSphere配置

本项目使用ShardingSphere实现分库分表功能，主要涉及以下配置文件和组件：

## 1. 配置文件说明

### sharding.yaml - 基础分表配置
```yaml
databaseName: sharding-db  # 重要：必须与@DS注解中的名称一致

dataSources:
  ds0:
    dataSourceClassName: com.zaxxer.hikari.HikariDataSource
    driverClassName: com.mysql.cj.jdbc.Driver
    jdbcUrl: jdbc:mysql://localhost:3306/jeecg-boot?useSSL=false&useUnicode=true&characterEncoding=utf-8
    username: root
    password: root

rules:
  - !SHARDING
    tables:
      sys_log:  # 分表的逻辑表名
        actualDataNodes: ds0.sys_log$->{0..1}  # 实际表：sys_log0, sys_log1
        tableStrategy:
          standard:
            shardingColumn: log_type  # 分片字段
            shardingAlgorithmName: table_inline
    
    shardingAlgorithms:
      table_inline:
        type: INLINE
        props:
          algorithm-expression: sys_log$->{log_type % 2}  # 根据log_type取模分表
```

### sharding-multi.yaml - 分库分表+读写分离配置
```yaml
databaseName: sharding-db  # 与@DS注解保持一致

dataSources:
  ds0:  # 主库
    jdbcUrl: jdbc:mysql://localhost:3306/jeecg-boot?...
  ds1:  # 从库
    jdbcUrl: jdbc:mysql://localhost:3306/jeecg-boot2?...

rules:
  - !SHARDING
    tables:
      sys_log:
        actualDataNodes: ds$->{0..1}.sys_log$->{0..1}  # 2库2表
        databaseStrategy:  # 分库策略
          standard:
            shardingColumn: operate_type
            shardingAlgorithmName: database-inline
        tableStrategy:  # 分表策略
          standard:
            shardingColumn: log_type
            shardingAlgorithmName: table-classbased

  - !READWRITE_SPLITTING  # 读写分离
    dataSources:
      prds:
        writeDataSourceName: ds0  # 写库
        readDataSourceNames: [ds1]  # 读库
```

## 2. Spring Boot配置

### application-dev.yml中的数据源配置

```yaml
spring:
  datasource:
    dynamic:
      datasource:
        # 普通数据源
        master:
          url: jdbc:mysql://localhost:3306/jeecg-boot
          username: root
          password: root
        
        # ShardingSphere分片数据源
        sharding-db:  # 数据源名称，对应@DS("sharding-db")
          driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
          # 本地配置文件方式
          url: jdbc:shardingsphere:classpath:sharding.yaml
          # 或者Nacos配置方式
          url: jdbc:shardingsphere:nacos:sharding.yaml?serverAddr=${spring.cloud.nacos.config.server-addr}&namespace=${spring.cloud.nacos.config.namespace}&group=${spring.cloud.nacos.config.group}
```

**关键点：**
- `sharding-db` 是数据源的名称标识
- 这个名称必须与Service类上的`@DS("sharding-db")`注解保持一致

## 3. Service层使用

### ShardingSysLogServiceImpl类配置

```java
@Service
@DS("sharding-db")  // 指定使用sharding-db数据源
public class ShardingSysLogServiceImpl extends ServiceImpl<ShardingSysLogMapper, ShardingSysLog> 
    implements IShardingSysLogService {
}
```

**配置关系说明：**
1. `@DS("sharding-db")` 注解告诉MyBatis-Plus使用名为`sharding-db`的数据源
2. `sharding-db`对应application-dev.yml中配置的数据源名称
3. 该数据源使用ShardingSphere驱动，会根据sharding.yaml中的规则进行分片

## 4. 使用步骤

### 步骤1：准备数据库表
```sql
-- 在jeecg-boot数据库中创建分表
CREATE TABLE sys_log0 LIKE sys_log;
CREATE TABLE sys_log1 LIKE sys_log;
```

### 步骤2：配置application-dev.yml
```yaml
spring:
  datasource:
    dynamic:
      datasource:
        sharding-db:
          driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver
          url: jdbc:shardingsphere:classpath:sharding.yaml
```

### 步骤3：配置sharding.yaml
- 将配置文件放在`src/main/resources/`目录下
- 确保`databaseName: sharding-db`与数据源名称一致

### 步骤4：在Service上添加注解
```java
@DS("sharding-db")  // 使用分片数据源
public class ShardingSysLogServiceImpl {
    // 业务代码
}
```

### 步骤5：正常使用MyBatis-Plus
```java
// 插入数据时会自动根据log_type字段进行分表
shardingSysLogService.save(sysLog);

// 查询时也会根据分片规则路由到正确的表
shardingSysLogService.list();
```

## 5. 配置验证

启动项目后查看日志，如果看到类似输出说明配置成功：
```
Logic SQL: INSERT INTO sys_log (log_type, content) VALUES (?, ?)
Actual SQL: ds0 ::: INSERT INTO sys_log0 (log_type, content) VALUES (?, ?)
```

## 6. 注意事项

1. **名称一致性**：确保以下三处名称完全一致
   - application-dev.yml中的数据源名称：`sharding-db`
   - sharding.yaml中的databaseName：`sharding-db`
   - Service类注解：`@DS("sharding-db")`

2. **表结构一致**：所有分片表的结构必须完全一致

3. **分片键选择**：选择分布均匀的字段作为分片键，避免数据倾斜

4. **事务支持**：单表事务正常，跨表事务需要注意

这样配置后，通过ShardingSysLogServiceImpl操作的数据会自动根据分片规则分布到不同的表中。
