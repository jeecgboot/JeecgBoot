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
function _1(_2,_3){
_3=_3||{};
var _4={};
if(_3.onSubmit){
if(_3.onSubmit.call(_2,_4)==false){
return;
}
}
var _5=$(_2);
if(_3.url){
_5.attr("action",_3.url);
}
var _6="easyui_frame_"+(new Date().getTime());
var _7=$("<iframe id="+_6+" name="+_6+"></iframe>").attr("src",window.ActiveXObject?"javascript:false":"about:blank").css({position:"absolute",top:-1000,left:-1000});
var t=_5.attr("target"),a=_5.attr("action");
_5.attr("target",_6);
var _8=$();
try{
_7.appendTo("body");
_7.bind("load",cb);
for(var n in _4){
var f=$("<input type=\"hidden\" name=\""+n+"\">").val(_4[n]).appendTo(_5);
_8=_8.add(f);
}
_9();
_5[0].submit();
}
finally{
_5.attr("action",a);
t?_5.attr("target",t):_5.removeAttr("target");
_8.remove();
}
function _9(){
var f=$("#"+_6);
if(!f.length){
return;
}
try{
var s=f.contents()[0].readyState;
if(s&&s.toLowerCase()=="uninitialized"){
setTimeout(_9,100);
}
}
catch(e){
cb();
}
};
var _a=10;
function cb(){
var _b=$("#"+_6);
if(!_b.length){
return;
}
_b.unbind();
var _c="";
try{
var _d=_b.contents().find("body");
_c=_d.html();
if(_c==""){
if(--_a){
setTimeout(cb,100);
return;
}
}
var ta=_d.find(">textarea");
if(ta.length){
_c=ta.val();
}else{
var _e=_d.find(">pre");
if(_e.length){
_c=_e.html();
}
}
}
catch(e){
}
if(_3.success){
_3.success(_c);
}
setTimeout(function(){
_b.unbind();
_b.remove();
},100);
};
};
function _f(_10,_11){
if(!$.data(_10,"form")){
$.data(_10,"form",{options:$.extend({},$.fn.form.defaults)});
}
var _12=$.data(_10,"form").options;
if(typeof _11=="string"){
var _13={};
if(_12.onBeforeLoad.call(_10,_13)==false){
return;
}
$.ajax({url:_11,data:_13,dataType:"json",success:function(_14){
_15(_14);
},error:function(){
_12.onLoadError.apply(_10,arguments);
}});
}else{
_15(_11);
}
function _15(_16){
var _17=$(_10);
for(var _18 in _16){
var val=_16[_18];
var rr=_19(_18,val);
if(!rr.length){
var _1a=_1b(_18,val);
if(!_1a){
$("input[name=\""+_18+"\"]",_17).val(val);
$("textarea[name=\""+_18+"\"]",_17).val(val);
$("select[name=\""+_18+"\"]",_17).val(val);
}
}
_1c(_18,val);
}
_12.onLoadSuccess.call(_10,_16);
_29(_10);
};
function _19(_1d,val){
var rr=$(_10).find("input[name=\""+_1d+"\"][type=radio], input[name=\""+_1d+"\"][type=checkbox]");
rr._propAttr("checked",false);
rr.each(function(){
var f=$(this);
if(f.val()==String(val)||$.inArray(f.val(),$.isArray(val)?val:[val])>=0){
f._propAttr("checked",true);
}
});
return rr;
};
function _1b(_1e,val){
var _1f=0;
var pp=["numberbox","slider"];
for(var i=0;i<pp.length;i++){
var p=pp[i];
var f=$(_10).find("input["+p+"Name=\""+_1e+"\"]");
if(f.length){
f[p]("setValue",val);
_1f+=f.length;
}
}
return _1f;
};
function _1c(_20,val){
var _21=$(_10);
var cc=["combobox","combotree","combogrid","datetimebox","datebox","combo"];
var c=_21.find("[comboName=\""+_20+"\"]");
if(c.length){
for(var i=0;i<cc.length;i++){
var _22=cc[i];
if(c.hasClass(_22+"-f")){
if(c[_22]("options").multiple){
c[_22]("setValues",val);
}else{
c[_22]("setValue",val);
}
return;
}
}
}
};
};
function _23(_24){
$("input,select,textarea",_24).each(function(){
var t=this.type,tag=this.tagName.toLowerCase();
if(t=="text"||t=="hidden"||t=="password"||tag=="textarea"){
this.value="";
}else{
if(t=="file"){
var _25=$(this);
var _26=_25.clone().val("");
_26.insertAfter(_25);
if(_25.data("validatebox")){
_25.validatebox("destroy");
_26.validatebox();
}else{
_25.remove();
}
}else{
if(t=="checkbox"||t=="radio"){
this.checked=false;
}else{
if(tag=="select"){
this.selectedIndex=-1;
}
}
}
}
});
var t=$(_24);
var _27=["combo","combobox","combotree","combogrid","slider"];
for(var i=0;i<_27.length;i++){
var _28=_27[i];
var r=t.find("."+_28+"-f");
if(r.length&&r[_28]){
r[_28]("clear");
}
}
_29(_24);
};
function _2a(_2b){
_2b.reset();
var t=$(_2b);
var _2c=["combo","combobox","combotree","combogrid","datebox","datetimebox","spinner","timespinner","numberbox","numberspinner","slider"];
for(var i=0;i<_2c.length;i++){
var _2d=_2c[i];
var r=t.find("."+_2d+"-f");
if(r.length&&r[_2d]){
r[_2d]("reset");
}
}
_29(_2b);
};
function _2e(_2f){
var _30=$.data(_2f,"form").options;
var _31=$(_2f);
_31.unbind(".form").bind("submit.form",function(){
setTimeout(function(){
_1(_2f,_30);
},0);
return false;
});
};
function _29(_32){
if($.fn.validatebox){
var t=$(_32);
t.find(".validatebox-text:not(:disabled)").validatebox("validate");
var _33=t.find(".validatebox-invalid");
_33.filter(":not(:disabled):first").focus();
return _33.length==0;
}
return true;
};
function _34(_35,_36){
$(_35).find(".validatebox-text:not(:disabled)").validatebox(_36?"disableValidation":"enableValidation");
};
$.fn.form=function(_37,_38){
if(typeof _37=="string"){
return $.fn.form.methods[_37](this,_38);
}
_37=_37||{};
return this.each(function(){
if(!$.data(this,"form")){
$.data(this,"form",{options:$.extend({},$.fn.form.defaults,_37)});
}
_2e(this);
});
};
$.fn.form.methods={submit:function(jq,_39){
return jq.each(function(){
var _3a=$.extend({},$.fn.form.defaults,$.data(this,"form")?$.data(this,"form").options:{},_39||{});
_1(this,_3a);
});
},load:function(jq,_3b){
return jq.each(function(){
_f(this,_3b);
});
},clear:function(jq){
return jq.each(function(){
_23(this);
});
},reset:function(jq){
return jq.each(function(){
_2a(this);
});
},validate:function(jq){
return _29(jq[0]);
},disableValidation:function(jq){
return jq.each(function(){
_34(this,true);
});
},enableValidation:function(jq){
return jq.each(function(){
_34(this,false);
});
}};
$.fn.form.defaults={url:null,onSubmit:function(_3c){
return $(this).form("validate");
},success:function(_3d){
},onBeforeLoad:function(_3e){
},onLoadSuccess:function(_3f){
},onLoadError:function(){
}};
})(jQuery);

