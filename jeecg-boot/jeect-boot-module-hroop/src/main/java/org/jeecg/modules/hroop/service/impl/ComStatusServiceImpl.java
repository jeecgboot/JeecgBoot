package org.jeecg.modules.hroop.service.impl;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.hroop.entity.ComStatus;
import org.jeecg.modules.hroop.mapper.ComStatusMapper;
import org.jeecg.modules.hroop.service.IComStatusService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 更新状态表
 * @Author: jeecg-boot
 * @Date: 2020-10-19
 * @Version: V1.0
 */
@Service
public class ComStatusServiceImpl extends ServiceImpl<ComStatusMapper, ComStatus> implements IComStatusService {

    @Override
    public String getExportFields() {

//        //获取当前登录人
//        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        //权限配置列导出示例
//        List<String> noAuthList = new ArrayList<>();
//        List<String> exportFieldsList = new ArrayList<>();
//        //1.此前缀必须与列表字段权限控制前缀一致
//        String permsPrefix = "comstatus:";
//        //查询配置菜单有效字段
//        List<String> allAuth = this.baseMapper.queryAllAuth(permsPrefix);
//
//        //查询已授权字段
//        List<String> userAuth = this.baseMapper.queryUserAuth(sysUser.getId(), permsPrefix);
//        //列出未授权字段
//        for (String perms : allAuth) {
//            if (!userAuth.contains(perms)) {
//                noAuthList.add(perms.substring(permsPrefix.length()));
//            }
//        }
//        //实体类中字段与未授权字段比较，列出需导出字段
//        Field[] fileds = ComStatus.class.getDeclaredFields();
//        List<Field> list = new ArrayList(Arrays.asList(fileds));
//        for (Field field : list) {
//            if (!noAuthList.contains(field.getName())) {
//                exportFieldsList.add(field.getName());
//            }
//        }
//        return exportFieldsList != null && exportFieldsList.size() > 0 ? String.join(",", exportFieldsList) : "";
        return null;
    }
}
