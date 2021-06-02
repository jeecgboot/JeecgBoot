package org.jeecg.modules.system.vo.thirdapp;

import com.jeecg.qywx.api.department.vo.Department;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信树结构的部门
 *
 * @author sunjianlei
 */
public class JwDepartmentTreeVo extends Department {

    private List<JwDepartmentTreeVo> children;

    public List<JwDepartmentTreeVo> getChildren() {
        return children;
    }

    public JwDepartmentTreeVo setChildren(List<JwDepartmentTreeVo> children) {
        this.children = children;
        return this;
    }

    public JwDepartmentTreeVo(Department department) {
        BeanUtils.copyProperties(department, this);
    }

    /**
     * 是否有子项
     */
    public boolean hasChildren() {
        return children != null && children.size() > 0;
    }

    @Override
    public String toString() {
        return "JwDepartmentTree{" +
                "children=" + children +
                "} " + super.toString();
    }

    /**
     * 静态辅助方法，将list转为tree结构
     */
    public static List<JwDepartmentTreeVo> listToTree(List<Department> allDepartment) {
        // 先找出所有的父级
        List<JwDepartmentTreeVo> treeList = getByParentId("1", allDepartment);
        getChildrenRecursion(treeList, allDepartment);
        return treeList;
    }

    private static List<JwDepartmentTreeVo> getByParentId(String parentId, List<Department> allDepartment) {
        List<JwDepartmentTreeVo> list = new ArrayList<>();
        for (Department department : allDepartment) {
            if (parentId.equals(department.getParentid())) {
                list.add(new JwDepartmentTreeVo(department));
            }
        }
        return list;
    }

    private static void getChildrenRecursion(List<JwDepartmentTreeVo> treeList, List<Department> allDepartment) {
        for (JwDepartmentTreeVo departmentTree : treeList) {
            // 递归寻找子级
            List<JwDepartmentTreeVo> children = getByParentId(departmentTree.getId(), allDepartment);
            if (children.size() > 0) {
                departmentTree.setChildren(children);
                getChildrenRecursion(children, allDepartment);
            }
        }
    }

}
