package org.jeecg.modules.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.jeecg.modules.system.security.DictQueryBlackListHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: DuplicateCheckAction
 * @Description: 重复校验工具
 * @Author 张代浩
 * @Date 2019-03-25
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/duplicate")
@Api(tags="重复校验")
public class DuplicateCheckController {

	@Autowired
	SysDictMapper sysDictMapper;

	@Autowired
	DictQueryBlackListHandler dictQueryBlackListHandler;

	/**
	 * 校验数据是否在系统中是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<String> doDuplicateCheck(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		log.debug("----duplicate check------："+ duplicateCheckVo.toString());
		//关联表字典（举例：sys_user,realname,id）
		//SQL注入校验（只限制非法串改数据库）
		final String[] sqlInjCheck = {duplicateCheckVo.getTableName(),duplicateCheckVo.getFieldName()};
		SqlInjectionUtil.filterContent(sqlInjCheck);
		// update-begin-author:taoyan date:20211227 for: JTC-25 【online报表】oracle 操作问题 录入弹框啥都不填直接保存 ①编码不是应该提示必填么？②报错也应该是具体文字提示，不是后台错误日志
		if(StringUtils.isEmpty(duplicateCheckVo.getFieldVal())){
			Result rs = new Result();
			rs.setCode(500);
			rs.setSuccess(true);
			rs.setMessage("数据为空,不作处理！");
			return rs;
		}
		//update-begin-author:taoyan date:20220329 for: VUEN-223【安全漏洞】当前被攻击的接口
		String checkSql = duplicateCheckVo.getTableName() + SymbolConstant.COMMA + duplicateCheckVo.getFieldName() + SymbolConstant.COMMA;
		if(!dictQueryBlackListHandler.isPass(checkSql)){
			return Result.error(dictQueryBlackListHandler.getError());
		}
		//update-end-author:taoyan date:20220329 for: VUEN-223【安全漏洞】当前被攻击的接口
		// update-end-author:taoyan date:20211227 for: JTC-25 【online报表】oracle 操作问题 录入弹框啥都不填直接保存 ①编码不是应该提示必填么？②报错也应该是具体文字提示，不是后台错误日志
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.duplicateCheckCountSql(duplicateCheckVo);
		} else {
			// [1].添加页面校验
			num = sysDictMapper.duplicateCheckCountSqlNoDataId(duplicateCheckVo);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}

	/**
	 * VUEN-2584【issue】平台sql注入漏洞几个问题
	 * 部分特殊函数 可以将查询结果混夹在错误信息中，导致数据库的信息暴露
	 * @param e
	 * @return
	 */
	@ExceptionHandler(java.sql.SQLException.class)
	public Result<?> handleSQLException(Exception e){
		String msg = e.getMessage();
		String extractvalue = "extractvalue";
		String updatexml = "updatexml";
		if(msg!=null && (msg.toLowerCase().indexOf(extractvalue)>=0 || msg.toLowerCase().indexOf(updatexml)>=0)){
			return Result.error("校验失败，sql解析异常！");
		}
		return Result.error("校验失败，sql解析异常！" + msg);
	}
}
