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
var _3=$.data(_2,"accordion");
var _4=_3.options;
var _5=_3.panels;
var cc=$(_2);
_4.fit?$.extend(_4,cc._fit()):cc._fit(false);
if(!isNaN(_4.width)){
cc._outerWidth(_4.width);
}else{
cc.css("width","");
}
var _6=0;
var _7="auto";
var _8=cc.find(">div.panel>div.accordion-header");
if(_8.length){
_6=$(_8[0]).css("height","")._outerHeight();
}
if(!isNaN(_4.height)){
cc._outerHeight(_4.height);
_7=cc.height()-_6*_8.length;
}else{
cc.css("height","");
}
_9(true,_7-_9(false)+1);
function _9(_a,_b){
var _c=0;
for(var i=0;i<_5.length;i++){
var p=_5[i];
var h=p.panel("header")._outerHeight(_6);
if(p.panel("options").collapsible==_a){
var _d=isNaN(_b)?undefined:(_b+_6*h.length);
p.panel("resize",{width:cc.width(),height:(_a?_d:undefined)});
_c+=p.panel("panel").outerHeight()-_6;
}
}
return _c;
};
};
function _e(_f,_10,_11,all){
var _12=$.data(_f,"accordion").panels;
var pp=[];
for(var i=0;i<_12.length;i++){
var p=_12[i];
if(_10){
if(p.panel("options")[_10]==_11){
pp.push(p);
}
}else{
if(p[0]==$(_11)[0]){
return i;
}
}
}
if(_10){
return all?pp:(pp.length?pp[0]:null);
}else{
return -1;
}
};
function _13(_14){
return _e(_14,"collapsed",false,true);
};
function _15(_16){
var pp=_13(_16);
return pp.length?pp[0]:null;
};
function _17(_18,_19){
return _e(_18,null,_19);
};
function _1a(_1b,_1c){
var _1d=$.data(_1b,"accordion").panels;
if(typeof _1c=="number"){
if(_1c<0||_1c>=_1d.length){
return null;
}else{
return _1d[_1c];
}
}
return _e(_1b,"title",_1c);
};
function _1e(_1f){
var _20=$.data(_1f,"accordion").options;
var cc=$(_1f);
if(_20.border){
cc.removeClass("accordion-noborder");
}else{
cc.addClass("accordion-noborder");
}
};
function _21(_22){
var _23=$.data(_22,"accordion");
var cc=$(_22);
cc.addClass("accordion");
_23.panels=[];
cc.children("div").each(function(){
var _24=$.extend({},$.parser.parseOptions(this),{selected:($(this).attr("selected")?true:undefined)});
var pp=$(this);
_23.panels.push(pp);
_27(_22,pp,_24);
});
cc.bind("_resize",function(e,_25){
var _26=$.data(_22,"accordion").options;
if(_26.fit==true||_25){
_1(_22);
}
return false;
});
};
function _27(_28,pp,_29){
var _2a=$.data(_28,"accordion").options;
pp.panel($.extend({},{collapsible:true,minimizable:false,maximizable:false,closable:false,doSize:false,collapsed:true,headerCls:"accordion-header",bodyCls:"accordion-body"},_29,{onBeforeExpand:function(){
if(_29.onBeforeExpand){
if(_29.onBeforeExpand.call(this)==false){
return false;
}
}
if(!_2a.multiple){
var all=$.grep(_13(_28),function(p){
return p.panel("options").collapsible;
});
for(var i=0;i<all.length;i++){
_35(_28,_17(_28,all[i]));
}
}
var _2b=$(this).panel("header");
_2b.addClass("accordion-header-selected");
_2b.find(".accordion-collapse").removeClass("accordion-expand");
},onExpand:function(){
if(_29.onExpand){
_29.onExpand.call(this);
}
_2a.onSelect.call(_28,$(this).panel("options").title,_17(_28,this));
},onBeforeCollapse:function(){
if(_29.onBeforeCollapse){
if(_29.onBeforeCollapse.call(this)==false){
return false;
}
}
var _2c=$(this).panel("header");
_2c.removeClass("accordion-header-selected");
_2c.find(".accordion-collapse").addClass("accordion-expand");
},onCollapse:function(){
if(_29.onCollapse){
_29.onCollapse.call(this);
}
_2a.onUnselect.call(_28,$(this).panel("options").title,_17(_28,this));
}}));
var _2d=pp.panel("header");
var _2e=_2d.children("div.panel-tool");
_2e.children("a.panel-tool-collapse").hide();
var t=$("<a href=\"javascript:void(0)\"></a>").addClass("accordion-collapse accordion-expand").appendTo(_2e);
t.bind("click",function(){
var _2f=_17(_28,pp);
if(pp.panel("options").collapsed){
_30(_28,_2f);
}else{
_35(_28,_2f);
}
return false;
});
pp.panel("options").collapsible?t.show():t.hide();
_2d.click(function(){
$(this).find("a.accordion-collapse:visible").triggerHandler("click");
return false;
});
};
function _30(_31,_32){
var p=_1a(_31,_32);
if(!p){
return;
}
_33(_31);
var _34=$.data(_31,"accordion").options;
p.panel("expand",_34.animate);
};
function _35(_36,_37){
var p=_1a(_36,_37);
if(!p){
return;
}
_33(_36);
var _38=$.data(_36,"accordion").options;
p.panel("collapse",_38.animate);
};
function _39(_3a){
var _3b=$.data(_3a,"accordion").options;
var p=_e(_3a,"selected",true);
if(p){
_3c(_17(_3a,p));
}else{
_3c(_3b.selected);
}
function _3c(_3d){
var _3e=_3b.animate;
_3b.animate=false;
_30(_3a,_3d);
_3b.animate=_3e;
};
};
function _33(_3f){
var _40=$.data(_3f,"accordion").panels;
for(var i=0;i<_40.length;i++){
_40[i].stop(true,true);
}
};
function add(_41,_42){
var _43=$.data(_41,"accordion");
var _44=_43.options;
var _45=_43.panels;
if(_42.selected==undefined){
_42.selected=true;
}
_33(_41);
var pp=$("<div></div>").appendTo(_41);
_45.push(pp);
_27(_41,pp,_42);
_1(_41);
_44.onAdd.call(_41,_42.title,_45.length-1);
if(_42.selected){
_30(_41,_45.length-1);
}
};
function _46(_47,_48){
var _49=$.data(_47,"accordion");
var _4a=_49.options;
var _4b=_49.panels;
_33(_47);
var _4c=_1a(_47,_48);
var _4d=_4c.panel("options").title;
var _4e=_17(_47,_4c);
if(!_4c){
return;
}
if(_4a.onBeforeRemove.call(_47,_4d,_4e)==false){
return;
}
_4b.splice(_4e,1);
_4c.panel("destroy");
if(_4b.length){
_1(_47);
var _4f=_15(_47);
if(!_4f){
_30(_47,0);
}
}
_4a.onRemove.call(_47,_4d,_4e);
};
$.fn.accordion=function(_50,_51){
if(typeof _50=="string"){
return $.fn.accordion.methods[_50](this,_51);
}
_50=_50||{};
return this.each(function(){
var _52=$.data(this,"accordion");
if(_52){
$.extend(_52.options,_50);
}else{
$.data(this,"accordion",{options:$.extend({},$.fn.accordion.defaults,$.fn.accordion.parseOptions(this),_50),accordion:$(this).addClass("accordion"),panels:[]});
_21(this);
}
_1e(this);
_1(this);
_39(this);
});
};
$.fn.accordion.methods={options:function(jq){
return $.data(jq[0],"accordion").options;
},panels:function(jq){
return $.data(jq[0],"accordion").panels;
},resize:function(jq){
return jq.each(function(){
_1(this);
});
},getSelections:function(jq){
return _13(jq[0]);
},getSelected:function(jq){
return _15(jq[0]);
},getPanel:function(jq,_53){
return _1a(jq[0],_53);
},getPanelIndex:function(jq,_54){
return _17(jq[0],_54);
},select:function(jq,_55){
return jq.each(function(){
_30(this,_55);
});
},unselect:function(jq,_56){
return jq.each(function(){
_35(this,_56);
});
},add:function(jq,_57){
return jq.each(function(){
add(this,_57);
});
},remove:function(jq,_58){
return jq.each(function(){
_46(this,_58);
});
}};
$.fn.accordion.parseOptions=function(_59){
var t=$(_59);
return $.extend({},$.parser.parseOptions(_59,["width","height",{fit:"boolean",border:"boolean",animate:"boolean",multiple:"boolean",selected:"number"}]));
};
$.fn.accordion.defaults={width:"auto",height:"auto",fit:false,border:true,animate:true,multiple:false,selected:0,onSelect:function(_5a,_5b){
},onUnselect:function(_5c,_5d){
},onAdd:function(_5e,_5f){
},onBeforeRemove:function(_60,_61){
},onRemove:function(_62,_63){
}};
})(jQuery);

