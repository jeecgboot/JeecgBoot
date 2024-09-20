<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "jobinfo" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.jobinfo_name}</h1>
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	<div class="row">
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
	            	<button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
	            </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-success add" type="button">${I18n.jobinfo_field_add}</button>
	            </div>
          	</div>
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <#--<div class="box-header hide">
			            	<h3 class="box-title">调度列表</h3>
			            </div>-->
			            <div class="box-body" >
			              	<table id="job_list" class="table table-bordered table-striped" width="100%" >
				                <thead>
					            	<tr>
					            		<th name="id" >${I18n.jobinfo_field_id}</th>
					                	<th name="jobGroup" >${I18n.jobinfo_field_jobgroup}</th>
					                  	<th name="jobDesc" >${I18n.jobinfo_field_jobdesc}</th>
                                        <th name="scheduleType" >${I18n.schedule_type}</th>
                                        <th name="glueType" >${I18n.jobinfo_field_gluetype}</th>
                                        <th name="executorParam" >${I18n.jobinfo_field_executorparam}</th>
					                  	<th name="addTime" >addTime</th>
					                  	<th name="updateTime" >updateTime</th>
					                  	<th name="author" >${I18n.jobinfo_field_author}</th>
					                  	<th name="alarmEmail" >${I18n.jobinfo_field_alarmemail}</th>
					                  	<th name="triggerStatus" >${I18n.system_status}</th>
					                  	<th>${I18n.system_opt}</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
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
                            <textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
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
<#--这里有问题，新建一个运行模式为 php 的任务后，GLUE 中没有下边的 php 代码-->
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
                            <textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
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
                            <textarea class="textarea form-control" name="executorParam" placeholder="${I18n.system_please_input}${I18n.jobinfo_field_executorparam}" maxlength="512" style="height: 63px; line-height: 1.2;"></textarea>
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

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<#-- cronGen -->
<script src="${request.contextPath}/static/plugins/cronGen/cronGen<#if I18n.admin_i18n?default('')?length gt 0 >_${I18n.admin_i18n}</#if>.js"></script>
<script src="${request.contextPath}/static/js/jobinfo.index.1.js"></script>
</body>
</html>
