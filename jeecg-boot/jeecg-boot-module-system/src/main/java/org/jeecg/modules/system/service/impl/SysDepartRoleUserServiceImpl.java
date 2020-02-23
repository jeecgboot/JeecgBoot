package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepartRoleUser;
import org.jeecg.modules.system.mapper.SysDepartRoleUserMapper;
import org.jeecg.modules.system.service.ISysDepartRoleUserService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @Description: 部门角色人员信息
 * @Author: jeecg-boot
 * @Date:   2020-02-13
 * @Version: V1.0
 */
@Service
public class SysDepartRoleUserServiceImpl extends ServiceImpl<SysDepartRoleUserMapper, SysDepartRoleUser> implements ISysDepartRoleUserService {

    @Override
    public void deptRoleUserAdd(String userId, String newRoleId, String oldRoleId) {
        List<String> add = getDiff(oldRoleId,newRoleId);
        if(add!=null && add.size()>0) {
            List<SysDepartRoleUser> list = new ArrayList<>();
            for (String roleId : add) {
                if(oConvertUtils.isNotEmpty(roleId)) {
                    SysDepartRoleUser rolepms = new SysDepartRoleUser(userId, roleId);
                    list.add(rolepms);
                }
            }
            this.saveBatch(list);
        }
        List<String> remove = getDiff(newRoleId,oldRoleId);
        if(remove!=null && remove.size()>0) {
            for (String roleId : remove) {
                this.remove(new QueryWrapper<SysDepartRoleUser>().lambda().eq(SysDepartRoleUser::getUserId, userId).eq(SysDepartRoleUser::getDroleId, roleId));
            }
        }
    }

    /**
     * 从diff中找出main中没有的元素
     * @param main
     * @param diff
     * @return
     */
    private List<String> getDiff(String main, String diff){
        if(oConvertUtils.isEmpty(diff)) {
            return null;
        }
        if(oConvertUtils.isEmpty(main)) {
            return Arrays.asList(diff.split(","));
        }

        String[] mainArr = main.split(",");
        String[] diffArr = diff.split(",");
        Map<String, Integer> map = new HashMap<>();
        for (String string : mainArr) {
            map.put(string, 1);
        }
        List<String> res = new ArrayList<String>();
        for (String key : diffArr) {
            if(oConvertUtils.isNotEmpty(key) && !map.containsKey(key)) {
                res.add(key);
            }
        }
        return res;
    }
}
