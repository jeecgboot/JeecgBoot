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
function _1(_2,_3){
var _4=$.data(_2,"combo");
var _5=_4.options;
var _6=_4.combo;
var _7=_4.panel;
if(_3){
_5.width=_3;
}
if(isNaN(_5.width)){
var c=$(_2).clone();
c.css("visibility","hidden");
c.appendTo("body");
_5.width=c.outerWidth();
c.remove();
}
_6.appendTo("body");
var _8=_6.find("input.combo-text");
var _9=_6.find(".combo-arrow");
var _a=_5.hasDownArrow?_9._outerWidth():0;
_6._outerWidth(_5.width)._outerHeight(_5.height);
_8._outerWidth(_6.width()-_a);
_8.css({height:_6.height()+"px",lineHeight:_6.height()+"px"});
_9._outerHeight(_6.height());
_7.panel("resize",{width:(_5.panelWidth?_5.panelWidth:_6.outerWidth()),height:_5.panelHeight});
_6.insertAfter(_2);
};
function _b(_c){
$(_c).addClass("combo-f").hide();
var _d=$("<span class=\"combo\">"+"<input type=\"text\" class=\"combo-text\" autocomplete=\"off\">"+"<span><span class=\"combo-arrow\"></span></span>"+"<input type=\"hidden\" class=\"combo-value\">"+"</span>").insertAfter(_c);
var _e=$("<div class=\"combo-panel\"></div>").appendTo("body");
_e.panel({doSize:false,closed:true,cls:"combo-p",style:{position:"absolute",zIndex:10},onOpen:function(){
var p=$(this).panel("panel");
if($.fn.menu){
p.css("z-index",$.fn.menu.defaults.zIndex++);
}else{
if($.fn.window){
p.css("z-index",$.fn.window.defaults.zIndex++);
}
}
$(this).panel("resize");
},onBeforeClose:function(){
_1c(this);
},onClose:function(){
var _f=$.data(_c,"combo");
if(_f){
_f.options.onHidePanel.call(_c);
}
}});
var _10=$(_c).attr("name");
if(_10){
_d.find("input.combo-value").attr("name",_10);
$(_c).removeAttr("name").attr("comboName",_10);
}
return {combo:_d,panel:_e};
};
function _11(_12){
var _13=$.data(_12,"combo");
var _14=_13.options;
var _15=_13.combo;
if(_14.hasDownArrow){
_15.find(".combo-arrow").show();
}else{
_15.find(".combo-arrow").hide();
}
_16(_12,_14.disabled);
_17(_12,_14.readonly);
};
function _18(_19){
var _1a=$.data(_19,"combo");
var _1b=_1a.combo.find("input.combo-text");
_1b.validatebox("destroy");
_1a.panel.panel("destroy");
_1a.combo.remove();
$(_19).remove();
};
function _1c(_1d){
$(_1d).find(".combo-f").each(function(){
var p=$(this).combo("panel");
if(p.is(":visible")){
p.panel("close");
}
});
};
function _1e(_1f){
var _20=$.data(_1f,"combo");
var _21=_20.options;
var _22=_20.panel;
var _23=_20.combo;
var _24=_23.find(".combo-text");
var _25=_23.find(".combo-arrow");
$(document).unbind(".combo").bind("mousedown.combo",function(e){
var p=$(e.target).closest("span.combo,div.combo-p");
if(p.length){
_1c(p);
return;
}
$("body>div.combo-p>div.combo-panel:visible").panel("close");
});
_24.unbind(".combo");
_25.unbind(".combo");
if(!_21.disabled&&!_21.readonly){
_24.bind("click.combo",function(e){
if(!_21.editable){
_26.call(this);
}else{
var p=$(this).closest("div.combo-panel");
$("div.combo-panel:visible").not(_22).not(p).panel("close");
}
}).bind("keydown.combo paste.combo drop.combo",function(e){
switch(e.keyCode){
case 38:
_21.keyHandler.up.call(_1f,e);
break;
case 40:
_21.keyHandler.down.call(_1f,e);
break;
case 37:
_21.keyHandler.left.call(_1f,e);
break;
case 39:
_21.keyHandler.right.call(_1f,e);
break;
case 13:
e.preventDefault();
_21.keyHandler.enter.call(_1f,e);
return false;
case 9:
case 27:
_27(_1f);
break;
default:
if(_21.editable){
if(_20.timer){
clearTimeout(_20.timer);
}
_20.timer=setTimeout(function(){
var q=_24.val();
if(_20.previousValue!=q){
_20.previousValue=q;
$(_1f).combo("showPanel");
_21.keyHandler.query.call(_1f,_24.val(),e);
$(_1f).combo("validate");
}
},_21.delay);
}
}
});
_25.bind("click.combo",function(){
_26.call(this);
}).bind("mouseenter.combo",function(){
$(this).addClass("combo-arrow-hover");
}).bind("mouseleave.combo",function(){
$(this).removeClass("combo-arrow-hover");
});
}
function _26(){
if(_22.is(":visible")){
_27(_1f);
}else{
var p=$(this).closest("div.combo-panel");
$("div.combo-panel:visible").not(_22).not(p).panel("close");
$(_1f).combo("showPanel");
}
_24.focus();
};
};
function _28(_29){
var _2a=$.data(_29,"combo");
var _2b=_2a.options;
var _2c=_2a.combo;
var _2d=_2a.panel;
_2d.panel("move",{left:_2e(),top:_2f()});
if(_2d.panel("options").closed){
_2d.panel("open");
_2b.onShowPanel.call(_29);
}
(function(){
if(_2d.is(":visible")){
_2d.panel("move",{left:_2e(),top:_2f()});
setTimeout(arguments.callee,200);
}
})();
function _2e(){
var _30=_2c.offset().left;
if(_2b.panelAlign=="right"){
_30+=_2c._outerWidth()-_2d._outerWidth();
}
if(_30+_2d._outerWidth()>$(window)._outerWidth()+$(document).scrollLeft()){
_30=$(window)._outerWidth()+$(document).scrollLeft()-_2d._outerWidth();
}
if(_30<0){
_30=0;
}
return _30;
};
function _2f(){
var top=_2c.offset().top+_2c._outerHeight();
if(top+_2d._outerHeight()>$(window)._outerHeight()+$(document).scrollTop()){
top=_2c.offset().top-_2d._outerHeight();
}
if(top<$(document).scrollTop()){
top=_2c.offset().top+_2c._outerHeight();
}
return top;
};
};
function _27(_31){
var _32=$.data(_31,"combo").panel;
_32.panel("close");
};
function _33(_34){
var _35=$.data(_34,"combo").options;
var _36=$(_34).combo("textbox");
_36.validatebox($.extend({},_35,{deltaX:(_35.hasDownArrow?_35.deltaX:(_35.deltaX>0?1:-1))}));
};
function _16(_37,_38){
var _39=$.data(_37,"combo");
var _3a=_39.options;
var _3b=_39.combo;
if(_38){
_3a.disabled=true;
$(_37).attr("disabled",true);
_3b.find(".combo-value").attr("disabled",true);
_3b.find(".combo-text").attr("disabled",true);
}else{
_3a.disabled=false;
$(_37).removeAttr("disabled");
_3b.find(".combo-value").removeAttr("disabled");
_3b.find(".combo-text").removeAttr("disabled");
}
};
function _17(_3c,_3d){
var _3e=$.data(_3c,"combo");
var _3f=_3e.options;
_3f.readonly=_3d==undefined?true:_3d;
var _40=_3f.readonly?true:(!_3f.editable);
_3e.combo.find(".combo-text").attr("readonly",_40).css("cursor",_40?"pointer":"");
};
function _41(_42){
var _43=$.data(_42,"combo");
var _44=_43.options;
var _45=_43.combo;
if(_44.multiple){
_45.find("input.combo-value").remove();
}else{
_45.find("input.combo-value").val("");
}
_45.find("input.combo-text").val("");
};
function _46(_47){
var _48=$.data(_47,"combo").combo;
return _48.find("input.combo-text").val();
};
function _49(_4a,_4b){
var _4c=$.data(_4a,"combo");
var _4d=_4c.combo.find("input.combo-text");
if(_4d.val()!=_4b){
_4d.val(_4b);
$(_4a).combo("validate");
_4c.previousValue=_4b;
}
};
function _4e(_4f){
var _50=[];
var _51=$.data(_4f,"combo").combo;
_51.find("input.combo-value").each(function(){
_50.push($(this).val());
});
return _50;
};
function _52(_53,_54){
var _55=$.data(_53,"combo").options;
var _56=_4e(_53);
var _57=$.data(_53,"combo").combo;
_57.find("input.combo-value").remove();
var _58=$(_53).attr("comboName");
for(var i=0;i<_54.length;i++){
var _59=$("<input type=\"hidden\" class=\"combo-value\">").appendTo(_57);
if(_58){
_59.attr("name",_58);
}
_59.val(_54[i]);
}
var tmp=[];
for(var i=0;i<_56.length;i++){
tmp[i]=_56[i];
}
var aa=[];
for(var i=0;i<_54.length;i++){
for(var j=0;j<tmp.length;j++){
if(_54[i]==tmp[j]){
aa.push(_54[i]);
tmp.splice(j,1);
break;
}
}
}
if(aa.length!=_54.length||_54.length!=_56.length){
if(_55.multiple){
_55.onChange.call(_53,_54,_56);
}else{
_55.onChange.call(_53,_54[0],_56[0]);
}
}
};
function _5a(_5b){
var _5c=_4e(_5b);
return _5c[0];
};
function _5d(_5e,_5f){
_52(_5e,[_5f]);
};
function _60(_61){
var _62=$.data(_61,"combo").options;
var fn=_62.onChange;
_62.onChange=function(){
};
if(_62.multiple){
if(_62.value){
if(typeof _62.value=="object"){
_52(_61,_62.value);
}else{
_5d(_61,_62.value);
}
}else{
_52(_61,[]);
}
_62.originalValue=_4e(_61);
}else{
_5d(_61,_62.value);
_62.originalValue=_62.value;
}
_62.onChange=fn;
};
$.fn.combo=function(_63,_64){
if(typeof _63=="string"){
var _65=$.fn.combo.methods[_63];
if(_65){
return _65(this,_64);
}else{
return this.each(function(){
var _66=$(this).combo("textbox");
_66.validatebox(_63,_64);
});
}
}
_63=_63||{};
return this.each(function(){
var _67=$.data(this,"combo");
if(_67){
$.extend(_67.options,_63);
}else{
var r=_b(this);
_67=$.data(this,"combo",{options:$.extend({},$.fn.combo.defaults,$.fn.combo.parseOptions(this),_63),combo:r.combo,panel:r.panel,previousValue:null});
$(this).removeAttr("disabled");
}
_11(this);
_1(this);
_1e(this);
_33(this);
_60(this);
});
};
$.fn.combo.methods={options:function(jq){
return $.data(jq[0],"combo").options;
},panel:function(jq){
return $.data(jq[0],"combo").panel;
},textbox:function(jq){
return $.data(jq[0],"combo").combo.find("input.combo-text");
},destroy:function(jq){
return jq.each(function(){
_18(this);
});
},resize:function(jq,_68){
return jq.each(function(){
_1(this,_68);
});
},showPanel:function(jq){
return jq.each(function(){
_28(this);
});
},hidePanel:function(jq){
return jq.each(function(){
_27(this);
});
},disable:function(jq){
return jq.each(function(){
_16(this,true);
_1e(this);
});
},enable:function(jq){
return jq.each(function(){
_16(this,false);
_1e(this);
});
},readonly:function(jq,_69){
return jq.each(function(){
_17(this,_69);
_1e(this);
});
},isValid:function(jq){
var _6a=$.data(jq[0],"combo").combo.find("input.combo-text");
return _6a.validatebox("isValid");
},clear:function(jq){
return jq.each(function(){
_41(this);
});
},reset:function(jq){
return jq.each(function(){
var _6b=$.data(this,"combo").options;
if(_6b.multiple){
$(this).combo("setValues",_6b.originalValue);
}else{
$(this).combo("setValue",_6b.originalValue);
}
});
},getText:function(jq){
return _46(jq[0]);
},setText:function(jq,_6c){
return jq.each(function(){
_49(this,_6c);
});
},getValues:function(jq){
return _4e(jq[0]);
},setValues:function(jq,_6d){
return jq.each(function(){
_52(this,_6d);
});
},getValue:function(jq){
return _5a(jq[0]);
},setValue:function(jq,_6e){
return jq.each(function(){
_5d(this,_6e);
});
}};
$.fn.combo.parseOptions=function(_6f){
var t=$(_6f);
return $.extend({},$.fn.validatebox.parseOptions(_6f),$.parser.parseOptions(_6f,["width","height","separator","panelAlign",{panelWidth:"number",editable:"boolean",hasDownArrow:"boolean",delay:"number",selectOnNavigation:"boolean"}]),{panelHeight:(t.attr("panelHeight")=="auto"?"auto":parseInt(t.attr("panelHeight"))||undefined),multiple:(t.attr("multiple")?true:undefined),disabled:(t.attr("disabled")?true:undefined),readonly:(t.attr("readonly")?true:undefined),value:(t.val()||undefined)});
};
$.fn.combo.defaults=$.extend({},$.fn.validatebox.defaults,{width:"auto",height:22,panelWidth:null,panelHeight:200,panelAlign:"left",multiple:false,selectOnNavigation:true,separator:",",editable:true,disabled:false,readonly:false,hasDownArrow:true,value:"",delay:200,deltaX:19,keyHandler:{up:function(e){
},down:function(e){
},left:function(e){
},right:function(e){
},enter:function(e){
},query:function(q,e){
}},onShowPanel:function(){
},onHidePanel:function(){
},onChange:function(_70,_71){
}});
})(jQuery);

