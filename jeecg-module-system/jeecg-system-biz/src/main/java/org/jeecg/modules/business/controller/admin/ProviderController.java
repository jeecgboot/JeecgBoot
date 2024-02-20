package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.Provider;
import org.jeecg.modules.business.service.IProviderService;

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
 * @Description: provider
 * @Author: jeecg-boot
 * @Date:   2024-02-14
 * @Version: V1.0
 */
@Api(tags="provider")
@RestController
@RequestMapping("/business/provider")
@Slf4j
public class ProviderController extends JeecgController<Provider, IProviderService> {
	@Autowired
	private IProviderService providerService;
	
	/**
	 * 分页列表查询
	 *
	 * @param provider
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "provider-分页列表查询")
	@ApiOperation(value="provider-分页列表查询", notes="provider-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Provider>> queryPageList(Provider provider,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Provider> queryWrapper = QueryGenerator.initQueryWrapper(provider, req.getParameterMap());
		Page<Provider> page = new Page<Provider>(pageNo, pageSize);
		IPage<Provider> pageList = providerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param provider
	 * @return
	 */
	@AutoLog(value = "provider-添加")
	@ApiOperation(value="provider-添加", notes="provider-添加")
	@RequiresPermissions("business:provider:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Provider provider) {
		providerService.save(provider);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param provider
	 * @return
	 */
	@AutoLog(value = "provider-编辑")
	@ApiOperation(value="provider-编辑", notes="provider-编辑")
	@RequiresPermissions("business:provider:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Provider provider) {
		providerService.updateById(provider);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "provider-通过id删除")
	@ApiOperation(value="provider-通过id删除", notes="provider-通过id删除")
	@RequiresPermissions("business:provider:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		providerService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "provider-批量删除")
	@ApiOperation(value="provider-批量删除", notes="provider-批量删除")
	@RequiresPermissions("business:provider:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.providerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "provider-通过id查询")
	@ApiOperation(value="provider-通过id查询", notes="provider-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Provider> queryById(@RequestParam(name="id",required=true) String id) {
		Provider provider = providerService.getById(id);
		if(provider==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(provider);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param provider
    */
    @RequiresPermissions("business:provider:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Provider provider) {
        return super.exportXls(request, provider, Provider.class, "provider");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:provider:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Provider.class);
    }

}
