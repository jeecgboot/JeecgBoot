/**
 * jQuery EasyUI 1.3.6
 * 
 * Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
 * To use it on other terms please contact us at info@jeasyui.com
 *
 */
(function($){
var _1;
function _2(_3){
var _4=$.data(_3,"propertygrid");
var _5=$.data(_3,"propertygrid").options;
$(_3).datagrid($.extend({},_5,{cls:"propertygrid",view:(_5.showGroup?_5.groupView:_5.view),onClickRow:function(_6,_7){
if(_1!=this){
_a(_1);
_1=this;
}
if(_5.editIndex!=_6&&_7.editor){
var _8=$(this).datagrid("getColumnOption","value");
_8.editor=_7.editor;
_a(_1);
$(this).datagrid("beginEdit",_6);
$(this).datagrid("getEditors",_6)[0].target.focus();
_5.editIndex=_6;
}
_5.onClickRow.call(_3,_6,_7);
},loadFilter:function(_9){
_a(this);
return _5.loadFilter.call(this,_9);
}}));
$(document).unbind(".propertygrid").bind("mousedown.propertygrid",function(e){
var p=$(e.target).closest("div.datagrid-view,div.combo-panel");
if(p.length){
return;
}
_a(_1);
_1=undefined;
});
};
function _a(_b){
var t=$(_b);
if(!t.length){
return;
}
var _c=$.data(_b,"propertygrid").options;
var _d=_c.editIndex;
if(_d==undefined){
return;
}
var ed=t.datagrid("getEditors",_d)[0];
if(ed){
ed.target.blur();
if(t.datagrid("validateRow",_d)){
t.datagrid("endEdit",_d);
}else{
t.datagrid("cancelEdit",_d);
}
}
_c.editIndex=undefined;
};
$.fn.propertygrid=function(_e,_f){
if(typeof _e=="string"){
var _10=$.fn.propertygrid.methods[_e];
if(_10){
return _10(this,_f);
}else{
return this.datagrid(_e,_f);
}
}
_e=_e||{};
return this.each(function(){
var _11=$.data(this,"propertygrid");
if(_11){
$.extend(_11.options,_e);
}else{
var _12=$.extend({},$.fn.propertygrid.defaults,$.fn.propertygrid.parseOptions(this),_e);
_12.frozenColumns=$.extend(true,[],_12.frozenColumns);
_12.columns=$.extend(true,[],_12.columns);
$.data(this,"propertygrid",{options:_12});
}
_2(this);
});
};
$.fn.propertygrid.methods={options:function(jq){
return $.data(jq[0],"propertygrid").options;
}};
$.fn.propertygrid.parseOptions=function(_13){
return $.extend({},$.fn.datagrid.parseOptions(_13),$.parser.parseOptions(_13,[{showGroup:"boolean"}]));
};
var _14=$.extend({},$.fn.datagrid.defaults.view,{render:function(_15,_16,_17){
var _18=[];
var _19=this.groups;
for(var i=0;i<_19.length;i++){
_18.push(this.renderGroup.call(this,_15,i,_19[i],_17));
}
$(_16).html(_18.join(""));
},renderGroup:function(_1a,_1b,_1c,_1d){
var _1e=$.data(_1a,"datagrid");
var _1f=_1e.options;
var _20=$(_1a).datagrid("getColumnFields",_1d);
var _21=[];
_21.push("<div class=\"datagrid-group\" group-index="+_1b+">");
_21.push("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"height:100%\"><tbody>");
_21.push("<tr>");
if((_1d&&(_1f.rownumbers||_1f.frozenColumns.length))||(!_1d&&!(_1f.rownumbers||_1f.frozenColumns.length))){
_21.push("<td style=\"border:0;text-align:center;width:25px\"><span class=\"datagrid-row-expander datagrid-row-collapse\" style=\"display:inline-block;width:16px;height:16px;cursor:pointer\">&nbsp;</span></td>");
}
_21.push("<td style=\"border:0;\">");
if(!_1d){
_21.push("<span class=\"datagrid-group-title\">");
_21.push(_1f.groupFormatter.call(_1a,_1c.value,_1c.rows));
_21.push("</span>");
}
_21.push("</td>");
_21.push("</tr>");
_21.push("</tbody></table>");
_21.push("</div>");
_21.push("<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>");
var _22=_1c.startIndex;
for(var j=0;j<_1c.rows.length;j++){
var css=_1f.rowStyler?_1f.rowStyler.call(_1a,_22,_1c.rows[j]):"";
var _23="";
var _24="";
if(typeof css=="string"){
_24=css;
}else{
if(css){
_23=css["class"]||"";
_24=css["style"]||"";
}
}
var cls="class=\"datagrid-row "+(_22%2&&_1f.striped?"datagrid-row-alt ":" ")+_23+"\"";
var _25=_24?"style=\""+_24+"\"":"";
var _26=_1e.rowIdPrefix+"-"+(_1d?1:2)+"-"+_22;
_21.push("<tr id=\""+_26+"\" datagrid-row-index=\""+_22+"\" "+cls+" "+_25+">");
_21.push(this.renderRow.call(this,_1a,_20,_1d,_22,_1c.rows[j]));
_21.push("</tr>");
_22++;
}
_21.push("</tbody></table>");
return _21.join("");
},bindEvents:function(_27){
var _28=$.data(_27,"datagrid");
var dc=_28.dc;
var _29=dc.body1.add(dc.body2);
var _2a=($.data(_29[0],"events")||$._data(_29[0],"events")).click[0].handler;
_29.unbind("click").bind("click",function(e){
var tt=$(e.target);
var _2b=tt.closest("span.datagrid-row-expander");
if(_2b.length){
var _2c=_2b.closest("div.datagrid-group").attr("group-index");
if(_2b.hasClass("datagrid-row-collapse")){
$(_27).datagrid("collapseGroup",_2c);
}else{
$(_27).datagrid("expandGroup",_2c);
}
}else{
_2a(e);
}
e.stopPropagation();
});
},onBeforeRender:function(_2d,_2e){
var _2f=$.data(_2d,"datagrid");
var _30=_2f.options;
_31();
var _32=[];
for(var i=0;i<_2e.length;i++){
var row=_2e[i];
var _33=_34(row[_30.groupField]);
if(!_33){
_33={value:row[_30.groupField],rows:[row]};
_32.push(_33);
}else{
_33.rows.push(row);
}
}
var _35=0;
var _36=[];
for(var i=0;i<_32.length;i++){
var _33=_32[i];
_33.startIndex=_35;
_35+=_33.rows.length;
_36=_36.concat(_33.rows);
}
_2f.data.rows=_36;
this.groups=_32;
var _37=this;
setTimeout(function(){
_37.bindEvents(_2d);
},0);
function _34(_38){
for(var i=0;i<_32.length;i++){
var _39=_32[i];
if(_39.value==_38){
return _39;
}
}
return null;
};
function _31(){
if(!$("#datagrid-group-style").length){
$("head").append("<style id=\"datagrid-group-style\">"+".datagrid-group{height:25px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;}"+"</style>");
}
};
}});
$.extend($.fn.datagrid.methods,{expandGroup:function(jq,_3a){
return jq.each(function(){
var _3b=$.data(this,"datagrid").dc.view;
var _3c=_3b.find(_3a!=undefined?"div.datagrid-group[group-index=\""+_3a+"\"]":"div.datagrid-group");
var _3d=_3c.find("span.datagrid-row-expander");
if(_3d.hasClass("datagrid-row-expand")){
_3d.removeClass("datagrid-row-expand").addClass("datagrid-row-collapse");
_3c.next("table").show();
}
$(this).datagrid("fixRowHeight");
});
},collapseGroup:function(jq,_3e){
return jq.each(function(){
var _3f=$.data(this,"datagrid").dc.view;
var _40=_3f.find(_3e!=undefined?"div.datagrid-group[group-index=\""+_3e+"\"]":"div.datagrid-group");
var _41=_40.find("span.datagrid-row-expander");
if(_41.hasClass("datagrid-row-collapse")){
_41.removeClass("datagrid-row-collapse").addClass("datagrid-row-expand");
_40.next("table").hide();
}
$(this).datagrid("fixRowHeight");
});
}});
$.fn.propertygrid.defaults=$.extend({},$.fn.datagrid.defaults,{singleSelect:true,remoteSort:false,fitColumns:true,loadMsg:"",frozenColumns:[[{field:"f",width:16,resizable:false}]],columns:[[{field:"name",title:"Name",width:100,sortable:true},{field:"value",title:"Value",width:100,resizable:false}]],showGroup:false,groupView:_14,groupField:"group",groupFormatter:function(_42,_43){
return _42;
}});
})(jQuery);

