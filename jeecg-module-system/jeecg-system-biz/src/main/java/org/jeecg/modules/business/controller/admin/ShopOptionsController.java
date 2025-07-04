package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.ShopOptions;
import org.jeecg.modules.business.entity.ShopWithOptions;
import org.jeecg.modules.business.service.IShopOptionsService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.ShopOptionsAddParam;
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
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	//@AutoLog(value = "客户选项列表-分页列表查询")
	@ApiOperation(value="客户选项列表-分页列表查询", notes="客户选项列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ShopWithOptions>> queryPageList(@RequestParam(name = "pageNo", defaultValue="1") Integer pageNo,
														@RequestParam(name = "pageSize", defaultValue="10") Integer pageSize,
														@RequestParam(name = "shopIds[]", required=false) List<String> shopIds,
														@RequestParam(name = "clientId", required=false) String clientId,
														@RequestParam(name = "showAll", defaultValue = "false") Boolean showAll,
														@RequestParam(name = "hasOptions", defaultValue = "1") Integer hasOptions,
														@RequestParam(name = "order", defaultValue="ASC") String order
	){
		String parsedOrder = order.toUpperCase();
		if(!parsedOrder.equals("ASC") && !parsedOrder.equals("DESC")) {
			return Result.error("Error 400 Bad Request");
		}
		int total = shopOptionsService.countWithFilters(shopIds, clientId, showAll, hasOptions);
		List<ShopWithOptions> shopWithOptionsList = shopOptionsService.listWithFilters(pageNo, pageSize, shopIds, clientId, showAll, hasOptions, parsedOrder);
		IPage<ShopWithOptions> page = new Page<>();
		page.setRecords(shopWithOptionsList);
		page.setTotal(total);
		page.setCurrent(pageNo);
		page.setSize(pageSize);
		return Result.OK(page);
	}
	
	/**
	 *   添加
	 *
	 * @param shopOptionsAddParam a list of ShopOptionsAddParam containing shop Ids
	 * @return
	 */
	@AutoLog(value = "客户选项列表-添加")
	@ApiOperation(value="客户选项列表-添加", notes="客户选项列表-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ShopOptionsAddParam shopOptionsAddParam) {
		List<ShopOptions> shopOptionsList = shopOptionsAddParam.getShopIds().stream().map(shopId -> {
			ShopOptions shopOptions = new ShopOptions();
			shopOptions.setShopId(shopId);
			shopOptions.setUseBalance(shopOptionsAddParam.getUseBalance());
			shopOptions.setShowBalance(shopOptionsAddParam.getShowBalance());
			shopOptions.setBalanceThreshold(shopOptionsAddParam.getBalanceThreshold());
			shopOptions.setIsAutoInvoice(shopOptionsAddParam.getIsAutoInvoice());
			shopOptions.setIsChronologicalOrder(shopOptionsAddParam.getIsChronologicalOrder());
			shopOptions.setIsBreakdownInvoice(shopOptionsAddParam.getIsBreakdownInvoice());
			shopOptions.setIsCompleteInvoice(shopOptionsAddParam.getIsCompleteInvoice());
			shopOptions.setCanSelfInvoice(shopOptionsAddParam.getCanSelfInvoice());
			shopOptions.setCanSelfP(shopOptionsAddParam.getCanSelfP());
			shopOptions.setCanSelfL(shopOptionsAddParam.getCanSelfL());
			shopOptions.setCanSelfPL(shopOptionsAddParam.getCanSelfPL());
			shopOptions.setIsSelfIgnoreStock(shopOptionsAddParam.getIsSelfIgnoreStock());
			shopOptions.setHasStock(shopOptionsAddParam.getHasStock());
			shopOptions.setHasShippingInvoiceRemark(shopOptionsAddParam.getHasShippingInvoiceRemark());
			return shopOptions;
		}).collect(Collectors.toList());
		shopOptionsService.saveBatch(shopOptionsList);
		return Result.OK("sys.api.entryAddSuccess");
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
		return Result.OK("sys.api.entryEditSuccess");
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
		return Result.OK("sys.api.entryDeleteSuccess");
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
