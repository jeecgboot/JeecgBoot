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
var _1=0;
function _2(_3,_4){
var _5=$.data(_3,"combobox");
var _6=_5.options;
var _7=_5.data;
for(var i=0;i<_7.length;i++){
if(_7[i][_6.valueField]==_4){
return i;
}
}
return -1;
};
function _8(_9,_a){
var _b=$.data(_9,"combobox").options;
var _c=$(_9).combo("panel");
var _d=_b.finder.getEl(_9,_a);
if(_d.length){
if(_d.position().top<=0){
var h=_c.scrollTop()+_d.position().top;
_c.scrollTop(h);
}else{
if(_d.position().top+_d.outerHeight()>_c.height()){
var h=_c.scrollTop()+_d.position().top+_d.outerHeight()-_c.height();
_c.scrollTop(h);
}
}
}
};
function _e(_f,dir){
var _10=$.data(_f,"combobox").options;
var _11=$(_f).combobox("panel");
var _12=_11.children("div.combobox-item-hover");
if(!_12.length){
_12=_11.children("div.combobox-item-selected");
}
_12.removeClass("combobox-item-hover");
var _13="div.combobox-item:visible:not(.combobox-item-disabled):first";
var _14="div.combobox-item:visible:not(.combobox-item-disabled):last";
if(!_12.length){
_12=_11.children(dir=="next"?_13:_14);
}else{
if(dir=="next"){
_12=_12.nextAll(_13);
if(!_12.length){
_12=_11.children(_13);
}
}else{
_12=_12.prevAll(_13);
if(!_12.length){
_12=_11.children(_14);
}
}
}
if(_12.length){
_12.addClass("combobox-item-hover");
var row=_10.finder.getRow(_f,_12);
if(row){
_8(_f,row[_10.valueField]);
if(_10.selectOnNavigation){
_15(_f,row[_10.valueField]);
}
}
}
};
function _15(_16,_17){
var _18=$.data(_16,"combobox").options;
var _19=$(_16).combo("getValues");
if($.inArray(_17+"",_19)==-1){
if(_18.multiple){
_19.push(_17);
}else{
_19=[_17];
}
_1a(_16,_19);
_18.onSelect.call(_16,_18.finder.getRow(_16,_17));
}
};
function _1b(_1c,_1d){
var _1e=$.data(_1c,"combobox").options;
var _1f=$(_1c).combo("getValues");
var _20=$.inArray(_1d+"",_1f);
if(_20>=0){
_1f.splice(_20,1);
_1a(_1c,_1f);
_1e.onUnselect.call(_1c,_1e.finder.getRow(_1c,_1d));
}
};
function _1a(_21,_22,_23){
var _24=$.data(_21,"combobox").options;
var _25=$(_21).combo("panel");
_25.find("div.combobox-item-selected").removeClass("combobox-item-selected");
var vv=[],ss=[];
for(var i=0;i<_22.length;i++){
var v=_22[i];
var s=v;
_24.finder.getEl(_21,v).addClass("combobox-item-selected");
var row=_24.finder.getRow(_21,v);
if(row){
s=row[_24.textField];
}
vv.push(v);
ss.push(s);
}
$(_21).combo("setValues",vv);
if(!_23){
$(_21).combo("setText",ss.join(_24.separator));
}
};
function _26(_27,_28,_29){
var _2a=$.data(_27,"combobox");
var _2b=_2a.options;
_2a.data=_2b.loadFilter.call(_27,_28);
_2a.groups=[];
_28=_2a.data;
var _2c=$(_27).combobox("getValues");
var dd=[];
var _2d=undefined;
for(var i=0;i<_28.length;i++){
var row=_28[i];
var v=row[_2b.valueField]+"";
var s=row[_2b.textField];
var g=row[_2b.groupField];
if(g){
if(_2d!=g){
_2d=g;
_2a.groups.push(g);
dd.push("<div id=\""+(_2a.groupIdPrefix+"_"+(_2a.groups.length-1))+"\" class=\"combobox-group\">");
dd.push(_2b.groupFormatter?_2b.groupFormatter.call(_27,g):g);
dd.push("</div>");
}
}else{
_2d=undefined;
}
var cls="combobox-item"+(row.disabled?" combobox-item-disabled":"")+(g?" combobox-gitem":"");
dd.push("<div id=\""+(_2a.itemIdPrefix+"_"+i)+"\" class=\""+cls+"\">");
dd.push(_2b.formatter?_2b.formatter.call(_27,row):s);
dd.push("</div>");
if(row["selected"]&&$.inArray(v,_2c)==-1){
_2c.push(v);
}
}
$(_27).combo("panel").html(dd.join(""));
if(_2b.multiple){
_1a(_27,_2c,_29);
}else{
_1a(_27,_2c.length?[_2c[_2c.length-1]]:[],_29);
}
_2b.onLoadSuccess.call(_27,_28);
};
function _2e(_2f,url,_30,_31){
var _32=$.data(_2f,"combobox").options;
if(url){
_32.url=url;
}
_30=_30||{};
if(_32.onBeforeLoad.call(_2f,_30)==false){
return;
}
_32.loader.call(_2f,_30,function(_33){
_26(_2f,_33,_31);
},function(){
_32.onLoadError.apply(this,arguments);
});
};
function _34(_35,q){
var _36=$.data(_35,"combobox");
var _37=_36.options;
if(_37.multiple&&!q){
_1a(_35,[],true);
}else{
_1a(_35,[q],true);
}
if(_37.mode=="remote"){
_2e(_35,null,{q:q},true);
}else{
var _38=$(_35).combo("panel");
_38.find("div.combobox-item-selected,div.combobox-item-hover").removeClass("combobox-item-selected combobox-item-hover");
_38.find("div.combobox-item,div.combobox-group").hide();
var _39=_36.data;
var vv=[];
var qq=_37.multiple?q.split(_37.separator):[q];
$.map(qq,function(q){
q=$.trim(q);
var _3a=undefined;
for(var i=0;i<_39.length;i++){
var row=_39[i];
if(_37.filter.call(_35,q,row)){
var v=row[_37.valueField];
var s=row[_37.textField];
var g=row[_37.groupField];
var _3b=_37.finder.getEl(_35,v).show();
if(s.toLowerCase()==q.toLowerCase()){
vv.push(v);
_3b.addClass("combobox-item-selected");
}
if(_37.groupField&&_3a!=g){
$("#"+_36.groupIdPrefix+"_"+$.inArray(g,_36.groups)).show();
_3a=g;
}
}
}
});
_1a(_35,vv,true);
}
};
function _3c(_3d){
var t=$(_3d);
var _3e=t.combobox("options");
var _3f=t.combobox("panel");
var _40=_3f.children("div.combobox-item-hover");
if(_40.length){
var row=_3e.finder.getRow(_3d,_40);
var _41=row[_3e.valueField];
if(_3e.multiple){
if(_40.hasClass("combobox-item-selected")){
t.combobox("unselect",_41);
}else{
t.combobox("select",_41);
}
}else{
t.combobox("select",_41);
}
}
var vv=[];
$.map(t.combobox("getValues"),function(v){
if(_2(_3d,v)>=0){
vv.push(v);
}
});
t.combobox("setValues",vv);
if(!_3e.multiple){
t.combobox("hidePanel");
}
};
function _42(_43){
var _44=$.data(_43,"combobox");
var _45=_44.options;
_1++;
_44.itemIdPrefix="_easyui_combobox_i"+_1;
_44.groupIdPrefix="_easyui_combobox_g"+_1;
$(_43).addClass("combobox-f");
$(_43).combo($.extend({},_45,{onShowPanel:function(){
$(_43).combo("panel").find("div.combobox-item,div.combobox-group").show();
_8(_43,$(_43).combobox("getValue"));
_45.onShowPanel.call(_43);
}}));
$(_43).combo("panel").unbind().bind("mouseover",function(e){
$(this).children("div.combobox-item-hover").removeClass("combobox-item-hover");
var _46=$(e.target).closest("div.combobox-item");
if(!_46.hasClass("combobox-item-disabled")){
_46.addClass("combobox-item-hover");
}
e.stopPropagation();
}).bind("mouseout",function(e){
$(e.target).closest("div.combobox-item").removeClass("combobox-item-hover");
e.stopPropagation();
}).bind("click",function(e){
var _47=$(e.target).closest("div.combobox-item");
if(!_47.length||_47.hasClass("combobox-item-disabled")){
return;
}
var row=_45.finder.getRow(_43,_47);
if(!row){
return;
}
var _48=row[_45.valueField];
if(_45.multiple){
if(_47.hasClass("combobox-item-selected")){
_1b(_43,_48);
}else{
_15(_43,_48);
}
}else{
_15(_43,_48);
$(_43).combo("hidePanel");
}
e.stopPropagation();
});
};
$.fn.combobox=function(_49,_4a){
if(typeof _49=="string"){
var _4b=$.fn.combobox.methods[_49];
if(_4b){
return _4b(this,_4a);
}else{
return this.combo(_49,_4a);
}
}
_49=_49||{};
return this.each(function(){
var _4c=$.data(this,"combobox");
if(_4c){
$.extend(_4c.options,_49);
_42(this);
}else{
_4c=$.data(this,"combobox",{options:$.extend({},$.fn.combobox.defaults,$.fn.combobox.parseOptions(this),_49),data:[]});
_42(this);
var _4d=$.fn.combobox.parseData(this);
if(_4d.length){
_26(this,_4d);
}
}
if(_4c.options.data){
_26(this,_4c.options.data);
}
_2e(this);
});
};
$.fn.combobox.methods={options:function(jq){
var _4e=jq.combo("options");
return $.extend($.data(jq[0],"combobox").options,{originalValue:_4e.originalValue,disabled:_4e.disabled,readonly:_4e.readonly});
},getData:function(jq){
return $.data(jq[0],"combobox").data;
},setValues:function(jq,_4f){
return jq.each(function(){
_1a(this,_4f);
});
},setValue:function(jq,_50){
return jq.each(function(){
_1a(this,[_50]);
});
},clear:function(jq){
return jq.each(function(){
$(this).combo("clear");
var _51=$(this).combo("panel");
_51.find("div.combobox-item-selected").removeClass("combobox-item-selected");
});
},reset:function(jq){
return jq.each(function(){
var _52=$(this).combobox("options");
if(_52.multiple){
$(this).combobox("setValues",_52.originalValue);
}else{
$(this).combobox("setValue",_52.originalValue);
}
});
},loadData:function(jq,_53){
return jq.each(function(){
_26(this,_53);
});
},reload:function(jq,url){
return jq.each(function(){
_2e(this,url);
});
},select:function(jq,_54){
return jq.each(function(){
_15(this,_54);
});
},unselect:function(jq,_55){
return jq.each(function(){
_1b(this,_55);
});
}};
$.fn.combobox.parseOptions=function(_56){
var t=$(_56);
return $.extend({},$.fn.combo.parseOptions(_56),$.parser.parseOptions(_56,["valueField","textField","groupField","mode","method","url"]));
};
$.fn.combobox.parseData=function(_57){
var _58=[];
var _59=$(_57).combobox("options");
$(_57).children().each(function(){
if(this.tagName.toLowerCase()=="optgroup"){
var _5a=$(this).attr("label");
$(this).children().each(function(){
_5b(this,_5a);
});
}else{
_5b(this);
}
});
return _58;
function _5b(el,_5c){
var t=$(el);
var row={};
row[_59.valueField]=t.attr("value")!=undefined?t.attr("value"):t.text();
row[_59.textField]=t.text();
row["selected"]=t.is(":selected");
row["disabled"]=t.is(":disabled");
if(_5c){
_59.groupField=_59.groupField||"group";
row[_59.groupField]=_5c;
}
_58.push(row);
};
};
$.fn.combobox.defaults=$.extend({},$.fn.combo.defaults,{valueField:"value",textField:"text",groupField:null,groupFormatter:function(_5d){
return _5d;
},mode:"local",method:"post",url:null,data:null,keyHandler:{up:function(e){
_e(this,"prev");
e.preventDefault();
},down:function(e){
_e(this,"next");
e.preventDefault();
},left:function(e){
},right:function(e){
},enter:function(e){
_3c(this);
},query:function(q,e){
_34(this,q);
}},filter:function(q,row){
var _5e=$(this).combobox("options");
return row[_5e.textField].toLowerCase().indexOf(q.toLowerCase())==0;
},formatter:function(row){
var _5f=$(this).combobox("options");
return row[_5f.textField];
},loader:function(_60,_61,_62){
var _63=$(this).combobox("options");
if(!_63.url){
return false;
}
$.ajax({type:_63.method,url:_63.url,data:_60,dataType:"json",success:function(_64){
_61(_64);
},error:function(){
_62.apply(this,arguments);
}});
},loadFilter:function(_65){
return _65;
},finder:{getEl:function(_66,_67){
var _68=_2(_66,_67);
var id=$.data(_66,"combobox").itemIdPrefix+"_"+_68;
return $("#"+id);
},getRow:function(_69,p){
var _6a=$.data(_69,"combobox");
var _6b=(p instanceof jQuery)?p.attr("id").substr(_6a.itemIdPrefix.length+1):_2(_69,p);
return _6a.data[parseInt(_6b)];
}},onBeforeLoad:function(_6c){
},onLoadSuccess:function(){
},onLoadError:function(){
},onSelect:function(_6d){
},onUnselect:function(_6e){
}});
})(jQuery);

