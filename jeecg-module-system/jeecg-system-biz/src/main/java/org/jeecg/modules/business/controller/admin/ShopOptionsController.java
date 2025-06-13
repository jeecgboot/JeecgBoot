package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.ShopOptions;
import org.jeecg.modules.business.service.IShopOptionsService;

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

 /**
 * @Description: 客户选项列表
 * @Author: jeecg-boot
 * @Date:   2025-06-12
 * @Version: V1.0
 */
@Api(tags="客户选项列表")
@RestController
@RequestMapping("/shopOptions")
@Slf4j
public class ShopOptionsController extends JeecgController<ShopOptions, IShopOptionsService> {
	@Autowired
	private IShopOptionsService shopOptionsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param shopOptions
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "客户选项列表-分页列表查询")
	@ApiOperation(value="客户选项列表-分页列表查询", notes="客户选项列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ShopOptions>> queryPageList(ShopOptions shopOptions,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ShopOptions> queryWrapper = QueryGenerator.initQueryWrapper(shopOptions, req.getParameterMap());
		Page<ShopOptions> page = new Page<ShopOptions>(pageNo, pageSize);
		IPage<ShopOptions> pageList = shopOptionsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param shopOptions
	 * @return
	 */
	@AutoLog(value = "客户选项列表-添加")
	@ApiOperation(value="客户选项列表-添加", notes="客户选项列表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ShopOptions shopOptions) {
		shopOptionsService.save(shopOptions);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param shopOptions
	 * @return
	 */
	@AutoLog(value = "客户选项列表-编辑")
	@ApiOperation(value="客户选项列表-编辑", notes="客户选项列表-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ShopOptions shopOptions) {
		shopOptionsService.updateById(shopOptions);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户选项列表-通过id删除")
	@ApiOperation(value="客户选项列表-通过id删除", notes="客户选项列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		shopOptionsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户选项列表-批量删除")
	@ApiOperation(value="客户选项列表-批量删除", notes="客户选项列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shopOptionsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "客户选项列表-通过id查询")
	@ApiOperation(value="客户选项列表-通过id查询", notes="客户选项列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ShopOptions> queryById(@RequestParam(name="id",required=true) String id) {
		ShopOptions shopOptions = shopOptionsService.getById(id);
		if(shopOptions==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shopOptions);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shopOptions
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShopOptions shopOptions) {
        return super.exportXls(request, shopOptions, ShopOptions.class, "客户选项列表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ShopOptions.class);
    }

}
