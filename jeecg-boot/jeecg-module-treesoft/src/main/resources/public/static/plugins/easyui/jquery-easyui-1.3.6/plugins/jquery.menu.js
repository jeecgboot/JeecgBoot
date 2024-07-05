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
$(_2).appendTo("body");
$(_2).addClass("menu-top");
$(document).unbind(".menu").bind("mousedown.menu",function(e){
var m=$(e.target).closest("div.menu,div.combo-p");
if(m.length){
return;
}
$("body>div.menu-top:visible").menu("hide");
});
var _3=_4($(_2));
for(var i=0;i<_3.length;i++){
_5(_3[i]);
}
function _4(_6){
var _7=[];
_6.addClass("menu");
_7.push(_6);
if(!_6.hasClass("menu-content")){
_6.children("div").each(function(){
var _8=$(this).children("div");
if(_8.length){
_8.insertAfter(_2);
this.submenu=_8;
var mm=_4(_8);
_7=_7.concat(mm);
}
});
}
return _7;
};
function _5(_9){
var wh=$.parser.parseOptions(_9[0],["width","height"]);
_9[0].originalHeight=wh.height||0;
if(_9.hasClass("menu-content")){
_9[0].originalWidth=wh.width||_9._outerWidth();
}else{
_9[0].originalWidth=wh.width||0;
_9.children("div").each(function(){
var _a=$(this);
var _b=$.extend({},$.parser.parseOptions(this,["name","iconCls","href",{separator:"boolean"}]),{disabled:(_a.attr("disabled")?true:undefined)});
if(_b.separator){
_a.addClass("menu-sep");
}
if(!_a.hasClass("menu-sep")){
_a[0].itemName=_b.name||"";
_a[0].itemHref=_b.href||"";
var _c=_a.addClass("menu-item").html();
_a.empty().append($("<div class=\"menu-text\"></div>").html(_c));
if(_b.iconCls){
$("<div class=\"menu-icon\"></div>").addClass(_b.iconCls).appendTo(_a);
}
if(_b.disabled){
_d(_2,_a[0],true);
}
if(_a[0].submenu){
$("<div class=\"menu-rightarrow\"></div>").appendTo(_a);
}
_e(_2,_a);
}
});
$("<div class=\"menu-line\"></div>").prependTo(_9);
}
_f(_2,_9);
_9.hide();
_10(_2,_9);
};
};
function _f(_11,_12){
var _13=$.data(_11,"menu").options;
var _14=_12.attr("style")||"";
_12.css({display:"block",left:-10000,height:"auto",overflow:"hidden"});
var el=_12[0];
var _15=el.originalWidth||0;
if(!_15){
_15=0;
_12.find("div.menu-text").each(function(){
if(_15<$(this)._outerWidth()){
_15=$(this)._outerWidth();
}
$(this).closest("div.menu-item")._outerHeight($(this)._outerHeight()+2);
});
_15+=40;
}
_15=Math.max(_15,_13.minWidth);
var _16=el.originalHeight||_12.outerHeight();
var _17=Math.max(el.originalHeight,_12.outerHeight())-2;
_12._outerWidth(_15)._outerHeight(_16);
_12.children("div.menu-line")._outerHeight(_17);
_14+=";width:"+el.style.width+";height:"+el.style.height;
_12.attr("style",_14);
};
function _10(_18,_19){
var _1a=$.data(_18,"menu");
_19.unbind(".menu").bind("mouseenter.menu",function(){
if(_1a.timer){
clearTimeout(_1a.timer);
_1a.timer=null;
}
}).bind("mouseleave.menu",function(){
if(_1a.options.hideOnUnhover){
_1a.timer=setTimeout(function(){
_1b(_18);
},100);
}
});
};
function _e(_1c,_1d){
if(!_1d.hasClass("menu-item")){
return;
}
_1d.unbind(".menu");
_1d.bind("click.menu",function(){
if($(this).hasClass("menu-item-disabled")){
return;
}
if(!this.submenu){
_1b(_1c);
var _1e=$(this).attr("href");
if(_1e){
location.href=_1e;
}
}
var _1f=$(_1c).menu("getItem",this);
$.data(_1c,"menu").options.onClick.call(_1c,_1f);
}).bind("mouseenter.menu",function(e){
_1d.siblings().each(function(){
if(this.submenu){
_22(this.submenu);
}
$(this).removeClass("menu-active");
});
_1d.addClass("menu-active");
if($(this).hasClass("menu-item-disabled")){
_1d.addClass("menu-active-disabled");
return;
}
var _20=_1d[0].submenu;
if(_20){
$(_1c).menu("show",{menu:_20,parent:_1d});
}
}).bind("mouseleave.menu",function(e){
_1d.removeClass("menu-active menu-active-disabled");
var _21=_1d[0].submenu;
if(_21){
if(e.pageX>=parseInt(_21.css("left"))){
_1d.addClass("menu-active");
}else{
_22(_21);
}
}else{
_1d.removeClass("menu-active");
}
});
};
function _1b(_23){
var _24=$.data(_23,"menu");
if(_24){
if($(_23).is(":visible")){
_22($(_23));
_24.options.onHide.call(_23);
}
}
return false;
};
function _25(_26,_27){
var _28,top;
_27=_27||{};
var _29=$(_27.menu||_26);
if(_29.hasClass("menu-top")){
var _2a=$.data(_26,"menu").options;
$.extend(_2a,_27);
_28=_2a.left;
top=_2a.top;
if(_2a.alignTo){
var at=$(_2a.alignTo);
_28=at.offset().left;
top=at.offset().top+at._outerHeight();
if(_2a.align=="right"){
_28+=at.outerWidth()-_29.outerWidth();
}
}
if(_28+_29.outerWidth()>$(window)._outerWidth()+$(document)._scrollLeft()){
_28=$(window)._outerWidth()+$(document).scrollLeft()-_29.outerWidth()-5;
}
if(_28<0){
_28=0;
}
if(top+_29.outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=$(window)._outerHeight()+$(document).scrollTop()-_29.outerHeight()-5;
}
}else{
var _2b=_27.parent;
_28=_2b.offset().left+_2b.outerWidth()-2;
if(_28+_29.outerWidth()+5>$(window)._outerWidth()+$(document).scrollLeft()){
_28=_2b.offset().left-_29.outerWidth()+2;
}
var top=_2b.offset().top-3;
if(top+_29.outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=$(window)._outerHeight()+$(document).scrollTop()-_29.outerHeight()-5;
}
}
_29.css({left:_28,top:top});
_29.show(0,function(){
if(!_29[0].shadow){
_29[0].shadow=$("<div class=\"menu-shadow\"></div>").insertAfter(_29);
}
_29[0].shadow.css({display:"block",zIndex:$.fn.menu.defaults.zIndex++,left:_29.css("left"),top:_29.css("top"),width:_29.outerWidth(),height:_29.outerHeight()});
_29.css("z-index",$.fn.menu.defaults.zIndex++);
if(_29.hasClass("menu-top")){
$.data(_29[0],"menu").options.onShow.call(_29[0]);
}
});
};
function _22(_2c){
if(!_2c){
return;
}
_2d(_2c);
_2c.find("div.menu-item").each(function(){
if(this.submenu){
_22(this.submenu);
}
$(this).removeClass("menu-active");
});
function _2d(m){
m.stop(true,true);
if(m[0].shadow){
m[0].shadow.hide();
}
m.hide();
};
};
function _2e(_2f,_30){
var _31=null;
var tmp=$("<div></div>");
function _32(_33){
_33.children("div.menu-item").each(function(){
var _34=$(_2f).menu("getItem",this);
var s=tmp.empty().html(_34.text).text();
if(_30==$.trim(s)){
_31=_34;
}else{
if(this.submenu&&!_31){
_32(this.submenu);
}
}
});
};
_32($(_2f));
tmp.remove();
return _31;
};
function _d(_35,_36,_37){
var t=$(_36);
if(!t.hasClass("menu-item")){
return;
}
if(_37){
t.addClass("menu-item-disabled");
if(_36.onclick){
_36.onclick1=_36.onclick;
_36.onclick=null;
}
}else{
t.removeClass("menu-item-disabled");
if(_36.onclick1){
_36.onclick=_36.onclick1;
_36.onclick1=null;
}
}
};
function _38(_39,_3a){
var _3b=$(_39);
if(_3a.parent){
if(!_3a.parent.submenu){
var _3c=$("<div class=\"menu\"><div class=\"menu-line\"></div></div>").appendTo("body");
_3c.hide();
_3a.parent.submenu=_3c;
$("<div class=\"menu-rightarrow\"></div>").appendTo(_3a.parent);
}
_3b=_3a.parent.submenu;
}
if(_3a.separator){
var _3d=$("<div class=\"menu-sep\"></div>").appendTo(_3b);
}else{
var _3d=$("<div class=\"menu-item\"></div>").appendTo(_3b);
$("<div class=\"menu-text\"></div>").html(_3a.text).appendTo(_3d);
}
if(_3a.iconCls){
$("<div class=\"menu-icon\"></div>").addClass(_3a.iconCls).appendTo(_3d);
}
if(_3a.id){
_3d.attr("id",_3a.id);
}
if(_3a.name){
_3d[0].itemName=_3a.name;
}
if(_3a.href){
_3d[0].itemHref=_3a.href;
}
if(_3a.onclick){
if(typeof _3a.onclick=="string"){
_3d.attr("onclick",_3a.onclick);
}else{
_3d[0].onclick=eval(_3a.onclick);
}
}
if(_3a.handler){
_3d[0].onclick=eval(_3a.handler);
}
if(_3a.disabled){
_d(_39,_3d[0],true);
}
_e(_39,_3d);
_10(_39,_3b);
_f(_39,_3b);
};
function _3e(_3f,_40){
function _41(el){
if(el.submenu){
el.submenu.children("div.menu-item").each(function(){
_41(this);
});
var _42=el.submenu[0].shadow;
if(_42){
_42.remove();
}
el.submenu.remove();
}
$(el).remove();
};
_41(_40);
};
function _43(_44){
$(_44).children("div.menu-item").each(function(){
_3e(_44,this);
});
if(_44.shadow){
_44.shadow.remove();
}
$(_44).remove();
};
$.fn.menu=function(_45,_46){
if(typeof _45=="string"){
return $.fn.menu.methods[_45](this,_46);
}
_45=_45||{};
return this.each(function(){
var _47=$.data(this,"menu");
if(_47){
$.extend(_47.options,_45);
}else{
_47=$.data(this,"menu",{options:$.extend({},$.fn.menu.defaults,$.fn.menu.parseOptions(this),_45)});
_1(this);
}
$(this).css({left:_47.options.left,top:_47.options.top});
});
};
$.fn.menu.methods={options:function(jq){
return $.data(jq[0],"menu").options;
},show:function(jq,pos){
return jq.each(function(){
_25(this,pos);
});
},hide:function(jq){
return jq.each(function(){
_1b(this);
});
},destroy:function(jq){
return jq.each(function(){
_43(this);
});
},setText:function(jq,_48){
return jq.each(function(){
$(_48.target).children("div.menu-text").html(_48.text);
});
},setIcon:function(jq,_49){
return jq.each(function(){
$(_49.target).children("div.menu-icon").remove();
if(_49.iconCls){
$("<div class=\"menu-icon\"></div>").addClass(_49.iconCls).appendTo(_49.target);
}
});
},getItem:function(jq,_4a){
var t=$(_4a);
var _4b={target:_4a,id:t.attr("id"),text:$.trim(t.children("div.menu-text").html()),disabled:t.hasClass("menu-item-disabled"),name:_4a.itemName,href:_4a.itemHref,onclick:_4a.onclick};
var _4c=t.children("div.menu-icon");
if(_4c.length){
var cc=[];
var aa=_4c.attr("class").split(" ");
for(var i=0;i<aa.length;i++){
if(aa[i]!="menu-icon"){
cc.push(aa[i]);
}
}
_4b.iconCls=cc.join(" ");
}
return _4b;
},findItem:function(jq,_4d){
return _2e(jq[0],_4d);
},appendItem:function(jq,_4e){
return jq.each(function(){
_38(this,_4e);
});
},removeItem:function(jq,_4f){
return jq.each(function(){
_3e(this,_4f);
});
},enableItem:function(jq,_50){
return jq.each(function(){
_d(this,_50,false);
});
},disableItem:function(jq,_51){
return jq.each(function(){
_d(this,_51,true);
});
}};
$.fn.menu.parseOptions=function(_52){
return $.extend({},$.parser.parseOptions(_52,["left","top",{minWidth:"number",hideOnUnhover:"boolean"}]));
};
$.fn.menu.defaults={zIndex:110000,left:0,top:0,alignTo:null,align:"left",minWidth:120,hideOnUnhover:true,onShow:function(){
},onHide:function(){
},onClick:function(_53){
}};
})(jQuery);

