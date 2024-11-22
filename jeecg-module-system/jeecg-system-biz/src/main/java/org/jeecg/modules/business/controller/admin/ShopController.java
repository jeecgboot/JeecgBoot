package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.service.IShopService;

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
 * @Description: shop
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Api(tags="shop")
@RestController
@RequestMapping("/shop")
@Slf4j
public class ShopController extends JeecgController<Shop, IShopService> {
	@Autowired
	private IShopService shopService;
	
	/**
	 * 分页列表查询
	 *
	 * @param shop
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "shop-分页列表查询")
	@ApiOperation(value="shop-分页列表查询", notes="shop-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Shop>> queryPageList(Shop shop,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Shop> queryWrapper = QueryGenerator.initQueryWrapper(shop, req.getParameterMap());
		Page<Shop> page = new Page<Shop>(pageNo, pageSize);
		IPage<Shop> pageList = shopService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param shop
	 * @return
	 */
	@AutoLog(value = "shop-添加")
	@ApiOperation(value="shop-添加", notes="shop-添加")
	@RequiresPermissions("shop:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Shop shop) {
		shopService.save(shop);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param shop
	 * @return
	 */
	@AutoLog(value = "shop-编辑")
	@ApiOperation(value="shop-编辑", notes="shop-编辑")
	@RequiresPermissions("shop:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Shop shop) {
		shopService.updateById(shop);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "shop-通过id删除")
	@ApiOperation(value="shop-通过id删除", notes="shop-通过id删除")
	@RequiresPermissions("shop:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		shopService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "shop-批量删除")
	@ApiOperation(value="shop-批量删除", notes="shop-批量删除")
	@RequiresPermissions("shop:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.shopService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "shop-通过id查询")
	@ApiOperation(value="shop-通过id查询", notes="shop-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Shop> queryById(@RequestParam(name="id",required=true) String id) {
		Shop shop = shopService.getById(id);
		if(shop==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(shop);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param shop
    */
    @RequiresPermissions("shop:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Shop shop) {
        return super.exportXls(request, shop, Shop.class, "shop");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("shop:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Shop.class);
    }

	@GetMapping(value = "/shopGroupedByClient")
	public Result<?> listShopGroupedByClient() {
		return Result.OK(shopService.listShopGroupedByClient());
	}
}
