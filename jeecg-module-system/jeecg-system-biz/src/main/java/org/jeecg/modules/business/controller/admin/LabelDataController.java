package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.LabelData;
import org.jeecg.modules.business.service.ILabelDataService;

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
 * @Description: 自定义分类
 * @Author: jeecg-boot
 * @Date:   2025-02-06
 * @Version: V1.0
 */
@Api(tags="自定义分类")
@RestController
@RequestMapping("/labelData")
@Slf4j
public class LabelDataController extends JeecgController<LabelData, ILabelDataService> {
	@Autowired
	private ILabelDataService labelDataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param labelData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "自定义分类-分页列表查询")
	@ApiOperation(value="自定义分类-分页列表查询", notes="自定义分类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<LabelData>> queryPageList(LabelData labelData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LabelData> queryWrapper = QueryGenerator.initQueryWrapper(labelData, req.getParameterMap());
		Page<LabelData> page = new Page<LabelData>(pageNo, pageSize);
		IPage<LabelData> pageList = labelDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param labelData
	 * @return
	 */
	@AutoLog(value = "自定义分类-添加")
	@ApiOperation(value="自定义分类-添加", notes="自定义分类-添加")
	@RequiresPermissions("business:label_data:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody LabelData labelData) {
		labelDataService.save(labelData);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param labelData
	 * @return
	 */
	@AutoLog(value = "自定义分类-编辑")
	@ApiOperation(value="自定义分类-编辑", notes="自定义分类-编辑")
	@RequiresPermissions("business:label_data:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody LabelData labelData) {
		labelDataService.updateById(labelData);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "自定义分类-通过id删除")
	@ApiOperation(value="自定义分类-通过id删除", notes="自定义分类-通过id删除")
	@RequiresPermissions("business:label_data:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		labelDataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "自定义分类-批量删除")
	@ApiOperation(value="自定义分类-批量删除", notes="自定义分类-批量删除")
	@RequiresPermissions("business:label_data:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.labelDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "自定义分类-通过id查询")
	@ApiOperation(value="自定义分类-通过id查询", notes="自定义分类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<LabelData> queryById(@RequestParam(name="id",required=true) String id) {
		LabelData labelData = labelDataService.getById(id);
		if(labelData==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(labelData);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param labelData
    */
    @RequiresPermissions("business:label_data:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LabelData labelData) {
        return super.exportXls(request, labelData, LabelData.class, "自定义分类");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:label_data:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LabelData.class);
    }

}
