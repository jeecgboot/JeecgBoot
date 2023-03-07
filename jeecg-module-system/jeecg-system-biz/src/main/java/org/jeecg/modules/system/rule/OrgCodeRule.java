package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author scott
 * @Date 2019/12/9 11:33
 * @Description: 机构编码生成规则
 */
public class OrgCodeRule implements IFillRuleHandler {

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        ISysDepartService sysDepartService = (ISysDepartService) SpringContextUtils.getBean("sysDepartServiceImpl");

        LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
        LambdaQueryWrapper<SysDepart> query1 = new LambdaQueryWrapper<SysDepart>();
        // 创建一个List集合,存储查询返回的所有SysDepart对象
        List<SysDepart> departList = new ArrayList<>();
        String[] strArray = new String[2];
        //定义部门类型
        String orgType = "";
        // 定义新编码字符串
        String newOrgCode = "";
        // 定义旧编码字符串
        String oldOrgCode = "";

        String parentId = null;
        if (formData != null && formData.size() > 0) {
            Object obj = formData.get("parentId");
            if (obj != null) {
                parentId = obj.toString();
            }
        } else {
            if (params != null) {
                Object obj = params.get("parentId");
                if (obj != null) {
                    parentId = obj.toString();
                }
            }
        }

        //如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回
        if (StringUtil.isNullOrEmpty(parentId)) {
            // 线判断数据库中的表是否为空,空则直接返回初始编码
            //获取最大值code的部门信息
            //update-begin---author:wangshuai ---date:20230211  for：[QQYUN-4209]租户隔离下部门新建不了------------
            Page<SysDepart> page = new Page<>(1,1);
            IPage<SysDepart> pageList = sysDepartService.getMaxCodeDepart(page,"");
            List<SysDepart> records = pageList.getRecords();
            if (null==records || records.size()==0) {
            //update-end---author:wangshuai ---date:20230211  for：[QQYUN-4209]租户隔离下部门新建不了------------
                strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
                strArray[1] = "1";
                return strArray;
            } else {
                SysDepart depart = records.get(0);
                oldOrgCode = depart.getOrgCode();
                orgType = depart.getOrgType();
                newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
            }
        } else {//反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
            //获取自己部门最大值orgCode部门信息
            //update-begin---author:wangshuai ---date:20230211  for：[QQYUN-4209]租户隔离下部门新建不了------------
            Page<SysDepart> page = new Page<>(1,1);
            IPage<SysDepart> pageList = sysDepartService.getMaxCodeDepart(page,parentId);
            List<SysDepart> records = pageList.getRecords();
            // 查询出父级部门
            SysDepart depart = sysDepartService.getDepartById(parentId);
            //update-end---author:wangshuai ---date:20230211  for：[QQYUN-4209]租户隔离下部门新建不了------------
            // 获取父级部门的Code
            String parentCode = depart.getOrgCode();
            // 根据父级部门类型算出当前部门的类型
            orgType = String.valueOf(Integer.valueOf(depart.getOrgType()) + 1);
            // 处理同级部门为null的情况
            if (null == records || records.size()==0) {
                // 直接生成当前的部门编码并返回
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
            } else { //处理有同级部门的情况
                // 获取同级部门的编码,利用工具类
                String subCode = records.get(0).getOrgCode();
                // 返回生成的当前部门编码
                newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
            }
        }
        // 返回最终封装了部门编码和部门类型的数组
        strArray[0] = newOrgCode;
        strArray[1] = orgType;
        return strArray;
    }
}
