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
$(_2).addClass("tooltip-f");
};
function _3(_4){
var _5=$.data(_4,"tooltip").options;
$(_4).unbind(".tooltip").bind(_5.showEvent+".tooltip",function(e){
_10(_4,e);
}).bind(_5.hideEvent+".tooltip",function(e){
_17(_4,e);
}).bind("mousemove.tooltip",function(e){
if(_5.trackMouse){
_5.trackMouseX=e.pageX;
_5.trackMouseY=e.pageY;
_6(_4);
}
});
};
function _7(_8){
var _9=$.data(_8,"tooltip");
if(_9.showTimer){
clearTimeout(_9.showTimer);
_9.showTimer=null;
}
if(_9.hideTimer){
clearTimeout(_9.hideTimer);
_9.hideTimer=null;
}
};
function _6(_a){
var _b=$.data(_a,"tooltip");
if(!_b||!_b.tip){
return;
}
var _c=_b.options;
var _d=_b.tip;
if(_c.trackMouse){
t=$();
var _e=_c.trackMouseX+_c.deltaX;
var _f=_c.trackMouseY+_c.deltaY;
}else{
var t=$(_a);
var _e=t.offset().left+_c.deltaX;
var _f=t.offset().top+_c.deltaY;
}
switch(_c.position){
case "right":
_e+=t._outerWidth()+12+(_c.trackMouse?12:0);
_f-=(_d._outerHeight()-t._outerHeight())/2;
break;
case "left":
_e-=_d._outerWidth()+12+(_c.trackMouse?12:0);
_f-=(_d._outerHeight()-t._outerHeight())/2;
break;
case "top":
_e-=(_d._outerWidth()-t._outerWidth())/2;
_f-=_d._outerHeight()+12+(_c.trackMouse?12:0);
break;
case "bottom":
_e-=(_d._outerWidth()-t._outerWidth())/2;
_f+=t._outerHeight()+12+(_c.trackMouse?12:0);
break;
}
if(!$(_a).is(":visible")){
_e=-100000;
_f=-100000;
}
_d.css({left:_e,top:_f,zIndex:(_c.zIndex!=undefined?_c.zIndex:($.fn.window?$.fn.window.defaults.zIndex++:""))});
_c.onPosition.call(_a,_e,_f);
};
function _10(_11,e){
var _12=$.data(_11,"tooltip");
var _13=_12.options;
var tip=_12.tip;
if(!tip){
tip=$("<div tabindex=\"-1\" class=\"tooltip\">"+"<div class=\"tooltip-content\"></div>"+"<div class=\"tooltip-arrow-outer\"></div>"+"<div class=\"tooltip-arrow\"></div>"+"</div>").appendTo("body");
_12.tip=tip;
_14(_11);
}
tip.removeClass("tooltip-top tooltip-bottom tooltip-left tooltip-right").addClass("tooltip-"+_13.position);
_7(_11);
_12.showTimer=setTimeout(function(){
_6(_11);
tip.show();
_13.onShow.call(_11,e);
var _15=tip.children(".tooltip-arrow-outer");
var _16=tip.children(".tooltip-arrow");
var bc="border-"+_13.position+"-color";
_15.add(_16).css({borderTopColor:"",borderBottomColor:"",borderLeftColor:"",borderRightColor:""});
_15.css(bc,tip.css(bc));
_16.css(bc,tip.css("backgroundColor"));
},_13.showDelay);
};
function _17(_18,e){
var _19=$.data(_18,"tooltip");
if(_19&&_19.tip){
_7(_18);
_19.hideTimer=setTimeout(function(){
_19.tip.hide();
_19.options.onHide.call(_18,e);
},_19.options.hideDelay);
}
};
function _14(_1a,_1b){
var _1c=$.data(_1a,"tooltip");
var _1d=_1c.options;
if(_1b){
_1d.content=_1b;
}
if(!_1c.tip){
return;
}
var cc=typeof _1d.content=="function"?_1d.content.call(_1a):_1d.content;
_1c.tip.children(".tooltip-content").html(cc);
_1d.onUpdate.call(_1a,cc);
};
function _1e(_1f){
var _20=$.data(_1f,"tooltip");
if(_20){
_7(_1f);
var _21=_20.options;
if(_20.tip){
_20.tip.remove();
}
if(_21._title){
$(_1f).attr("title",_21._title);
}
$.removeData(_1f,"tooltip");
$(_1f).unbind(".tooltip").removeClass("tooltip-f");
_21.onDestroy.call(_1f);
}
};
$.fn.tooltip=function(_22,_23){
if(typeof _22=="string"){
return $.fn.tooltip.methods[_22](this,_23);
}
_22=_22||{};
return this.each(function(){
var _24=$.data(this,"tooltip");
if(_24){
$.extend(_24.options,_22);
}else{
$.data(this,"tooltip",{options:$.extend({},$.fn.tooltip.defaults,$.fn.tooltip.parseOptions(this),_22)});
_1(this);
}
_3(this);
_14(this);
});
};
$.fn.tooltip.methods={options:function(jq){
return $.data(jq[0],"tooltip").options;
},tip:function(jq){
return $.data(jq[0],"tooltip").tip;
},arrow:function(jq){
return jq.tooltip("tip").children(".tooltip-arrow-outer,.tooltip-arrow");
},show:function(jq,e){
return jq.each(function(){
_10(this,e);
});
},hide:function(jq,e){
return jq.each(function(){
_17(this,e);
});
},update:function(jq,_25){
return jq.each(function(){
_14(this,_25);
});
},reposition:function(jq){
return jq.each(function(){
_6(this);
});
},destroy:function(jq){
return jq.each(function(){
_1e(this);
});
}};
$.fn.tooltip.parseOptions=function(_26){
var t=$(_26);
var _27=$.extend({},$.parser.parseOptions(_26,["position","showEvent","hideEvent","content",{deltaX:"number",deltaY:"number",showDelay:"number",hideDelay:"number"}]),{_title:t.attr("title")});
t.attr("title","");
if(!_27.content){
_27.content=_27._title;
}
return _27;
};
$.fn.tooltip.defaults={position:"bottom",content:null,trackMouse:false,deltaX:0,deltaY:0,showEvent:"mouseenter",hideEvent:"mouseleave",showDelay:200,hideDelay:100,onShow:function(e){
},onHide:function(e){
},onUpdate:function(_28){
},onPosition:function(_29,top){
},onDestroy:function(){
}};
})(jQuery);

