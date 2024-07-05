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
var _3=$.data(_2,"combogrid");
var _4=_3.options;
var _5=_3.grid;
$(_2).addClass("combogrid-f").combo(_4);
var _6=$(_2).combo("panel");
if(!_5){
_5=$("<table></table>").appendTo(_6);
_3.grid=_5;
}
_5.datagrid($.extend({},_4,{border:false,fit:true,singleSelect:(!_4.multiple),onLoadSuccess:function(_7){
var _8=$(_2).combo("getValues");
var _9=_4.onSelect;
_4.onSelect=function(){
};
_1a(_2,_8,_3.remainText);
_4.onSelect=_9;
_4.onLoadSuccess.apply(_2,arguments);
},onClickRow:_a,onSelect:function(_b,_c){
_d();
_4.onSelect.call(this,_b,_c);
},onUnselect:function(_e,_f){
_d();
_4.onUnselect.call(this,_e,_f);
},onSelectAll:function(_10){
_d();
_4.onSelectAll.call(this,_10);
},onUnselectAll:function(_11){
if(_4.multiple){
_d();
}
_4.onUnselectAll.call(this,_11);
}}));
function _a(_12,row){
_3.remainText=false;
_d();
if(!_4.multiple){
$(_2).combo("hidePanel");
}
_4.onClickRow.call(this,_12,row);
};
function _d(){
var _13=_5.datagrid("getSelections");
var vv=[],ss=[];
for(var i=0;i<_13.length;i++){
vv.push(_13[i][_4.idField]);
ss.push(_13[i][_4.textField]);
}
if(!_4.multiple){
$(_2).combo("setValues",(vv.length?vv:[""]));
}else{
$(_2).combo("setValues",vv);
}
if(!_3.remainText){
$(_2).combo("setText",ss.join(_4.separator));
}
};
};
function nav(_14,dir){
var _15=$.data(_14,"combogrid");
var _16=_15.options;
var _17=_15.grid;
var _18=_17.datagrid("getRows").length;
if(!_18){
return;
}
var tr=_16.finder.getTr(_17[0],null,"highlight");
if(!tr.length){
tr=_16.finder.getTr(_17[0],null,"selected");
}
var _19;
if(!tr.length){
_19=(dir=="next"?0:_18-1);
}else{
var _19=parseInt(tr.attr("datagrid-row-index"));
_19+=(dir=="next"?1:-1);
if(_19<0){
_19=_18-1;
}
if(_19>=_18){
_19=0;
}
}
_17.datagrid("highlightRow",_19);
if(_16.selectOnNavigation){
_15.remainText=false;
_17.datagrid("selectRow",_19);
}
};
function _1a(_1b,_1c,_1d){
var _1e=$.data(_1b,"combogrid");
var _1f=_1e.options;
var _20=_1e.grid;
var _21=_20.datagrid("getRows");
var ss=[];
var _22=$(_1b).combo("getValues");
var _23=$(_1b).combo("options");
var _24=_23.onChange;
_23.onChange=function(){
};
_20.datagrid("clearSelections");
for(var i=0;i<_1c.length;i++){
var _25=_20.datagrid("getRowIndex",_1c[i]);
if(_25>=0){
_20.datagrid("selectRow",_25);
ss.push(_21[_25][_1f.textField]);
}else{
ss.push(_1c[i]);
}
}
$(_1b).combo("setValues",_22);
_23.onChange=_24;
$(_1b).combo("setValues",_1c);
if(!_1d){
var s=ss.join(_1f.separator);
if($(_1b).combo("getText")!=s){
$(_1b).combo("setText",s);
}
}
};
function _26(_27,q){
var _28=$.data(_27,"combogrid");
var _29=_28.options;
var _2a=_28.grid;
_28.remainText=true;
if(_29.multiple&&!q){
_1a(_27,[],true);
}else{
_1a(_27,[q],true);
}
if(_29.mode=="remote"){
_2a.datagrid("clearSelections");
_2a.datagrid("load",$.extend({},_29.queryParams,{q:q}));
}else{
if(!q){
return;
}
_2a.datagrid("clearSelections").datagrid("highlightRow",-1);
var _2b=_2a.datagrid("getRows");
var qq=_29.multiple?q.split(_29.separator):[q];
$.map(qq,function(q){
q=$.trim(q);
if(q){
$.map(_2b,function(row,i){
if(q==row[_29.textField]){
_2a.datagrid("selectRow",i);
}else{
if(_29.filter.call(_27,q,row)){
_2a.datagrid("highlightRow",i);
}
}
});
}
});
}
};
function _2c(_2d){
var _2e=$.data(_2d,"combogrid");
var _2f=_2e.options;
var _30=_2e.grid;
var tr=_2f.finder.getTr(_30[0],null,"highlight");
_2e.remainText=false;
if(tr.length){
var _31=parseInt(tr.attr("datagrid-row-index"));
if(_2f.multiple){
if(tr.hasClass("datagrid-row-selected")){
_30.datagrid("unselectRow",_31);
}else{
_30.datagrid("selectRow",_31);
}
}else{
_30.datagrid("selectRow",_31);
}
}
var vv=[];
$.map(_30.datagrid("getSelections"),function(row){
vv.push(row[_2f.idField]);
});
$(_2d).combogrid("setValues",vv);
if(!_2f.multiple){
$(_2d).combogrid("hidePanel");
}
};
$.fn.combogrid=function(_32,_33){
if(typeof _32=="string"){
var _34=$.fn.combogrid.methods[_32];
if(_34){
return _34(this,_33);
}else{
return this.combo(_32,_33);
}
}
_32=_32||{};
return this.each(function(){
var _35=$.data(this,"combogrid");
if(_35){
$.extend(_35.options,_32);
}else{
_35=$.data(this,"combogrid",{options:$.extend({},$.fn.combogrid.defaults,$.fn.combogrid.parseOptions(this),_32)});
}
_1(this);
});
};
$.fn.combogrid.methods={options:function(jq){
var _36=jq.combo("options");
return $.extend($.data(jq[0],"combogrid").options,{originalValue:_36.originalValue,disabled:_36.disabled,readonly:_36.readonly});
},grid:function(jq){
return $.data(jq[0],"combogrid").grid;
},setValues:function(jq,_37){
return jq.each(function(){
_1a(this,_37);
});
},setValue:function(jq,_38){
return jq.each(function(){
_1a(this,[_38]);
});
},clear:function(jq){
return jq.each(function(){
$(this).combogrid("grid").datagrid("clearSelections");
$(this).combo("clear");
});
},reset:function(jq){
return jq.each(function(){
var _39=$(this).combogrid("options");
if(_39.multiple){
$(this).combogrid("setValues",_39.originalValue);
}else{
$(this).combogrid("setValue",_39.originalValue);
}
});
}};
$.fn.combogrid.parseOptions=function(_3a){
var t=$(_3a);
return $.extend({},$.fn.combo.parseOptions(_3a),$.fn.datagrid.parseOptions(_3a),$.parser.parseOptions(_3a,["idField","textField","mode"]));
};
$.fn.combogrid.defaults=$.extend({},$.fn.combo.defaults,$.fn.datagrid.defaults,{loadMsg:null,idField:null,textField:null,mode:"local",keyHandler:{up:function(e){
nav(this,"prev");
e.preventDefault();
},down:function(e){
nav(this,"next");
e.preventDefault();
},left:function(e){
},right:function(e){
},enter:function(e){
_2c(this);
},query:function(q,e){
_26(this,q);
}},filter:function(q,row){
var _3b=$(this).combogrid("options");
return row[_3b.textField].toLowerCase().indexOf(q.toLowerCase())==0;
}});
})(jQuery);

