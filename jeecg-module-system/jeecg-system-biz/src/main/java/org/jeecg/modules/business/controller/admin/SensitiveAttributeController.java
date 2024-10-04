package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SensitiveAttribute;
import org.jeecg.modules.business.service.ISensitiveAttributeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: sensitive_attribute
 * @Author: jeecg-boot
 * @Date:   2024-09-24
 * @Version: V1.0
 */
@Api(tags="sensitive_attribute")
@RestController
@RequestMapping("/sensitiveAttribute")
@Slf4j
public class SensitiveAttributeController extends JeecgController<SensitiveAttribute, ISensitiveAttributeService> {
	@Autowired
	private ISensitiveAttributeService sensitiveAttributeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sensitiveAttribute
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sensitive_attribute-分页列表查询")
	@ApiOperation(value="sensitive_attribute-分页列表查询", notes="sensitive_attribute-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SensitiveAttribute>> queryPageList(SensitiveAttribute sensitiveAttribute,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SensitiveAttribute> queryWrapper = QueryGenerator.initQueryWrapper(sensitiveAttribute, req.getParameterMap());
		Page<SensitiveAttribute> page = new Page<SensitiveAttribute>(pageNo, pageSize);
		IPage<SensitiveAttribute> pageList = sensitiveAttributeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sensitiveAttribute
	 * @return
	 */
	@AutoLog(value = "sensitive_attribute-添加")
	@ApiOperation(value="sensitive_attribute-添加", notes="sensitive_attribute-添加")
	@RequiresPermissions("business:sensitive_attribute:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SensitiveAttribute sensitiveAttribute) {
		sensitiveAttributeService.save(sensitiveAttribute);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sensitiveAttribute
	 * @return
	 */
	@AutoLog(value = "sensitive_attribute-编辑")
	@ApiOperation(value="sensitive_attribute-编辑", notes="sensitive_attribute-编辑")
	@RequiresPermissions("business:sensitive_attribute:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SensitiveAttribute sensitiveAttribute) {
		sensitiveAttributeService.updateById(sensitiveAttribute);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sensitive_attribute-通过id删除")
	@ApiOperation(value="sensitive_attribute-通过id删除", notes="sensitive_attribute-通过id删除")
	@RequiresPermissions("business:sensitive_attribute:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sensitiveAttributeService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sensitive_attribute-批量删除")
	@ApiOperation(value="sensitive_attribute-批量删除", notes="sensitive_attribute-批量删除")
	@RequiresPermissions("business:sensitive_attribute:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sensitiveAttributeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sensitive_attribute-通过id查询")
	@ApiOperation(value="sensitive_attribute-通过id查询", notes="sensitive_attribute-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SensitiveAttribute> queryById(@RequestParam(name="id",required=true) String id) {
		SensitiveAttribute sensitiveAttribute = sensitiveAttributeService.getById(id);
		if(sensitiveAttribute==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sensitiveAttribute);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sensitiveAttribute
    */
    @RequiresPermissions("business:sensitive_attribute:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SensitiveAttribute sensitiveAttribute) {
        return super.exportXls(request, sensitiveAttribute, SensitiveAttribute.class, "sensitive_attribute");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sensitive_attribute:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SensitiveAttribute.class);
    }

}
