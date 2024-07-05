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
var _1=false;
function _2(_3){
var _4=$.data(_3,"layout");
var _5=_4.options;
var _6=_4.panels;
var cc=$(_3);
if(_3.tagName=="BODY"){
cc._fit();
}else{
_5.fit?cc.css(cc._fit()):cc._fit(false);
}
var _7={top:0,left:0,width:cc.width(),height:cc.height()};
_8(_9(_6.expandNorth)?_6.expandNorth:_6.north,"n");
_8(_9(_6.expandSouth)?_6.expandSouth:_6.south,"s");
_a(_9(_6.expandEast)?_6.expandEast:_6.east,"e");
_a(_9(_6.expandWest)?_6.expandWest:_6.west,"w");
_6.center.panel("resize",_7);
function _b(pp){
var _c=pp.panel("options");
return Math.min(Math.max(_c.height,_c.minHeight),_c.maxHeight);
};
function _d(pp){
var _e=pp.panel("options");
return Math.min(Math.max(_e.width,_e.minWidth),_e.maxWidth);
};
function _8(pp,_f){
if(!pp.length||!_9(pp)){
return;
}
var _10=pp.panel("options");
var _11=_b(pp);
pp.panel("resize",{width:cc.width(),height:_11,left:0,top:(_f=="n"?0:cc.height()-_11)});
_7.height-=_11;
if(_f=="n"){
_7.top+=_11;
if(!_10.split&&_10.border){
_7.top--;
}
}
if(!_10.split&&_10.border){
_7.height++;
}
};
function _a(pp,_12){
if(!pp.length||!_9(pp)){
return;
}
var _13=pp.panel("options");
var _14=_d(pp);
pp.panel("resize",{width:_14,height:_7.height,left:(_12=="e"?cc.width()-_14:0),top:_7.top});
_7.width-=_14;
if(_12=="w"){
_7.left+=_14;
if(!_13.split&&_13.border){
_7.left--;
}
}
if(!_13.split&&_13.border){
_7.width++;
}
};
};
function _15(_16){
var cc=$(_16);
cc.addClass("layout");
function _17(cc){
cc.children("div").each(function(){
var _18=$.fn.layout.parsePanelOptions(this);
if("north,south,east,west,center".indexOf(_18.region)>=0){
_1b(_16,_18,this);
}
});
};
cc.children("form").length?_17(cc.children("form")):_17(cc);
cc.append("<div class=\"layout-split-proxy-h\"></div><div class=\"layout-split-proxy-v\"></div>");
cc.bind("_resize",function(e,_19){
var _1a=$.data(_16,"layout").options;
if(_1a.fit==true||_19){
_2(_16);
}
return false;
});
};
function _1b(_1c,_1d,el){
_1d.region=_1d.region||"center";
var _1e=$.data(_1c,"layout").panels;
var cc=$(_1c);
var dir=_1d.region;
if(_1e[dir].length){
return;
}
var pp=$(el);
if(!pp.length){
pp=$("<div></div>").appendTo(cc);
}
var _1f=$.extend({},$.fn.layout.paneldefaults,{width:(pp.length?parseInt(pp[0].style.width)||pp.outerWidth():"auto"),height:(pp.length?parseInt(pp[0].style.height)||pp.outerHeight():"auto"),doSize:false,collapsible:true,cls:("layout-panel layout-panel-"+dir),bodyCls:"layout-body",onOpen:function(){
var _20=$(this).panel("header").children("div.panel-tool");
_20.children("a.panel-tool-collapse").hide();
var _21={north:"up",south:"down",east:"right",west:"left"};
if(!_21[dir]){
return;
}
var _22="layout-button-"+_21[dir];
var t=_20.children("a."+_22);
if(!t.length){
t=$("<a href=\"javascript:void(0)\"></a>").addClass(_22).appendTo(_20);
t.bind("click",{dir:dir},function(e){
_2f(_1c,e.data.dir);
return false;
});
}
$(this).panel("options").collapsible?t.show():t.hide();
}},_1d);
pp.panel(_1f);
_1e[dir]=pp;
if(pp.panel("options").split){
var _23=pp.panel("panel");
_23.addClass("layout-split-"+dir);
var _24="";
if(dir=="north"){
_24="s";
}
if(dir=="south"){
_24="n";
}
if(dir=="east"){
_24="w";
}
if(dir=="west"){
_24="e";
}
_23.resizable($.extend({},{handles:_24,onStartResize:function(e){
_1=true;
if(dir=="north"||dir=="south"){
var _25=$(">div.layout-split-proxy-v",_1c);
}else{
var _25=$(">div.layout-split-proxy-h",_1c);
}
var top=0,_26=0,_27=0,_28=0;
var pos={display:"block"};
if(dir=="north"){
pos.top=parseInt(_23.css("top"))+_23.outerHeight()-_25.height();
pos.left=parseInt(_23.css("left"));
pos.width=_23.outerWidth();
pos.height=_25.height();
}else{
if(dir=="south"){
pos.top=parseInt(_23.css("top"));
pos.left=parseInt(_23.css("left"));
pos.width=_23.outerWidth();
pos.height=_25.height();
}else{
if(dir=="east"){
pos.top=parseInt(_23.css("top"))||0;
pos.left=parseInt(_23.css("left"))||0;
pos.width=_25.width();
pos.height=_23.outerHeight();
}else{
if(dir=="west"){
pos.top=parseInt(_23.css("top"))||0;
pos.left=_23.outerWidth()-_25.width();
pos.width=_25.width();
pos.height=_23.outerHeight();
}
}
}
}
_25.css(pos);
$("<div class=\"layout-mask\"></div>").css({left:0,top:0,width:cc.width(),height:cc.height()}).appendTo(cc);
},onResize:function(e){
if(dir=="north"||dir=="south"){
var _29=$(">div.layout-split-proxy-v",_1c);
_29.css("top",e.pageY-$(_1c).offset().top-_29.height()/2);
}else{
var _29=$(">div.layout-split-proxy-h",_1c);
_29.css("left",e.pageX-$(_1c).offset().left-_29.width()/2);
}
return false;
},onStopResize:function(e){
cc.children("div.layout-split-proxy-v,div.layout-split-proxy-h").hide();
pp.panel("resize",e.data);
_2(_1c);
_1=false;
cc.find(">div.layout-mask").remove();
}},_1d));
}
};
function _2a(_2b,_2c){
var _2d=$.data(_2b,"layout").panels;
if(_2d[_2c].length){
_2d[_2c].panel("destroy");
_2d[_2c]=$();
var _2e="expand"+_2c.substring(0,1).toUpperCase()+_2c.substring(1);
if(_2d[_2e]){
_2d[_2e].panel("destroy");
_2d[_2e]=undefined;
}
}
};
function _2f(_30,_31,_32){
if(_32==undefined){
_32="normal";
}
var _33=$.data(_30,"layout").panels;
var p=_33[_31];
var _34=p.panel("options");
if(_34.onBeforeCollapse.call(p)==false){
return;
}
var _35="expand"+_31.substring(0,1).toUpperCase()+_31.substring(1);
if(!_33[_35]){
_33[_35]=_36(_31);
_33[_35].panel("panel").bind("click",function(){
var _37=_38();
p.panel("expand",false).panel("open").panel("resize",_37.collapse);
p.panel("panel").animate(_37.expand,function(){
$(this).unbind(".layout").bind("mouseleave.layout",{region:_31},function(e){
if(_1==true){
return;
}
_2f(_30,e.data.region);
});
});
return false;
});
}
var _39=_38();
if(!_9(_33[_35])){
_33.center.panel("resize",_39.resizeC);
}
p.panel("panel").animate(_39.collapse,_32,function(){
p.panel("collapse",false).panel("close");
_33[_35].panel("open").panel("resize",_39.expandP);
$(this).unbind(".layout");
});
function _36(dir){
var _3a;
if(dir=="east"){
_3a="layout-button-left";
}else{
if(dir=="west"){
_3a="layout-button-right";
}else{
if(dir=="north"){
_3a="layout-button-down";
}else{
if(dir=="south"){
_3a="layout-button-up";
}
}
}
}
var p=$("<div></div>").appendTo(_30);
p.panel($.extend({},$.fn.layout.paneldefaults,{cls:("layout-expand layout-expand-"+dir),title:"&nbsp;",closed:true,minWidth:0,minHeight:0,doSize:false,tools:[{iconCls:_3a,handler:function(){
_3d(_30,_31);
return false;
}}]}));
p.panel("panel").hover(function(){
$(this).addClass("layout-expand-over");
},function(){
$(this).removeClass("layout-expand-over");
});
return p;
};
function _38(){
var cc=$(_30);
var _3b=_33.center.panel("options");
var _3c=_34.collapsedSize;
if(_31=="east"){
var ww=_3b.width+_34.width-_3c;
if(_34.split||!_34.border){
ww++;
}
return {resizeC:{width:ww},expand:{left:cc.width()-_34.width},expandP:{top:_3b.top,left:cc.width()-_3c,width:_3c,height:_3b.height},collapse:{left:cc.width(),top:_3b.top,height:_3b.height}};
}else{
if(_31=="west"){
var ww=_3b.width+_34.width-_3c;
if(_34.split||!_34.border){
ww++;
}
return {resizeC:{width:ww,left:_3c-1},expand:{left:0},expandP:{left:0,top:_3b.top,width:_3c,height:_3b.height},collapse:{left:-_34.width,top:_3b.top,height:_3b.height}};
}else{
if(_31=="north"){
var hh=_3b.height;
if(!_9(_33.expandNorth)){
hh+=_34.height-_3c+((_34.split||!_34.border)?1:0);
}
_33.east.add(_33.west).add(_33.expandEast).add(_33.expandWest).panel("resize",{top:_3c-1,height:hh});
return {resizeC:{top:_3c-1,height:hh},expand:{top:0},expandP:{top:0,left:0,width:cc.width(),height:_3c},collapse:{top:-_34.height,width:cc.width()}};
}else{
if(_31=="south"){
var hh=_3b.height;
if(!_9(_33.expandSouth)){
hh+=_34.height-_3c+((_34.split||!_34.border)?1:0);
}
_33.east.add(_33.west).add(_33.expandEast).add(_33.expandWest).panel("resize",{height:hh});
return {resizeC:{height:hh},expand:{top:cc.height()-_34.height},expandP:{top:cc.height()-_3c,left:0,width:cc.width(),height:_3c},collapse:{top:cc.height(),width:cc.width()}};
}
}
}
}
};
};
function _3d(_3e,_3f){
var _40=$.data(_3e,"layout").panels;
var p=_40[_3f];
var _41=p.panel("options");
if(_41.onBeforeExpand.call(p)==false){
return;
}
var _42=_43();
var _44="expand"+_3f.substring(0,1).toUpperCase()+_3f.substring(1);
if(_40[_44]){
_40[_44].panel("close");
p.panel("panel").stop(true,true);
p.panel("expand",false).panel("open").panel("resize",_42.collapse);
p.panel("panel").animate(_42.expand,function(){
_2(_3e);
});
}
function _43(){
var cc=$(_3e);
var _45=_40.center.panel("options");
if(_3f=="east"&&_40.expandEast){
return {collapse:{left:cc.width(),top:_45.top,height:_45.height},expand:{left:cc.width()-_40["east"].panel("options").width}};
}else{
if(_3f=="west"&&_40.expandWest){
return {collapse:{left:-_40["west"].panel("options").width,top:_45.top,height:_45.height},expand:{left:0}};
}else{
if(_3f=="north"&&_40.expandNorth){
return {collapse:{top:-_40["north"].panel("options").height,width:cc.width()},expand:{top:0}};
}else{
if(_3f=="south"&&_40.expandSouth){
return {collapse:{top:cc.height(),width:cc.width()},expand:{top:cc.height()-_40["south"].panel("options").height}};
}
}
}
}
};
};
function _9(pp){
if(!pp){
return false;
}
if(pp.length){
return pp.panel("panel").is(":visible");
}else{
return false;
}
};
function _46(_47){
var _48=$.data(_47,"layout").panels;
if(_48.east.length&&_48.east.panel("options").collapsed){
_2f(_47,"east",0);
}
if(_48.west.length&&_48.west.panel("options").collapsed){
_2f(_47,"west",0);
}
if(_48.north.length&&_48.north.panel("options").collapsed){
_2f(_47,"north",0);
}
if(_48.south.length&&_48.south.panel("options").collapsed){
_2f(_47,"south",0);
}
};
$.fn.layout=function(_49,_4a){
if(typeof _49=="string"){
return $.fn.layout.methods[_49](this,_4a);
}
_49=_49||{};
return this.each(function(){
var _4b=$.data(this,"layout");
if(_4b){
$.extend(_4b.options,_49);
}else{
var _4c=$.extend({},$.fn.layout.defaults,$.fn.layout.parseOptions(this),_49);
$.data(this,"layout",{options:_4c,panels:{center:$(),north:$(),south:$(),east:$(),west:$()}});
_15(this);
}
_2(this);
_46(this);
});
};
$.fn.layout.methods={resize:function(jq){
return jq.each(function(){
_2(this);
});
},panel:function(jq,_4d){
return $.data(jq[0],"layout").panels[_4d];
},collapse:function(jq,_4e){
return jq.each(function(){
_2f(this,_4e);
});
},expand:function(jq,_4f){
return jq.each(function(){
_3d(this,_4f);
});
},add:function(jq,_50){
return jq.each(function(){
_1b(this,_50);
_2(this);
if($(this).layout("panel",_50.region).panel("options").collapsed){
_2f(this,_50.region,0);
}
});
},remove:function(jq,_51){
return jq.each(function(){
_2a(this,_51);
_2(this);
});
}};
$.fn.layout.parseOptions=function(_52){
return $.extend({},$.parser.parseOptions(_52,[{fit:"boolean"}]));
};
$.fn.layout.defaults={fit:false};
$.fn.layout.parsePanelOptions=function(_53){
var t=$(_53);
return $.extend({},$.fn.panel.parseOptions(_53),$.parser.parseOptions(_53,["region",{split:"boolean",collpasedSize:"number",minWidth:"number",minHeight:"number",maxWidth:"number",maxHeight:"number"}]));
};
$.fn.layout.paneldefaults=$.extend({},$.fn.panel.defaults,{region:null,split:false,collapsedSize:28,minWidth:10,minHeight:10,maxWidth:10000,maxHeight:10000});
})(jQuery);

