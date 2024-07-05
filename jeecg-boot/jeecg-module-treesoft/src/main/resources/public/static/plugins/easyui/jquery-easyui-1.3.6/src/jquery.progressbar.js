/**
 * jQuery EasyUI 1.3.6
 * 
 * Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
 * To use it on other terms please contact us at info@jeasyui.com
 *
 */
/**
 * progressbar - jQuery EasyUI
 * 
 * Dependencies:
 * 	 none
 * 
 */
(function($){
	function init(target){
		$(target).addClass('progressbar');
		$(target).html('<div class="progressbar-text"></div><div class="progressbar-value"><div class="progressbar-text"></div></div>');
		return $(target);
	}
	
	function setSize(target,width){
		var opts = $.data(target, 'progressbar').options;
		var bar = $.data(target, 'progressbar').bar;
		if (width) opts.width = width;
		bar._outerWidth(opts.width)._outerHeight(opts.height);
		
		bar.find('div.progressbar-text').width(bar.width());
		bar.find('div.progressbar-text,div.progressbar-value').css({
			height: bar.height()+'px',
			lineHeight: bar.height()+'px'
		});
	}
	
	$.fn.progressbar = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.progressbar.methods[options];
			if (method){
				return method(this, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'progressbar');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'progressbar', {
					options: $.extend({}, $.fn.progressbar.defaults, $.fn.progressbar.parseOptions(this), options),
					bar: init(this)
				});
			}
			$(this).progressbar('setValue', state.options.value);
			setSize(this);
		});
	};
	
	$.fn.progressbar.methods = {
		options: function(jq){
			return $.data(jq[0], 'progressbar').options;
		},
		resize: function(jq, width){
			return jq.each(function(){
				setSize(this, width);
			});
		},
		getValue: function(jq){
			return $.data(jq[0], 'progressbar').options.value;
		},
		setValue: function(jq, value){
			if (value < 0) value = 0;
			if (value > 100) value = 100;
			return jq.each(function(){
				var opts = $.data(this, 'progressbar').options;
				var text = opts.text.replace(/{value}/, value);
				var oldValue = opts.value;
				opts.value = value;
				$(this).find('div.progressbar-value').width(value+'%');
				$(this).find('div.progressbar-text').html(text);
				if (oldValue != value){
					opts.onChange.call(this, value, oldValue);
				}
			});
		}
	};
	
	$.fn.progressbar.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, ['width','height','text',{value:'number'}]));
	};
	
	$.fn.progressbar.defaults = {
		width: 'auto',
		height: 22,
		value: 0,	// percentage value
		text: '{value}%',
		onChange:function(newValue,oldValue){}
	};
})(jQuery);
