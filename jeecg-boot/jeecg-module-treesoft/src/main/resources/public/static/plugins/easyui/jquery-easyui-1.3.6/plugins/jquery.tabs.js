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
var _3=$.data(_2,"tabs").options;
if(_3.tabPosition=="left"||_3.tabPosition=="right"||!_3.showHeader){
return;
}
var _4=$(_2).children("div.tabs-header");
var _5=_4.children("div.tabs-tool");
var _6=_4.children("div.tabs-scroller-left");
var _7=_4.children("div.tabs-scroller-right");
var _8=_4.children("div.tabs-wrap");
var _9=_4.outerHeight();
if(_3.plain){
_9-=_9-_4.height();
}
_5._outerHeight(_9);
var _a=0;
$("ul.tabs li",_4).each(function(){
_a+=$(this).outerWidth(true);
});
var _b=_4.width()-_5._outerWidth();
if(_a>_b){
_6.add(_7).show()._outerHeight(_9);
if(_3.toolPosition=="left"){
_5.css({left:_6.outerWidth(),right:""});
_8.css({marginLeft:_6.outerWidth()+_5._outerWidth(),marginRight:_7._outerWidth(),width:_b-_6.outerWidth()-_7.outerWidth()});
}else{
_5.css({left:"",right:_7.outerWidth()});
_8.css({marginLeft:_6.outerWidth(),marginRight:_7.outerWidth()+_5._outerWidth(),width:_b-_6.outerWidth()-_7.outerWidth()});
}
}else{
_6.add(_7).hide();
if(_3.toolPosition=="left"){
_5.css({left:0,right:""});
_8.css({marginLeft:_5._outerWidth(),marginRight:0,width:_b});
}else{
_5.css({left:"",right:0});
_8.css({marginLeft:0,marginRight:_5._outerWidth(),width:_b});
}
}
};
function _c(_d){
var _e=$.data(_d,"tabs").options;
var _f=$(_d).children("div.tabs-header");
if(_e.tools){
if(typeof _e.tools=="string"){
$(_e.tools).addClass("tabs-tool").appendTo(_f);
$(_e.tools).show();
}else{
_f.children("div.tabs-tool").remove();
var _10=$("<div class=\"tabs-tool\"><table cellspacing=\"0\" cellpadding=\"0\" style=\"height:100%\"><tr></tr></table></div>").appendTo(_f);
var tr=_10.find("tr");
for(var i=0;i<_e.tools.length;i++){
var td=$("<td></td>").appendTo(tr);
var _11=$("<a href=\"javascript:void(0);\"></a>").appendTo(td);
_11[0].onclick=eval(_e.tools[i].handler||function(){
});
_11.linkbutton($.extend({},_e.tools[i],{plain:true}));
}
}
}else{
_f.children("div.tabs-tool").remove();
}
};
function _12(_13){
var _14=$.data(_13,"tabs");
var _15=_14.options;
var cc=$(_13);
_15.fit?$.extend(_15,cc._fit()):cc._fit(false);
cc.width(_15.width).height(_15.height);
var _16=$(_13).children("div.tabs-header");
var _17=$(_13).children("div.tabs-panels");
var _18=_16.find("div.tabs-wrap");
var ul=_18.find(".tabs");
for(var i=0;i<_14.tabs.length;i++){
var _19=_14.tabs[i].panel("options");
var p_t=_19.tab.find("a.tabs-inner");
var _1a=parseInt(_19.tabWidth||_15.tabWidth)||undefined;
if(_1a){
p_t._outerWidth(_1a);
}else{
p_t.css("width","");
}
p_t._outerHeight(_15.tabHeight);
p_t.css("lineHeight",p_t.height()+"px");
}
if(_15.tabPosition=="left"||_15.tabPosition=="right"){
_16._outerWidth(_15.showHeader?_15.headerWidth:0);
_17._outerWidth(cc.width()-_16.outerWidth());
_16.add(_17)._outerHeight(_15.height);
_18._outerWidth(_16.width());
ul._outerWidth(_18.width()).css("height","");
}else{
var lrt=_16.children("div.tabs-scroller-left,div.tabs-scroller-right,div.tabs-tool");
_16._outerWidth(_15.width).css("height","");
if(_15.showHeader){
_16.css("background-color","");
_18.css("height","");
lrt.show();
}else{
_16.css("background-color","transparent");
_16._outerHeight(0);
_18._outerHeight(0);
lrt.hide();
}
ul._outerHeight(_15.tabHeight).css("width","");
_1(_13);
var _1b=_15.height;
if(!isNaN(_1b)){
_17._outerHeight(_1b-_16.outerHeight());
}else{
_17.height("auto");
}
var _1a=_15.width;
if(!isNaN(_1a)){
_17._outerWidth(_1a);
}else{
_17.width("auto");
}
}
};
function _1c(_1d){
var _1e=$.data(_1d,"tabs").options;
var tab=_1f(_1d);
if(tab){
var _20=$(_1d).children("div.tabs-panels");
var _21=_1e.width=="auto"?"auto":_20.width();
var _22=_1e.height=="auto"?"auto":_20.height();
tab.panel("resize",{width:_21,height:_22});
}
};
function _23(_24){
var _25=$.data(_24,"tabs").tabs;
var cc=$(_24);
cc.addClass("tabs-container");
var pp=$("<div class=\"tabs-panels\"></div>").insertBefore(cc);
cc.children("div").each(function(){
pp[0].appendChild(this);
});
cc[0].appendChild(pp[0]);
$("<div class=\"tabs-header\">"+"<div class=\"tabs-scroller-left\"></div>"+"<div class=\"tabs-scroller-right\"></div>"+"<div class=\"tabs-wrap\">"+"<ul class=\"tabs\"></ul>"+"</div>"+"</div>").prependTo(_24);
cc.children("div.tabs-panels").children("div").each(function(i){
var _26=$.extend({},$.parser.parseOptions(this),{selected:($(this).attr("selected")?true:undefined)});
var pp=$(this);
_25.push(pp);
_36(_24,pp,_26);
});
cc.children("div.tabs-header").find(".tabs-scroller-left, .tabs-scroller-right").hover(function(){
$(this).addClass("tabs-scroller-over");
},function(){
$(this).removeClass("tabs-scroller-over");
});
cc.bind("_resize",function(e,_27){
var _28=$.data(_24,"tabs").options;
if(_28.fit==true||_27){
_12(_24);
_1c(_24);
}
return false;
});
};
function _29(_2a){
var _2b=$.data(_2a,"tabs");
var _2c=_2b.options;
$(_2a).children("div.tabs-header").unbind().bind("click",function(e){
if($(e.target).hasClass("tabs-scroller-left")){
$(_2a).tabs("scrollBy",-_2c.scrollIncrement);
}else{
if($(e.target).hasClass("tabs-scroller-right")){
$(_2a).tabs("scrollBy",_2c.scrollIncrement);
}else{
var li=$(e.target).closest("li");
if(li.hasClass("tabs-disabled")){
return;
}
var a=$(e.target).closest("a.tabs-close");
if(a.length){
_4c(_2a,_2d(li));
}else{
if(li.length){
var _2e=_2d(li);
var _2f=_2b.tabs[_2e].panel("options");
if(_2f.collapsible){
_2f.closed?_41(_2a,_2e):_6b(_2a,_2e);
}else{
_41(_2a,_2e);
}
}
}
}
}
}).bind("contextmenu",function(e){
var li=$(e.target).closest("li");
if(li.hasClass("tabs-disabled")){
return;
}
if(li.length){
_2c.onContextMenu.call(_2a,e,li.find("span.tabs-title").html(),_2d(li));
}
});
function _2d(li){
var _30=0;
li.parent().children("li").each(function(i){
if(li[0]==this){
_30=i;
return false;
}
});
return _30;
};
};
function _31(_32){
var _33=$.data(_32,"tabs").options;
var _34=$(_32).children("div.tabs-header");
var _35=$(_32).children("div.tabs-panels");
_34.removeClass("tabs-header-top tabs-header-bottom tabs-header-left tabs-header-right");
_35.removeClass("tabs-panels-top tabs-panels-bottom tabs-panels-left tabs-panels-right");
if(_33.tabPosition=="top"){
_34.insertBefore(_35);
}else{
if(_33.tabPosition=="bottom"){
_34.insertAfter(_35);
_34.addClass("tabs-header-bottom");
_35.addClass("tabs-panels-top");
}else{
if(_33.tabPosition=="left"){
_34.addClass("tabs-header-left");
_35.addClass("tabs-panels-right");
}else{
if(_33.tabPosition=="right"){
_34.addClass("tabs-header-right");
_35.addClass("tabs-panels-left");
}
}
}
}
if(_33.plain==true){
_34.addClass("tabs-header-plain");
}else{
_34.removeClass("tabs-header-plain");
}
if(_33.border==true){
_34.removeClass("tabs-header-noborder");
_35.removeClass("tabs-panels-noborder");
}else{
_34.addClass("tabs-header-noborder");
_35.addClass("tabs-panels-noborder");
}
};
function _36(_37,pp,_38){
var _39=$.data(_37,"tabs");
_38=_38||{};
pp.panel($.extend({},_38,{border:false,noheader:true,closed:true,doSize:false,iconCls:(_38.icon?_38.icon:undefined),onLoad:function(){
if(_38.onLoad){
_38.onLoad.call(this,arguments);
}
_39.options.onLoad.call(_37,$(this));
}}));
var _3a=pp.panel("options");
var _3b=$(_37).children("div.tabs-header").find("ul.tabs");
_3a.tab=$("<li></li>").appendTo(_3b);
_3a.tab.append("<a href=\"javascript:void(0)\" class=\"tabs-inner\">"+"<span class=\"tabs-title\"></span>"+"<span class=\"tabs-icon\"></span>"+"</a>");
$(_37).tabs("update",{tab:pp,options:_3a});
};
function _3c(_3d,_3e){
var _3f=$.data(_3d,"tabs").options;
var _40=$.data(_3d,"tabs").tabs;
if(_3e.selected==undefined){
_3e.selected=true;
}
var pp=$("<div></div>").appendTo($(_3d).children("div.tabs-panels"));
_40.push(pp);
_36(_3d,pp,_3e);
_3f.onAdd.call(_3d,_3e.title,_40.length-1);
_12(_3d);
if(_3e.selected){
_41(_3d,_40.length-1);
}
};
function _42(_43,_44){
var _45=$.data(_43,"tabs").selectHis;
var pp=_44.tab;
var _46=pp.panel("options").title;
pp.panel($.extend({},_44.options,{iconCls:(_44.options.icon?_44.options.icon:undefined)}));
var _47=pp.panel("options");
var tab=_47.tab;
var _48=tab.find("span.tabs-title");
var _49=tab.find("span.tabs-icon");
_48.html(_47.title);
_49.attr("class","tabs-icon");
tab.find("a.tabs-close").remove();
if(_47.closable){
_48.addClass("tabs-closable");
$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>").appendTo(tab);
}else{
_48.removeClass("tabs-closable");
}
if(_47.iconCls){
_48.addClass("tabs-with-icon");
_49.addClass(_47.iconCls);
}else{
_48.removeClass("tabs-with-icon");
}
if(_46!=_47.title){
for(var i=0;i<_45.length;i++){
if(_45[i]==_46){
_45[i]=_47.title;
}
}
}
tab.find("span.tabs-p-tool").remove();
if(_47.tools){
var _4a=$("<span class=\"tabs-p-tool\"></span>").insertAfter(tab.find("a.tabs-inner"));
if($.isArray(_47.tools)){
for(var i=0;i<_47.tools.length;i++){
var t=$("<a href=\"javascript:void(0)\"></a>").appendTo(_4a);
t.addClass(_47.tools[i].iconCls);
if(_47.tools[i].handler){
t.bind("click",{handler:_47.tools[i].handler},function(e){
if($(this).parents("li").hasClass("tabs-disabled")){
return;
}
e.data.handler.call(this);
});
}
}
}else{
$(_47.tools).children().appendTo(_4a);
}
var pr=_4a.children().length*12;
if(_47.closable){
pr+=8;
}else{
pr-=3;
_4a.css("right","5px");
}
_48.css("padding-right",pr+"px");
}
_12(_43);
$.data(_43,"tabs").options.onUpdate.call(_43,_47.title,_4b(_43,pp));
};
function _4c(_4d,_4e){
var _4f=$.data(_4d,"tabs").options;
var _50=$.data(_4d,"tabs").tabs;
var _51=$.data(_4d,"tabs").selectHis;
if(!_52(_4d,_4e)){
return;
}
var tab=_53(_4d,_4e);
var _54=tab.panel("options").title;
var _55=_4b(_4d,tab);
if(_4f.onBeforeClose.call(_4d,_54,_55)==false){
return;
}
var tab=_53(_4d,_4e,true);
tab.panel("options").tab.remove();
tab.panel("destroy");
_4f.onClose.call(_4d,_54,_55);
_12(_4d);
for(var i=0;i<_51.length;i++){
if(_51[i]==_54){
_51.splice(i,1);
i--;
}
}
var _56=_51.pop();
if(_56){
_41(_4d,_56);
}else{
if(_50.length){
_41(_4d,0);
}
}
};
function _53(_57,_58,_59){
var _5a=$.data(_57,"tabs").tabs;
if(typeof _58=="number"){
if(_58<0||_58>=_5a.length){
return null;
}else{
var tab=_5a[_58];
if(_59){
_5a.splice(_58,1);
}
return tab;
}
}
for(var i=0;i<_5a.length;i++){
var tab=_5a[i];
if(tab.panel("options").title==_58){
if(_59){
_5a.splice(i,1);
}
return tab;
}
}
return null;
};
function _4b(_5b,tab){
var _5c=$.data(_5b,"tabs").tabs;
for(var i=0;i<_5c.length;i++){
if(_5c[i][0]==$(tab)[0]){
return i;
}
}
return -1;
};
function _1f(_5d){
var _5e=$.data(_5d,"tabs").tabs;
for(var i=0;i<_5e.length;i++){
var tab=_5e[i];
if(tab.panel("options").closed==false){
return tab;
}
}
return null;
};
function _5f(_60){
var _61=$.data(_60,"tabs");
var _62=_61.tabs;
for(var i=0;i<_62.length;i++){
if(_62[i].panel("options").selected){
_41(_60,i);
return;
}
}
_41(_60,_61.options.selected);
};
function _41(_63,_64){
var _65=$.data(_63,"tabs");
var _66=_65.options;
var _67=_65.tabs;
var _68=_65.selectHis;
if(_67.length==0){
return;
}
var _69=_53(_63,_64);
if(!_69){
return;
}
var _6a=_1f(_63);
if(_6a){
if(_69[0]==_6a[0]){
_1c(_63);
return;
}
_6b(_63,_4b(_63,_6a));
if(!_6a.panel("options").closed){
return;
}
}
_69.panel("open");
var _6c=_69.panel("options").title;
_68.push(_6c);
var tab=_69.panel("options").tab;
tab.addClass("tabs-selected");
var _6d=$(_63).find(">div.tabs-header>div.tabs-wrap");
var _6e=tab.position().left;
var _6f=_6e+tab.outerWidth();
if(_6e<0||_6f>_6d.width()){
var _70=_6e-(_6d.width()-tab.width())/2;
$(_63).tabs("scrollBy",_70);
}else{
$(_63).tabs("scrollBy",0);
}
_1c(_63);
_66.onSelect.call(_63,_6c,_4b(_63,_69));
};
function _6b(_71,_72){
var _73=$.data(_71,"tabs");
var p=_53(_71,_72);
if(p){
var _74=p.panel("options");
if(!_74.closed){
p.panel("close");
if(_74.closed){
_74.tab.removeClass("tabs-selected");
_73.options.onUnselect.call(_71,_74.title,_4b(_71,p));
}
}
}
};
function _52(_75,_76){
return _53(_75,_76)!=null;
};
function _77(_78,_79){
var _7a=$.data(_78,"tabs").options;
_7a.showHeader=_79;
$(_78).tabs("resize");
};
$.fn.tabs=function(_7b,_7c){
if(typeof _7b=="string"){
return $.fn.tabs.methods[_7b](this,_7c);
}
_7b=_7b||{};
return this.each(function(){
var _7d=$.data(this,"tabs");
var _7e;
if(_7d){
_7e=$.extend(_7d.options,_7b);
_7d.options=_7e;
}else{
$.data(this,"tabs",{options:$.extend({},$.fn.tabs.defaults,$.fn.tabs.parseOptions(this),_7b),tabs:[],selectHis:[]});
_23(this);
}
_c(this);
_31(this);
_12(this);
_29(this);
_5f(this);
});
};
$.fn.tabs.methods={options:function(jq){
var cc=jq[0];
var _7f=$.data(cc,"tabs").options;
var s=_1f(cc);
_7f.selected=s?_4b(cc,s):-1;
return _7f;
},tabs:function(jq){
return $.data(jq[0],"tabs").tabs;
},resize:function(jq){
return jq.each(function(){
_12(this);
_1c(this);
});
},add:function(jq,_80){
return jq.each(function(){
_3c(this,_80);
});
},close:function(jq,_81){
return jq.each(function(){
_4c(this,_81);
});
},getTab:function(jq,_82){
return _53(jq[0],_82);
},getTabIndex:function(jq,tab){
return _4b(jq[0],tab);
},getSelected:function(jq){
return _1f(jq[0]);
},select:function(jq,_83){
return jq.each(function(){
_41(this,_83);
});
},unselect:function(jq,_84){
return jq.each(function(){
_6b(this,_84);
});
},exists:function(jq,_85){
return _52(jq[0],_85);
},update:function(jq,_86){
return jq.each(function(){
_42(this,_86);
});
},enableTab:function(jq,_87){
return jq.each(function(){
$(this).tabs("getTab",_87).panel("options").tab.removeClass("tabs-disabled");
});
},disableTab:function(jq,_88){
return jq.each(function(){
$(this).tabs("getTab",_88).panel("options").tab.addClass("tabs-disabled");
});
},showHeader:function(jq){
return jq.each(function(){
_77(this,true);
});
},hideHeader:function(jq){
return jq.each(function(){
_77(this,false);
});
},scrollBy:function(jq,_89){
return jq.each(function(){
var _8a=$(this).tabs("options");
var _8b=$(this).find(">div.tabs-header>div.tabs-wrap");
var pos=Math.min(_8b._scrollLeft()+_89,_8c());
_8b.animate({scrollLeft:pos},_8a.scrollDuration);
function _8c(){
var w=0;
var ul=_8b.children("ul");
ul.children("li").each(function(){
w+=$(this).outerWidth(true);
});
return w-_8b.width()+(ul.outerWidth()-ul.width());
};
});
}};
$.fn.tabs.parseOptions=function(_8d){
return $.extend({},$.parser.parseOptions(_8d,["width","height","tools","toolPosition","tabPosition",{fit:"boolean",border:"boolean",plain:"boolean",headerWidth:"number",tabWidth:"number",tabHeight:"number",selected:"number",showHeader:"boolean"}]));
};
$.fn.tabs.defaults={width:"auto",height:"auto",headerWidth:150,tabWidth:"auto",tabHeight:27,selected:0,showHeader:true,plain:false,fit:false,border:true,tools:null,toolPosition:"right",tabPosition:"top",scrollIncrement:100,scrollDuration:400,onLoad:function(_8e){
},onSelect:function(_8f,_90){
},onUnselect:function(_91,_92){
},onBeforeClose:function(_93,_94){
},onClose:function(_95,_96){
},onAdd:function(_97,_98){
},onUpdate:function(_99,_9a){
},onContextMenu:function(e,_9b,_9c){
}};
})(jQuery);

