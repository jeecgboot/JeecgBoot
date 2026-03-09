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
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.airag.prompts.consts.AiPromptsConsts;
import org.jeecg.modules.airag.prompts.entity.AiragExtData;
import org.jeecg.modules.airag.prompts.service.IAiragExtDataService;
import org.jeecg.modules.airag.prompts.vo.AiragDebugVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: airag_ext_data
 * @Author: jeecg-boot
 * @Date:   2025-12-24
 * @Version: V1.0
 */
@Tag(name="airag_ext_data")
@RestController
@RequestMapping("/airag/extData")
@Slf4j
public class AiragExtDataController extends JeecgController<AiragExtData, IAiragExtDataService> {
	@Autowired
	private IAiragExtDataService airagExtDataService;
	
	/**
	 * 分页列表查询
	 *
	 * @param airagExtData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "airag_ext_data-分页列表查询")
	@Operation(summary="airag_ext_data-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<AiragExtData>> queryPageList(AiragExtData airagExtData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AiragExtData> queryWrapper = QueryGenerator.initQueryWrapper(airagExtData, req.getParameterMap());
		Page<AiragExtData> page = new Page<AiragExtData>(pageNo, pageSize);
		queryWrapper.eq("biz_type", AiPromptsConsts.BIZ_TYPE_EVALUATOR);
		IPage<AiragExtData> pageList = airagExtDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	/**
	 * 调用轨迹列表查询
	 *
	 * @param airagExtData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@Operation(summary="airag_ext_data-分页列表查询")
	@GetMapping(value = "/getTrackList")
	public Result<IPage<AiragExtData>> getTrackList(AiragExtData airagExtData,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<AiragExtData> queryWrapper = QueryGenerator.initQueryWrapper(airagExtData, req.getParameterMap());
		Page<AiragExtData> page = new Page<AiragExtData>(pageNo, pageSize);
		queryWrapper.eq("biz_type", AiPromptsConsts.BIZ_TYPE_TRACK);
		String metadata = airagExtData.getMetadata();
		if(oConvertUtils.isEmpty(metadata)){
			return Result.OK();
		}
		IPage<AiragExtData> pageList = airagExtDataService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param airagExtData
	 * @return
	 */
	@AutoLog(value = "airag_ext_data-添加")
	@Operation(summary="airag_ext_data-添加")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody AiragExtData airagExtData) {
		airagExtData.setBizType(AiPromptsConsts.BIZ_TYPE_EVALUATOR);
		airagExtDataService.save(airagExtData);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param airagExtData
	 * @return
	 */
	@AutoLog(value = "airag_ext_data-编辑")
	@Operation(summary="airag_ext_data-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody AiragExtData airagExtData) {
		airagExtDataService.updateById(airagExtData);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "airag_ext_data-通过id删除")
	@Operation(summary="airag_ext_data-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		airagExtDataService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "airag_ext_data-批量删除")
	@Operation(summary="airag_ext_data-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.airagExtDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "airag_ext_data-通过id查询")
	@Operation(summary="airag_ext_data-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<AiragExtData> queryById(@RequestParam(name="id",required=true) String id) {
		AiragExtData airagExtData = airagExtDataService.getById(id);
		if(airagExtData==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(airagExtData);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "airag_ext_data-通过id查询")
	@Operation(summary="airag_ext_data-通过id查询")
	@GetMapping(value = "/queryTrackById")
	public Result<List<AiragExtData>> queryTrackById(@RequestParam(name="id",required=true) String id) {
		AiragExtData airagExtData = airagExtDataService.getById(id);
		String status = airagExtData.getStatus();
		if(AiPromptsConsts.STATUS_RUNNING.equals(status)) {
			return Result.error("处理中，请稍后刷新");
		}
		List<AiragExtData> trackList = airagExtDataService.queryTrackById(id);
		return Result.OK(trackList);
	}
	 /**
	  * 构造器调试
	  *
	  * @param debugVo
	  * @return
	  */
	 @PostMapping(value = "/evaluator/debug")
	 public Result<?> debugEvaluator(@RequestBody AiragDebugVo debugVo) {
		 return airagExtDataService.debugEvaluator(debugVo);
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param airagExtData
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AiragExtData airagExtData) {
        return super.exportXls(request, airagExtData, AiragExtData.class, "airag_ext_data");
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
        return super.importExcel(request, response, AiragExtData.class);
    }

}
