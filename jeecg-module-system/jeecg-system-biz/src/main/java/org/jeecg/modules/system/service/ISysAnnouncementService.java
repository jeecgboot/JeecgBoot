package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysAnnouncement;

import java.util.Date;
import java.util.List;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {

    /**
     * 保存系统通告
     * @param sysAnnouncement
     */
	public void saveAnnouncement(SysAnnouncement sysAnnouncement);

    /**
     * 修改系统通告
     * @param sysAnnouncement
     * @return
     */
	public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement);

    /**
     * 保存系统通告
     * @param title 标题
     * @param msgContent 信息内容
     */
	public void saveSysAnnouncement(String title, String msgContent);

    /**
     * 分页查询系统通告
     * @param page 当前页数
     * @param userId 用户id
     * @param msgCategory 消息类型
     * @return Page<SysAnnouncement>
     */
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory, Integer tenantId, Date beginDate);

    /**
     * 获取用户未读消息数量
     *
     * @param userId 用户id
     * @return
     */
    public Integer getUnreadMessageCountByUserId(String userId, Date beginDate);


    /**
     *  补全当前登录用户的消息阅读记录 
     * @作废无用 2023-09-19
     */
    @Deprecated
	void completeAnnouncementSendInfo();

    /**
     * 补全所有用户的推送公告关系数据
     *
     * @param commentId
     * @param tenantId
     */
    void batchInsertSysAnnouncementSend(String commentId, Integer tenantId);
    
    /**
     * 分页查询当前登录用户的消息， 并且标记哪些是未读消息
     */
    List<SysAnnouncement> querySysMessageList(int pageSize, int pageNo, String fromUser, String starFlag, Date beginDate, Date endDate);

    /**
     * 修改为已读消息
     */
    void updateReaded(List<String> annoceIdList);


    /**
     * 清除所有未读消息
     */
    void clearAllUnReadMessage();

    /**
     * 查询用户未阅读的通知公告
     * @param userId
     * @return
     */
    public List<String> getNotSendedAnnouncementlist(String userId);
}
