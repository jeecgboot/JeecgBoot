package org.jeecg.modules.business.controller.admin;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.Balance;
import org.jeecg.modules.business.service.IBalanceService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: balance
 * @Author: jeecg-boot
 * @Date:   2023-10-12
 * @Version: V1.0
 */
@Api(tags="balance")
@RestController
@RequestMapping("/balance")
@Slf4j
public class BalanceController extends JeecgController<Balance, IBalanceService> {
	@Autowired
	private IBalanceService balanceService;
	
	/**
	 * 分页列表查询
	 *
	 * @param balance
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<Balance>> queryPageList(Balance balance,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Balance> queryWrapper = QueryGenerator.initQueryWrapper(balance, req.getParameterMap());
		Page<Balance> page = new Page<Balance>(pageNo, pageSize);
		IPage<Balance> pageList = balanceService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param balance
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Balance balance) {
		balanceService.save(balance);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param balance
	 * @return
	 */
	@AutoLog(value = "balance-编辑")
	@ApiOperation(value="balance-编辑", notes="balance-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Balance balance) {
		balanceService.updateById(balance);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "balance-通过id删除")
	@ApiOperation(value="balance-通过id删除", notes="balance-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		balanceService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "balance-批量删除")
	@ApiOperation(value="balance-批量删除", notes="balance-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.balanceService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "balance-通过id查询")
	@ApiOperation(value="balance-通过id查询", notes="balance-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Balance> queryById(@RequestParam(name="id",required=true) String id) {
		Balance balance = balanceService.getById(id);
		if(balance==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(balance);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param balance
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Balance balance) {
        return super.exportXls(request, balance, Balance.class, "balance");
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
        return super.importExcel(request, response, Balance.class);
    }

	@GetMapping(value = "/getBalanceByClientIdAndCurrency")
	public Result<?> getBalanceByClientIdAndCurrency(@RequestParam(name="clientId") String clientId, @RequestParam(name="currency") String currency) {
		BigDecimal balance = balanceService.getBalanceByClientIdAndCurrency(clientId, currency);
		return Result.OK(balance == null ? 0 : balance);
	}

}
