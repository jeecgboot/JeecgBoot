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
var _3=$.data(_2,"timespinner").options;
$(_2).addClass("timespinner-f");
$(_2).spinner(_3);
$(_2).unbind(".timespinner");
$(_2).bind("click.timespinner",function(){
var _4=0;
if(this.selectionStart!=null){
_4=this.selectionStart;
}else{
if(this.createTextRange){
var _5=_2.createTextRange();
var s=document.selection.createRange();
s.setEndPoint("StartToStart",_5);
_4=s.text.length;
}
}
if(_4>=0&&_4<=2){
_3.highlight=0;
}else{
if(_4>=3&&_4<=5){
_3.highlight=1;
}else{
if(_4>=6&&_4<=8){
_3.highlight=2;
}
}
}
_7(_2);
}).bind("blur.timespinner",function(){
_6(_2);
});
};
function _7(_8){
var _9=$.data(_8,"timespinner").options;
var _a=0,_b=0;
if(_9.highlight==0){
_a=0;
_b=2;
}else{
if(_9.highlight==1){
_a=3;
_b=5;
}else{
if(_9.highlight==2){
_a=6;
_b=8;
}
}
}
if(_8.selectionStart!=null){
_8.setSelectionRange(_a,_b);
}else{
if(_8.createTextRange){
var _c=_8.createTextRange();
_c.collapse();
_c.moveEnd("character",_b);
_c.moveStart("character",_a);
_c.select();
}
}
$(_8).focus();
};
function _d(_e,_f){
var _10=$.data(_e,"timespinner").options;
if(!_f){
return null;
}
var vv=_f.split(_10.separator);
for(var i=0;i<vv.length;i++){
if(isNaN(vv[i])){
return null;
}
}
while(vv.length<3){
vv.push(0);
}
return new Date(1900,0,0,vv[0],vv[1],vv[2]);
};
function _6(_11){
var _12=$.data(_11,"timespinner").options;
var _13=$(_11).val();
var _14=_d(_11,_13);
if(!_14){
_12.value="";
$(_11).spinner("setValue","");
return;
}
var _15=_d(_11,_12.min);
var _16=_d(_11,_12.max);
if(_15&&_15>_14){
_14=_15;
}
if(_16&&_16<_14){
_14=_16;
}
var tt=[_17(_14.getHours()),_17(_14.getMinutes())];
if(_12.showSeconds){
tt.push(_17(_14.getSeconds()));
}
var val=tt.join(_12.separator);
_12.value=val;
$(_11).spinner("setValue",val);
function _17(_18){
return (_18<10?"0":"")+_18;
};
};
function _19(_1a,_1b){
var _1c=$.data(_1a,"timespinner").options;
var val=$(_1a).val();
if(val==""){
val=[0,0,0].join(_1c.separator);
}
var vv=val.split(_1c.separator);
for(var i=0;i<vv.length;i++){
vv[i]=parseInt(vv[i],10);
}
if(_1b==true){
vv[_1c.highlight]-=_1c.increment;
}else{
vv[_1c.highlight]+=_1c.increment;
}
$(_1a).val(vv.join(_1c.separator));
_6(_1a);
_7(_1a);
};
$.fn.timespinner=function(_1d,_1e){
if(typeof _1d=="string"){
var _1f=$.fn.timespinner.methods[_1d];
if(_1f){
return _1f(this,_1e);
}else{
return this.spinner(_1d,_1e);
}
}
_1d=_1d||{};
return this.each(function(){
var _20=$.data(this,"timespinner");
if(_20){
$.extend(_20.options,_1d);
}else{
$.data(this,"timespinner",{options:$.extend({},$.fn.timespinner.defaults,$.fn.timespinner.parseOptions(this),_1d)});
}
_1(this);
});
};
$.fn.timespinner.methods={options:function(jq){
var _21=$.data(jq[0],"timespinner").options;
return $.extend(_21,{value:jq.val(),originalValue:jq.spinner("options").originalValue});
},setValue:function(jq,_22){
return jq.each(function(){
$(this).val(_22);
_6(this);
});
},getHours:function(jq){
var _23=$.data(jq[0],"timespinner").options;
var vv=jq.val().split(_23.separator);
return parseInt(vv[0],10);
},getMinutes:function(jq){
var _24=$.data(jq[0],"timespinner").options;
var vv=jq.val().split(_24.separator);
return parseInt(vv[1],10);
},getSeconds:function(jq){
var _25=$.data(jq[0],"timespinner").options;
var vv=jq.val().split(_25.separator);
return parseInt(vv[2],10)||0;
}};
$.fn.timespinner.parseOptions=function(_26){
return $.extend({},$.fn.spinner.parseOptions(_26),$.parser.parseOptions(_26,["separator",{showSeconds:"boolean",highlight:"number"}]));
};
$.fn.timespinner.defaults=$.extend({},$.fn.spinner.defaults,{separator:":",showSeconds:false,highlight:0,spin:function(_27){
_19(this,_27);
}});
})(jQuery);

