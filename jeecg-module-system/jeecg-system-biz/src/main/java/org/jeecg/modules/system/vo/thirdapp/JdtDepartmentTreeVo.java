package org.jeecg.modules.system.vo.thirdapp;

import com.jeecg.dingtalk.api.department.vo.Department;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 钉钉树结构的部门
 *
 * @author sunjianlei
 */
public class JdtDepartmentTreeVo extends Department {

    private List<JdtDepartmentTreeVo> children;

    public List<JdtDepartmentTreeVo> getChildren() {
        return children;
    }

    public JdtDepartmentTreeVo setChildren(List<JdtDepartmentTreeVo> children) {
        this.children = children;
        return this;
    }

    public JdtDepartmentTreeVo(Department department) {
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
    public static List<JdtDepartmentTreeVo> listToTree(List<Department> allDepartment) {
        // 先找出所有的父级
        List<JdtDepartmentTreeVo> treeList = getByParentId(1, allDepartment);
        getChildrenRecursion(treeList, allDepartment);
        return treeList;
    }

    private static List<JdtDepartmentTreeVo> getByParentId(Integer parentId, List<Department> allDepartment) {
        List<JdtDepartmentTreeVo> list = new ArrayList<>();
        for (Department department : allDepartment) {
            if (parentId.equals(department.getParent_id())) {
                list.add(new JdtDepartmentTreeVo(department));
            }
        }
        return list;
    }

    private static void getChildrenRecursion(List<JdtDepartmentTreeVo> treeList, List<Department> allDepartment) {
        for (JdtDepartmentTreeVo departmentTree : treeList) {
            // 递归寻找子级
            List<JdtDepartmentTreeVo> children = getByParentId(departmentTree.getDept_id(), allDepartment);
            if (children.size() > 0) {
                departmentTree.setChildren(children);
                getChildrenRecursion(children, allDepartment);
            }
        }
    }

}
