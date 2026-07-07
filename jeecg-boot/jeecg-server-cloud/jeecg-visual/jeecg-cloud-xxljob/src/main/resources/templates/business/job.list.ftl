<!DOCTYPE html>
<html>
<head>
	<#-- import macro -->
	<#import "../framework/common/common.macro.ftl" as netCommon>

	<!-- 1-style start -->
	<#-- select2：should before adminlte -->
	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/select2/select2.min.css">
	<@netCommon.commonStyle />
	<link rel="stylesheet" href="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.css">
	<!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
	<section class="content">

		<!-- 2-content start -->

		<#-- 查询区域 -->
		<div class="box" style="margin-bottom:9px;">
			<div class="box-body">
				<div class="row" id="data_filter" >

					<div class="col-xs-3">
						<div class="input-group">
							<span class="input-group-addon">${I18n.jobinfo_field_jobgroup}</span>
							<select class="form-control" id="jobGroup" >
								<#list JobGroupList as group>
									<option value="${group.id}" <#if jobGroup==group.id>selected</#if> >${group.title}</option>
								</#list>
							</select>
						</div>
					</div>
					<div class="col-xs-1">
						<div class="input-group">
							<select class="form-control" id="triggerStatus" >
								<option value="-1" >${I18n.system_all}</option>
								<option value="0" >${I18n.jobinfo_opt_stop}</option>
								<option value="1" >${I18n.jobinfo_opt_start}</option>
							</select>
						</div>
					</div>
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control" id="jobDesc" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_jobdesc}" >
						</div>
					</div>
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control" id="executorHandler" placeholder="${I18n.system_please_input}JobHandler" >
						</div>
					</div>
					<div class="col-xs-2">
						<div class="input-group">
							<input type="text" class="form-control" id="author" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_author}" >
						</div>
					</div>

					<div class="col-xs-1">
						<button class="btn btn-block btn-primary searchBtn" >${I18n.system_search}</button>
					</div>
					<div class="col-xs-1">
						<button class="btn btn-block btn-default resetBtn" >${I18n.system_reset}</button>
					</div>
				</div>
			</div>
		</div>

		<#-- 数据表格区域 -->
		<div class="row">
			<div class="col-xs-12">
				<div class="box">
					<div class="box-header pull-left" id="data_operation" >
						<button class="btn btn-sm btn-info add" type="button"><i class="fa fa-plus" ></i>${I18n.system_opt_add}</button>                        <#-- add -->
						<button class="btn btn-sm btn-warning selectOnlyOne update" type="button"><i class="fa fa-edit"></i>${I18n.system_opt_edit}</button>    <#-- update -->
                        <button class="btn btn-sm btn-warning selectOnlyOne glue_ide" type="button">GLUE IDE</button>									        <#-- GLUE IDE：'BEAN' != row.glueType -->
						<button class="btn btn-sm btn-danger selectOnlyOne delete" type="button"><i class="fa fa-remove "></i>${I18n.system_opt_del}</button>   <#-- delete -->
						｜
						<button class="btn btn-sm btn-default selectOnlyOne job_copy" type="button">${I18n.system_opt_copy}</button>
						<button class="btn btn-sm btn-warning selectOnlyOne job_resume" type="button">${I18n.jobinfo_opt_start}</button>				<#-- 启动 -->
						<button class="btn btn-sm btn-warning selectOnlyOne job_pause" type="button">${I18n.jobinfo_opt_stop}</button>					<#-- 停止 -->
						｜
						<button class="btn btn-sm btn-primary selectOnlyOne job_trigger" type="button">${I18n.jobinfo_opt_run}</button>					<#-- 执行一次 -->
						<button class="btn btn-sm btn-primary selectOnlyOne job_log" type="button">${I18n.jobinfo_opt_log}</button>						<#-- 执行日志：base_url +'/joblog?jobId='+ row.id -->
						<button class="btn btn-sm btn-default selectOnlyOne job_registryinfo" type="button">${I18n.jobinfo_opt_registryinfo}</button>	<#-- 注册节点 -->
						<button class="btn btn-sm btn-default selectOnlyOne job_next_time" type="button">${I18n.jobinfo_opt_next_time}</button>			<#-- 下次执行时间：row.scheduleType == 'CRON' || row.scheduleType == 'FIX_RATE' -->
					</div>
					<div class="box-body" >
						<table id="data_list" class="table table-bordered table-striped" width="100%" >
							<thead></thead>
							<tbody></tbody>
							<tfoot></tfoot>
						</table>
					</div>
				</div>
			</div>
		</div>

		<!-- job新增.模态框 -->
		<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.jobinfo_field_add}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >

							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_base}</p>    <#-- 基础信息 -->
							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_jobgroup}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="jobGroup" >
										<#list JobGroupList as group>
											<option value="${group.id}" <#if jobGroup==group.id>selected</#if> >${group.title}</option>
										</#list>
									</select>
								</div>

								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_jobdesc}<font color="red">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="jobDesc" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_jobdesc}" maxlength="50" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_author}<font color="red">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="author" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_author}" maxlength="50" ></div>
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_alarmemail}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="alarmEmail" placeholder="${I18n.jobinfo_field_alarmemail_placeholder}" maxlength="100" ></div>
							</div>

							<br>
							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_schedule}</p>    <#-- 调度 -->
							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.schedule_type}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control scheduleType" name="scheduleType" >
										<#list ScheduleTypeEnum as item>
											<option value="${item}" <#if 'CRON' == item >selected</#if> >${item.title}</option>
										</#list>
									</select>
								</div>

								<input type="hidden" name="scheduleConf" />
								<div class="schedule_conf schedule_conf_NONE" style="display: none" >
								</div>
								<div class="schedule_conf schedule_conf_CRON" >
									<label for="lastname" class="col-sm-2 control-label">Cron<font color="red">*</font></label>
									<div class="col-sm-4"><input type="text" class="form-control" name="schedule_conf_CRON" placeholder="${I18n.system_please_input}Cron" maxlength="128" ></div>
								</div>
								<div class="schedule_conf schedule_conf_FIX_RATE" style="display: none" >
									<label for="lastname" class="col-sm-2 control-label">${I18n.schedule_type_fix_rate}<font color="red">*</font></label>
									<div class="col-sm-4"><input type="text" class="form-control" name="schedule_conf_FIX_RATE" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
								</div>
								<div class="schedule_conf schedule_conf_FIX_DELAY" style="display: none" >
									<label for="lastname" class="col-sm-2 control-label">${I18n.schedule_type_fix_delay}<font color="red">*</font></label>
									<div class="col-sm-4"><input type="text" class="form-control" name="schedule_conf_FIX_DELAY" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
								</div>
							</div>

							<br>
							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_job}</p>    <#-- 任务配置 -->

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_gluetype}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control glueType" name="glueType" >
										<#list GlueTypeEnum as item>
											<option value="${item}" >${item.desc}</option>
										</#list>
									</select>
								</div>
								<label for="firstname" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="executorHandler" placeholder="${I18n.system_please_input}JobHandler" maxlength="100" ></div>
							</div>

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
								<div class="col-sm-10">
									<textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="2048" style="height: 70px; line-height: 1.2;"></textarea>
								</div>
							</div>

							<br>
							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_advanced}</p>    <#-- 高级配置 -->

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorRouteStrategy}<font color="black">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="executorRouteStrategy" >
										<#list ExecutorRouteStrategyEnum as item>
											<option value="${item}" >${item.title}</option>
										</#list>
									</select>
								</div>

								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_childJobId}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="childJobId" placeholder="${I18n.jobinfo_field_childJobId_placeholder}" maxlength="100" ></div>
							</div>

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.misfire_strategy}<font color="black">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="misfireStrategy" >
										<#list MisfireStrategyEnum as item>
											<option value="${item}" <#if 'DO_NOTHING' == item >selected</#if> >${item.title}</option>
										</#list>
									</select>
								</div>

								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorBlockStrategy}<font color="black">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="executorBlockStrategy" >
										<#list ExecutorBlockStrategyEnum as item>
											<option value="${item}" >${item.title}</option>
										</#list>
									</select>
								</div>
							</div>

							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_timeout}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="executorTimeout" placeholder="${I18n.jobinfo_field_executorTimeout_placeholder}" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorFailRetryCount}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="executorFailRetryCount" placeholder="${I18n.jobinfo_field_executorFailRetryCount_placeholder}" maxlength="4" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
							</div>

							<hr>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-6">
									<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
								</div>
							</div>

<input type="hidden" name="glueRemark" value="GLUE代码初始化" >
<textarea name="glueSource" style="display:none;" ></textarea>
<textarea class="glueSource_java" style="display:none;" >
package com.xxl.job.service.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;

public class DemoGlueJobHandler extends IJobHandler {

	@Override
	public void execute() throws Exception {
		XxlJobHelper.log("XXL-JOB, Hello World.");
	}

}
</textarea>
<textarea class="glueSource_shell" style="display:none;" >
#!/bin/bash
echo "xxl-job: hello shell"

echo "${I18n.jobinfo_script_location}：$0"
echo "${I18n.jobinfo_field_executorparam}：$1"
echo "${I18n.jobinfo_shard_index} = $2"
echo "${I18n.jobinfo_shard_total} = $3"
<#--echo "参数数量：$#"
for param in $*
do
    echo "参数 : $param"
    sleep 1s
done-->

echo "Good bye!"
exit 0
</textarea>
<textarea class="glueSource_python" style="display:none;" >
#!/usr/bin/python
# -*- coding: UTF-8 -*-
import time
import sys

print("xxl-job: hello python")

print("${I18n.jobinfo_script_location}：", sys.argv[0])
print("${I18n.jobinfo_field_executorparam}：", sys.argv[1])
print("${I18n.jobinfo_shard_index}：", sys.argv[2])
print("${I18n.jobinfo_shard_total}：", sys.argv[3])

print("Good bye!")
exit(0)
</textarea>
<textarea class="glueSource_python2" style="display:none;" >
#!/usr/bin/python
# -*- coding: UTF-8 -*-
import time
import sys

print "xxl-job: hello python"

print "${I18n.jobinfo_script_location}：", sys.argv[0]
print "${I18n.jobinfo_field_executorparam}：", sys.argv[1]
print "${I18n.jobinfo_shard_index}：", sys.argv[2]
print "${I18n.jobinfo_shard_total}：", sys.argv[3]
<#--for i in range(1, len(sys.argv)):
	time.sleep(1)
	print "参数", i, sys.argv[i]-->

print "Good bye!"
exit(0)
<#--
import logging
logging.basicConfig(level=logging.DEBUG)
logging.info("脚本文件：" + sys.argv[0])
-->
</textarea>
<textarea class="glueSource_php" style="display:none;" >
<?php

    echo "xxl-job: hello php  \n";

    echo "${I18n.jobinfo_script_location}：$argv[0]  \n";
    echo "${I18n.jobinfo_field_executorparam}：$argv[1]  \n";
    echo "${I18n.jobinfo_shard_index} = $argv[2]  \n";
    echo "${I18n.jobinfo_shard_total} = $argv[3]  \n";

    echo "Good bye!  \n";
    exit(0);

?>
</textarea>
<textarea class="glueSource_nodejs" style="display:none;" >
#!/usr/bin/env node
console.log("xxl-job: hello nodejs")

var arguments = process.argv

console.log("${I18n.jobinfo_script_location}: " + arguments[1])
console.log("${I18n.jobinfo_field_executorparam}: " + arguments[2])
console.log("${I18n.jobinfo_shard_index}: " + arguments[3])
console.log("${I18n.jobinfo_shard_total}: " + arguments[4])
<#--for (var i = 2; i < arguments.length; i++){
	console.log("参数 %s = %s", (i-1), arguments[i]);
}-->

console.log("Good bye!")
process.exit(0)
</textarea>
<textarea class="glueSource_powershell" style="display:none;" >
Write-Host "xxl-job: hello powershell"

Write-Host "${I18n.jobinfo_script_location}: " $MyInvocation.MyCommand.Definition
Write-Host "${I18n.jobinfo_field_executorparam}: "
	if ($args.Count -gt 2) { $args[0..($args.Count-3)] }
Write-Host "${I18n.jobinfo_shard_index}: " $args[$args.Count-2]
Write-Host "${I18n.jobinfo_shard_total}: " $args[$args.Count-1]

Write-Host "Good bye!"
exit 0
</textarea>
						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- 更新.模态框 -->
		<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.jobinfo_field_update}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >

							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_base}</p>    <#-- 基础信息 -->
							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_jobgroup}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="jobGroup" >
										<#list JobGroupList as group>
											<option value="${group.id}" >${group.title}</option>
										</#list>
									</select>
								</div>

								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_jobdesc}<font color="red">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="jobDesc" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_jobdesc}" maxlength="50" ></div>
							</div>
							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_author}<font color="red">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="author" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_author}" maxlength="50" ></div>
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_alarmemail}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="alarmEmail" placeholder="${I18n.jobinfo_field_alarmemail_placeholder}" maxlength="100" ></div>
							</div>

							<br>
							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_schedule}</p>    <#-- 调度配置 -->
							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.schedule_type}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control scheduleType" name="scheduleType" >
										<#list ScheduleTypeEnum as item>
											<option value="${item}" >${item.title}</option>
										</#list>
									</select>
								</div>

								<input type="hidden" name="scheduleConf" />
								<div class="schedule_conf schedule_conf_NONE" style="display: none" >
								</div>
								<div class="schedule_conf schedule_conf_CRON" >
									<label for="lastname" class="col-sm-2 control-label">Cron<font color="red">*</font></label>
									<div class="col-sm-4"><input type="text" class="form-control" name="schedule_conf_CRON" placeholder="${I18n.system_please_input}Cron" maxlength="128" ></div>
								</div>
								<div class="schedule_conf schedule_conf_FIX_RATE" style="display: none" >
									<label for="lastname" class="col-sm-2 control-label">${I18n.schedule_type_fix_rate}<font color="red">*</font></label>
									<div class="col-sm-4"><input type="text" class="form-control" name="schedule_conf_FIX_RATE" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
								</div>
								<div class="schedule_conf schedule_conf_FIX_DELAY" style="display: none" >
									<label for="lastname" class="col-sm-2 control-label">${I18n.schedule_type_fix_delay}<font color="red">*</font></label>
									<div class="col-sm-4"><input type="text" class="form-control" name="schedule_conf_FIX_DELAY" placeholder="${I18n.system_please_input} （ Second ）" maxlength="10" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
								</div>
							</div>

							<br>
							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_job}</p>    <#-- 任务配置 -->

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_gluetype}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control glueType" name="glueType" disabled >
										<#list GlueTypeEnum as item>
											<option value="${item}" >${item.desc}</option>
										</#list>
									</select>
								</div>
								<label for="firstname" class="col-sm-2 control-label">JobHandler<font color="red">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="executorHandler" placeholder="${I18n.system_please_input}JobHandler" maxlength="100" ></div>
							</div>

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
								<div class="col-sm-10">
									<textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="2048" style="height: 70px; line-height: 1.2;"></textarea>
								</div>
							</div>

							<br>
							<p style="margin: 0 0 10px;text-align: left;border-bottom: 1px solid #e5e5e5;color: gray;">${I18n.jobinfo_conf_advanced}</p>    <#-- 高级配置 -->

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorRouteStrategy}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="executorRouteStrategy" >
										<#list ExecutorRouteStrategyEnum as item>
											<option value="${item}" >${item.title}</option>
										</#list>
									</select>
								</div>

								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_childJobId}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="childJobId" placeholder="${I18n.jobinfo_field_childJobId_placeholder}" maxlength="100" ></div>
							</div>

							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.misfire_strategy}<font color="black">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="misfireStrategy" >
										<#list MisfireStrategyEnum as item>
											<option value="${item}" <#if 'DO_NOTHING' == item >selected</#if> >${item.title}</option>
										</#list>
									</select>
								</div>

								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorBlockStrategy}<font color="red">*</font></label>
								<div class="col-sm-4">
									<select class="form-control" name="executorBlockStrategy" >
										<#list ExecutorBlockStrategyEnum as item>
											<option value="${item}" >${item.title}</option>
										</#list>
									</select>
								</div>
							</div>

							<div class="form-group">
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_timeout}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="executorTimeout" placeholder="${I18n.jobinfo_field_executorTimeout_placeholder}" maxlength="6" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
								<label for="lastname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorFailRetryCount}<font color="black">*</font></label>
								<div class="col-sm-4"><input type="text" class="form-control" name="executorFailRetryCount" placeholder="${I18n.jobinfo_field_executorFailRetryCount_placeholder}" maxlength="4" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" ></div>
							</div>

							<hr>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-6">
									<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
									<input type="hidden" name="id" >
								</div>
							</div>

						</form>
					</div>
				</div>
			</div>
		</div>

		<#-- trigger -->
		<div class="modal fade" id="jobTriggerModal" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-dialog ">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" >${I18n.jobinfo_opt_run}</h4>
					</div>
					<div class="modal-body">
						<form class="form-horizontal form" role="form" >
							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobinfo_field_executorparam}<font color="black">*</font></label>
								<div class="col-sm-10">
									<textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="2048" style="height: 70px; line-height: 1.2;"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="firstname" class="col-sm-2 control-label">${I18n.jobgroup_field_registryList}<font color="black">*</font></label>
								<div class="col-sm-10">
									<textarea class="textarea form-control" name="addressList" placeholder="${I18n.jobinfo_opt_run_tips}" maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
								</div>
							</div>
							<hr>
							<div class="form-group">
								<div class="col-sm-offset-3 col-sm-6">
									<button type="button" class="btn btn-primary ok" >${I18n.system_save}</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
									<input type="hidden" name="id" >
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<!-- 2-content end -->

	</section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${request.contextPath}/static/plugins/bootstrap-table/locale/<#if I18n.admin_i18n?? && I18n.admin_i18n == 'en'>bootstrap-table-en-US.min.js<#else>bootstrap-table-zh-CN.min.js</#if>"></script>
<#-- admin table -->
<script src="${request.contextPath}/static/biz/common/admin.table.js"></script>
<#-- admin util -->
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<#-- moment -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<#-- select2 -->
<script src="${request.contextPath}/static/adminlte/bower_components/select2/select2.min.js"></script>
<#-- cronGen -->
<script src="${request.contextPath}/static/plugins/cronGen/<#if I18n.admin_i18n?? && I18n.admin_i18n == 'en'>cronGen_en.js<#else>cronGen.js</#if>"></script>
<script>
	$(function() {

		// ---------------------- filter ----------------------

		// select2：init
		$('#jobGroup').select2();

		/**
		 * jobGroup change
		 */
		$('#jobGroup').on('change', function(){
			//reload
			var jobGroup = $('#jobGroup').val();
			window.location.href = base_url + "/jobinfo?jobGroup=" + jobGroup;
		});

		// reset filter
		var jobGroup = '${jobGroup}';
		function resetFilter(){
			if (jobGroup > 0) {
				$("#jobGroup").val( jobGroup );
			}
		}
		resetFilter();

		// ---------------------- table ----------------------

		/**
		 * init table
		 */
		$.adminTable.initTable({
			table: '#data_list',
			url: base_url + "/jobinfo/pageList",
			queryParams: function (params) {
				var obj = {};
				obj.jobGroup = $('#jobGroup').val();
				obj.triggerStatus = $('#triggerStatus').val();
				obj.jobDesc = $('#jobDesc').val();
				obj.executorHandler = $('#executorHandler').val();
				obj.author = $('#author').val();
				obj.offset = params.offset;
				obj.pagesize = params.limit;
				return obj;
			},resetHandler : function() {
				// default
				$('#data_filter input[type="text"]').val('');
				$('#data_filter select').each(function() {
					$(this).prop('selectedIndex', 0);
				});

				// reset filter
				resetFilter();
			},
			columns:[
				{
					checkbox: true,
					field: 'state',
					width: '5',
					widthUnit: '%',
					align: 'center',
					valign: 'middle'
				},{
					title: I18n.jobinfo_field_id,
					field: 'id',
					width: '5',
					widthUnit: '%',
					align: 'left'
				}
				,{
					title: I18n.jobinfo_field_jobdesc,
					field: 'jobDesc',
					width: '25',
					widthUnit: '%',
					align: 'left',
					formatter: function(value, row, index) {
						if (value.length > 15) {
							return '<span title="' + value + '">' + value.substr(0, 15) + '...</span>';
						} else {
							return value;
						}
					}
				},{
					title: I18n.schedule_type,
					field: 'scheduleType',
					width: '15',
					widthUnit: '%',
					formatter: function(value, row, index) {
						if (row.scheduleConf) {
							return row.scheduleType + '：'+ row.scheduleConf;
						} else {
							return row.scheduleType;
						}
					}
				},{
					title: I18n.jobinfo_field_gluetype,
					field: 'glueType',
					width: '25',
					widthUnit: '%',
					formatter: function(value, row, index) {
						// find glueType title
						let glueTypeTitle = '';
						$("#addModal .form select[name=glueType] option").each(function () {
							if (row.glueType == $(this).val()) {
								glueTypeTitle = $(this).text();
							}
						});

						// append handler
						if (row.executorHandler) {
							return glueTypeTitle +"：" + row.executorHandler;
						} else {
							return glueTypeTitle;
						}
					}
				},{
					title: I18n.system_status,
					field: 'triggerStatus',
					width: '10',
					widthUnit: '%',
					formatter: function(value, row, index) {
						// 调度状态：0-停止，1-运行
						if (1 == value) {
							return '<small class="label label-success" >RUNNING</small>';
						} else {
							return '<small class="label label-default" >STOP</small>';
						}
						return value;
					}
				},{
					title: I18n.jobinfo_field_author,
					field: 'author',
					width: '10',
					widthUnit: '%'
				}
			]
		});

		// ---------------------- delete ----------------------

		/**
		 * delete
		 */
		/*$.adminTable.initDelete({
			url: base_url + "/jobinfo/delete"
		});*/
		$("#data_operation").on('click', '.delete',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select ids
			const selectIds = (rows && rows.length > 0) ? rows.map(row => row.id) : [];
			if (selectIds.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}

			// do delete
			layer.confirm( I18n.system_ok + I18n.system_opt_del + '?', {
				icon: 3,
				title: I18n.system_tips ,
				btn: [ I18n.system_ok, I18n.system_cancel ]
			}, function(index){
				layer.close(index);

				$.ajax({
					type : 'POST',
					url : base_url + "/jobinfo/delete",
					data : {
						"ids" : selectIds
					},
					dataType : "json",
					success : function(data){
						if (data.code === 200) {
							layer.msg( I18n.system_opt_del + I18n.system_success );
							// refresh table
							$('#data_filter .searchBtn').click();
						} else {
							layer.msg( data.msg || I18n.system_opt_del + I18n.system_fail );
						}
					},
					error: function(xhr, status, error) {
						// Handle error
						console.log("Error: " + error);
						layer.open({
							icon: '2',
							content: (I18n.system_opt_del + I18n.system_fail)
						});
					}
				});
			});
		});

		// ---------------------- start  ----------------------

		/**
		 * start
		 */
		$("#data_operation").on('click', '.job_resume',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select ids
			const selectIds = (rows && rows.length > 0) ? rows.map(row => row.id) : [];
			if (selectIds.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}

			// invoke
			layer.confirm( I18n.system_ok + I18n.jobinfo_opt_start + '?', {
				icon: 3,
				title: I18n.system_tips ,
				btn: [ I18n.system_ok, I18n.system_cancel ]
			}, function(index){
				layer.close(index);

				$.ajax({
					type : 'POST',
					url : base_url + "/jobinfo/start",
					data : {
						"ids" : selectIds
					},
					dataType : "json",
					success : function(data){
						if (data.code === 200) {
							layer.msg( I18n.jobinfo_opt_start + I18n.system_success );
							// refresh table
							$('#data_filter .searchBtn').click();
						} else {
							layer.msg( data.msg || I18n.jobinfo_opt_start + I18n.system_fail );
						}
					},
					error: function(xhr, status, error) {
						// Handle error
						console.log("Error: " + error);
						layer.open({
							icon: '2',
							content: (I18n.jobinfo_opt_start + I18n.system_fail)
						});
					}
				});
			});
		});

		// ---------------------- stop ----------------------

		/**
		 * stop
		 */
		$("#data_operation").on('click', '.job_pause',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select ids
			const selectIds = (rows && rows.length > 0) ? rows.map(row => row.id) : [];
			if (selectIds.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}

			// invoke
			layer.confirm( I18n.system_ok + I18n.jobinfo_opt_stop + '?', {
				icon: 3,
				title: I18n.system_tips ,
				btn: [ I18n.system_ok, I18n.system_cancel ]
			}, function(index){
				layer.close(index);

				$.ajax({
					type : 'POST',
					url : base_url + "/jobinfo/stop",
					data : {
						"ids" : selectIds
					},
					dataType : "json",
					success : function(data){
						if (data.code === 200) {
							layer.msg( I18n.jobinfo_opt_stop + I18n.system_success );
							// refresh table
							$('#data_filter .searchBtn').click();
						} else {
							layer.msg( data.msg || I18n.jobinfo_opt_stop + I18n.system_fail );
						}
					},
					error: function(xhr, status, error) {
						// Handle error
						console.log("Error: " + error);
						layer.open({
							icon: '2',
							content: (I18n.jobinfo_opt_stop + I18n.system_fail)
						});
					}
				});
			});
		});

		// ---------------------- trigger ----------------------

		/**
		 * job trigger
		 */
		$("#data_operation").on('click', '.job_trigger',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// fill modal
			$("#jobTriggerModal .form input[name='id']").val( row.id );
			$("#jobTriggerModal .form textarea[name='executorParam']").val( row.executorParam );

			$('#jobTriggerModal').modal({backdrop: false, keyboard: false}).modal('show');
		});
		$("#jobTriggerModal .ok").on('click',function() {
			$.ajax({
				type : 'POST',
				url : base_url + "/jobinfo/trigger",
				data : {
					"id" : $("#jobTriggerModal .form input[name='id']").val(),
					"executorParam" : $("#jobTriggerModal .textarea[name='executorParam']").val(),
					"addressList" : $("#jobTriggerModal .textarea[name='addressList']").val()
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						$('#jobTriggerModal').modal('hide');

						layer.msg( I18n.jobinfo_opt_run + I18n.system_success );
					} else {
						layer.msg( data.msg || I18n.jobinfo_opt_run + I18n.system_fail );
					}
				}
			});
		});
		$("#jobTriggerModal").on('hide.bs.modal', function () {
			$("#jobTriggerModal .form")[0].reset();
		});

		// ---------------------- registryinfo ----------------------

		/**
		 * job registryinfo
		 */
		$("#data_operation").on('click', '.job_registryinfo',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// invoke
			$.ajax({
				type : 'POST',
				url : base_url + "/jobgroup/loadById",
				data : {
					"id" : row.jobGroup
				},
				dataType : "json",
				success : function(data){

					var html = '<div>';
					if (data.code == 200 && data.data.registryList) {
						for (var index in data.data.registryList) {
							html += (parseInt(index)+1) + '. <span class="badge bg-green" >' + data.data.registryList[index] + '</span><br>';
						}
					}
					html += '</div>';

					layer.open({
						title: I18n.jobinfo_opt_registryinfo ,
						btn: [ I18n.system_ok ],
						content: html
					});

				}
			});

		});

		// ---------------------- job_log ----------------------

		/**
		 * job_log
		 */
		$("#data_operation").on('click', '.job_log',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// open tab
			let url = base_url +'/joblog?jobId='+ row.id;
			openTab(url, I18n.joblog_name, false);
		});

		// ---------------------- glue_ide ----------------------

		/**
		 * glue_ide
		 */
		$("#data_operation").on('click', '.glue_ide',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// valid
			if ('BEAN' === row.glueType) {
				layer.msg(I18n.jobinfo_glue_gluetype_invalid);
				return;
			}

			// open tab
			let url = base_url +'/jobcode?jobId='+ row.id;
			window.open(url);
			//openTab(url, 'GLUE IDE', false);
		});

		// ---------------------- job_next_time ----------------------

		/**
		 * job registryinfo
		 */
		$("#data_operation").on('click', '.job_next_time',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// invoke
			$.ajax({
				type : 'POST',
				url : base_url + "/jobinfo/nextTriggerTime",
				data : {
					"scheduleType" : row.scheduleType,
					"scheduleConf" : row.scheduleConf
				},
				dataType : "json",
				success : function(data){

					if (data.code != 200) {
						layer.open({
							title: I18n.jobinfo_opt_next_time ,
							btn: [ I18n.system_ok ],
							content: data.msg
						});
					} else {
						var html = '<center>';
						if (data.code == 200 && data.data) {
							for (var index in data.data) {
								html += '<span>' + data.data[index] + '</span><br>';
							}
						}
						html += '</center>';

						layer.open({
							title: I18n.jobinfo_opt_next_time ,
							btn: [ I18n.system_ok ],
							content: html
						});
					}

				}
			});

		});

		// ---------------------- add ----------------------

		/**
		 * add
		 */
		$.adminTable.initAdd( {
			url: base_url + "/jobinfo/insert",
			rules : {
				jobDesc : {
					required : true,
					maxlength: 50
				},
				author : {
					required : true
				}
			},
			messages : {
				jobDesc : {
					required : I18n.system_please_input + I18n.jobinfo_field_jobdesc
				},
				author : {
					required : I18n.system_please_input + I18n.jobinfo_field_author
				}
			},
			writeFormData: function() {
				// init-cronGen
				$("#addModal .form input[name='schedule_conf_CRON']").show().siblings().remove();
				$("#addModal .form input[name='schedule_conf_CRON']").cronGen({});

				// 》init scheduleType
				$("#addModal .form select[name=scheduleType]").change();

				// 》init glueType
				$("#addModal .form select[name=glueType]").change();
			},
			readFormData: function() {

				// process executorTimeout+executorFailRetryCount
				var executorTimeout = $("#addModal .form input[name='executorTimeout']").val();
				if(!/^\d+$/.test(executorTimeout)) {
					executorTimeout = 0;
				}
				$("#addModal .form input[name='executorTimeout']").val(executorTimeout);
				var executorFailRetryCount = $("#addModal .form input[name='executorFailRetryCount']").val();
				if(!/^\d+$/.test(executorFailRetryCount)) {
					executorFailRetryCount = 0;
				}
				$("#addModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);

				// process schedule_conf
				var scheduleType = $("#addModal .form select[name='scheduleType']").val();
				var scheduleConf;
				if (scheduleType == 'CRON') {
					scheduleConf = $("#addModal .form input[name='cronGen_display']").val();
				} else if (scheduleType == 'FIX_RATE') {
					scheduleConf = $("#addModal .form input[name='schedule_conf_FIX_RATE']").val();
				} else if (scheduleType == 'FIX_DELAY') {
					scheduleConf = $("#addModal .form input[name='schedule_conf_FIX_DELAY']").val();
				}
				$("#addModal .form input[name='scheduleConf']").val( scheduleConf );

				return $("#addModal .form").serialize();
			}
		});

		// scheduleType change
		$(".scheduleType").change(function(){
			var scheduleType = $(this).val();
			$(this).parents("form").find(".schedule_conf").hide();
			$(this).parents("form").find(".schedule_conf_" + scheduleType).show();

		});

		// glueType change
		$(".glueType").change(function(){
			// executorHandler
			var $executorHandler = $(this).parents("form").find("input[name='executorHandler']");
			var glueType = $(this).val();
			if ('BEAN' != glueType) {
				$executorHandler.val("");
				$executorHandler.attr("readonly","readonly");
			} else {
				$executorHandler.removeAttr("readonly");
			}
		});

		// glueType init source
		$("#addModal .glueType").change(function(){
			// glueSource
			var glueType = $(this).val();
			if ('GLUE_GROOVY'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_java").val() );
			} else if ('GLUE_SHELL'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_shell").val() );
			} else if ('GLUE_PYTHON'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_python").val() );
			} else if ('GLUE_PYTHON2'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_python2").val() );
			} else if ('GLUE_PHP'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_php").val() );
			} else if ('GLUE_NODEJS'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_nodejs").val() );
			} else if ('GLUE_POWERSHELL'==glueType){
				$("#addModal .form textarea[name='glueSource']").val( $("#addModal .form .glueSource_powershell").val() );
			} else {
				$("#addModal .form textarea[name='glueSource']").val("");
			}
		});

		// ---------------------- update ----------------------

		/**
		 * init update
		 */
		$.adminTable.initUpdate( {
			url: base_url + "/jobinfo/update",
			rules : {
				jobDesc : {
					required : true,
					maxlength: 50
				},
				author : {
					required : true
				}
			},
			messages : {
				jobDesc : {
					required : I18n.system_please_input + I18n.jobinfo_field_jobdesc
				},
				author : {
					required : I18n.system_please_input + I18n.jobinfo_field_author
				}
			},
			writeFormData: function(row) {

				// fill base
				$("#updateModal .form input[name='id']").val( row.id );
				$('#updateModal .form select[name=jobGroup] option[value='+ row.jobGroup +']').prop('selected', true);
				$("#updateModal .form input[name='jobDesc']").val( row.jobDesc );
				$("#updateModal .form input[name='author']").val( row.author );
				$("#updateModal .form input[name='alarmEmail']").val( row.alarmEmail );

				// fill trigger
				$('#updateModal .form select[name=scheduleType] option[value='+ row.scheduleType +']').prop('selected', true);
				$("#updateModal .form input[name='scheduleConf']").val( row.scheduleConf );
				if (row.scheduleType == 'CRON') {
					$("#updateModal .form input[name='schedule_conf_CRON']").val( row.scheduleConf );
				} else if (row.scheduleType == 'FIX_RATE') {
					$("#updateModal .form input[name='schedule_conf_FIX_RATE']").val( row.scheduleConf );
				} else if (row.scheduleType == 'FIX_DELAY') {
					$("#updateModal .form input[name='schedule_conf_FIX_DELAY']").val( row.scheduleConf );
				}

				// 》init scheduleType
				$("#updateModal .form select[name=scheduleType]").change();

				// fill job
				$('#updateModal .form select[name=glueType] option[value='+ row.glueType +']').prop('selected', true);
				$("#updateModal .form input[name='executorHandler']").val( row.executorHandler );
				$("#updateModal .form textarea[name='executorParam']").val( row.executorParam );

				// 》init glueType
				$("#updateModal .form select[name=glueType]").change();

				// 》init-cronGen
				$("#updateModal .form input[name='schedule_conf_CRON']").show().siblings().remove();
				$("#updateModal .form input[name='schedule_conf_CRON']").cronGen({});

				// fill advanced
				$('#updateModal .form select[name=executorRouteStrategy] option[value='+ row.executorRouteStrategy +']').prop('selected', true);
				$("#updateModal .form input[name='childJobId']").val( row.childJobId );
				$('#updateModal .form select[name=misfireStrategy] option[value='+ row.misfireStrategy +']').prop('selected', true);
				$('#updateModal .form select[name=executorBlockStrategy] option[value='+ row.executorBlockStrategy +']').prop('selected', true);
				$("#updateModal .form input[name='executorTimeout']").val( row.executorTimeout );
				$("#updateModal .form input[name='executorFailRetryCount']").val( row.executorFailRetryCount );

			},
			readFormData: function() {

				// process executorTimeout + executorFailRetryCount
				var executorTimeout = $("#updateModal .form input[name='executorTimeout']").val();
				if(!/^\d+$/.test(executorTimeout)) {
					executorTimeout = 0;
				}
				$("#updateModal .form input[name='executorTimeout']").val(executorTimeout);
				var executorFailRetryCount = $("#updateModal .form input[name='executorFailRetryCount']").val();
				if(!/^\d+$/.test(executorFailRetryCount)) {
					executorFailRetryCount = 0;
				}
				$("#updateModal .form input[name='executorFailRetryCount']").val(executorFailRetryCount);


				// process schedule_conf
				var scheduleType = $("#updateModal .form select[name='scheduleType']").val();
				var scheduleConf;
				if (scheduleType == 'CRON') {
					scheduleConf = $("#updateModal .form input[name='cronGen_display']").val();
				} else if (scheduleType == 'FIX_RATE') {
					scheduleConf = $("#updateModal .form input[name='schedule_conf_FIX_RATE']").val();
				} else if (scheduleType == 'FIX_DELAY') {
					scheduleConf = $("#updateModal .form input[name='schedule_conf_FIX_DELAY']").val();
				}
				$("#updateModal .form input[name='scheduleConf']").val( scheduleConf );

				return $("#updateModal .form").serialize();
			}
		});

		// ---------------------- job_copy ----------------------

		/**
		 * job_copy
		 */
		$("#data_operation").on('click', '.job_copy',function() {
			// get select rows
			var rows = $.adminTable.table.bootstrapTable('getSelections');

			// find select row
			if (rows.length !== 1) {
				layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
				return;
			}
			var row = rows[0];

			// open addModel
			$("#data_operation .add").click();

			// fill base
			$('#addModal .form select[name=jobGroup] option[value='+ row.jobGroup +']').prop('selected', true);
			$("#addModal .form input[name='jobDesc']").val( row.jobDesc );
			$("#addModal .form input[name='author']").val( row.author );
			$("#addModal .form input[name='alarmEmail']").val( row.alarmEmail );

			// fill trigger
			$('#addModal .form select[name=scheduleType] option[value='+ row.scheduleType +']').prop('selected', true);
			$("#addModal .form input[name='scheduleConf']").val( row.scheduleConf );
			if (row.scheduleType == 'CRON') {
				$("#addModal .form input[name='schedule_conf_CRON']").val( row.scheduleConf );
			} else if (row.scheduleType == 'FIX_RATE') {
				$("#addModal .form input[name='schedule_conf_FIX_RATE']").val( row.scheduleConf );
			} else if (row.scheduleType == 'FIX_DELAY') {
				$("#addModal .form input[name='schedule_conf_FIX_DELAY']").val( row.scheduleConf );
			}

			// 》init scheduleType
			$("#addModal .form select[name=scheduleType]").change();

			// fill job
			$('#addModal .form select[name=glueType] option[value='+ row.glueType +']').prop('selected', true);
			$("#addModal .form input[name='executorHandler']").val( row.executorHandler );
			$("#addModal .form textarea[name='executorParam']").val( row.executorParam );

			// 》init glueType
			$("#addModal .form select[name=glueType]").change();

			// 》init-cronGen
			$("#addModal .form input[name='schedule_conf_CRON']").show().siblings().remove();
			$("#addModal .form input[name='schedule_conf_CRON']").cronGen({});

			// fill advanced
			$('#addModal .form select[name=executorRouteStrategy] option[value='+ row.executorRouteStrategy +']').prop('selected', true);
			$("#addModal .form input[name='childJobId']").val( row.childJobId );
			$('#addModal .form select[name=misfireStrategy] option[value='+ row.misfireStrategy +']').prop('selected', true);
			$('#addModal .form select[name=executorBlockStrategy] option[value='+ row.executorBlockStrategy +']').prop('selected', true);
			$("#addModal .form input[name='executorTimeout']").val( row.executorTimeout );
			$("#addModal .form input[name='executorFailRetryCount']").val( row.executorFailRetryCount );
		});

	});

</script>
<!-- 3-script end -->

</body>
</html>