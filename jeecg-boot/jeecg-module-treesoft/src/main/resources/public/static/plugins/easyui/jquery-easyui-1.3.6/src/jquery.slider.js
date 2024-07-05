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
 * slider - jQuery EasyUI
 * 
 * Dependencies:
 *  draggable
 * 
 */
(function($){
	function init(target){
		var slider = $('<div class="slider">' +
				'<div class="slider-inner">' +
				'<a href="javascript:void(0)" class="slider-handle"></a>' +
				'<span class="slider-tip"></span>' +
				'</div>' +
				'<div class="slider-rule"></div>' +
				'<div class="slider-rulelabel"></div>' +
				'<div style="clear:both"></div>' +
				'<input type="hidden" class="slider-value">' +
				'</div>').insertAfter(target);
		var t = $(target);
		t.addClass('slider-f').hide();
		var name = t.attr('name');
		if (name){
			slider.find('input.slider-value').attr('name', name);
			t.removeAttr('name').attr('sliderName', name);
		}
		return slider;
	}
	
	/**
	 * set the slider size, for vertical slider, the height property is required
	 */
	function setSize(target, param){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		
		if (param){
			if (param.width) opts.width = param.width;
			if (param.height) opts.height = param.height;
		}
		if (opts.mode == 'h'){
			slider.css('height', '');
			slider.children('div').css('height', '');
			if (!isNaN(opts.width)){
				slider.width(opts.width);
			}
		} else {
			slider.css('width', '');
			slider.children('div').css('width', '');
			if (!isNaN(opts.height)){
				slider.height(opts.height);
				slider.find('div.slider-rule').height(opts.height);
				slider.find('div.slider-rulelabel').height(opts.height);
				slider.find('div.slider-inner')._outerHeight(opts.height);
			}
		}
		initValue(target);
	}
	
	/**
	 * show slider rule if needed
	 */
	function showRule(target){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		
		var aa = opts.mode == 'h' ? opts.rule : opts.rule.slice(0).reverse();
		if (opts.reversed){
			aa = aa.slice(0).reverse();
		}
		_build(aa);
		
		function _build(aa){
			var rule = slider.find('div.slider-rule');
			var label = slider.find('div.slider-rulelabel');
			rule.empty();
			label.empty();
			for(var i=0; i<aa.length; i++){
				var distance = i*100/(aa.length-1)+'%';
				var span = $('<span></span>').appendTo(rule);
				span.css((opts.mode=='h'?'left':'top'), distance);
				
				// show the labels
				if (aa[i] != '|'){
					span = $('<span></span>').appendTo(label);
					span.html(aa[i]);
					if (opts.mode == 'h'){
						span.css({
							left: distance,
							marginLeft: -Math.round(span.outerWidth()/2)
						});
					} else {
						span.css({
							top: distance,
							marginTop: -Math.round(span.outerHeight()/2)
						});
					}
				}
			}
		}
	}
	
	/**
	 * build the slider and set some properties
	 */
	function buildSlider(target){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		
		slider.removeClass('slider-h slider-v slider-disabled');
		slider.addClass(opts.mode == 'h' ? 'slider-h' : 'slider-v');
		slider.addClass(opts.disabled ? 'slider-disabled' : '');
		
		slider.find('a.slider-handle').draggable({
			axis:opts.mode,
			cursor:'pointer',
			disabled: opts.disabled,
			onDrag:function(e){
				var left = e.data.left;
				var width = slider.width();
				if (opts.mode!='h'){
					left = e.data.top;
					width = slider.height();
				}
				if (left < 0 || left > width) {
					return false;
				} else {
					var value = pos2value(target, left);
					adjustValue(value);
					return false;
				}
			},
			onBeforeDrag:function(){
				state.isDragging = true;
			},
			onStartDrag:function(){
				opts.onSlideStart.call(target, opts.value);
			},
			onStopDrag:function(e){
				var value = pos2value(target, (opts.mode=='h'?e.data.left:e.data.top));
				adjustValue(value);
				opts.onSlideEnd.call(target, opts.value);
				opts.onComplete.call(target, opts.value);
				state.isDragging = false;
			}
		});
		slider.find('div.slider-inner').unbind('.slider').bind('mousedown.slider', function(e){
			if (state.isDragging){return}
			var pos = $(this).offset();
			var value = pos2value(target, (opts.mode=='h'?(e.pageX-pos.left):(e.pageY-pos.top)));
			adjustValue(value);
			opts.onComplete.call(target, opts.value);
		});
		
		function adjustValue(value){
			var s = Math.abs(value % opts.step);
			if (s < opts.step/2){
				value -= s;
			} else {
				value = value - s + opts.step;
			}
			setValue(target, value);
		}
	}
	
	/**
	 * set a specified value to slider
	 */
	function setValue(target, value){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		var oldValue = opts.value;
		if (value < opts.min) value = opts.min;
		if (value > opts.max) value = opts.max;
		
		opts.value = value;
		$(target).val(value);
		slider.find('input.slider-value').val(value);
		
		var pos = value2pos(target, value);
		var tip = slider.find('.slider-tip');
		if (opts.showTip){
			tip.show();
			tip.html(opts.tipFormatter.call(target, opts.value));
		} else {
			tip.hide();
		}
		
		if (opts.mode == 'h'){
			var style = 'left:'+pos+'px;';
			slider.find('.slider-handle').attr('style', style);
			tip.attr('style', style +  'margin-left:' + (-Math.round(tip.outerWidth()/2)) + 'px');
		} else {
			var style = 'top:' + pos + 'px;';
			slider.find('.slider-handle').attr('style', style);
			tip.attr('style', style + 'margin-left:' + (-Math.round(tip.outerWidth())) + 'px');
		}
		
		if (oldValue != value){
			opts.onChange.call(target, value, oldValue);
		}
	}
	
	function initValue(target){
		var opts = $.data(target, 'slider').options;
		var fn = opts.onChange;
		opts.onChange = function(){};
		setValue(target, opts.value);
		opts.onChange = fn;
	}
	
	/**
	 * translate value to slider position
	 */
//	function value2pos(target, value){
//		var state = $.data(target, 'slider');
//		var opts = state.options;
//		var slider = state.slider;
//		if (opts.mode == 'h'){
//			var pos = (value-opts.min)/(opts.max-opts.min)*slider.width();
//			if (opts.reversed){
//				pos = slider.width() - pos;
//			}
//		} else {
//			var pos = slider.height() - (value-opts.min)/(opts.max-opts.min)*slider.height();
//			if (opts.reversed){
//				pos = slider.height() - pos;
//			}
//		}
//		return pos.toFixed(0);
//	}
	function value2pos(target, value){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		var size = opts.mode == 'h' ? slider.width() : slider.height();
		var pos = opts.converter.toPosition.call(target, value, size);
		if (opts.mode == 'v'){
			pos = slider.height() - pos;
		}
		if (opts.reversed){
			pos = size - pos;
		}
		return pos.toFixed(0);
	}
	
	/**
	 * translate slider position to value
	 */
//	function pos2value(target, pos){
//		var state = $.data(target, 'slider');
//		var opts = state.options;
//		var slider = state.slider;
//		if (opts.mode == 'h'){
//			var value = opts.min + (opts.max-opts.min)*(pos/slider.width());
//		} else {
//			var value = opts.min + (opts.max-opts.min)*((slider.height()-pos)/slider.height());
//		}
//		return opts.reversed ? opts.max - value.toFixed(0) : value.toFixed(0);
//	}
	function pos2value(target, pos){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		var size = opts.mode == 'h' ? slider.width() : slider.height();
		var value = opts.converter.toValue.call(target, opts.mode=='h'?(opts.reversed?(size-pos):pos):(size-pos), size);
		return value.toFixed(0);
//		var value = opts.converter.toValue.call(target, opts.mode=='h'?pos:(size-pos), size);
//		return opts.reversed ? opts.max - value.toFixed(0) : value.toFixed(0);
	}
	
	$.fn.slider = function(options, param){
		if (typeof options == 'string'){
			return $.fn.slider.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'slider');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'slider', {
					options: $.extend({}, $.fn.slider.defaults, $.fn.slider.parseOptions(this), options),
					slider: init(this)
				});
				$(this).removeAttr('disabled');
			}
			
			var opts = state.options;
			opts.min = parseFloat(opts.min);
			opts.max = parseFloat(opts.max);
			opts.value = parseFloat(opts.value);
			opts.step = parseFloat(opts.step);
			opts.originalValue = opts.value;
			
			buildSlider(this);
			showRule(this);
			setSize(this);
		});
	};
	
	$.fn.slider.methods = {
		options: function(jq){
			return $.data(jq[0], 'slider').options;
		},
		destroy: function(jq){
			return jq.each(function(){
				$.data(this, 'slider').slider.remove();
				$(this).remove();
			});
		},
		resize: function(jq, param){
			return jq.each(function(){
				setSize(this, param);
			});
		},
		getValue: function(jq){
			return jq.slider('options').value;
		},
		setValue: function(jq, value){
			return jq.each(function(){
				setValue(this, value);
			});
		},
		clear: function(jq){
			return jq.each(function(){
				var opts = $(this).slider('options');
				setValue(this, opts.min);
			});
		},
		reset: function(jq){
			return jq.each(function(){
				var opts = $(this).slider('options');
				setValue(this, opts.originalValue);
			});
		},
		enable: function(jq){
			return jq.each(function(){
				$.data(this, 'slider').options.disabled = false;
				buildSlider(this);
			});
		},
		disable: function(jq){
			return jq.each(function(){
				$.data(this, 'slider').options.disabled = true;
				buildSlider(this);
			});
		}
	};
	
	$.fn.slider.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [
			'width','height','mode',{reversed:'boolean',showTip:'boolean',min:'number',max:'number',step:'number'}
		]), {
			value: (t.val() || undefined),
			disabled: (t.attr('disabled') ? true : undefined),
			rule: (t.attr('rule') ? eval(t.attr('rule')) : undefined)
		});
	};
	
	$.fn.slider.defaults = {
		width: 'auto',
		height: 'auto',
		mode: 'h',	// 'h'(horizontal) or 'v'(vertical)
		reversed: false,
		showTip: false,
		disabled: false,
		value: 0,
		min: 0,
		max: 100,
		step: 1,
		rule: [],	// [0,'|',100]
		tipFormatter: function(value){return value},
		converter:{
			toPosition:function(value, size){
				var opts = $(this).slider('options');
				return (value-opts.min)/(opts.max-opts.min)*size;
			},
			toValue:function(pos, size){
				var opts = $(this).slider('options');
				return opts.min + (opts.max-opts.min)*(pos/size);
			}
		},
		onChange: function(value, oldValue){},
		onSlideStart: function(value){},
		onSlideEnd: function(value){},
		onComplete: function(value){}
	};
})(jQuery);
