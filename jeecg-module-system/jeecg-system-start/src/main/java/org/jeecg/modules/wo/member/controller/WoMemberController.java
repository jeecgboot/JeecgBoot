package org.jeecg.modules.wo.member.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.wo.member.entity.WoMember;
import org.jeecg.modules.wo.member.service.IWoMemberService;
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
 * @Description: 点餐用户
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Slf4j
@Api(tags="点餐用户")
@RestController
@RequestMapping("/member/woMember")
public class WoMemberController extends JeecgController<WoMember, IWoMemberService> {
	@Autowired
	private IWoMemberService woMemberService;
	
	/**
	 * 分页列表查询
	 *
	 * @param woMember
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "点餐用户-分页列表查询")
	@ApiOperation(value="点餐用户-分页列表查询", notes="点餐用户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WoMember woMember,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WoMember> queryWrapper = QueryGenerator.initQueryWrapper(woMember, req.getParameterMap());
		Page<WoMember> page = new Page<WoMember>(pageNo, pageSize);
		IPage<WoMember> pageList = woMemberService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param woMember
	 * @return
	 */
	@AutoLog(value = "点餐用户-添加")
	@ApiOperation(value="点餐用户-添加", notes="点餐用户-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WoMember woMember) {
		woMemberService.save(woMember);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param woMember
	 * @return
	 */
	@AutoLog(value = "点餐用户-编辑")
	@ApiOperation(value="点餐用户-编辑", notes="点餐用户-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody WoMember woMember) {
		woMemberService.updateById(woMember);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点餐用户-通过id删除")
	@ApiOperation(value="点餐用户-通过id删除", notes="点餐用户-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		woMemberService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "点餐用户-批量删除")
	@ApiOperation(value="点餐用户-批量删除", notes="点餐用户-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.woMemberService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "点餐用户-通过id查询")
	@ApiOperation(value="点餐用户-通过id查询", notes="点餐用户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WoMember woMember = woMemberService.getById(id);
		return Result.OK(woMember);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param woMember
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WoMember woMember) {
      return super.exportXls(request, woMember, WoMember.class, "点餐用户");
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
      return super.importExcel(request, response, WoMember.class);
  }

}
