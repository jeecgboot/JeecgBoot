package org.jeecg.modules.airag.prompts.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.airag.prompts.entity.AiragPrompts;
import org.jeecg.modules.airag.prompts.service.IAiragPromptsService;
import org.jeecg.modules.airag.prompts.vo.AiragExperimentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
 /**
 * @Description: airag_prompts
 * @Author: jeecg-boot
 * @Date:   2025-12-24
 * @Version: V1.0
 */
@Tag(name="airag_prompts")
@RestController
@RequestMapping("/airag/prompts")
@Slf4j
public class AiragPromptsController extends JeecgController<AiragPrompts, IAiragPromptsService> {
	@Autowired
	private IAiragPromptsService airagPromptsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airagPrompts
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "airag_prompts-分页列表查询")
	@Operation(summary="airag_prompts-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AiragPrompts>> queryPageList(AiragPrompts airagPrompts,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AiragPrompts> queryWrapper = QueryGenerator.initQueryWrapper(airagPrompts, req.getParameterMap());
		Page<AiragPrompts> page = new Page<AiragPrompts>(pageNo, pageSize);
		IPage<AiragPrompts> pageList = airagPromptsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param airagPrompts
	 * @return
	 */
	@AutoLog(value = "airag_prompts-添加")
	@Operation(summary="airag_prompts-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AiragPrompts airagPrompts) {
		airagPrompts.setDelFlag(CommonConstant.DEL_FLAG_0);
		airagPrompts.setStatus("0");
		airagPromptsService.save(airagPrompts);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airagPrompts
	 * @return
	 */
	@AutoLog(value = "airag_prompts-编辑")
	@Operation(summary="airag_prompts-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AiragPrompts airagPrompts) {
		airagPromptsService.updateById(airagPrompts);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airag_prompts-通过id删除")
	@Operation(summary="airag_prompts-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		airagPromptsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airag_prompts-批量删除")
	@Operation(summary="airag_prompts-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airagPromptsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "airag_prompts-通过id查询")
	@Operation(summary="airag_prompts-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AiragPrompts> queryById(@RequestParam(name="id",required=true) String id) {
		AiragPrompts airagPrompts = airagPromptsService.getById(id);
		if(airagPrompts==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(airagPrompts);
	}
	 /**
	  * 构造器调试
	  *
	  * @param experimentVo
	  * @return
	  */
	 @PostMapping(value = "/experiment")
	 public Result<?> promptExperiment(@RequestBody AiragExperimentVo experimentVo, HttpServletRequest request) {
		 return airagPromptsService.promptExperiment(experimentVo,request);
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param airagPrompts
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AiragPrompts airagPrompts) {
        return super.exportXls(request, airagPrompts, AiragPrompts.class, "airag_prompts");
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
        return super.importExcel(request, response, AiragPrompts.class);
    }

}
