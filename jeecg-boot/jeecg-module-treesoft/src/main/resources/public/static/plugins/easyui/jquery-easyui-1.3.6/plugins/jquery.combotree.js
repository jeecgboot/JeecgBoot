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
var _3=$.data(_2,"combotree");
var _4=_3.options;
var _5=_3.tree;
$(_2).addClass("combotree-f");
$(_2).combo(_4);
var _6=$(_2).combo("panel");
if(!_5){
_5=$("<ul></ul>").appendTo(_6);
$.data(_2,"combotree").tree=_5;
}
_5.tree($.extend({},_4,{checkbox:_4.multiple,onLoadSuccess:function(_7,_8){
var _9=$(_2).combotree("getValues");
if(_4.multiple){
var _a=_5.tree("getChecked");
for(var i=0;i<_a.length;i++){
var id=_a[i].id;
(function(){
for(var i=0;i<_9.length;i++){
if(id==_9[i]){
return;
}
}
_9.push(id);
})();
}
}
var _b=$(this).tree("options");
var _c=_b.onCheck;
var _d=_b.onSelect;
_b.onCheck=_b.onSelect=function(){
};
$(_2).combotree("setValues",_9);
_b.onCheck=_c;
_b.onSelect=_d;
_4.onLoadSuccess.call(this,_7,_8);
},onClick:function(_e){
if(_4.multiple){
$(this).tree(_e.checked?"uncheck":"check",_e.target);
}else{
$(_2).combo("hidePanel");
}
_11(_2);
_4.onClick.call(this,_e);
},onCheck:function(_f,_10){
_11(_2);
_4.onCheck.call(this,_f,_10);
}}));
};
function _11(_12){
var _13=$.data(_12,"combotree");
var _14=_13.options;
var _15=_13.tree;
var vv=[],ss=[];
if(_14.multiple){
var _16=_15.tree("getChecked");
for(var i=0;i<_16.length;i++){
vv.push(_16[i].id);
ss.push(_16[i].text);
}
}else{
var _17=_15.tree("getSelected");
if(_17){
vv.push(_17.id);
ss.push(_17.text);
}
}
$(_12).combo("setValues",vv).combo("setText",ss.join(_14.separator));
};
function _18(_19,_1a){
var _1b=$.data(_19,"combotree").options;
var _1c=$.data(_19,"combotree").tree;
_1c.find("span.tree-checkbox").addClass("tree-checkbox0").removeClass("tree-checkbox1 tree-checkbox2");
var vv=[],ss=[];
for(var i=0;i<_1a.length;i++){
var v=_1a[i];
var s=v;
var _1d=_1c.tree("find",v);
if(_1d){
s=_1d.text;
_1c.tree("check",_1d.target);
_1c.tree("select",_1d.target);
}
vv.push(v);
ss.push(s);
}
$(_19).combo("setValues",vv).combo("setText",ss.join(_1b.separator));
};
$.fn.combotree=function(_1e,_1f){
if(typeof _1e=="string"){
var _20=$.fn.combotree.methods[_1e];
if(_20){
return _20(this,_1f);
}else{
return this.combo(_1e,_1f);
}
}
_1e=_1e||{};
return this.each(function(){
var _21=$.data(this,"combotree");
if(_21){
$.extend(_21.options,_1e);
}else{
$.data(this,"combotree",{options:$.extend({},$.fn.combotree.defaults,$.fn.combotree.parseOptions(this),_1e)});
}
_1(this);
});
};
$.fn.combotree.methods={options:function(jq){
var _22=jq.combo("options");
return $.extend($.data(jq[0],"combotree").options,{originalValue:_22.originalValue,disabled:_22.disabled,readonly:_22.readonly});
},tree:function(jq){
return $.data(jq[0],"combotree").tree;
},loadData:function(jq,_23){
return jq.each(function(){
var _24=$.data(this,"combotree").options;
_24.data=_23;
var _25=$.data(this,"combotree").tree;
_25.tree("loadData",_23);
});
},reload:function(jq,url){
return jq.each(function(){
var _26=$.data(this,"combotree").options;
var _27=$.data(this,"combotree").tree;
if(url){
_26.url=url;
}
_27.tree({url:_26.url});
});
},setValues:function(jq,_28){
return jq.each(function(){
_18(this,_28);
});
},setValue:function(jq,_29){
return jq.each(function(){
_18(this,[_29]);
});
},clear:function(jq){
return jq.each(function(){
var _2a=$.data(this,"combotree").tree;
_2a.find("div.tree-node-selected").removeClass("tree-node-selected");
var cc=_2a.tree("getChecked");
for(var i=0;i<cc.length;i++){
_2a.tree("uncheck",cc[i].target);
}
$(this).combo("clear");
});
},reset:function(jq){
return jq.each(function(){
var _2b=$(this).combotree("options");
if(_2b.multiple){
$(this).combotree("setValues",_2b.originalValue);
}else{
$(this).combotree("setValue",_2b.originalValue);
}
});
}};
$.fn.combotree.parseOptions=function(_2c){
return $.extend({},$.fn.combo.parseOptions(_2c),$.fn.tree.parseOptions(_2c));
};
$.fn.combotree.defaults=$.extend({},$.fn.combo.defaults,$.fn.tree.defaults,{editable:false});
})(jQuery);

