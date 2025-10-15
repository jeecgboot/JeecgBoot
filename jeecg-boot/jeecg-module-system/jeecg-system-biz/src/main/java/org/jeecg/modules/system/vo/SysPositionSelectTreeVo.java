package org.jeecg.modules.system.vo;

import lombok.Data;
import org.jeecg.common.constant.enums.DepartCategoryEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;

import java.util.ArrayList;
import java.util.List;

/**
* @Description: 岗位下拉选择树
*
* @author: wangshuai
* @date: 2025/8/18 9:40
*/
@Data
public class SysPositionSelectTreeVo {
    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String title;
    private boolean isLeaf;
    /** 是否显示复选框 */
    private boolean checkable;
    /** 是否禁用 */
    private boolean disabled;
    // 以下所有字段均与SysDepart相同
    private String id;
    /**父级id*/
    private String parentId;
    /**部门类别*/
    private String orgCategory;
    /**部门编码*/
    private String orgCode;
    
    private List<SysPositionSelectTreeVo> children = new ArrayList<>();

    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param sysDepart
     */
    public SysPositionSelectTreeVo(SysDepart sysDepart) {
        this.value = sysDepart.getId();
        this.title = sysDepart.getDepartName();
        this.id = sysDepart.getId();
        this.parentId = sysDepart.getParentId();
        this.orgCategory = sysDepart.getOrgCategory();
        this.orgCode = sysDepart.getOrgCode();
        if(0 == sysDepart.getIzLeaf()){
            this.isLeaf = false;
        }else{
            this.isLeaf = true;
        }
        if(DepartCategoryEnum.DEPART_CATEGORY_POST.getValue().equals(sysDepart.getOrgCategory())){
            this.checkable = true;
            this.disabled = false;
        }else{
            this.checkable = false;
            this.disabled = true;
        }
    }

    public SysPositionSelectTreeVo(SysDepartPositionVo position) {
        this.value = position.getId();
        if(oConvertUtils.isNotEmpty(position.getDepartName())){
            this.title = position.getPositionName() + "("+position.getDepartName()+")";
        }else{
            this.title = position.getPositionName();
        }
        this.id = position.getId();
        this.parentId = position.getDepPostParentId();
        this.orgCategory = "3";
        if(0 == position.getIzLeaf()){
            this.isLeaf = false;
        }else{
            this.isLeaf = true;
        }
        this.checkable = true;
        this.disabled = false;
    }
}
