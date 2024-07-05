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
$(_2).addClass("searchbox-f").hide();
var _3=$("<span class=\"searchbox\"></span>").insertAfter(_2);
var _4=$("<input type=\"text\" class=\"searchbox-text\">").appendTo(_3);
$("<span><span class=\"searchbox-button\"></span></span>").appendTo(_3);
var _5=$(_2).attr("name");
if(_5){
_4.attr("name",_5);
$(_2).removeAttr("name").attr("searchboxName",_5);
}
return _3;
};
function _6(_7,_8){
var _9=$.data(_7,"searchbox").options;
var sb=$.data(_7,"searchbox").searchbox;
if(_8){
_9.width=_8;
}
sb.appendTo("body");
if(isNaN(_9.width)){
_9.width=sb._outerWidth();
}
var _a=sb.find("span.searchbox-button");
var _b=sb.find("a.searchbox-menu");
var _c=sb.find("input.searchbox-text");
sb._outerWidth(_9.width)._outerHeight(_9.height);
_c._outerWidth(sb.width()-_b._outerWidth()-_a._outerWidth());
_c.css({height:sb.height()+"px",lineHeight:sb.height()+"px"});
_b._outerHeight(sb.height());
_a._outerHeight(sb.height());
var _d=_b.find("span.l-btn-left");
_d._outerHeight(sb.height());
_d.find("span.l-btn-text").css({height:_d.height()+"px",lineHeight:_d.height()+"px"});
sb.insertAfter(_7);
};
function _e(_f){
var _10=$.data(_f,"searchbox");
var _11=_10.options;
if(_11.menu){
_10.menu=$(_11.menu).menu({onClick:function(_12){
_13(_12);
}});
var _14=_10.menu.children("div.menu-item:first");
_10.menu.children("div.menu-item").each(function(){
var _15=$.extend({},$.parser.parseOptions(this),{selected:($(this).attr("selected")?true:undefined)});
if(_15.selected){
_14=$(this);
return false;
}
});
_14.triggerHandler("click");
}else{
_10.searchbox.find("a.searchbox-menu").remove();
_10.menu=null;
}
function _13(_16){
_10.searchbox.find("a.searchbox-menu").remove();
var mb=$("<a class=\"searchbox-menu\" href=\"javascript:void(0)\"></a>").html(_16.text);
mb.prependTo(_10.searchbox).menubutton({menu:_10.menu,iconCls:_16.iconCls});
_10.searchbox.find("input.searchbox-text").attr("name",_16.name||_16.text);
_6(_f);
};
};
function _17(_18){
var _19=$.data(_18,"searchbox");
var _1a=_19.options;
var _1b=_19.searchbox.find("input.searchbox-text");
var _1c=_19.searchbox.find(".searchbox-button");
_1b.unbind(".searchbox");
_1c.unbind(".searchbox");
if(!_1a.disabled){
_1b.bind("blur.searchbox",function(e){
_1a.value=$(this).val();
if(_1a.value==""){
$(this).val(_1a.prompt);
$(this).addClass("searchbox-prompt");
}else{
$(this).removeClass("searchbox-prompt");
}
}).bind("focus.searchbox",function(e){
if($(this).val()!=_1a.value){
$(this).val(_1a.value);
}
$(this).removeClass("searchbox-prompt");
}).bind("keydown.searchbox",function(e){
if(e.keyCode==13){
e.preventDefault();
_1a.value=$(this).val();
_1a.searcher.call(_18,_1a.value,_1b._propAttr("name"));
return false;
}
});
_1c.bind("click.searchbox",function(){
_1a.searcher.call(_18,_1a.value,_1b._propAttr("name"));
}).bind("mouseenter.searchbox",function(){
$(this).addClass("searchbox-button-hover");
}).bind("mouseleave.searchbox",function(){
$(this).removeClass("searchbox-button-hover");
});
}
};
function _1d(_1e,_1f){
var _20=$.data(_1e,"searchbox");
var _21=_20.options;
var _22=_20.searchbox.find("input.searchbox-text");
var mb=_20.searchbox.find("a.searchbox-menu");
if(_1f){
_21.disabled=true;
$(_1e).attr("disabled",true);
_22.attr("disabled",true);
if(mb.length){
mb.menubutton("disable");
}
}else{
_21.disabled=false;
$(_1e).removeAttr("disabled");
_22.removeAttr("disabled");
if(mb.length){
mb.menubutton("enable");
}
}
};
function _23(_24){
var _25=$.data(_24,"searchbox");
var _26=_25.options;
var _27=_25.searchbox.find("input.searchbox-text");
_26.originalValue=_26.value;
if(_26.value){
_27.val(_26.value);
_27.removeClass("searchbox-prompt");
}else{
_27.val(_26.prompt);
_27.addClass("searchbox-prompt");
}
};
$.fn.searchbox=function(_28,_29){
if(typeof _28=="string"){
return $.fn.searchbox.methods[_28](this,_29);
}
_28=_28||{};
return this.each(function(){
var _2a=$.data(this,"searchbox");
if(_2a){
$.extend(_2a.options,_28);
}else{
_2a=$.data(this,"searchbox",{options:$.extend({},$.fn.searchbox.defaults,$.fn.searchbox.parseOptions(this),_28),searchbox:_1(this)});
}
_e(this);
_23(this);
_17(this);
_1d(this,_2a.options.disabled);
_6(this);
});
};
$.fn.searchbox.methods={options:function(jq){
return $.data(jq[0],"searchbox").options;
},menu:function(jq){
return $.data(jq[0],"searchbox").menu;
},textbox:function(jq){
return $.data(jq[0],"searchbox").searchbox.find("input.searchbox-text");
},getValue:function(jq){
return $.data(jq[0],"searchbox").options.value;
},setValue:function(jq,_2b){
return jq.each(function(){
$(this).searchbox("options").value=_2b;
$(this).searchbox("textbox").val(_2b);
$(this).searchbox("textbox").blur();
});
},clear:function(jq){
return jq.each(function(){
$(this).searchbox("setValue","");
});
},reset:function(jq){
return jq.each(function(){
var _2c=$(this).searchbox("options");
$(this).searchbox("setValue",_2c.originalValue);
});
},getName:function(jq){
return $.data(jq[0],"searchbox").searchbox.find("input.searchbox-text").attr("name");
},selectName:function(jq,_2d){
return jq.each(function(){
var _2e=$.data(this,"searchbox").menu;
if(_2e){
_2e.children("div.menu-item[name=\""+_2d+"\"]").triggerHandler("click");
}
});
},destroy:function(jq){
return jq.each(function(){
var _2f=$(this).searchbox("menu");
if(_2f){
_2f.menu("destroy");
}
$.data(this,"searchbox").searchbox.remove();
$(this).remove();
});
},resize:function(jq,_30){
return jq.each(function(){
_6(this,_30);
});
},disable:function(jq){
return jq.each(function(){
_1d(this,true);
_17(this);
});
},enable:function(jq){
return jq.each(function(){
_1d(this,false);
_17(this);
});
}};
$.fn.searchbox.parseOptions=function(_31){
var t=$(_31);
return $.extend({},$.parser.parseOptions(_31,["width","height","prompt","menu"]),{value:(t.val()||undefined),disabled:(t.attr("disabled")?true:undefined),searcher:(t.attr("searcher")?eval(t.attr("searcher")):undefined)});
};
$.fn.searchbox.defaults={width:"auto",height:22,prompt:"",value:"",menu:null,disabled:false,searcher:function(_32,_33){
}};
})(jQuery);

