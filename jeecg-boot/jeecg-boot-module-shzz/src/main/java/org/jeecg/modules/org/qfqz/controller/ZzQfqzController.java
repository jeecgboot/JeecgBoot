package org.jeecg.modules.org.qfqz.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.org.qfqz.entity.ZzQfqz;
import org.jeecg.modules.org.qfqz.service.IZzQfqzService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description: 群防群治组织
 * @Author: jeecg-boot
 * @Date:   2020-02-05
 * @Version: V1.0
 */
@RestController
@RequestMapping("/zzQfqz/zzQfqz")
@Slf4j
public class ZzQfqzController extends JeecgController<ZzQfqz, IZzQfqzService> {
	@Autowired
	private IZzQfqzService zzQfqzService;
	
	/**
	 * 分页列表查询
	 *
	 * @param zzQfqz
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZzQfqz zzQfqz,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzQfqz> queryWrapper = QueryGenerator.initQueryWrapper(zzQfqz, req.getParameterMap());
		Page<ZzQfqz> page = new Page<ZzQfqz>(pageNo, pageSize);
		IPage<ZzQfqz> pageList = zzQfqzService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param zzQfqz
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzQfqz zzQfqz) {
		zzQfqzService.save(zzQfqz);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param zzQfqz
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzQfqz zzQfqz) {
		zzQfqzService.updateById(zzQfqz);
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
		zzQfqzService.removeById(id);
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
		this.zzQfqzService.removeByIds(Arrays.asList(ids.split(",")));
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
		ZzQfqz zzQfqz = zzQfqzService.getById(id);
		if(zzQfqz==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(zzQfqz);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zzQfqz
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZzQfqz zzQfqz) {
        return super.exportXls(request, zzQfqz, ZzQfqz.class, "群防群治组织");
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
        return super.importExcel(request, response, ZzQfqz.class);
    }

}
