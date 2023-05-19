package org.jeecg.codegenerate;

import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOneToMany;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.MainTableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.SubTableVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器入口【一对多】
 * @Author 张代浩
 * @site www.jeecg.com
 * 
 */
public class JeecgOneToMainUtil2 {

	/**
	 * 一对多(父子表)数据模型，生成方法
	 * @param args
	 */
	public static void main(String[] args) {
		//第一步：设置主表配置
		MainTableVo mainTable = new MainTableVo();
        //表名
		mainTable.setTableName("wo_order");
        //实体名
		mainTable.setEntityName("WoOrder");
        //包名
		mainTable.setEntityPackage("order");
        //描述
		mainTable.setFtlDescription("订单管理");
		
		//第二步：设置子表集合配置
		List<SubTableVo> subTables = new ArrayList<SubTableVo>();
		//[1].子表一
		SubTableVo po = new SubTableVo();
        //表名
		po.setTableName("wo_order_detail");
        //实体名
		po.setEntityName("WoOrderDetail");
        //包名
		po.setEntityPackage("order");
        //描述
		po.setFtlDescription("订单明细");
		//子表外键参数配置
		/*说明: 
		 * a) 子表引用主表主键ID作为外键，外键字段必须以_ID结尾;
		 * b) 主表和子表的外键字段名字，必须相同（除主键ID外）;
		 * c) 多个外键字段，采用逗号分隔;
		*/
		po.setForeignKeys(new String[]{"order_id"});
		subTables.add(po);

		mainTable.setSubTables(subTables);
		
		//第三步：一对多(父子表)数据模型,代码生成
		try {
			new CodeGenerateOneToMany(mainTable,subTables).generateCodeFile(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
