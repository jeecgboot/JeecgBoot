Jeecg-Boot 快速开发平台
===============

当前最新版本： 2.0.0（发布日期：20190518）


## 后端技术架构
- 基础框架：Spring Boot 2.1.3.RELEASE

- 持久层框架：Mybatis-plus_3.0.6

- 安全框架：Apache Shiro 1.4.0，Jwt_3.7.0

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

 
## 开发文档

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
  



- **代码生成器**

** 功能说明**：   一键生成的代码（包括：controller、service、dao、mapper、entity、vue）
 
 **模板位置**： src/main/resources/jeecg/code-template  

**使用方法**： 
 
 【**一对一模板**】  
  
**1.**先找到**jeecg-boot/src/resources/jeecg**下的  
**jeecg_config.properties**和**jeecg_database.properties**两个文件。  
**jeecg_config.properties:** 用来配置文件生成的路径，  
    
**jeecg_database.properties:** 用来配置数据库相关配置.  
         
         
**2.**配置好这些配置之后，我们需要找到**jeecg-boot/src/main/java/org/jeecg/JeecgOneGUI.java**类，也就是启动一对一代码生成器的入口；
  
    
**3.**右键运行该类，紧接着会弹出一个窗口，如下图:  
        ![](https://static.oschina.net/uploads/img/201904/14222638_Svth.png)  
          
          
**4.**然后根据窗口左侧的提示，在右侧填写对应的信息即可.
     
 【**一对多模板**】  
   
   
**1.**先找到**jeecg-boot/src/resources/jeecg**下的
    
**jeecg_config.properties**和**jeecg_database.properties**两个文件。  
  
**jeecg_config.properties:** 是配置文件生成路径的，  
  
  
**jeecg_database.properties:** 是配置数据库相关配置的文件。  
       
       
**2.**接着我们需要找到**jeecg-boot/src/main/java/org/jeecg/JeecgOneToMainUtil.java**这个类。
     该类是生成一对多模板的启动入口。  
       
       
**3.**该类中需要三个步骤来配置一对多表的信息。  
  
    
  (1) 第一步: 配置主表信息,代码如下:
  
```
		//第一步：设置主表配置
		MainTableVo mainTable = new MainTableVo();
		mainTable.setTableName("jeecg_order_main");//表名
		mainTable.setEntityName("TestOrderMain");	 //实体名
		mainTable.setEntityPackage("test2");	 //包名
		mainTable.setFtlDescription("订单");	 //描述
		
```
  (2) 第二步: 配置子表信息，**有多个则配置多个**, 代码如下: 
     
  ①比如: 配置子表 1:
  
  ```
		//第二步：设置子表集合配置
		List<SubTableVo> subTables = new ArrayList<SubTableVo>();
		//[1].子表一
		SubTableVo po = new SubTableVo();
		po.setTableName("jeecg_order_customer");//表名
		po.setEntityName("TestOrderCustom");	    //实体名
		po.setEntityPackage("test2");	        //包名
		po.setFtlDescription("客户明细");       //描述
		//子表外键参数配置
		/*说明: 
		 * a) 子表引用主表主键ID作为外键，外键字段必须以_ID结尾;
		 * b) 主表和子表的外键字段名字，必须相同（除主键ID外）;
		 * c) 多个外键字段，采用逗号分隔;
		*/
		po.setForeignKeys(new String[]{"order_id"});
		subTables.add(po);
  ```
  ②比如: 配置子表 2:  
  
```
		//[2].子表二
		SubTableVo po2 = new SubTableVo();
		po2.setTableName("jeecg_order_ticket");		//表名
		po2.setEntityName("TestOrderTicket");			//实体名
		po2.setEntityPackage("test2"); 				//包名
		po2.setFtlDescription("产品明细");			//描述
		//子表外键参数配置
		/*说明: 
		 * a) 子表引用主表主键ID作为外键，外键字段必须以_ID结尾;
		 * b) 主表和子表的外键字段名字，必须相同（除主键ID外）;
		 * c) 多个外键字段，采用逗号分隔;
		*/
		po2.setForeignKeys(new String[]{"order_id"});
		subTables.add(po2);  
  ```
  ③将整合了子表VO的subTables添加到主表对象当中去: 

```
  mainTable.setSubTables(subTables);
  ```
  ④需要注意如下代码,该代码的作用是,为子表设置主外键关联,当添加数据时,  
主表的主键将会添加到子表的"order_id"中:  
  
```
  		po2.setForeignKeys(new String[]{"order_id"});
  ```
    
  (3) 第三步: 启动(run)程序，生成代码, 代码如下:
  
  ```
  		//第三步：一对多(父子表)数据模型,代码生成
		new CodeGenerateOneToMany(mainTable,subTables).generateCodeFile();
  ```
 
[在线文档](https://github.com/zhangdaiscott/autopoi)  
  

- **编码排重使用示例**   

重复校验效果：
![输入图片说明](https://static.oschina.net/uploads/img/201904/19191836_eGkQ.png "在这里输入图片标题")

1.引入排重接口,代码如下:  
 
```
import { duplicateCheck } from '@/api/api'
  ```
2.找到编码必填校验规则的前端代码,代码如下:  
  
```
<a-input placeholder="请输入编码" v-decorator="['code', validatorRules.code ]"/>

code: {
            rules: [
              { required: true, message: '请输入编码!' },
              {validator: this.validateCode}
            ]
          },
  ```
3.找到rules里validator对应的方法在哪里,然后使用第一步中引入的排重校验接口.  
  以用户online表单编码为示例,其中四个必传的参数有:  
    
```
  {tableName:表名,fieldName:字段名,fieldVal:字段值,dataId:表的主键},
  ```
 具体使用代码如下:  
 
```
    validateCode(rule, value, callback){
        let pattern = /^[a-z|A-Z][a-z|A-Z|\d|_|-]{0,}$/;
        if(!pattern.test(value)){
          callback('编码必须以字母开头，可包含数字、下划线、横杠');
        } else {
          var params = {
            tableName: "onl_cgreport_head",
            fieldName: "code",
            fieldVal: value,
            dataId: this.model.id
          };
          duplicateCheck(params).then((res)=>{
            if(res.success){
             callback();
            }else{
              callback(res.message);
            }
          })
        }
      },
```