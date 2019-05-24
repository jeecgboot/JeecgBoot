package org.jeecg.modules.quartz.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.quartz.entity.QuartzJob;
import org.jeecg.modules.quartz.service.IQuartzJobService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-01-02
 * @Version:V1.0
 */
@RestController
@RequestMapping("/sys/quartzJob")
@Slf4j
@Api(tags = "定时任务接口")
public class QuartzJobController {
	@Autowired
	private IQuartzJobService quartzJobService;
	@Autowired
	private Scheduler scheduler;

	/**
	 * 分页列表查询
	 * 
	 * @param quartzJob
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<QuartzJob>> queryPageList(QuartzJob quartzJob, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<QuartzJob>> result = new Result<IPage<QuartzJob>>();
		QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, req.getParameterMap());
		Page<QuartzJob> page = new Page<QuartzJob>(pageNo, pageSize);
		IPage<QuartzJob> pageList = quartzJobService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加定时任务
	 * 
	 * @param quartzJob
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<?> add(@RequestBody QuartzJob quartzJob) {
		Result<QuartzJob> result = new Result<QuartzJob>();

		List<QuartzJob> list = quartzJobService.findByJobClassName(quartzJob.getJobClassName());
		if (list != null && list.size() > 0) {
			return Result.error("该定时任务类名已存在");
		}
		try {
			boolean ok = quartzJobService.saveAndScheduleJob(quartzJob);
			if (ok) {
				result.success("创建定时任务成功");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("创建定时任务失败，" + e.getMessage());
		}
		return result;
	}

	/**
	 * 更新定时任务
	 * 
	 * @param quartzJob
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<?> eidt(@RequestBody QuartzJob quartzJob) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		QuartzJob quartzJobEntity = quartzJobService.getById(quartzJob.getId());
		if (quartzJobEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = true;
			try {
				ok = quartzJobService.editAndScheduleJob(quartzJob);
			} catch (SchedulerException e) {
				log.error(e.getMessage(),e);
				return Result.error("更新定时任务失败!");
			}
			if (ok) {
				result.success("更新定时任务成功!");
			}
		}
		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<QuartzJob> delete(@RequestParam(name = "id", required = true) String id) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = quartzJobService.deleteAndStopJob(quartzJob);
			if (ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<QuartzJob> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			for (String id : Arrays.asList(ids.split(","))) {
				QuartzJob job = quartzJobService.getById(id);
				quartzJobService.deleteAndStopJob(job);
			}
			result.success("删除定时任务成功!");
		}
		return result;
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param job
	 * @return
	 */
	@GetMapping(value = "/pause")
	@ApiOperation(value = "暂停定时任务")
	public Result<Object> pauseJob(@RequestParam(name = "jobClassName", required = true) String jobClassName) {
		QuartzJob job = null;
		try {
			job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
			if (job == null) {
				return Result.error("定时任务不存在！");
			}
			scheduler.pauseJob(JobKey.jobKey(jobClassName.trim()));
		} catch (SchedulerException e) {
			throw new JeecgBootException("暂停定时任务失败");
		}
		job.setStatus(CommonConstant.STATUS_DISABLE);
		quartzJobService.updateById(job);
		return Result.ok("暂停定时任务成功");
	}

	/**
	 * 启动定时任务
	 * 
	 * @param job
	 * @return
	 */
	@GetMapping(value = "/resume")
	@ApiOperation(value = "恢复定时任务")
	public Result<Object> resumeJob(@RequestParam(name = "jobClassName", required = true) String jobClassName) {
		QuartzJob job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
		if (job == null) {
			return Result.error("定时任务不存在！");
		}
		quartzJobService.resumeJob(job);
		//scheduler.resumeJob(JobKey.jobKey(job.getJobClassName().trim()));
		return Result.ok("恢复定时任务成功");
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<QuartzJob> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<QuartzJob> result = new Result<QuartzJob>();
		QuartzJob quartzJob = quartzJobService.getById(id);
		if (quartzJob == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(quartzJob);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, QuartzJob quartzJob) {
		// Step.1 组装查询条件
		QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, request.getParameterMap());
		// Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<QuartzJob> pageList = quartzJobService.list(queryWrapper);
		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "定时任务列表");
		mv.addObject(NormalExcelConstants.CLASS, QuartzJob.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("定时任务列表数据", "导出人:Jeecg", "导出信息"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
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
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<QuartzJob> listQuartzJobs = ExcelImportUtil.importExcel(file.getInputStream(), QuartzJob.class, params);
				for (QuartzJob quartzJobExcel : listQuartzJobs) {
					quartzJobService.save(quartzJobExcel);
				}
				return Result.ok("文件导入成功！数据行数：" + listQuartzJobs.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败！");
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}
}
