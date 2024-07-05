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
var _3=$.data(_2,"splitbutton").options;
$(_2).menubutton(_3);
$(_2).addClass("s-btn");
};
$.fn.splitbutton=function(_4,_5){
if(typeof _4=="string"){
var _6=$.fn.splitbutton.methods[_4];
if(_6){
return _6(this,_5);
}else{
return this.menubutton(_4,_5);
}
}
_4=_4||{};
return this.each(function(){
var _7=$.data(this,"splitbutton");
if(_7){
$.extend(_7.options,_4);
}else{
$.data(this,"splitbutton",{options:$.extend({},$.fn.splitbutton.defaults,$.fn.splitbutton.parseOptions(this),_4)});
$(this).removeAttr("disabled");
}
_1(this);
});
};
$.fn.splitbutton.methods={options:function(jq){
var _8=jq.menubutton("options");
var _9=$.data(jq[0],"splitbutton").options;
$.extend(_9,{disabled:_8.disabled,toggle:_8.toggle,selected:_8.selected});
return _9;
}};
$.fn.splitbutton.parseOptions=function(_a){
var t=$(_a);
return $.extend({},$.fn.linkbutton.parseOptions(_a),$.parser.parseOptions(_a,["menu",{plain:"boolean",duration:"number"}]));
};
$.fn.splitbutton.defaults=$.extend({},$.fn.linkbutton.defaults,{plain:true,menu:null,duration:100,cls:{btn1:"m-btn-active s-btn-active",btn2:"m-btn-plain-active s-btn-plain-active",arrow:"m-btn-downarrow",trigger:"m-btn-line"}});
})(jQuery);

