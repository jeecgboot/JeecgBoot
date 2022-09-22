package org.jeecg.modules.system.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysAnnouncement;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
public interface SysAnnouncementMapper extends BaseMapper<SysAnnouncement> {

    /**
     * 通过消息类型和用户id获取系统通告
     * @param page
     * @param userId 用户id
     * @param msgCategory 消息类型
     * @return
     */
	List<SysAnnouncement> querySysCementListByUserId(Page<SysAnnouncement> page, @Param("userId")String userId,@Param("msgCategory")String msgCategory);

    /**
     * 分页查询消息列表
     * @param page
     * @param userId
     * @param fromUser
     * @param beginDate
     * @param endDate
     * @return
     */
	List<SysAnnouncement> queryMessageList(Page<SysAnnouncement> page, @Param("userId")String userId, @Param("fromUser")String fromUser, @Param("starFlag")String starFlag, @Param("beginDate")Date beginDate, @Param("endDate")Date endDate);
}
