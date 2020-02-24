package com.ibm.fms.modules.reimburse.base.controller;

import org.jeecg.common.system.query.QueryGenerator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import java.util.Arrays;
import org.jeecg.common.util.oConvertUtils;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorCompanyInfo;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorAccountInfo;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorContactsInfo;
import com.ibm.fms.modules.reimburse.base.entity.MdmVendorInfo;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorInfoService;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorCompanyInfoService;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorAccountInfoService;
import com.ibm.fms.modules.reimburse.base.service.IMdmVendorContactsInfoService;


 /**
 * @Description: 主数据供应商信息
 * @Author: jeecg-boot
 * @Date:   2020-01-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/mdmVendorInfo")
@Slf4j
public class MdmVendorInfoController extends JeecgController<MdmVendorInfo, IMdmVendorInfoService> {

	@Autowired
	private IMdmVendorInfoService mdmVendorInfoService;

	@Autowired
	private IMdmVendorCompanyInfoService mdmVendorCompanyInfoService;

	@Autowired
	private IMdmVendorAccountInfoService mdmVendorAccountInfoService;

	@Autowired
	private IMdmVendorContactsInfoService mdmVendorContactsInfoService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param mdmVendorInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(MdmVendorInfo mdmVendorInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<MdmVendorInfo> queryWrapper = QueryGenerator.initQueryWrapper(mdmVendorInfo, req.getParameterMap());
		Page<MdmVendorInfo> page = new Page<MdmVendorInfo>(pageNo, pageSize);
		IPage<MdmVendorInfo> pageList = mdmVendorInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
     *   添加
     * @param mdmVendorInfo
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MdmVendorInfo mdmVendorInfo) {
        mdmVendorInfoService.save(mdmVendorInfo);
        return Result.ok("添加成功！");
    }

    /**
     *  编辑
     * @param mdmVendorInfo
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody MdmVendorInfo mdmVendorInfo) {
        mdmVendorInfoService.updateById(mdmVendorInfo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        mdmVendorInfoService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.mdmVendorInfoService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MdmVendorInfo mdmVendorInfo) {
        return super.exportXls(request, mdmVendorInfo, MdmVendorInfo.class, "主数据供应商信息");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MdmVendorInfo.class);
    }
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-主数据供应商归属组织-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listMdmVendorCompanyInfoByMainId")
    public Result<?> listMdmVendorCompanyInfoByMainId(MdmVendorCompanyInfo mdmVendorCompanyInfo,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<MdmVendorCompanyInfo> queryWrapper = QueryGenerator.initQueryWrapper(mdmVendorCompanyInfo, req.getParameterMap());
        Page<MdmVendorCompanyInfo> page = new Page<MdmVendorCompanyInfo>(pageNo, pageSize);
        IPage<MdmVendorCompanyInfo> pageList = mdmVendorCompanyInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param mdmVendorCompanyInfo
	 * @return
	 */
	@PostMapping(value = "/addMdmVendorCompanyInfo")
	public Result<?> addMdmVendorCompanyInfo(@RequestBody MdmVendorCompanyInfo mdmVendorCompanyInfo) {
		mdmVendorCompanyInfoService.save(mdmVendorCompanyInfo);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param mdmVendorCompanyInfo
	 * @return
	 */
	@PutMapping(value = "/editMdmVendorCompanyInfo")
	public Result<?> editMdmVendorCompanyInfo(@RequestBody MdmVendorCompanyInfo mdmVendorCompanyInfo) {
		mdmVendorCompanyInfoService.updateById(mdmVendorCompanyInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteMdmVendorCompanyInfo")
	public Result<?> deleteMdmVendorCompanyInfo(@RequestParam(name="id",required=true) String id) {
		mdmVendorCompanyInfoService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchMdmVendorCompanyInfo")
	public Result<?> deleteBatchMdmVendorCompanyInfo(@RequestParam(name="ids",required=true) String ids) {
		this.mdmVendorCompanyInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-主数据供应商归属组织-end----------------------------------------------*/

    /*--------------------------------子表处理-主数据供应商银行账户-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listMdmVendorAccountInfoByMainId")
    public Result<?> listMdmVendorAccountInfoByMainId(MdmVendorAccountInfo mdmVendorAccountInfo,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<MdmVendorAccountInfo> queryWrapper = QueryGenerator.initQueryWrapper(mdmVendorAccountInfo, req.getParameterMap());
        Page<MdmVendorAccountInfo> page = new Page<MdmVendorAccountInfo>(pageNo, pageSize);
        IPage<MdmVendorAccountInfo> pageList = mdmVendorAccountInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param mdmVendorAccountInfo
	 * @return
	 */
	@PostMapping(value = "/addMdmVendorAccountInfo")
	public Result<?> addMdmVendorAccountInfo(@RequestBody MdmVendorAccountInfo mdmVendorAccountInfo) {
		mdmVendorAccountInfoService.save(mdmVendorAccountInfo);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param mdmVendorAccountInfo
	 * @return
	 */
	@PutMapping(value = "/editMdmVendorAccountInfo")
	public Result<?> editMdmVendorAccountInfo(@RequestBody MdmVendorAccountInfo mdmVendorAccountInfo) {
		mdmVendorAccountInfoService.updateById(mdmVendorAccountInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteMdmVendorAccountInfo")
	public Result<?> deleteMdmVendorAccountInfo(@RequestParam(name="id",required=true) String id) {
		mdmVendorAccountInfoService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchMdmVendorAccountInfo")
	public Result<?> deleteBatchMdmVendorAccountInfo(@RequestParam(name="ids",required=true) String ids) {
		this.mdmVendorAccountInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-主数据供应商银行账户-end----------------------------------------------*/

    /*--------------------------------子表处理-主数据供应商联系人-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listMdmVendorContactsInfoByMainId")
    public Result<?> listMdmVendorContactsInfoByMainId(MdmVendorContactsInfo mdmVendorContactsInfo,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<MdmVendorContactsInfo> queryWrapper = QueryGenerator.initQueryWrapper(mdmVendorContactsInfo, req.getParameterMap());
        Page<MdmVendorContactsInfo> page = new Page<MdmVendorContactsInfo>(pageNo, pageSize);
        IPage<MdmVendorContactsInfo> pageList = mdmVendorContactsInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param mdmVendorContactsInfo
	 * @return
	 */
	@PostMapping(value = "/addMdmVendorContactsInfo")
	public Result<?> addMdmVendorContactsInfo(@RequestBody MdmVendorContactsInfo mdmVendorContactsInfo) {
		mdmVendorContactsInfoService.save(mdmVendorContactsInfo);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param mdmVendorContactsInfo
	 * @return
	 */
	@PutMapping(value = "/editMdmVendorContactsInfo")
	public Result<?> editMdmVendorContactsInfo(@RequestBody MdmVendorContactsInfo mdmVendorContactsInfo) {
		mdmVendorContactsInfoService.updateById(mdmVendorContactsInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteMdmVendorContactsInfo")
	public Result<?> deleteMdmVendorContactsInfo(@RequestParam(name="id",required=true) String id) {
		mdmVendorContactsInfoService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchMdmVendorContactsInfo")
	public Result<?> deleteBatchMdmVendorContactsInfo(@RequestParam(name="ids",required=true) String ids) {
		this.mdmVendorContactsInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-主数据供应商联系人-end----------------------------------------------*/




}
