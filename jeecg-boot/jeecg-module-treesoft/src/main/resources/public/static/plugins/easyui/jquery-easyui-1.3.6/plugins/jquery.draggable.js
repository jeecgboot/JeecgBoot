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
function _1(e){
var _2=$.data(e.data.target,"draggable");
var _3=_2.options;
var _4=_2.proxy;
var _5=e.data;
var _6=_5.startLeft+e.pageX-_5.startX;
var _7=_5.startTop+e.pageY-_5.startY;
if(_4){
if(_4.parent()[0]==document.body){
if(_3.deltaX!=null&&_3.deltaX!=undefined){
_6=e.pageX+_3.deltaX;
}else{
_6=e.pageX-e.data.offsetWidth;
}
if(_3.deltaY!=null&&_3.deltaY!=undefined){
_7=e.pageY+_3.deltaY;
}else{
_7=e.pageY-e.data.offsetHeight;
}
}else{
if(_3.deltaX!=null&&_3.deltaX!=undefined){
_6+=e.data.offsetWidth+_3.deltaX;
}
if(_3.deltaY!=null&&_3.deltaY!=undefined){
_7+=e.data.offsetHeight+_3.deltaY;
}
}
}
if(e.data.parent!=document.body){
_6+=$(e.data.parent).scrollLeft();
_7+=$(e.data.parent).scrollTop();
}
if(_3.axis=="h"){
_5.left=_6;
}else{
if(_3.axis=="v"){
_5.top=_7;
}else{
_5.left=_6;
_5.top=_7;
}
}
};
function _8(e){
var _9=$.data(e.data.target,"draggable");
var _a=_9.options;
var _b=_9.proxy;
if(!_b){
_b=$(e.data.target);
}
_b.css({left:e.data.left,top:e.data.top});
$("body").css("cursor",_a.cursor);
};
function _c(e){
$.fn.draggable.isDragging=true;
var _d=$.data(e.data.target,"draggable");
var _e=_d.options;
var _f=$(".droppable").filter(function(){
return e.data.target!=this;
}).filter(function(){
var _10=$.data(this,"droppable").options.accept;
if(_10){
return $(_10).filter(function(){
return this==e.data.target;
}).length>0;
}else{
return true;
}
});
_d.droppables=_f;
var _11=_d.proxy;
if(!_11){
if(_e.proxy){
if(_e.proxy=="clone"){
_11=$(e.data.target).clone().insertAfter(e.data.target);
}else{
_11=_e.proxy.call(e.data.target,e.data.target);
}
_d.proxy=_11;
}else{
_11=$(e.data.target);
}
}
_11.css("position","absolute");
_1(e);
_8(e);
_e.onStartDrag.call(e.data.target,e);
return false;
};
function _12(e){
var _13=$.data(e.data.target,"draggable");
_1(e);
if(_13.options.onDrag.call(e.data.target,e)!=false){
_8(e);
}
var _14=e.data.target;
_13.droppables.each(function(){
var _15=$(this);
if(_15.droppable("options").disabled){
return;
}
var p2=_15.offset();
if(e.pageX>p2.left&&e.pageX<p2.left+_15.outerWidth()&&e.pageY>p2.top&&e.pageY<p2.top+_15.outerHeight()){
if(!this.entered){
$(this).trigger("_dragenter",[_14]);
this.entered=true;
}
$(this).trigger("_dragover",[_14]);
}else{
if(this.entered){
$(this).trigger("_dragleave",[_14]);
this.entered=false;
}
}
});
return false;
};
function _16(e){
$.fn.draggable.isDragging=false;
_12(e);
var _17=$.data(e.data.target,"draggable");
var _18=_17.proxy;
var _19=_17.options;
if(_19.revert){
if(_1a()==true){
$(e.data.target).css({position:e.data.startPosition,left:e.data.startLeft,top:e.data.startTop});
}else{
if(_18){
var _1b,top;
if(_18.parent()[0]==document.body){
_1b=e.data.startX-e.data.offsetWidth;
top=e.data.startY-e.data.offsetHeight;
}else{
_1b=e.data.startLeft;
top=e.data.startTop;
}
_18.animate({left:_1b,top:top},function(){
_1c();
});
}else{
$(e.data.target).animate({left:e.data.startLeft,top:e.data.startTop},function(){
$(e.data.target).css("position",e.data.startPosition);
});
}
}
}else{
$(e.data.target).css({position:"absolute",left:e.data.left,top:e.data.top});
_1a();
}
_19.onStopDrag.call(e.data.target,e);
$(document).unbind(".draggable");
setTimeout(function(){
$("body").css("cursor","");
},100);
function _1c(){
if(_18){
_18.remove();
}
_17.proxy=null;
};
function _1a(){
var _1d=false;
_17.droppables.each(function(){
var _1e=$(this);
if(_1e.droppable("options").disabled){
return;
}
var p2=_1e.offset();
if(e.pageX>p2.left&&e.pageX<p2.left+_1e.outerWidth()&&e.pageY>p2.top&&e.pageY<p2.top+_1e.outerHeight()){
if(_19.revert){
$(e.data.target).css({position:e.data.startPosition,left:e.data.startLeft,top:e.data.startTop});
}
$(this).trigger("_drop",[e.data.target]);
_1c();
_1d=true;
this.entered=false;
return false;
}
});
if(!_1d&&!_19.revert){
_1c();
}
return _1d;
};
return false;
};
$.fn.draggable=function(_1f,_20){
if(typeof _1f=="string"){
return $.fn.draggable.methods[_1f](this,_20);
}
return this.each(function(){
var _21;
var _22=$.data(this,"draggable");
if(_22){
_22.handle.unbind(".draggable");
_21=$.extend(_22.options,_1f);
}else{
_21=$.extend({},$.fn.draggable.defaults,$.fn.draggable.parseOptions(this),_1f||{});
}
var _23=_21.handle?(typeof _21.handle=="string"?$(_21.handle,this):_21.handle):$(this);
$.data(this,"draggable",{options:_21,handle:_23});
if(_21.disabled){
$(this).css("cursor","");
return;
}
_23.unbind(".draggable").bind("mousemove.draggable",{target:this},function(e){
if($.fn.draggable.isDragging){
return;
}
var _24=$.data(e.data.target,"draggable").options;
if(_25(e)){
$(this).css("cursor",_24.cursor);
}else{
$(this).css("cursor","");
}
}).bind("mouseleave.draggable",{target:this},function(e){
$(this).css("cursor","");
}).bind("mousedown.draggable",{target:this},function(e){
if(_25(e)==false){
return;
}
$(this).css("cursor","");
var _26=$(e.data.target).position();
var _27=$(e.data.target).offset();
var _28={startPosition:$(e.data.target).css("position"),startLeft:_26.left,startTop:_26.top,left:_26.left,top:_26.top,startX:e.pageX,startY:e.pageY,offsetWidth:(e.pageX-_27.left),offsetHeight:(e.pageY-_27.top),target:e.data.target,parent:$(e.data.target).parent()[0]};
$.extend(e.data,_28);
var _29=$.data(e.data.target,"draggable").options;
if(_29.onBeforeDrag.call(e.data.target,e)==false){
return;
}
$(document).bind("mousedown.draggable",e.data,_c);
$(document).bind("mousemove.draggable",e.data,_12);
$(document).bind("mouseup.draggable",e.data,_16);
});
function _25(e){
var _2a=$.data(e.data.target,"draggable");
var _2b=_2a.handle;
var _2c=$(_2b).offset();
var _2d=$(_2b).outerWidth();
var _2e=$(_2b).outerHeight();
var t=e.pageY-_2c.top;
var r=_2c.left+_2d-e.pageX;
var b=_2c.top+_2e-e.pageY;
var l=e.pageX-_2c.left;
return Math.min(t,r,b,l)>_2a.options.edge;
};
});
};
$.fn.draggable.methods={options:function(jq){
return $.data(jq[0],"draggable").options;
},proxy:function(jq){
return $.data(jq[0],"draggable").proxy;
},enable:function(jq){
return jq.each(function(){
$(this).draggable({disabled:false});
});
},disable:function(jq){
return jq.each(function(){
$(this).draggable({disabled:true});
});
}};
$.fn.draggable.parseOptions=function(_2f){
var t=$(_2f);
return $.extend({},$.parser.parseOptions(_2f,["cursor","handle","axis",{"revert":"boolean","deltaX":"number","deltaY":"number","edge":"number"}]),{disabled:(t.attr("disabled")?true:undefined)});
};
$.fn.draggable.defaults={proxy:null,revert:false,cursor:"move",deltaX:null,deltaY:null,handle:null,disabled:false,edge:0,axis:null,onBeforeDrag:function(e){
},onStartDrag:function(e){
},onDrag:function(e){
},onStopDrag:function(e){
}};
$.fn.draggable.isDragging=false;
})(jQuery);

