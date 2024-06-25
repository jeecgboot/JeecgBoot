package org.jeecg.modules.system.service;

import java.util.List;

import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.model.AnnouncementSendModel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
public interface ISysAnnouncementSendService extends IService<SysAnnouncementSend> {

	/**
	 * 获取我的消息
	 * @param announcementSendModel
     * @param page 当前页数
	 * @return
	 */
	public Page<AnnouncementSendModel> getMyAnnouncementSendPage(Page<AnnouncementSendModel> page,AnnouncementSendModel announcementSendModel);

	/**
	 * 根据消息发送记录ID获取消息内容
	 * @return
	 */
	AnnouncementSendModel getOne(String sendId);
	

}
