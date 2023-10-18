package org.jeecg.modules.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
	ISysDictService sysDictService;

	/**
	 * 校验数据是否在系统中是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<String> doDuplicateCheck(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		log.debug("----duplicate check------："+ duplicateCheckVo.toString());
		
		// 1.填值为空，直接返回
		if(StringUtils.isEmpty(duplicateCheckVo.getFieldVal())){
			Result rs = new Result();
			rs.setCode(500);
			rs.setSuccess(true);
			rs.setMessage("数据为空,不作处理！");
			return rs;
		}
		
		// 2.返回结果
		if (sysDictService.duplicateCheckData(duplicateCheckVo)) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}


}
