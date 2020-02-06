package org.jeecg.modules.staff.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.staff.entity.ZzStaff;
import org.jeecg.modules.staff.service.IZzStaffService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 员工
 * @Author: jeecg-boot
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/zzStaff/zzStaff")
@Slf4j
public class ZzStaffController extends JeecgController<ZzStaff, IZzStaffService> {
	@Autowired
	private IZzStaffService zzStaffService;
	
	/**
	 * 分页列表查询
	 *
	 * @param zzStaff
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZzStaff zzStaff,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzStaff> queryWrapper = QueryGenerator.initQueryWrapper(zzStaff, req.getParameterMap());
		Page<ZzStaff> page = new Page<ZzStaff>(pageNo, pageSize);
		IPage<ZzStaff> pageList = zzStaffService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param zzStaff
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzStaff zzStaff) {
		zzStaffService.save(zzStaff);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param zzStaff
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzStaff zzStaff) {
		zzStaffService.updateById(zzStaff);
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
		zzStaffService.removeById(id);
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
		this.zzStaffService.removeByIds(Arrays.asList(ids.split(",")));
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
		ZzStaff zzStaff = zzStaffService.getById(id);
		if(zzStaff==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(zzStaff);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zzStaff
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZzStaff zzStaff) {
        return super.exportXls(request, zzStaff, ZzStaff.class, "员工");
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
        return super.importExcel(request, response, ZzStaff.class);
    }

}
