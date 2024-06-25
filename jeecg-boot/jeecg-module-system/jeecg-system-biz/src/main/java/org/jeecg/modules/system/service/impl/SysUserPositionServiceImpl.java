package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysPosition;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserPosition;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.mapper.SysUserPositionMapper;
import org.jeecg.modules.system.service.ISysUserPositionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 用户职位关系表
 * @Author: jeecg-boot
 * @Date: 2023-02-14
 * @Version: V1.0
 */
@Service
public class SysUserPositionServiceImpl extends ServiceImpl<SysUserPositionMapper, SysUserPosition> implements ISysUserPositionService {

    @Autowired
    private SysUserPositionMapper sysUserPositionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public IPage<SysUser> getPositionUserList(Page<SysUser> page, String positionId) {
        return page.setRecords(sysUserPositionMapper.getPositionUserList(page, positionId));
    }

    @Override
    public void saveUserPosition(String userIds, String positionId) {
        String[] userIdArray = userIds.split(SymbolConstant.COMMA);
        //存在的用户
        StringBuilder userBuilder = new StringBuilder();
        for (String userId : userIdArray) {
            //获取成员是否存在于职位中
            Long count = sysUserPositionMapper.getUserPositionCount(userId, positionId);
            if (count == 0) {
                //插入到用户职位关系表里面
                SysUserPosition userPosition = new SysUserPosition();
                userPosition.setPositionId(positionId);
                userPosition.setUserId(userId);
                sysUserPositionMapper.insert(userPosition);
            } else {
                userBuilder.append(userId).append(SymbolConstant.COMMA);
            }
        }
        //如果用户id存在，说明已存在用户职位关系表中,提示用户已存在
        String uIds = userBuilder.toString();
        if (oConvertUtils.isNotEmpty(uIds)) {
            //查询用户列表
            List<SysUser> sysUsers = userMapper.selectBatchIds(Arrays.asList(uIds.split(SymbolConstant.COMMA)));
            String realnames = sysUsers.stream().map(SysUser::getRealname).collect(Collectors.joining(SymbolConstant.COMMA));
            throw new JeecgBootException(realnames + "已存在该职位中");
        }
    }

    @Override
    public void removeByPositionId(String positionId) {
        sysUserPositionMapper.removeByPositionId(positionId);
    }

    @Override
    public void removePositionUser(String userIds, String positionId) {
        String[] userIdArray = userIds.split(SymbolConstant.COMMA);
		sysUserPositionMapper.removePositionUser(Arrays.asList(userIdArray),positionId);
    }

}
