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
$(_2).addClass("numberspinner-f");
var _3=$.data(_2,"numberspinner").options;
$(_2).spinner(_3).numberbox($.extend({},_3,{width:"auto"}));
};
function _4(_5,_6){
var _7=$.data(_5,"numberspinner").options;
var v=parseFloat($(_5).numberbox("getValue")||_7.value)||0;
if(_6==true){
v-=_7.increment;
}else{
v+=_7.increment;
}
$(_5).numberbox("setValue",v);
};
$.fn.numberspinner=function(_8,_9){
if(typeof _8=="string"){
var _a=$.fn.numberspinner.methods[_8];
if(_a){
return _a(this,_9);
}else{
return this.spinner(_8,_9);
}
}
_8=_8||{};
return this.each(function(){
var _b=$.data(this,"numberspinner");
if(_b){
$.extend(_b.options,_8);
}else{
$.data(this,"numberspinner",{options:$.extend({},$.fn.numberspinner.defaults,$.fn.numberspinner.parseOptions(this),_8)});
}
_1(this);
});
};
$.fn.numberspinner.methods={options:function(jq){
var _c=$.data(jq[0],"numberspinner").options;
return $.extend(_c,{value:jq.numberbox("getValue"),originalValue:jq.numberbox("options").originalValue});
},setValue:function(jq,_d){
return jq.each(function(){
$(this).numberbox("setValue",_d);
});
},getValue:function(jq){
return jq.numberbox("getValue");
},clear:function(jq){
return jq.each(function(){
$(this).spinner("clear");
$(this).numberbox("clear");
});
},reset:function(jq){
return jq.each(function(){
var _e=$(this).numberspinner("options");
$(this).numberspinner("setValue",_e.originalValue);
});
}};
$.fn.numberspinner.parseOptions=function(_f){
return $.extend({},$.fn.spinner.parseOptions(_f),$.fn.numberbox.parseOptions(_f),{});
};
$.fn.numberspinner.defaults=$.extend({},$.fn.spinner.defaults,$.fn.numberbox.defaults,{spin:function(_10){
_4(this,_10);
}});
})(jQuery);

