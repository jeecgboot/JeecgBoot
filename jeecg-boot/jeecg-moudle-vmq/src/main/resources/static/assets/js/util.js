(function($){$.fn.navList=function(){var $this=$(this);$a=$this.find('a'),b=[];$a.each(function(){var $this=$(this),indent=Math.max(0,$this.parents('li').length-1),href=$this.attr('href'),target=$this.attr('target');b.push('<a '+
'class="link depth-'+indent+'"'+
((typeof target!=='undefined'&&target!='')?' target="'+target+'"':'')+
((typeof href!=='undefined'&&href!='')?' href="'+href+'"':'')+
'>'+
'<span class="indent-'+indent+'"></span>'+
$this.text()+
'</a>');});return b.join('');};$.fn.panel=function(userConfig){if(this.length==0)
return $this;if(this.length>1){for(var i=0;i<this.length;i++)
$(this[i]).panel(userConfig);return $this;}
var $this=$(this),$body=$('body'),$window=$(window),id=$this.attr('id'),config;config=$.extend({delay:0,hideOnClick:false,hideOnEscape:false,hideOnSwipe:false,resetScroll:false,resetForms:false,side:null,target:$this,visibleClass:'visible'},userConfig);if(typeof config.target!='jQuery')
config.target=$(config.target);$this._hide=function(event){if(!config.target.hasClass(config.visibleClass))
return;if(event){event.preventDefault();event.stopPropagation();}
config.target.removeClass(config.visibleClass);window.setTimeout(function(){if(config.resetScroll)
$this.scrollTop(0);if(config.resetForms)
$this.find('form').each(function(){this.reset();});},config.delay);};$this.css('-ms-overflow-style','-ms-autohiding-scrollbar').css('-webkit-overflow-scrolling','touch');if(config.hideOnClick){$this.find('a').css('-webkit-tap-highlight-color','rgba(0,0,0,0)');$this.on('click','a',function(event){var $a=$(this),href=$a.attr('href'),target=$a.attr('target');if(!href||href=='#'||href==''||href=='#'+id)
return;event.preventDefault();event.stopPropagation();$this._hide();window.setTimeout(function(){if(target=='_blank')
window.open(href);else
window.location.href=href;},config.delay+10);});}
$this.on('touchstart',function(event){$this.touchPosX=event.originalEvent.touches[0].pageX;$this.touchPosY=event.originalEvent.touches[0].pageY;})
$this.on('touchmove',function(event){if($this.touchPosX===null||$this.touchPosY===null)
return;var diffX=$this.touchPosX-event.originalEvent.touches[0].pageX,diffY=$this.touchPosY-event.originalEvent.touches[0].pageY,th=$this.outerHeight(),ts=($this.get(0).scrollHeight-$this.scrollTop());if(config.hideOnSwipe){var result=false,boundary=20,delta=50;switch(config.side){case 'left':result=(diffY<boundary&&diffY>(-1*boundary))&&(diffX>delta);break;case 'right':result=(diffY<boundary&&diffY>(-1*boundary))&&(diffX<(-1*delta));break;case 'top':result=(diffX<boundary&&diffX>(-1*boundary))&&(diffY>delta);break;case 'bottom':result=(diffX<boundary&&diffX>(-1*boundary))&&(diffY<(-1*delta));break;default:break;}
if(result){$this.touchPosX=null;$this.touchPosY=null;$this._hide();return false;}}
if(($this.scrollTop()<0&&diffY<0)||(ts>(th-2)&&ts<(th+2)&&diffY>0)){event.preventDefault();event.stopPropagation();}});$this.on('click touchend touchstart touchmove',function(event){event.stopPropagation();});$this.on('click','a[href="#'+id+'"]',function(event){event.preventDefault();event.stopPropagation();config.target.removeClass(config.visibleClass);});$body.on('click touchend',function(event){$this._hide(event);});$body.on('click','a[href="#'+id+'"]',function(event){event.preventDefault();event.stopPropagation();config.target.toggleClass(config.visibleClass);});if(config.hideOnEscape)
$window.on('keydown',function(event){if(event.keyCode==27)
$this._hide(event);});return $this;};$.fn.placeholder=function(){if(typeof(document.createElement('input')).placeholder!='undefined')
return $(this);if(this.length==0)
return $this;if(this.length>1){for(var i=0;i<this.length;i++)
$(this[i]).placeholder();return $this;}
var $this=$(this);$this.find('input[type=text],textarea').each(function(){var i=$(this);if(i.val()==''||i.val()==i.attr('placeholder'))
i.addClass('polyfill-placeholder').val(i.attr('placeholder'));}).on('blur',function(){var i=$(this);if(i.attr('name').match(/-polyfill-field$/))
return;if(i.val()=='')
i.addClass('polyfill-placeholder').val(i.attr('placeholder'));}).on('focus',function(){var i=$(this);if(i.attr('name').match(/-polyfill-field$/))
return;if(i.val()==i.attr('placeholder'))
i.removeClass('polyfill-placeholder').val('');});$this.find('input[type=password]').each(function(){var i=$(this);var x=$($('<div>').append(i.clone()).remove().html().replace(/type="password"/i,'type="text"').replace(/type=password/i,'type=text'));if(i.attr('id')!='')
x.attr('id',i.attr('id')+'-polyfill-field');if(i.attr('name')!='')
x.attr('name',i.attr('name')+'-polyfill-field');x.addClass('polyfill-placeholder').val(x.attr('placeholder')).insertAfter(i);if(i.val()=='')
i.hide();else
x.hide();i.on('blur',function(event){event.preventDefault();var x=i.parent().find('input[name='+i.attr('name')+'-polyfill-field]');if(i.val()==''){i.hide();x.show();}});x.on('focus',function(event){event.preventDefault();var i=x.parent().find('input[name='+x.attr('name').replace('-polyfill-field','')+']');x.hide();i.show().focus();}).on('keypress',function(event){event.preventDefault();x.val('');});});$this.on('submit',function(){$this.find('input[type=text],input[type=password],textarea').each(function(event){var i=$(this);if(i.attr('name').match(/-polyfill-field$/))
i.attr('name','');if(i.val()==i.attr('placeholder')){i.removeClass('polyfill-placeholder');i.val('');}});}).on('reset',function(event){event.preventDefault();$this.find('select').val($('option:first').val());$this.find('input,textarea').each(function(){var i=$(this),x;i.removeClass('polyfill-placeholder');switch(this.type){case 'submit':case 'reset':break;case 'password':i.val(i.attr('defaultValue'));x=i.parent().find('input[name='+i.attr('name')+'-polyfill-field]');if(i.val()==''){i.hide();x.show();}
else{i.show();x.hide();}
break;case 'checkbox':case 'radio':i.attr('checked',i.attr('defaultValue'));break;case 'text':case 'textarea':i.val(i.attr('defaultValue'));if(i.val()==''){i.addClass('polyfill-placeholder');i.val(i.attr('placeholder'));}
break;default:i.val(i.attr('defaultValue'));break;}});});return $this;};$.prioritize=function($elements,condition){var key='__prioritize';if(typeof $elements!='jQuery')
$elements=$($elements);$elements.each(function(){var $e=$(this),$p,$parent=$e.parent();if($parent.length==0)
return;if(!$e.data(key)){if(!condition)
return;$p=$e.prev();if($p.length==0)
return;$e.prependTo($parent);$e.data(key,$p);}
else{if(condition)
return;$p=$e.data(key);$e.insertAfter($p);$e.removeData(key);}});};})(jQuery);