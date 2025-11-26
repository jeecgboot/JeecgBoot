package org.jeecg.modules.system.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.FileDownloadUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
@Service
@Slf4j
public class SysAnnouncementServiceImpl extends ServiceImpl<SysAnnouncementMapper, SysAnnouncement> implements ISysAnnouncementService {
	/**
	 * 补数据改成后台模式
	 */
	public static ExecutorService completeNoteThreadPool = new ThreadPoolExecutor(0, 1024, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
	
	@Resource
	private SysAnnouncementMapper sysAnnouncementMapper;
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;
	@Autowired
	private ISysAnnouncementSendService sysAnnouncementSendService;
	@Autowired
	private JeecgBaseConfig jeecgBaseConfig;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveAnnouncement(SysAnnouncement sysAnnouncement) {
		if(sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_ALL)) {
			sysAnnouncementMapper.insert(sysAnnouncement);
		}else {
			// 1.插入通告表记录
			sysAnnouncementMapper.insert(sysAnnouncement);
			// 2.插入用户通告阅读标记表记录
			String userId = sysAnnouncement.getUserIds();
            // 代码逻辑说明: [issues/5503]【公告】通知无法接收
			if(StringUtils.isNotBlank(userId) && userId.endsWith(",")){
				userId = userId.substring(0, (userId.length()-1));
			}
			String[] userIds = userId.split(",");
			String anntId = sysAnnouncement.getId();
			Date refDate = new Date();
			for(int i=0;i<userIds.length;i++) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(userIds[i]);
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				announcementSend.setReadTime(refDate);
				sysAnnouncementSendMapper.insert(announcementSend);
			}
		}
	}
	
	/**
	 * @功能：编辑消息信息
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement) {
		// 1.更新系统信息表数据
		sysAnnouncementMapper.updateById(sysAnnouncement);
		String userId = sysAnnouncement.getUserIds();
		if(oConvertUtils.isNotEmpty(userId)&&sysAnnouncement.getMsgType().equals(CommonConstant.MSG_TYPE_UESR)) {
			// 2.补充新的通知用户数据
			String[] userIds = userId.substring(0, (userId.length()-1)).split(",");
			String anntId = sysAnnouncement.getId();
			Date refDate = new Date();
			for(int i=0;i<userIds.length;i++) {
				LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
				queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
				queryWrapper.eq(SysAnnouncementSend::getUserId, userIds[i]);
				List<SysAnnouncementSend> announcementSends=sysAnnouncementSendMapper.selectList(queryWrapper);
				if(announcementSends.size()<=0) {
					SysAnnouncementSend announcementSend = new SysAnnouncementSend();
					announcementSend.setAnntId(anntId);
					announcementSend.setUserId(userIds[i]);
					announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
					announcementSend.setReadTime(refDate);
					sysAnnouncementSendMapper.insert(announcementSend);
				}
			}
			// 3. 删除多余通知用户数据
			Collection<String> delUserIds = Arrays.asList(userIds);
			LambdaQueryWrapper<SysAnnouncementSend> queryWrapper = new LambdaQueryWrapper<SysAnnouncementSend>();
			queryWrapper.notIn(SysAnnouncementSend::getUserId, delUserIds);
			queryWrapper.eq(SysAnnouncementSend::getAnntId, anntId);
			sysAnnouncementSendMapper.delete(queryWrapper);
		}
		return true;
	}

    /**
     * 流程执行完成保存消息通知
     * @param title 标题
     * @param msgContent 信息内容
     */
	@Override
	public void saveSysAnnouncement(String title, String msgContent) {
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender("JEECG BOOT");
		announcement.setPriority(CommonConstant.PRIORITY_L);
		announcement.setMsgType(CommonConstant.MSG_TYPE_ALL);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
		sysAnnouncementMapper.insert(announcement);
	}

	@Override
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page, String userId, String msgCategory, Integer tenantId, Date beginDate) {
		if (page.getSize() == -1) {
			return page.setRecords(sysAnnouncementMapper.querySysCementListByUserId(null, userId, msgCategory,tenantId,beginDate));
		} else {
			return page.setRecords(sysAnnouncementMapper.querySysCementListByUserId(page, userId, msgCategory,tenantId,beginDate));
		}
	}

	@Override
	public Integer getUnreadMessageCountByUserId(String userId, Date beginDate, String noticeType) {
		return sysAnnouncementMapper.getUnreadMessageCountByUserId(userId, beginDate, noticeType);
	}

	@Override
	public void completeAnnouncementSendInfo() {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String userId = sysUser.getId();
		List<String> announcementIds = this.getNotSendedAnnouncementlist(userId);
		List<SysAnnouncementSend> sysAnnouncementSendList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(announcementIds)) {
			for (String commentId : announcementIds) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(commentId);
				announcementSend.setUserId(userId);
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendList.add(announcementSend);
			}
		}
		if (!CollectionUtils.isEmpty(sysAnnouncementSendList)) {
			sysAnnouncementSendService.saveBatch(sysAnnouncementSendList);
		}
	}

	@Override
	public void batchInsertSysAnnouncementSend(String commentId, Integer tenantId) {
		if (MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL && oConvertUtils.isNotEmpty(tenantId)) {
			log.info("补全公告与用户的关系数据，租户ID = {}", tenantId);
		} else {
			tenantId = null;
		}
		
		List<String> userIdList = sysUserMapper.getTenantUserIdList(tenantId);
		List<SysAnnouncementSend> sysAnnouncementSendList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(userIdList)) {
			for (String userId : userIdList) {
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(commentId);
				announcementSend.setUserId(userId);
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendList.add(announcementSend);
			}
		}
		if (!CollectionUtils.isEmpty(sysAnnouncementSendList)) {
			log.info("补全公告与用户的关系数据，sysAnnouncementSendList size = {}", sysAnnouncementSendList.size());
			sysAnnouncementSendService.saveBatch(sysAnnouncementSendList);
		}
	}

	@Override
	public List<SysAnnouncement> querySysMessageList(int pageSize, int pageNo, String fromUser, String starFlag, String busType, String msgCategory, Date beginDate, Date endDate, String noticeType) {
//		//1. 补全send表的数据
//		completeNoteThreadPool.execute(()->{
//			completeAnnouncementSendInfo();
//		});
		
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		log.debug(" 获取登录人 LoginUser id: {}", sysUser.getId());
		Page<SysAnnouncement> page = new Page<SysAnnouncement>(pageNo,pageSize);
		List<SysAnnouncement> list = baseMapper.queryAllMessageList(page, sysUser.getId(), fromUser, starFlag, busType, msgCategory,beginDate, endDate, noticeType);
		return list;
	}

	@Override
	public void updateReaded(List<String> annoceIdList) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		sysAnnouncementSendMapper.updateReaded(sysUser.getId(), annoceIdList);
	}

	@Override
	public void clearAllUnReadMessage() {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		sysAnnouncementSendMapper.clearAllUnReadMessage(sysUser.getId());
	}

	/**
	 * 查询用户未读的通知公告，防止SQL注入写法调整
	 * @param userId
	 * @return
	 */

	@Override
	public List<String> getNotSendedAnnouncementlist(String userId) {
		return sysAnnouncementMapper.getNotSendedAnnouncementlist(new Date(), userId);
	}

	/**
	 * 更新访问量
	 * @param id
	 * @param increaseCount
	 */
	@Override
	public void updateVisitsNum(String id, int increaseCount) {
		SysAnnouncement sysAnnouncement = sysAnnouncementMapper.selectById(id);
		if (oConvertUtils.isNotEmpty(sysAnnouncement)) {
			int visits = oConvertUtils.getInt(sysAnnouncement.getVisitsNum(), 0);
			int totalValue = increaseCount + visits;
			sysAnnouncement.setVisitsNum(totalValue);
			sysAnnouncementMapper.updateById(sysAnnouncement);
			log.info("通知公告：{} 访问次数+1，总访问数量：{}", sysAnnouncement.getTitile(), sysAnnouncement.getVisitsNum());
		}
	}

	/**
	 * 批量下载文件
	 * @param id
	 * @param request
	 * @param response
	 */
	@Override
	public void downLoadFiles(String id, HttpServletRequest request, HttpServletResponse response) {
		// 参数校验
		if (oConvertUtils.isEmpty(id)) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		// 获取文章信息
		SysAnnouncement sysAnnouncement = this.baseMapper.selectById(id);
		if (oConvertUtils.isEmpty(sysAnnouncement)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		//设置HTTP响应头：准备文件下载
		response.reset();
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/force-download");
		ZipArchiveOutputStream zous = null;
		try {
			// 生成ZIP文件名：使用文章标题+时间戳避免重名
			String title = sysAnnouncement.getTitile() + new Date().getTime();
			String zipName = URLEncoder.encode( title + ".zip", "UTF-8").replaceAll("\\+", "%20");
			response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + zipName);
			// 创建ZIP输出流：直接输出到HTTP响应流
			zous = new ZipArchiveOutputStream(response.getOutputStream());
			zous.setUseZip64(Zip64Mode.AsNeeded);// 支持大文件

			// 批量下载文件
			String[] fileUrls = sysAnnouncement.getFiles().split(",");
			// 遍历所有文件URL
			for (int i = 0; i < fileUrls.length; i++) {
				String fileUrl = fileUrls[i].trim();
				if (oConvertUtils.isEmpty(fileUrl)) {
					continue;
				}
				// 生成ZIP内文件名：避免重名，添加序号
				String fileName = FileDownloadUtils.generateFileName(fileUrl, i, fileUrls.length);
				String uploadUrl = jeecgBaseConfig.getPath().getUpload();
				// 下载单个文件并添加到ZIP
				FileDownloadUtils.downLoadSingleFile(fileUrl,fileName,uploadUrl, zous);
			}
			// 完成ZIP写入
			zous.finish();
			// 刷新缓冲区确保数据发送
			response.flushBuffer();
		} catch (IOException e) {
			log.error("文件下载失败"+e.getMessage(), e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			// 确保流关闭，防止资源泄漏
			IoUtil.close(zous);
		}
	}
}
