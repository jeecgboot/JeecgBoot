package com.xxl.job.admin.business.controller;

import com.xxl.job.admin.business.mapper.XxlJobInfoMapper;
import com.xxl.job.admin.business.mapper.XxlJobLogGlueMapper;
import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLogGlue;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.admin.framework.util.JobGroupPermissionUtil;
import com.xxl.job.admin.framework.util.XssUtil;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.json.GsonTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * job code controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobcode")
public class JobCodeController {
	private static final Logger logger = LoggerFactory.getLogger(JobCodeController.class);
	
	@Resource
	private XxlJobInfoMapper xxlJobInfoMapper;
	@Resource
	private XxlJobLogGlueMapper xxlJobLogGlueMapper;

	@RequestMapping
	public String index(HttpServletRequest request, Model model, @RequestParam("jobId") int jobId) {
		XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(jobId);
		List<XxlJobLogGlue> jobLogGlues = xxlJobLogGlueMapper.findByJobId(jobId);

		if (jobInfo == null) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
		}
		if (GlueTypeEnum.BEAN == GlueTypeEnum.match(jobInfo.getGlueType())) {
			throw new RuntimeException(I18nUtil.getString("jobinfo_glue_gluetype_invalid"));
		}

		// valid jobGroup permission
		JobGroupPermissionUtil.validJobGroupPermission(request, jobInfo.getJobGroup());

		// Glue类型-字典
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());

		model.addAttribute("jobInfo", jobInfo);
		model.addAttribute("jobLogGlues", jobLogGlues);
		return "business/job.code";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Response<String> save(HttpServletRequest request,
								 @RequestParam("id") int id,
								 @RequestParam("glueSource") String glueSource,
								 @RequestParam("glueRemark") String glueRemark) {

		// valid
		if (StringTool.isBlank(glueSource)) {
			return Response.ofFail( (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_source")) );
		}
		if (glueRemark==null) {
			return Response.ofFail( (I18nUtil.getString("system_please_input") + I18nUtil.getString("jobinfo_glue_remark")) );
		}
		if (glueRemark.length()<4 || glueRemark.length()>100) {
			return Response.ofFail(I18nUtil.getString("jobinfo_glue_remark_limit"));
		}
		if (XssUtil.hasXss(glueRemark)) {
			return Response.ofFail(I18nUtil.getString("jobinfo_glue_remark") + I18nUtil.getString("system_invalid"));
		}
		XxlJobInfo existsJobInfo = xxlJobInfoMapper.loadById(id);
		if (existsJobInfo == null) {
			return Response.ofFail( I18nUtil.getString("jobinfo_glue_jobid_invalid"));
		}

		// valid jobGroup permission
		LoginInfo loginInfo = JobGroupPermissionUtil.validJobGroupPermission(request, existsJobInfo.getJobGroup());

		// update new code
		existsJobInfo.setGlueSource(glueSource);
		existsJobInfo.setGlueRemark(glueRemark);
		existsJobInfo.setGlueUpdatetime(new Date());

		existsJobInfo.setUpdateTime(new Date());
		xxlJobInfoMapper.update(existsJobInfo);

		// log old code
		XxlJobLogGlue xxlJobLogGlue = new XxlJobLogGlue();
		xxlJobLogGlue.setJobId(existsJobInfo.getId());
		xxlJobLogGlue.setGlueType(existsJobInfo.getGlueType());
		xxlJobLogGlue.setGlueSource(glueSource);
		xxlJobLogGlue.setGlueRemark(glueRemark);

		xxlJobLogGlue.setAddTime(new Date());
		xxlJobLogGlue.setUpdateTime(new Date());
		xxlJobLogGlueMapper.save(xxlJobLogGlue);

		// remove code backup more than 30
		xxlJobLogGlueMapper.removeOld(existsJobInfo.getId(), 30);

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobcode-update", GsonTool.toJson(xxlJobLogGlue));
		return Response.ofSuccess();
	}

}
