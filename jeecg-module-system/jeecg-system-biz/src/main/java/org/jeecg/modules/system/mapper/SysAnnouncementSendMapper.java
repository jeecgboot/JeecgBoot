package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.model.AnnouncementSendModel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date:  2019-02-21
 * @Version: V1.0
 */
public interface SysAnnouncementSendMapper extends BaseMapper<SysAnnouncementSend> {

	/**
	 * 获取我的消息
	 * @param announcementSendModel
	 * @param page
	 * @return
	 */
	public List<AnnouncementSendModel> getMyAnnouncementSendList(Page<AnnouncementSendModel> page,@Param("announcementSendModel") AnnouncementSendModel announcementSendModel);

	/**
	 * 获取一条记录
	 * @param sendId
	 * @return
	 */
	AnnouncementSendModel getOne(@Param("sendId") String sendId);


	/**
	 * 修改为已读消息
	 */
	void updateReaded(@Param("userId") String userId, @Param("annoceIdList") List<String> annoceIdList);

	/**
	 * 清除所有未读消息
	 * @param userId
	 */
	void clearAllUnReadMessage(@Param("userId") String userId);
}
