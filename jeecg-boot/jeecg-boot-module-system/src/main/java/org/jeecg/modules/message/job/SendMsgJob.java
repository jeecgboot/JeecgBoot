package org.jeecg.modules.message.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 发送消息任务
 * @author: jeecg-boot
 */

@Slf4j
public class SendMsgJob implements Job {

	@Autowired
	private ISysMessageService sysMessageService;

	@Autowired
	private ISysBaseAPI sysBaseAPI;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		log.info(String.format(" Jeecg-Boot 发送消息任务 SendMsgJob !  时间:" + DateUtils.getTimestamp()));

		// 1.读取消息中心数据，只查询未发送的和发送失败不超过次数的
		QueryWrapper<SysMessage> queryWrapper = new QueryWrapper<SysMessage>();
		queryWrapper.eq("es_send_status", SendMsgStatusEnum.WAIT.getCode())
				.or(i -> i.eq("es_send_status", SendMsgStatusEnum.FAIL.getCode()).lt("es_send_num", 6));
		List<SysMessage> sysMessages = sysMessageService.list(queryWrapper);
		System.out.println(sysMessages);
		// 2.根据不同的类型走不通的发送实现类
		for (SysMessage sysMessage : sysMessages) {
			//update-begin-author:taoyan date:2022-7-8 for: 模板消息发送测试调用方法修改
			Integer sendNum = sysMessage.getEsSendNum();
			try {
				MessageDTO md = new MessageDTO();
				md.setTitle(sysMessage.getEsTitle());
				md.setContent(sysMessage.getEsContent());
				md.setToUser(sysMessage.getEsReceiver());
				md.setType(sysMessage.getEsType());
				md.setToAll(false);
				sysBaseAPI.sendTemplateMessage(md);
				//发送消息成功
				sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
				//update-end-author:taoyan date:2022-7-8 for: 模板消息发送测试调用方法修改
			} catch (Exception e) {
				e.printStackTrace();
				// 发送消息出现异常
				sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
			}
			sysMessage.setEsSendNum(++sendNum);
			// 发送结果回写到数据库
			sysMessageService.updateById(sysMessage);
		}

	}

}
