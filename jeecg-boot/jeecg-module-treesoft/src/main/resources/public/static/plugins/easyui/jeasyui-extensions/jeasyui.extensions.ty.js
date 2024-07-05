/**
 * treegrid点击行,级联选中
 */
$.extend($.fn.treegrid.methods,{  
    
     cascadeCheck : function(target,param){  
         var opts = $.data(target[0], "treegrid").options;  
         if(opts.singleSelect)  
             return;  
         var idField = opts.idField;//这里的idField其实就是API里方法的id参数  
         var status = false;//用来标记当前节点的状态，true:勾选，false:未勾选  
         var selectNodes = $(target).treegrid('getSelections');//获取当前选中项  
         for(var i=0;i<selectNodes.length;i++){  
             if(selectNodes[i][idField]==param.id)  
                 status = true;
         }  
         //级联选择父节点  
         selectParent(target[0],param.id,idField,status);  
         selectChildren(target[0],param.id,idField,param.deepCascade,status);  

         function selectParent(target,id,idField,status){  
             var parent = $(target).treegrid('getParent',id);  
             if(parent){  
                 var parentId = parent[idField];  
                 if(status)  
                     $(target).treegrid('select',parentId);  
                 else {
                     if (!IsParentHasSelectedChildren(target,parentId,idField)){
                         $(target).treegrid('unselect',parentId);
                     }
                 }
                 selectParent(target,parentId,idField,status);  
             }  
         }  


         function selectChildren(target,id,idField,deepCascade,status){  
             //深度级联时先展开节点  
             if(!status&&deepCascade)  
                 $(target).treegrid('expand',id);  
             //根据ID获取下层孩子节点  
             var children = $(target).treegrid('getChildren',id);  
             for(var i=0;i<children.length;i++){  
                 var childId = children[i][idField];  
                 if(status)  
                     $(target).treegrid('select',childId);  
                 else 
                     $(target).treegrid('unselect',childId);  
                 selectChildren(target,childId,idField,deepCascade,status);//递归选择子节点  
             }  
         }

         /** 
          * 级联判断父节点 的子节点是否有被选择的
          */ 
         function IsParentHasSelectedChildren(target,id,idField,status){
             var count=0;
             var children = $(target).treegrid('getChildren',id);    
             var selectNodes = $(target).treegrid('getSelections');//获取当前选中项     
             var p=$(target).treegrid('find',id);  
             //注意,这里的children是指后代所有子节点,不是指儿子节点,所以要加上children[i]['_parentId']==p[idField]过滤出儿子节点
             for(var i=0;i<children.length ;i++  ){
                 var childId = children[i][idField];  
                 for(var j=0;j<selectNodes.length;j++){  
                     if(selectNodes[j][idField]==childId && children[i]['_parentId']==p[idField])  
                         count++;
                 }
             }
             //注意,click 函数在unselect事件之前运行,这里需要减去自己
             return count>0;
         }
     }  

 });

/**
 * 扩展tree，使其支持平滑数据格式
 */
$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField,iconCls;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		iconCls=opt.iconCls||'iconCls';
		var i, l, treeData = [], tmpMap = [];
		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				data[i]['iconCls'] = data[i][iconCls];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				data[i]['iconCls'] = data[i][iconCls];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/**
 * @author 孙宇
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展treegrid，使其支持平滑数据格式
 */
$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
	var opt = $(this).data().treegrid.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		iconCls=opt.iconCls||'iconCls';
		var i, l, treeData = [], tmpMap = [];
		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				data[i]['iconCls'] = data[i][iconCls];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				data[i]['iconCls'] = data[i][iconCls];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/**
 * @author 孙宇
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展combotree，使其支持平滑数据格式
 */
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;


/**
 * 是否选择行数据
 */
function rowIsNull(row){
	if(row){
		return false;
	}else{
		parent.$.messager.show({ title : "提示",msg: "请选择行数据！", position: "bottomRight" });
		return true;
	}
}

/**
 * ajax返回提示
 * @param data	返回的数据
 * @param dg datagrid
 * @param d	弹窗
 * @returns {Boolean} ajax是否成功
 */
function successTip(data,dg,d){
	if(data=='success'){
		if(dg!=null)
			dg.datagrid('reload');
		if(d!=null)
			d.panel('close');
		parent.$.messager.show({ title : "提示",msg: "操作成功！", position: "bottomRight" });
		return true;
	}else{
		parent.$.messager.alert(data);
		return false;
	}  
}
