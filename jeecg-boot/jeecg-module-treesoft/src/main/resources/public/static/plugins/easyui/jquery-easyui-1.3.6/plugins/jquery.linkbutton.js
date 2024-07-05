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
function _1(_2){
var _3=$.data(_2,"linkbutton").options;
var t=$(_2).empty();
t.addClass("l-btn").removeClass("l-btn-plain l-btn-selected l-btn-plain-selected");
t.removeClass("l-btn-small l-btn-medium l-btn-large").addClass("l-btn-"+_3.size);
if(_3.plain){
t.addClass("l-btn-plain");
}
if(_3.selected){
t.addClass(_3.plain?"l-btn-selected l-btn-plain-selected":"l-btn-selected");
}
t.attr("group",_3.group||"");
t.attr("id",_3.id||"");
var _4=$("<span class=\"l-btn-left\"></span>").appendTo(t);
if(_3.text){
$("<span class=\"l-btn-text\"></span>").html(_3.text).appendTo(_4);
}else{
$("<span class=\"l-btn-text l-btn-empty\">&nbsp;</span>").appendTo(_4);
}
if(_3.iconCls){
$("<span class=\"l-btn-icon\">&nbsp;</span>").addClass(_3.iconCls).appendTo(_4);
_4.addClass("l-btn-icon-"+_3.iconAlign);
}
t.unbind(".linkbutton").bind("focus.linkbutton",function(){
if(!_3.disabled){
$(this).addClass("l-btn-focus");
}
}).bind("blur.linkbutton",function(){
$(this).removeClass("l-btn-focus");
}).bind("click.linkbutton",function(){
if(!_3.disabled){
if(_3.toggle){
if(_3.selected){
$(this).linkbutton("unselect");
}else{
$(this).linkbutton("select");
}
}
_3.onClick.call(this);
}
return false;
});
_5(_2,_3.selected);
_6(_2,_3.disabled);
};
function _5(_7,_8){
var _9=$.data(_7,"linkbutton").options;
if(_8){
if(_9.group){
$("a.l-btn[group=\""+_9.group+"\"]").each(function(){
var o=$(this).linkbutton("options");
if(o.toggle){
$(this).removeClass("l-btn-selected l-btn-plain-selected");
o.selected=false;
}
});
}
$(_7).addClass(_9.plain?"l-btn-selected l-btn-plain-selected":"l-btn-selected");
_9.selected=true;
}else{
if(!_9.group){
$(_7).removeClass("l-btn-selected l-btn-plain-selected");
_9.selected=false;
}
}
};
function _6(_a,_b){
var _c=$.data(_a,"linkbutton");
var _d=_c.options;
$(_a).removeClass("l-btn-disabled l-btn-plain-disabled");
if(_b){
_d.disabled=true;
var _e=$(_a).attr("href");
if(_e){
_c.href=_e;
$(_a).attr("href","javascript:void(0)");
}
if(_a.onclick){
_c.onclick=_a.onclick;
_a.onclick=null;
}
_d.plain?$(_a).addClass("l-btn-disabled l-btn-plain-disabled"):$(_a).addClass("l-btn-disabled");
}else{
_d.disabled=false;
if(_c.href){
$(_a).attr("href",_c.href);
}
if(_c.onclick){
_a.onclick=_c.onclick;
}
}
};
$.fn.linkbutton=function(_f,_10){
if(typeof _f=="string"){
return $.fn.linkbutton.methods[_f](this,_10);
}
_f=_f||{};
return this.each(function(){
var _11=$.data(this,"linkbutton");
if(_11){
$.extend(_11.options,_f);
}else{
$.data(this,"linkbutton",{options:$.extend({},$.fn.linkbutton.defaults,$.fn.linkbutton.parseOptions(this),_f)});
$(this).removeAttr("disabled");
}
_1(this);
});
};
$.fn.linkbutton.methods={options:function(jq){
return $.data(jq[0],"linkbutton").options;
},enable:function(jq){
return jq.each(function(){
_6(this,false);
});
},disable:function(jq){
return jq.each(function(){
_6(this,true);
});
},select:function(jq){
return jq.each(function(){
_5(this,true);
});
},unselect:function(jq){
return jq.each(function(){
_5(this,false);
});
}};
$.fn.linkbutton.parseOptions=function(_12){
var t=$(_12);
return $.extend({},$.parser.parseOptions(_12,["id","iconCls","iconAlign","group","size",{plain:"boolean",toggle:"boolean",selected:"boolean"}]),{disabled:(t.attr("disabled")?true:undefined),text:$.trim(t.html()),iconCls:(t.attr("icon")||t.attr("iconCls"))});
};
$.fn.linkbutton.defaults={id:null,disabled:false,toggle:false,selected:false,group:null,plain:false,text:"",iconCls:null,iconAlign:"left",size:"small",onClick:function(){
}};
})(jQuery);

