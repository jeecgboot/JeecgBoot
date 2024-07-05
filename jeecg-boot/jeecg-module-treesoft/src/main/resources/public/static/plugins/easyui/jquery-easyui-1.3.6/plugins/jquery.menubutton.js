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
var _3=$.data(_2,"menubutton").options;
var _4=$(_2);
_4.linkbutton(_3);
_4.removeClass(_3.cls.btn1+" "+_3.cls.btn2).addClass("m-btn");
_4.removeClass("m-btn-small m-btn-medium m-btn-large").addClass("m-btn-"+_3.size);
var _5=_4.find(".l-btn-left");
$("<span></span>").addClass(_3.cls.arrow).appendTo(_5);
$("<span></span>").addClass("m-btn-line").appendTo(_5);
if(_3.menu){
$(_3.menu).menu();
var _6=$(_3.menu).menu("options");
var _7=_6.onShow;
var _8=_6.onHide;
$.extend(_6,{onShow:function(){
var _9=$(this).menu("options");
var _a=$(_9.alignTo);
var _b=_a.menubutton("options");
_a.addClass((_b.plain==true)?_b.cls.btn2:_b.cls.btn1);
_7.call(this);
},onHide:function(){
var _c=$(this).menu("options");
var _d=$(_c.alignTo);
var _e=_d.menubutton("options");
_d.removeClass((_e.plain==true)?_e.cls.btn2:_e.cls.btn1);
_8.call(this);
}});
}
_f(_2,_3.disabled);
};
function _f(_10,_11){
var _12=$.data(_10,"menubutton").options;
_12.disabled=_11;
var btn=$(_10);
var t=btn.find("."+_12.cls.trigger);
if(!t.length){
t=btn;
}
t.unbind(".menubutton");
if(_11){
btn.linkbutton("disable");
}else{
btn.linkbutton("enable");
var _13=null;
t.bind("click.menubutton",function(){
_14(_10);
return false;
}).bind("mouseenter.menubutton",function(){
_13=setTimeout(function(){
_14(_10);
},_12.duration);
return false;
}).bind("mouseleave.menubutton",function(){
if(_13){
clearTimeout(_13);
}
});
}
};
function _14(_15){
var _16=$.data(_15,"menubutton").options;
if(_16.disabled||!_16.menu){
return;
}
$("body>div.menu-top").menu("hide");
var btn=$(_15);
var mm=$(_16.menu);
if(mm.length){
mm.menu("options").alignTo=btn;
mm.menu("show",{alignTo:btn,align:_16.menuAlign});
}
btn.blur();
};
$.fn.menubutton=function(_17,_18){
if(typeof _17=="string"){
var _19=$.fn.menubutton.methods[_17];
if(_19){
return _19(this,_18);
}else{
return this.linkbutton(_17,_18);
}
}
_17=_17||{};
return this.each(function(){
var _1a=$.data(this,"menubutton");
if(_1a){
$.extend(_1a.options,_17);
}else{
$.data(this,"menubutton",{options:$.extend({},$.fn.menubutton.defaults,$.fn.menubutton.parseOptions(this),_17)});
$(this).removeAttr("disabled");
}
_1(this);
});
};
$.fn.menubutton.methods={options:function(jq){
var _1b=jq.linkbutton("options");
var _1c=$.data(jq[0],"menubutton").options;
_1c.toggle=_1b.toggle;
_1c.selected=_1b.selected;
return _1c;
},enable:function(jq){
return jq.each(function(){
_f(this,false);
});
},disable:function(jq){
return jq.each(function(){
_f(this,true);
});
},destroy:function(jq){
return jq.each(function(){
var _1d=$(this).menubutton("options");
if(_1d.menu){
$(_1d.menu).menu("destroy");
}
$(this).remove();
});
}};
$.fn.menubutton.parseOptions=function(_1e){
var t=$(_1e);
return $.extend({},$.fn.linkbutton.parseOptions(_1e),$.parser.parseOptions(_1e,["menu",{plain:"boolean",duration:"number"}]));
};
$.fn.menubutton.defaults=$.extend({},$.fn.linkbutton.defaults,{plain:true,menu:null,menuAlign:"left",duration:100,cls:{btn1:"m-btn-active",btn2:"m-btn-plain-active",arrow:"m-btn-downarrow",trigger:"m-btn"}});
})(jQuery);

