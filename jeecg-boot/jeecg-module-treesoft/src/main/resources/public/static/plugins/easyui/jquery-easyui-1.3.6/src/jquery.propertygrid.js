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
 * propertygrid - jQuery EasyUI
 * 
 * Dependencies:
 * 	 datagrid
 * 
 */
(function($){
	var currTarget;
	
	function buildGrid(target){
		var state = $.data(target, 'propertygrid');
		var opts = $.data(target, 'propertygrid').options;
		$(target).datagrid($.extend({}, opts, {
			cls:'propertygrid',
			view:(opts.showGroup ? opts.groupView : opts.view),
			onClickRow:function(index, row){
				if (currTarget != this){
//					leaveCurrRow();
					stopEditing(currTarget);
					currTarget = this;
				}
				if (opts.editIndex != index && row.editor){
					var col = $(this).datagrid('getColumnOption', "value");
					col.editor = row.editor;
//					leaveCurrRow();
					stopEditing(currTarget);
					$(this).datagrid('beginEdit', index);
					$(this).datagrid('getEditors', index)[0].target.focus();
					opts.editIndex = index;
				}
				opts.onClickRow.call(target, index, row);
			},
			loadFilter:function(data){
				stopEditing(this);
				return opts.loadFilter.call(this, data);
			}
		}));
		$(document).unbind('.propertygrid').bind('mousedown.propertygrid', function(e){
			var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
//			var p = $(e.target).closest('div.propertygrid,div.combo-panel');
			if (p.length){return;}
			stopEditing(currTarget);
			currTarget = undefined;
		});
		
//		function leaveCurrRow(){
//			var t = $(currTarget);
//			if (!t.length){return;}
//			var opts = $.data(currTarget, 'propertygrid').options;
//			var index = opts.editIndex;
//			if (index == undefined){return;}
//			var ed = t.datagrid('getEditors', index)[0];
//			if (ed){
//				ed.target.blur();
//				if (t.datagrid('validateRow', index)){
//					t.datagrid('endEdit', index);
//				} else {
//					t.datagrid('cancelEdit', index);
//				}
//			}
//			opts.editIndex = undefined;
//		}
	}
	
	function stopEditing(target){
		var t = $(target);
		if (!t.length){return}
		var opts = $.data(target, 'propertygrid').options;
		var index = opts.editIndex;
		if (index == undefined){return;}
		var ed = t.datagrid('getEditors', index)[0];
		if (ed){
			ed.target.blur();
			if (t.datagrid('validateRow', index)){
				t.datagrid('endEdit', index);
			} else {
				t.datagrid('cancelEdit', index);
			}
		}
		opts.editIndex = undefined;
	}
	
	$.fn.propertygrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.propertygrid.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'propertygrid');
			if (state){
				$.extend(state.options, options);
			} else {
				var opts = $.extend({}, $.fn.propertygrid.defaults, $.fn.propertygrid.parseOptions(this), options);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.columns = $.extend(true, [], opts.columns);
				$.data(this, 'propertygrid', {
					options: opts
				});
			}
			buildGrid(this);
		});
	}
	
	$.fn.propertygrid.methods = {
		options: function(jq){
			return $.data(jq[0], 'propertygrid').options;
		}
	};
	
	$.fn.propertygrid.parseOptions = function(target){
		return $.extend({}, $.fn.datagrid.parseOptions(target), $.parser.parseOptions(target,[{showGroup:'boolean'}]));
	};
	
	// the group view definition
	var groupview = $.extend({}, $.fn.datagrid.defaults.view, {
		render: function(target, container, frozen){
			var table = [];
			var groups = this.groups;
			for(var i=0; i<groups.length; i++){
				table.push(this.renderGroup.call(this, target, i, groups[i], frozen));
			}
			$(container).html(table.join(''));
		},
		
		renderGroup: function(target, groupIndex, group, frozen){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var fields = $(target).datagrid('getColumnFields', frozen);
			
			var table = [];
			table.push('<div class="datagrid-group" group-index=' + groupIndex + '>');
			table.push('<table cellspacing="0" cellpadding="0" border="0" style="height:100%"><tbody>');
			table.push('<tr>');
			if ((frozen && (opts.rownumbers || opts.frozenColumns.length)) ||
					(!frozen && !(opts.rownumbers || opts.frozenColumns.length))){
				table.push('<td style="border:0;text-align:center;width:25px"><span class="datagrid-row-expander datagrid-row-collapse" style="display:inline-block;width:16px;height:16px;cursor:pointer">&nbsp;</span></td>');
			}
			table.push('<td style="border:0;">');
			if (!frozen){
				table.push('<span class="datagrid-group-title">');
				table.push(opts.groupFormatter.call(target, group.value, group.rows));
				table.push('</span>');
			}
			table.push('</td>');
			table.push('</tr>');
			table.push('</tbody></table>');
			table.push('</div>');
			
			table.push('<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>');
			var index = group.startIndex;
			for(var j=0; j<group.rows.length; j++) {
				var css = opts.rowStyler ? opts.rowStyler.call(target, index, group.rows[j]) : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (css){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				
				var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
				var style = styleValue ? 'style="' + styleValue + '"' : '';
				var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
				table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
				table.push(this.renderRow.call(this, target, fields, frozen, index, group.rows[j]));
				table.push('</tr>');
				index++;
			}
			table.push('</tbody></table>');
			return table.join('');
		},
		
		bindEvents: function(target){
			var state = $.data(target, 'datagrid');
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
			body.unbind('click').bind('click', function(e){
				var tt = $(e.target);
				var expander = tt.closest('span.datagrid-row-expander');
				if (expander.length){
					var gindex = expander.closest('div.datagrid-group').attr('group-index');
					if (expander.hasClass('datagrid-row-collapse')){
						$(target).datagrid('collapseGroup', gindex);
					} else {
						$(target).datagrid('expandGroup', gindex);
					}
				} else {
					clickHandler(e);
				}
				e.stopPropagation();
			});
		},
		
		onBeforeRender: function(target, rows){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			
			initCss();
			
			var groups = [];
			for(var i=0; i<rows.length; i++){
				var row = rows[i];
				var group = getGroup(row[opts.groupField]);
				if (!group){
					group = {
						value: row[opts.groupField],
						rows: [row]
					};
					groups.push(group);
				} else {
					group.rows.push(row);
				}
			}
			
			var index = 0;
			var newRows = [];
			for(var i=0; i<groups.length; i++){
				var group = groups[i];
				group.startIndex = index;
				index += group.rows.length;
				newRows = newRows.concat(group.rows);
			}
			
			state.data.rows = newRows;
			this.groups = groups;
			
			var that = this;
			setTimeout(function(){
				that.bindEvents(target);
			},0);
			
			function getGroup(value){
				for(var i=0; i<groups.length; i++){
					var group = groups[i];
					if (group.value == value){
						return group;
					}
				}
				return null;
			}
			function initCss(){
				if (!$('#datagrid-group-style').length){
					$('head').append(
						'<style id="datagrid-group-style">' +
						'.datagrid-group{height:25px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;}' +
						'</style>'
					);
				}
			}
		}
	});

	$.extend($.fn.datagrid.methods, {
	    expandGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-expand')){
	                expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
	                group.next('table').show();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    },
	    collapseGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-collapse')){
	                expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
	                group.next('table').hide();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    }
	});
	// end of group view definition
	
	$.fn.propertygrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		singleSelect:true,
		remoteSort:false,
		fitColumns:true,
		loadMsg:'',
		frozenColumns:[[
		    {field:'f',width:16,resizable:false}
		]],
		columns:[[
		    {field:'name',title:'Name',width:100,sortable:true},
		    {field:'value',title:'Value',width:100,resizable:false}
		]],
		
		showGroup:false,
		groupView:groupview,
		groupField:'group',
		groupFormatter:function(fvalue,rows){return fvalue}
	});
})(jQuery);
