package org.jeecg.modules.system.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.model.AnnouncementSendModel;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
@Service
public class SysAnnouncementSendServiceImpl extends ServiceImpl<SysAnnouncementSendMapper, SysAnnouncementSend> implements ISysAnnouncementSendService {

	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Autowired
    private SysAnnouncementMapper sysAnnouncementMapper;

	@Override
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,
			AnnouncementSendModel announcementSendModel) {
		 return page.setRecords(sysAnnouncementSendMapper.getMyAnnouncementSendList(page, announcementSendModel));
	}

	@Override
	public AnnouncementSendModel getOne(String sendId) {
		return sysAnnouncementSendMapper.getOne(sendId);
	}

    /**
     * 获取当前用户已阅读数量
     * 
     * @param id
     * @return
     */
    @Override
    public long getReadCountByUserId(String id) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return sysAnnouncementSendMapper.getReadCountByUserId(id, sysUser.getId());
    }

    /**
     * 根据多个id批量删除已阅读的数量
     * 
     * @param ids
     */
    @Override
    public void deleteBatchByIds(String ids) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //根据用户id和阅读表的id获取所有阅读的数据
        List<String> sendIds = sysAnnouncementSendMapper.getReadAnnSendByUserId(Arrays.asList(ids.split(SymbolConstant.COMMA)),sysUser.getId());
        if(CollectionUtil.isNotEmpty(sendIds)){
            this.removeByIds(sendIds);
        }
    }

    /**
     * 根据busId更新阅读状态
     * @param busId
     * @param busType
     */
    @Override
    public void updateReadFlagByBusId(String busId, String busType) {
        SysAnnouncement announcement = sysAnnouncementMapper.selectOne(new QueryWrapper<SysAnnouncement>().eq("bus_type",busType).eq("bus_id",busId));
        if(oConvertUtils.isNotEmpty(announcement)){
            LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
            String userId = sysUser.getId();
            LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
            updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
            updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
            updateWrapper.eq(SysAnnouncementSend::getAnntId,announcement.getId());
            updateWrapper.eq(SysAnnouncementSend::getUserId,userId);
            SysAnnouncementSend announcementSend = new SysAnnouncementSend();
            sysAnnouncementSendMapper.update(announcementSend, updateWrapper);
        }
    }
}
