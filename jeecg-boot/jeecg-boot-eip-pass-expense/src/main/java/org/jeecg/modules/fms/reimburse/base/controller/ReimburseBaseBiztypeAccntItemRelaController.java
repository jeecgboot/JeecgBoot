package org.jeecg.modules.fms.reimburse.base.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseBiztypeAccntItemRela;
import org.jeecg.modules.fms.reimburse.base.service.IReimburseBaseBiztypeAccntItemRelaService;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizMainInfo;

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

 /**
 * @Description: reimburse_base_biztype_accnt_item_rela
 * @Author: jeecg-boot
 * @Date:   2020-02-10
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/rmbsErpAccntItemRela")
@Slf4j
public class ReimburseBaseBiztypeAccntItemRelaController extends JeecgController<ReimburseBaseBiztypeAccntItemRela, IReimburseBaseBiztypeAccntItemRelaService> {
	@Autowired
	private IReimburseBaseBiztypeAccntItemRelaService reimburseBaseBiztypeAccntItemRelaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reimburseBaseBiztypeAccntItemRela
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReimburseBaseBiztypeAccntItemRela reimburseBaseBiztypeAccntItemRela,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReimburseBaseBiztypeAccntItemRela> queryWrapper = QueryGenerator.initQueryWrapper(reimburseBaseBiztypeAccntItemRela, req.getParameterMap());
		Page<ReimburseBaseBiztypeAccntItemRela> page = new Page<ReimburseBaseBiztypeAccntItemRela>(pageNo, pageSize);
		IPage<ReimburseBaseBiztypeAccntItemRela> pageList = reimburseBaseBiztypeAccntItemRelaService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param reimburseBaseBiztypeAccntItemRela
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ReimburseBaseBiztypeAccntItemRela reimburseBaseBiztypeAccntItemRela) {
		reimburseBaseBiztypeAccntItemRelaService.save(reimburseBaseBiztypeAccntItemRela);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reimburseBaseBiztypeAccntItemRela
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ReimburseBaseBiztypeAccntItemRela reimburseBaseBiztypeAccntItemRela) {
		reimburseBaseBiztypeAccntItemRelaService.updateById(reimburseBaseBiztypeAccntItemRela);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		reimburseBaseBiztypeAccntItemRelaService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.reimburseBaseBiztypeAccntItemRelaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ReimburseBaseBiztypeAccntItemRela reimburseBaseBiztypeAccntItemRela = reimburseBaseBiztypeAccntItemRelaService.getById(id);
		if(reimburseBaseBiztypeAccntItemRela==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(reimburseBaseBiztypeAccntItemRela);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reimburseBaseBiztypeAccntItemRela
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReimburseBaseBiztypeAccntItemRela reimburseBaseBiztypeAccntItemRela) {
        return super.exportXls(request, reimburseBaseBiztypeAccntItemRela, ReimburseBaseBiztypeAccntItemRela.class, "reimburse_base_biztype_accnt_item_rela");
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
        return super.importExcel(request, response, ReimburseBaseBiztypeAccntItemRela.class);
    }
    
    
    /**
	 * 获取会计科目映射数据
	 * @param costTypeCenter 支出成本类型，成本中心类型|举例：1,2
	 * @param bizFeeInfo 业务大类代码,业务小类代码 | 举例：ZD01,ZD01001
	 * @return
	 */
	@RequestMapping(value = "/getErpAccntRela", method = RequestMethod.GET)
	public Result<List<DictModel>> getErpAccntRela(@RequestParam(name="costTypeCenter",required=true) String costTypeCenter,
			@RequestParam(name="bizFeeInfo",required=false) String bizFeeInfo) {
		log.info(" costTypeCenter : "+ costTypeCenter);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			String[] costInfoParams = null;
			if(costTypeCenter.indexOf(",")!=-1) {
				costInfoParams = costTypeCenter.split(",");
				
				if(costInfoParams !=null && costInfoParams.length<2) {
					result.error500("支出成本类型及成本中心类型格式不正确！");
					return result;
				}
				//SQL注入校验（只限制非法篡改数据库）
				final String[] sqlInjCheck = {costInfoParams[0],costInfoParams[1]};
				SqlInjectionUtil.filterContent(sqlInjCheck);
			}else {
				result.error500("支出成本类型及成本中心类型格式不正确!");
				return result;
			}
			log.info(" bizFeeInfo : "+ bizFeeInfo);
			if(bizFeeInfo.indexOf(",")!=-1) {
				String[] bizInfoParams = bizFeeInfo.split(",");
				
				if(bizInfoParams.length==1) {
					final String[] sqlInjCheck = {bizInfoParams[0]};
					SqlInjectionUtil.filterContent(sqlInjCheck);
					ls = this.reimburseBaseBiztypeAccntItemRelaService.queryFeeItemByBizTypeCostTypeNow(costInfoParams[0],costInfoParams[1], bizInfoParams[0]);
				}else if(bizInfoParams.length==2 && !bizInfoParams[1].equals("")) {
					final String[] sqlInjCheck = {bizInfoParams[0],bizInfoParams[1]};
					SqlInjectionUtil.filterContent(sqlInjCheck);
					ls = this.reimburseBaseBiztypeAccntItemRelaService.queryErpAccntByFeeItemNow(costInfoParams[0],costInfoParams[1], bizInfoParams[0], bizInfoParams[1]);
				}else {
					result.error500("业务大小类类型格式不正确！");
					return result;
				}
			}else {
				//只有业务支出成本类型信息，获取业务大类
				ls = this.reimburseBaseBiztypeAccntItemRelaService.queryBizTypeByCostTypeNow(costInfoParams[0],costInfoParams[1]);
			}
			
			 result.setSuccess(true);
			 result.setResult(ls);
			 log.info(result.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}
	
}
