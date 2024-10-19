<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
  	<!-- daterangepicker -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "joblog" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.joblog_name}</h1>
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    	<div class="row">
	    		<div class="col-xs-2">
 					<div class="input-group">
	                	<span class="input-group-addon">${I18n.jobinfo_field_jobgroup}</span>
                		<select class="form-control" id="jobGroup"  paramVal="<#if jobInfo?exists>${jobInfo.jobGroup}</#if>" >
                            <#if XXL_JOB_LOGIN_IDENTITY.role == 1>
                                <option value="0" >${I18n.system_all}</option>  <#-- 仅管理员支持查询全部；普通用户仅支持查询有权限的 jobGroup -->
                            </#if>
                			<#list JobGroupList as group>
                				<option value="${group.id}" >${group.title}</option>
                			</#list>
	                  	</select>
	              	</div>
	            </div>
	            <div class="col-xs-2">
	              	<div class="input-group">
	                	<span class="input-group-addon">${I18n.jobinfo_job}</span>
                        <select class="form-control" id="jobId" paramVal="<#if jobInfo?exists>${jobInfo.id}</#if>" >
                            <option value="0" >${I18n.system_all}</option>
						</select>
	              	</div>
	            </div>

                <div class="col-xs-2">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.joblog_status}</span>
                        <select class="form-control" id="logStatus" >
                            <option value="-1" >${I18n.joblog_status_all}</option>
                            <option value="1" >${I18n.joblog_status_suc}</option>
                            <option value="2" >${I18n.joblog_status_fail}</option>
                            <option value="3" >${I18n.joblog_status_running}</option>
                        </select>
                    </div>
                </div>

	            <div class="col-xs-4">
              		<div class="input-group">
                		<span class="input-group-addon">
	                  		${I18n.joblog_field_triggerTime}
	                	</span>
	                	<input type="text" class="form-control" id="filterTime" readonly >
	              	</div>
	            </div>

                <div class="col-xs-1">
                    <button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
                </div>

	            <div class="col-xs-1">
                    <button class="btn btn-block btn-default" id="clearLog">${I18n.joblog_clean}</button>
	            </div>
          	</div>
			
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <#--<div class="box-header hide"><h3 class="box-title">调度日志</h3></div>-->
			            <div class="box-body">
			              	<table id="joblog_list" class="table table-bordered table-striped display" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="jobId" >${I18n.jobinfo_field_id}</th>
                                        <th name="jobGroup" >jobGroup</th>
										<#--<th name="executorAddress" >执行器地址</th>
										<th name="glueType" >运行模式</th>
                                      	<th name="executorParam" >任务参数</th>-->
                                        <th name="triggerTime" >${I18n.joblog_field_triggerTime}</th>
                                        <th name="triggerCode" >${I18n.joblog_field_triggerCode}</th>
                                        <th name="triggerMsg" >${I18n.joblog_field_triggerMsg}</th>
					                  	<th name="handleTime" >${I18n.joblog_field_handleTime}</th>
					                  	<th name="handleCode" >${I18n.joblog_field_handleCode}</th>
					                  	<th name="handleMsg" >${I18n.joblog_field_handleMsg}</th>
					                  	<th name="handleMsg" >${I18n.system_opt}</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
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

<!-- 日志清理.模态框 -->
<div class="modal fade" id="clearLogModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >${I18n.joblog_clean_log}</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label class="col-sm-3 control-label">${I18n.jobinfo_field_jobgroup}：</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control jobGroupText" readonly >
							<input type="hidden" name="jobGroup" >
						</div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">${I18n.jobinfo_job}：</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control jobIdText" readonly >
                            <input type="hidden" name="jobId" >
						</div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label">${I18n.joblog_clean_type}：</label>
                        <div class="col-sm-9">
                            <select class="form-control" name="type" >
                                <option value="1" >${I18n.joblog_clean_type_1}</option>
                                <option value="2" >${I18n.joblog_clean_type_2}</option>
                                <option value="3" >${I18n.joblog_clean_type_3}</option>
                                <option value="4" >${I18n.joblog_clean_type_4}</option>
                                <option value="5" >${I18n.joblog_clean_type_5}</option>
                                <option value="6" >${I18n.joblog_clean_type_6}</option>
                                <option value="7" >${I18n.joblog_clean_type_7}</option>
                                <option value="8" >${I18n.joblog_clean_type_8}</option>
                                <option value="9" >${I18n.joblog_clean_type_9}</option>
                            </select>
                        </div>
                    </div>

                    <hr>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="button" class="btn btn-primary ok" >${I18n.system_ok}</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
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
<!-- daterangepicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/joblog.index.1.js"></script>
</body>
</html>
