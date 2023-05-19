package org.jeecg.modules.wo.store.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.wo.store.entity.WoStore;
import org.jeecg.modules.wo.store.service.IWoStoreService;
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
 * @Description: 点餐门店
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="点餐门店")
@RestController
@RequestMapping("/store/woStore")
public class WoStoreController extends JeecgController<WoStore, IWoStoreService> {
	@Autowired
	private IWoStoreService woStoreService;
	
	/**
	 * 分页列表查询
	 *
	 * @param woStore
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "点餐门店-分页列表查询")
	@ApiOperation(value="点餐门店-分页列表查询", notes="点餐门店-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WoStore woStore,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WoStore> queryWrapper = QueryGenerator.initQueryWrapper(woStore, req.getParameterMap());
		Page<WoStore> page = new Page<WoStore>(pageNo, pageSize);
		IPage<WoStore> pageList = woStoreService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 @GetMapping(value = "/queryall")
	 public Result<?> queryPageList(HttpServletRequest req) {
		 QueryWrapper<WoStore> queryWrapper = new QueryWrapper<>();
		 List<WoStore> list = woStoreService.list();
		 return Result.OK(list);
	 }

	
	/**
	 * 添加
	 *
	 * @param woStore
	 * @return
	 */
	@AutoLog(value = "点餐门店-添加")
	@ApiOperation(value="点餐门店-添加", notes="点餐门店-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WoStore woStore) {
		woStoreService.save(woStore);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param woStore
	 * @return
	 */
	@AutoLog(value = "点餐门店-编辑")
	@ApiOperation(value="点餐门店-编辑", notes="点餐门店-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody WoStore woStore) {
		woStoreService.updateById(woStore);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点餐门店-通过id删除")
	@ApiOperation(value="点餐门店-通过id删除", notes="点餐门店-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		woStoreService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "点餐门店-批量删除")
	@ApiOperation(value="点餐门店-批量删除", notes="点餐门店-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.woStoreService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点餐门店-通过id查询")
	@ApiOperation(value="点餐门店-通过id查询", notes="点餐门店-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WoStore woStore = woStoreService.getById(id);
		return Result.OK(woStore);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param woStore
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WoStore woStore) {
      return super.exportXls(request, woStore, WoStore.class, "点餐门店");
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
      return super.importExcel(request, response, WoStore.class);
  }

}
