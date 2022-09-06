package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysAnnouncement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

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
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page,String userId,String msgCategory);


}
