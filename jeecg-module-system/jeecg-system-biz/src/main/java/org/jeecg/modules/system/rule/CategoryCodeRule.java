package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.mapper.SysCategoryMapper;

import java.util.List;

/**
 * @Author scott
 * @Date 2019/12/9 11:32
 * @Description: 分类字典编码生成规则
 */
@Slf4j
public class CategoryCodeRule implements IFillRuleHandler {

    public static final String ROOT_PID_VALUE = "0";

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        log.info("系统自定义编码规则[category_code_rule]，params：{} ，formData： {}", params, formData);

        String categoryPid = ROOT_PID_VALUE;
        String categoryCode = null;

        if (formData != null && formData.size() > 0) {
            Object obj = formData.get("pid");
            if (oConvertUtils.isNotEmpty(obj)) {
                categoryPid = obj.toString();
            }
        } else {
            if (params != null) {
                Object obj = params.get("pid");
                if (oConvertUtils.isNotEmpty(obj)) {
                    categoryPid = obj.toString();
                }
            }
        }

        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        SysCategoryMapper baseMapper = (SysCategoryMapper) SpringContextUtils.getBean("sysCategoryMapper");
        //update-begin---author:wangshuai ---date:20230424  for：【issues/4846】开启saas多租户功能后，租户管理员在添加分类字典时，报错------------
        Page<SysCategory> page = new Page<>(1,1);
        List<SysCategory> list = baseMapper.getMaxCategoryCodeByPage(page,categoryPid);
        //update-end---author:wangshuai ---date:20230424  for：【issues/4846】开启saas多租户功能后，租户管理员在添加分类字典时，报错------------
        if (list == null || list.size() == 0) {
            if (ROOT_PID_VALUE.equals(categoryPid)) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                //update-begin---author:wangshuai ---date:20230424  for：【issues/4846】开启saas多租户功能后，租户管理员在添加分类字典时，报错------------
                SysCategory parent = (SysCategory) baseMapper.selectSysCategoryById(categoryPid);
                //update-end---author:wangshuai ---date:20230424  for：【issues/4846】开启saas多租户功能后，租户管理员在添加分类字典时，报错------------
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
