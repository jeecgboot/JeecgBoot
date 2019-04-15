Jeecg-Boot 快速开发平台
===============

当前最新版本： 1.1（发布日期：20190415）


## 后端技术架构
- 基础框架：Spring Boot 2.0.3.RELEASE

- 持久层框架：Mybatis-plus_3.0.6

- 安全框架：Apache Shiro 1.4.0-RC2，Jwt_3.4.1

- 数据库连接池：阿里巴巴Druid 1.1.10

- 缓存框架：redis

- 日志打印：logback

- 其他：fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。



## 开发环境

- 语言：Java 8

- IDE(JAVA)： Eclipse安装lombok插件 或者 IDEA

- 依赖管理：Maven

- 数据库：MySQL5.0  &  Oracle 11g

- 缓存：Redis

 
 #### 技术文档


- 官方文档 ：  [http://jeecg-boot.mydoc.io](http://jeecg-boot.mydoc.io)
 
- 零基础入门 ：  [http://jeecg-boot.mydoc.io/?t=344845](http://jeecg-boot.mydoc.io/?t=344845)

- 在线演示 ：  [http://boot.jeecg.org](http://boot.jeecg.org)
 
- QQ交流群 ：  284271917

- 视频教程 ：  https://pan.baidu.com/s/1Il0TS50I70vH1AG1y40wtw 提取码：hok5

- 常见问题 ：  [新手入门必看，汇总了常见各种问题](http://www.jeecg.org/forum.php?mod=viewthread&tid=7816&page=1&extra=#pid21237)



## 专项文档区

- 查询过滤器用法

```
QueryWrapper<?> queryWrapper = QueryGenerator.initQueryWrapper(?, req.getParameterMap());
```

代码示例：

```

	@GetMapping(value = "/list")
	public Result<IPage<JeecgDemo>> list(JeecgDemo jeecgDemo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, 
	                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<JeecgDemo>> result = new Result<IPage<JeecgDemo>>();
		
		//调用QueryGenerator的initQueryWrapper
		QueryWrapper<JeecgDemo> queryWrapper = QueryGenerator.initQueryWrapper(jeecgDemo, req.getParameterMap());
		
		Page<JeecgDemo> page = new Page<JeecgDemo>(pageNo, pageSize);
		IPage<JeecgDemo> pageList = jeecgDemoService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

```



- 查询规则 (本规则不适用于高级查询,高级查询有自己对应的查询类型可以选择 )

| 查询模式           | 用法    | 说明                         |
|---------- |-------------------------------------------------------|------------------|
| 模糊查询     | 支持左右模糊和全模糊  需要在查询输入框内前或后带\*或是前后全部带\* |    |
| 取非查询     | 在查询输入框前面输入! 则查询该字段不等于输入值的数据(数值类型不支持此种查询,可以将数值字段定义为字符串类型的) |    |
| \>  \>= < <=     | 同取非查询 在输入框前面输入对应特殊字符即表示走对应规则查询 |    |
| in查询     | 若传入的数据带,(逗号) 则表示该查询为in查询 |    |
| 多选字段模糊查询     | 上述4 有一个特例，若某一查询字段前后都带逗号 则会将其视为走这种查询方式 ,该查询方式是将查询条件以逗号分割再遍历数组 将每个元素作like查询 用or拼接,例如 现在name传入值 ,a,b,c, 那么结果sql就是 name like '%a%' or name like '%b%' or name like '%c%' |    |


- Autopoi使用文档（EXCEL工具类 -  EasyPOI衍变升级重构版本）
 
  [在线文档](https://github.com/zhangdaiscott/autopoi)
  
  
  