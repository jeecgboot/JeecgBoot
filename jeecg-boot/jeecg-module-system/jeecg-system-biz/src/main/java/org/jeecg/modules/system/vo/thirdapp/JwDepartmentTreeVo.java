package org.jeecg.modules.system.vo.thirdapp;

import com.jeecg.qywx.api.department.vo.Department;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Department> departmentOptional = allDepartment.stream().filter(item -> "0".equals(item.getParentid())).findAny();
        Department department = new Department();
        //判断是否找到数据
        if(departmentOptional.isPresent()){
            department = departmentOptional.get();
        }
        getChildrenRecursion(treeList, allDepartment);
        //update-begin---author:wangshuai---date:2024-04-10---for:【issues/6017】企业微信同步部门时没有最顶层的部门名，同步用户时，用户没有部门信息---
        JwDepartmentTreeVo treeVo = new JwDepartmentTreeVo(department);
        treeVo.setChildren(treeList);
        List<JwDepartmentTreeVo> list = new ArrayList<>();
        list.add(treeVo);
        return list;
        //update-begin---author:wangshuai---date:2024-04-10---for:【issues/6017】企业微信部门时没有最顶层的部门名，同步用户时，用户没有部门信息---
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
