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
var _3=$.data(_2,"datebox");
var _4=_3.options;
$(_2).addClass("datebox-f").combo($.extend({},_4,{onShowPanel:function(){
_5();
_10(_2,$(_2).datebox("getText"),true);
_4.onShowPanel.call(_2);
}}));
$(_2).combo("textbox").parent().addClass("datebox");
if(!_3.calendar){
_6();
}
_10(_2,_4.value);
function _6(){
var _7=$(_2).combo("panel").css("overflow","hidden");
_7.panel("options").onBeforeDestroy=function(){
var sc=$(this).find(".calendar-shared");
if(sc.length){
sc.insertBefore(sc[0].pholder);
}
};
var cc=$("<div class=\"datebox-calendar-inner\"></div>").appendTo(_7);
if(_4.sharedCalendar){
var sc=$(_4.sharedCalendar);
if(!sc[0].pholder){
sc[0].pholder=$("<div class=\"calendar-pholder\" style=\"display:none\"></div>").insertAfter(sc);
}
sc.addClass("calendar-shared").appendTo(cc);
if(!sc.hasClass("calendar")){
sc.calendar();
}
_3.calendar=sc;
}else{
_3.calendar=$("<div></div>").appendTo(cc).calendar();
}
$.extend(_3.calendar.calendar("options"),{fit:true,border:false,onSelect:function(_8){
var _9=$(this.target).datebox("options");
_10(this.target,_9.formatter.call(this.target,_8));
$(this.target).combo("hidePanel");
_9.onSelect.call(_2,_8);
}});
var _a=$("<div class=\"datebox-button\"><table cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%\"><tr></tr></table></div>").appendTo(_7);
var tr=_a.find("tr");
for(var i=0;i<_4.buttons.length;i++){
var td=$("<td></td>").appendTo(tr);
var _b=_4.buttons[i];
var t=$("<a href=\"javascript:void(0)\"></a>").html($.isFunction(_b.text)?_b.text(_2):_b.text).appendTo(td);
t.bind("click",{target:_2,handler:_b.handler},function(e){
e.data.handler.call(this,e.data.target);
});
}
tr.find("td").css("width",(100/_4.buttons.length)+"%");
};
function _5(){
var _c=$(_2).combo("panel");
var cc=_c.children("div.datebox-calendar-inner");
_c.children()._outerWidth(_c.width());
_3.calendar.appendTo(cc);
_3.calendar[0].target=_2;
if(_4.panelHeight!="auto"){
var _d=_c.height();
_c.children().not(cc).each(function(){
_d-=$(this).outerHeight();
});
cc._outerHeight(_d);
}
_3.calendar.calendar("resize");
};
};
function _e(_f,q){
_10(_f,q,true);
};
function _11(_12){
var _13=$.data(_12,"datebox");
var _14=_13.options;
var _15=_13.calendar.calendar("options").current;
if(_15){
_10(_12,_14.formatter.call(_12,_15));
$(_12).combo("hidePanel");
}
};
function _10(_16,_17,_18){
var _19=$.data(_16,"datebox");
var _1a=_19.options;
var _1b=_19.calendar;
$(_16).combo("setValue",_17);
_1b.calendar("moveTo",_1a.parser.call(_16,_17));
if(!_18){
if(_17){
_17=_1a.formatter.call(_16,_1b.calendar("options").current);
$(_16).combo("setValue",_17).combo("setText",_17);
}else{
$(_16).combo("setText",_17);
}
}
};
$.fn.datebox=function(_1c,_1d){
if(typeof _1c=="string"){
var _1e=$.fn.datebox.methods[_1c];
if(_1e){
return _1e(this,_1d);
}else{
return this.combo(_1c,_1d);
}
}
_1c=_1c||{};
return this.each(function(){
var _1f=$.data(this,"datebox");
if(_1f){
$.extend(_1f.options,_1c);
}else{
$.data(this,"datebox",{options:$.extend({},$.fn.datebox.defaults,$.fn.datebox.parseOptions(this),_1c)});
}
_1(this);
});
};
$.fn.datebox.methods={options:function(jq){
var _20=jq.combo("options");
return $.extend($.data(jq[0],"datebox").options,{originalValue:_20.originalValue,disabled:_20.disabled,readonly:_20.readonly});
},calendar:function(jq){
return $.data(jq[0],"datebox").calendar;
},setValue:function(jq,_21){
return jq.each(function(){
_10(this,_21);
});
},reset:function(jq){
return jq.each(function(){
var _22=$(this).datebox("options");
$(this).datebox("setValue",_22.originalValue);
});
}};
$.fn.datebox.parseOptions=function(_23){
return $.extend({},$.fn.combo.parseOptions(_23),$.parser.parseOptions(_23,["sharedCalendar"]));
};
$.fn.datebox.defaults=$.extend({},$.fn.combo.defaults,{panelWidth:180,panelHeight:"auto",sharedCalendar:null,keyHandler:{up:function(e){
},down:function(e){
},left:function(e){
},right:function(e){
},enter:function(e){
_11(this);
},query:function(q,e){
_e(this,q);
}},currentText:"Today",closeText:"Close",okText:"Ok",buttons:[{text:function(_24){
return $(_24).datebox("options").currentText;
},handler:function(_25){
$(_25).datebox("calendar").calendar({year:new Date().getFullYear(),month:new Date().getMonth()+1,current:new Date()});
_11(_25);
}},{text:function(_26){
return $(_26).datebox("options").closeText;
},handler:function(_27){
$(this).closest("div.combo-panel").panel("close");
}}],formatter:function(_28){
var y=_28.getFullYear();
var m=_28.getMonth()+1;
var d=_28.getDate();
return m+"/"+d+"/"+y;
},parser:function(s){
var t=Date.parse(s);
if(!isNaN(t)){
return new Date(t);
}else{
return new Date();
}
},onSelect:function(_29){
}});
})(jQuery);

