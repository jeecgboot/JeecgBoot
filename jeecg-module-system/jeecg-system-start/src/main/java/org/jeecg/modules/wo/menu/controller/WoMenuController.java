package org.jeecg.modules.wo.menu.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.wo.menu.entity.WoMenu;
import org.jeecg.modules.wo.menu.service.IWoMenuService;
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

 /**
 * @Description: 点餐菜品
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="点餐菜品")
@RestController
@RequestMapping("/menu/woMenu")
public class WoMenuController extends JeecgController<WoMenu, IWoMenuService> {
	@Autowired
	private IWoMenuService woMenuService;
	
	/**
	 * 分页列表查询
	 *
	 * @param woMenu
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "点餐菜品-分页列表查询")
	@ApiOperation(value="点餐菜品-分页列表查询", notes="点餐菜品-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WoMenu woMenu,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WoMenu> queryWrapper = QueryGenerator.initQueryWrapper(woMenu, req.getParameterMap());
		Page<WoMenu> page = new Page<WoMenu>(pageNo, pageSize);
		IPage<WoMenu> pageList = woMenuService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param woMenu
	 * @return
	 */
	@AutoLog(value = "点餐菜品-添加")
	@ApiOperation(value="点餐菜品-添加", notes="点餐菜品-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WoMenu woMenu) {
		woMenuService.save(woMenu);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param woMenu
	 * @return
	 */
	@AutoLog(value = "点餐菜品-编辑")
	@ApiOperation(value="点餐菜品-编辑", notes="点餐菜品-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody WoMenu woMenu) {
		woMenuService.updateById(woMenu);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点餐菜品-通过id删除")
	@ApiOperation(value="点餐菜品-通过id删除", notes="点餐菜品-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		woMenuService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "点餐菜品-批量删除")
	@ApiOperation(value="点餐菜品-批量删除", notes="点餐菜品-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.woMenuService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点餐菜品-通过id查询")
	@ApiOperation(value="点餐菜品-通过id查询", notes="点餐菜品-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WoMenu woMenu = woMenuService.getById(id);
		return Result.OK(woMenu);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param woMenu
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WoMenu woMenu) {
      return super.exportXls(request, woMenu, WoMenu.class, "点餐菜品");
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
      return super.importExcel(request, response, WoMenu.class);
  }

}
