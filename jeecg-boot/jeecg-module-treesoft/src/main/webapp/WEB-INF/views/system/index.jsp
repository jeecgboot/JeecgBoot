<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>TreeSoft数据库管理系统</title>
<meta name="Keywords" content="Treesoft数据库管理系统">
<meta name="Description" content="Treesoft数据库管理系统">
<%@ include file="/WEB-INF/views/include/easyui.jsp"%>
<%@ include file="/WEB-INF/views/include/codemirror.jsp"%>
<script src="${ctx}/static/plugins/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/easyui/jeasyui-extensions/jeasyui.extensions.datatime.js" type="text/javascript"></script>

<!--导入首页启动时需要的相应资源文件(首页相应功能的 js 库、css样式以及渲染首页界面的 js 文件)-->
<script src="${ctx}/static/plugins/easyui/common/index.js" type="text/javascript"></script>
<script src="${ctx}/static/plugins/easyui/common/indexSearch.js" type="text/javascript"></script>
<link href="${ctx}/static/plugins/easyui/common/index.css" rel="stylesheet" />
<script src="${ctx}/static/plugins/easyui/common/index-startup.js"></script>

<link type="text/css" rel="stylesheet" href="${ctx}/static/css/eclipse.css">  
<link type="text/css" rel="stylesheet" href="${ctx}/static/css/codemirror.css" />  
<link type="text/css" rel="stylesheet" href="${ctx}/static/css/show-hint.css" /> 
<link rel="icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">  
<link rel="shortcut icon" href="${ctx}/favicon.ico" mce_href="${ctx}/favicon.ico" type="image/x-icon">
  
<script type="text/javascript" src="${ctx}/static/js/codemirror.js"> </script> 
<script type="text/javascript" src="${ctx}/static/js/sql.js"> </script>  
<script type="text/javascript" src="${ctx}/static/js/show-hint.js"> </script>  
<script type="text/javascript" src="${ctx}/static/js/sql-hint.js"> </script>  
<style>  
  .CodeMirror { border: 1px solid #cccccc;  height: 98%;  }  
</style> 

</head>
<body>
	<!-- 容器遮罩 -->
    <div id="maskContainer">
        <div class="datagrid-mask" style="display: block;"></div>
        <div class="datagrid-mask-msg" style="display: block; left: 50%; margin-left: -52.5px;">
            正在加载...
        </div>
    </div>
    
    <div id="mainLayout" class="easyui-layout hidden" data-options="fit: true">
    
        <div id="northPanel"   data-options="region: 'north', border: false" style="height: 80px; overflow: hidden;">
           
            <div id="topbar"  style="width: 100%;height:52px; background: #0092dc url('${ctx}/static/images/mosaic-pattern.png') repeat;opacity:0.8;">
            
                <div class="top-bar-left">
                    <h1 style="margin-left: 10px; margin-top: 10px;color: #fff"> <img src="${ctx}/static/images/logo.png" >TreeSoft数据库管理系统<span style="color:#00824D;font-size:14px; font-weight:bold;"> TreeDMS</span> <span style="color: #fff;font-size:12px;">V2.3.1</span> </h1>
                </div>
                
                <div class="top-bar-right" >
                    <div id="timerSpan"> 
                    
                     <div id="operator" style="padding:5px;height:auto">
                      <div style="padding-right:20px;height:auto">
                         <c:if test="${fn:contains(permission,'monitor')}"> 
                           <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/alarm.gif" onclick="javascript:monitor()"  title="状态监控"/>
                           </div> 
                         </c:if>
                         
                        <c:if test="${fn:contains(permission,'json')}">  
                         <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_json.gif" onclick="javascript:jsonFormat()"  title="Json格式化"/>
                          </div> 
                         </c:if>
                         
                          <c:if test="${fn:contains(permission,'config')}">  
                           <div style="padding-right:20px; display:inline; cursor:pointer;">
                                 <img   src="${ctx}/static/images/btn_hd_support.gif" onclick="javascript:ShowConfigPage()"  title="数据库配置"/>
                          </div>  
                          </c:if>
						  <c:if test="${fn:contains(permission,'person')}">
							  <div style="padding-right:15px; display:inline; cursor:pointer;">
								  <img class="imageHead"  src="${ctx}/static/images/btn_person.gif" onclick="javascript:persons()"  title="权限管理"/>
							  </div>
						  </c:if>

                          <div style=" display:inline;cursor:pointer; ">
                             <img id="btnExit"   src="${ctx}/static/images/btn_hd_exit.gif" title="注销"   /> 
	       		          </div> 
	       		      </div> 
                      </div>
                    </div>
                    
                    <div id="themeSpan">
                        <a id="btnHideNorth" class="easyui-linkbutton" data-options="plain: true, iconCls: 'layout-button-up'"> </a>
                    </div>
                </div>
            </div>
            
            <div id="toolbar" class="panel-header panel-header-noborder top-toolbar">
                <div id="infobar">
                    <span class="icon-hamburg-user" style="padding-left: 25px; background-position: left center;">
                      ${username}，您好 
                    </span>
                </div>
               
                <div id="buttonbar">
                    <a href="javascript:void(0);"  id="btnFullScreen" class="easyui-linkbutton easyui-tooltip" title="全屏切换" data-options="plain: true, iconCls: 'icon-standard-arrow-inout'"  >全屏切换</a> 
                
                    <span>更换皮肤：</span>
                    <select id="themeSelector"></select>					
                    <a id="btnShowNorth" class="easyui-linkbutton" data-options="plain: true, iconCls: 'layout-button-down'" style="display: none;"></a>
               
                </div>
            </div>
        </div>

        <div data-options="region: 'west', title: '数据库选择', iconCls: 'icon-standard-map', split: true, minWidth: 200, maxWidth: 400" style="width: 220px; padding: 1px;">
			  
			<div id="eastLayout" class="easyui-layout" data-options="fit: true">
                <div data-options="region: 'north', split: false, border: false" style="height: 34px;">
                    <select class="combobox-f combo-f" style="width:200px;margin:5px; " id="databaseSelect"  >   </select> 
                </div>
                
                <div   data-options="region: 'center', border: false, title: '数据库', iconCls: 'icon-hamburg-database', tools: [{ iconCls: 'icon-hamburg-refresh', handler: function () {  dg.treegrid('reload'); } }]">
                       <input id="pid" name="pid" />  
                </div>
            </div>
			  
        </div>

        <div data-options="region: 'center'">
            <div id="mainTabs_tools" class="tabs-tool">
                <table>
                    <tr>
                        <td><a id="mainTabs_jumpHome" class="easyui-linkbutton easyui-tooltip" title="跳转至主页选项卡" data-options="plain: true, iconCls: 'icon-hamburg-home'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
						<td><a id="mainTabs_toggleAll" class="easyui-linkbutton easyui-tooltip" title="展开/折叠面板使选项卡最大化" data-options="plain: true, iconCls: 'icon-standard-arrow-out'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
                        <td><a id="mainTabs_refTab" class="easyui-linkbutton easyui-tooltip" title="刷新当前选中的选项卡" data-options="plain: true, iconCls: 'icon-standard-arrow-refresh'"></a></td>
                        <td><div class="datagrid-btn-separator"></div></td>
                        <td><a id="mainTabs_closeTab" class="easyui-linkbutton easyui-tooltip" title="关闭当前选中的选项卡" data-options="plain: true, iconCls: 'icon-standard-application-form-delete'"></a></td>
                    </tr>
                </table>
            </div>

            <div id="mainTabs" class="easyui-tabs" data-options="fit: true, border: false, showOption: true, enableNewTabMenu: true, tools: '#mainTabs_tools', enableJumpTabMenu: true">
                <div id="homePanel" data-options="title: '运行及展示', iconCls: 'icon-hamburg-home'">
                    
           
            <div id="eastLayout" class="easyui-layout" data-options="fit: true">
            
                <div data-options="region: 'north',split: true, border: false" style="height:280px">
                     <div id="operator"  class="panel-header panel-header-noborder  " style="padding:5px;height:auto"  >
                            <div>
	       		              <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-run" plain="true" onclick="run();">执行(F8)</a>
	       		             <span class="toolbar-item dialog-tool-separator"></span>
	        	             
	        	              <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="clearSQL()">清空(F7)</a>
	                         <span class="toolbar-item dialog-tool-separator"></span>
	                         
	                         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="saveSearchDialog()">SQL保存</a>
	                         <span class="toolbar-item dialog-tool-separator"></span>
	                         
	                         <%--<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-drawings" plain="true" onclick="selectTheme('eclipse')">样式一</a>
	                         <span class="toolbar-item dialog-tool-separator"></span>
	                         
	                         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-equalizer" plain="true" onclick="selectTheme('ambiance')">样式二</a>
	                         <span class="toolbar-item dialog-tool-separator"></span>
	                        
	                         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-hamburg-showreel" plain="true" onclick="selectTheme('erlang-dark')">样式三</a>
	                         <span class="toolbar-item dialog-tool-separator"></span>
	                         --%>
	                         
	                         <span class="l-btn-left l-btn-icon-left" style="margin-top: 4px"><span class="l-btn-text">更换样式:</span><span class="l-btn-icon icon-hamburg-drawings">&nbsp;</span></span>
										<select id="codeThemeSelector" onchange="selectTheme()"
											style="margin-top: 4px">
											<option selected>
												default
											</option>
											<option>
												3024-day
											</option>
											<option>
												3024-night
											</option>
											<option>
												abcdef
											</option>
											<option>
												ambiance
											</option>
											<option>
												base16-dark
											</option>
											<option>
												base16-light
											</option>
											<option>
												bespin
											</option>
											<option>
												blackboard
											</option>
											<option>
												cobalt
											</option>
											<option>
												colorforth
											</option>
											<option>
												dracula
											</option>
											<option>
												eclipse
											</option>
											<option>
												elegant
											</option>
											<option>
												erlang-dark
											</option>
											<option>
												hopscotch
											</option>
											<option>
												icecoder
											</option>
											<option>
												isotope
											</option>
											<option>
												lesser-dark
											</option>
											<option>
												liquibyte
											</option>
											<option>
												material
											</option>
											<option>
												mbo
											</option>
											<option>
												mdn-like
											</option>
											<option>
												midnight
											</option>
											<option>
												monokai
											</option>
											<option>
												neat
											</option>
											<option>
												neo
											</option>
											<option>
												night
											</option>
											<option>
												paraiso-dark
											</option>
											<option>
												paraiso-light
											</option>
											<option>
												pastel-on-dark
											</option>
											<option>
												railscasts
											</option>
											<option>
												rubyblue
											</option>
											<option>
												seti
											</option>
											<option>
												solarized dark
											</option>
											<option>
												solarized light
											</option>
											<option>
												the-matrix
											</option>
											<option>
												tomorrow-night-bright
											</option>
											<option>
												tomorrow-night-eighties
											</option>
											<option>
												ttcn
											</option>
											<option>
												twilight
											</option>
											<option>
												vibrant-ink
											</option>
											<option>
												xq-dark
											</option>
											<option>
												xq-light
											</option>
											<option>
												yeti
											</option>
											<option>
												zenburn
											</option>
										</select>

										<%--
	                         <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="format()">SQL输入提示</a>
	                         <span class="toolbar-item dialog-tool-separator"></span>
                            --%>
                            <span class="toolbar-item dialog-tool-separator"></span>
                              <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-tip" plain="true" title="F8   执行SQL语句 &#13;F7   清空SQL语句 &#13;可选中部分SQL执行 &#13;注释请以;分号结束"></a>
                             &nbsp;当前数据库：<span id="currentDbTitle"> </span>
                            </div> 
                       </div>
		                <div  style="width:100%;height:85%; " >
		                   
		                    <input type="hidden" id="searchHistoryId">
		                    <input type="hidden" id="searchHistoryName">
		                      <textarea  id="sqltextarea" style="margin:10px; font-size:14px;font-family: '微软雅黑';width:97%;height:95%; "> </textarea>
	                    </div>
                </div>
                
                <div  id="searchHistoryPanel"   data-options="region: 'center',split: true, collapsed: false,   border: false, title: '运行结果', iconCls: 'icon-standard-application-view-icons'  ">
                    
                     <div id="searchTabs" class="easyui-tabs" data-options="fit: true, border: false, showOption: true, enableNewTabMenu: true, enableJumpTabMenu: true">
                        <div id="searcHomePanel" data-options="title: '消息', iconCls: 'icon-hamburg-issue'">
                            
                            <textarea  id="executeMessage" style="margin:10px; font-size:14px;font-family: '微软雅黑';width:97%;height:95%; " readonly >   </textarea>
                           
                        </div>
                    </div>
                       
                </div>
            
            </div>

                </div>
            </div>
        </div>

		<div data-options="region: 'east', title: 'SQL帮助', iconCls: 'icon-standard-book', split: true,collapsed:true, minWidth: 160, maxWidth: 500" style="width: 220px;">
			<div id="eastLayout2" class="easyui-layout" data-options="fit: true">
				<div data-options="region: 'north', split: true, border: false" style="height: 220px;">
					<input id="sqlStudyList"  />
				</div>

				<div id="searchHistoryPanel2" data-options="region: 'center', split: true,  border: false, title: 'SQL收藏', iconCls: 'icon-standard-book-key', tools: [{ iconCls: 'icon-hamburg-refresh', handler: function () {  searchBG.treegrid('reload'); } }]">
					<input id="searchHistoryList" />
				</div>
			</div>
		</div>

        <div data-options="region: 'south', title: '关于...', iconCls: 'icon-standard-information', collapsed: true, border: false" style="height: 70px;">
            <div style="color: #4e5766; padding: 6px 0px 0px 0px; margin: 0px auto; text-align: center; font-size:12px; font-family:微软雅黑;">
                TreeSoft<sup>®</sup>&nbsp;CopyRight@2018 福州青格软件 版权所有  <a href="http://www.treesoft.cn" target="_blank" style="text-decoration:none;" > www.treesoft.cn </a> &nbsp;。
                &nbsp; 
            </div>
            
        </div>
    </div>
  
  <div id='tb3' style='padding:5px;height:auto'>    
    <div  >    
        <a href='#' class='easyui-linkbutton' iconCls='icon-add' plain='true'></a>    
        <a href='#' class='easyui-linkbutton' iconCls='icon-edit' plain='true'></a>           
    </div>  
</div> 
  
<div id="dlgg"   ></div>  
<div id="addRow" ></div> 
<input type="hidden" id="currentTableName" >

<div id="databaseMenu" class="easyui-menu" style="width:120px;">
        <div onclick="backupDatabase()" data-options="iconCls:'icon-table-save'">备份数据库</div>
		<div onclick="dropDatabase()" data-options="iconCls:'icon-table-delete'">删除数据库</div>
		<div class="menu-sep"></div>
		<div onclick="" data-options="iconCls:'icon-table-gear'">数据库属性</div>
</div>


<div id="tableMenu" class="easyui-menu" style="width:120px;">
        <div onclick="clickTable(tableName )" data-options="iconCls:'icon-table-edit'">打开表</div>
		<div onclick="designTable()" data-options="iconCls:'icon-table-gear'">设计表</div>
		<div onclick="addNewTable()" data-options="iconCls:'icon-table-add'">新增表</div>
		<div onclick="exportTable()" data-options="iconCls:'icon-table-go'">导出表</div>
		<div onclick="copyTable()" data-options="iconCls:'icon-table-lightning'">复制表</div>
		<div onclick="renameTable()" data-options="iconCls:'icon-table-relationship'">重命名</div>
		<div class="menu-sep"></div>
		<div onclick="dropTable()" data-options="iconCls:'icon-table-delete'">删除表</div>
		<div onclick="clearTable()" data-options="iconCls:'icon-table-row-delete'" >清空表</div>
		<div onclick="tableMess()" data-options="iconCls:'icon-table-gear'">表信息</div>
</div>

<div id="viewMenu" class="easyui-menu" style="width:120px;">
        <div onclick="openView(tableName)" data-options="iconCls:'icon-search'">打开视图</div>
		<div onclick="showViewSQL(databaseName,tableName)" data-options="iconCls:'icon-edit'">设计视图</div>
		<div class="menu-sep"></div>
		<div onclick="" data-options="iconCls:'icon-tip'">视图信息</div>
</div>

<iframe id="exeframe" name="exeframe" style="display:none"> </iframe>
<form id="form1" method="post" target="exeframe" action="${ctx}/system/permission/i/exportExcel"   accept-charset="utf-8" onsubmit="document.charset='utf-8'" >
   <input type="hidden" id="sContent" name="sContent" value=""/>
</form>

<form id="form3" method="post"  action="${ctx}/system/permission/i/exportDataToSQLFromSQL"  style="display:none"   >
   <input type="hidden" id="databaseConfigId"    name="databaseConfigId"  >
   <input type="hidden" id="databaseName" name="databaseName"   >
   <input type="hidden" id="sql"    name="sql"  >
   <input type="hidden" id="exportType" name="exportType"  >
   
</form>
 
</body>
<script type="text/javascript" src="${ctx}/static/js/index.js"> </script>  
</html>
