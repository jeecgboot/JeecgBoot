package com.xxl.job.admin.business.scheduler.alarm.impl;

import com.xxl.job.admin.business.scheduler.alarm.JobAlarm;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLog;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.core.context.XxlJobContext;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * job alarm by email
 *
 * @author xuxueli 2020-01-19
 */
@Component
public class EmailJobAlarm implements JobAlarm {
    private static Logger logger = LoggerFactory.getLogger(EmailJobAlarm.class);

    /**
     * fail alarm
     *
     * @param jobLog
     */
    @Override
    public boolean doAlarm(XxlJobInfo info, XxlJobLog jobLog){
        boolean alarmResult = true;

        // send monitor email
        if (info!=null && info.getAlarmEmail()!=null && !info.getAlarmEmail().trim().isEmpty()) {

            // alarmContent
            String alarmContent = "Alarm Job LogId=" + jobLog.getId();
            if (jobLog.getTriggerCode() != XxlJobContext.HANDLE_CODE_SUCCESS) {
                alarmContent += "<br>TriggerMsg=<br>" + jobLog.getTriggerMsg();
            }
            if (jobLog.getHandleCode()>0 && jobLog.getHandleCode() != XxlJobContext.HANDLE_CODE_SUCCESS) {
                alarmContent += "<br>HandleCode=" + jobLog.getHandleMsg();
            }

            // email info
            XxlJobGroup group = XxlJobAdminBootstrap.getInstance().getXxlJobGroupMapper().load(Integer.valueOf(info.getJobGroup()));
            String personal = I18nUtil.getString("admin_name_full");
            String title = I18nUtil.getString("jobconf_monitor");
            String content = MessageFormat.format(loadEmailJobAlarmTemplate(),
                    group!=null?group.getTitle():"null",
                    info.getId(),
                    info.getJobDesc(),
                    alarmContent);

            Set<String> emailSet = new HashSet<String>(Arrays.asList(info.getAlarmEmail().split(",")));
            for (String email: emailSet) {

                // make mail
                try {
                    MimeMessage mimeMessage = XxlJobAdminBootstrap.getInstance().getMailSender().createMimeMessage();

                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    helper.setFrom(XxlJobAdminBootstrap.getInstance().getEmailFrom(), personal);
                    helper.setTo(email);
                    helper.setSubject(title);
                    helper.setText(content, true);

                    XxlJobAdminBootstrap.getInstance().getMailSender().send(mimeMessage);
                } catch (Exception e) {
                    logger.error(">>>>>>>>>>> xxl-job, job fail alarm email send error, JobLogId:{}", jobLog.getId(), e);

                    alarmResult = false;
                }

            }
        }

        return alarmResult;
    }

    /**
     * load email job alarm template
     *
     * @return
     */
    private static final String loadEmailJobAlarmTemplate(){
        String mailBodyTemplate = "<h5>" + I18nUtil.getString("jobconf_monitor_detail") + "：</span>" +
                "<table border=\"1\" cellpadding=\"3\" style=\"border-collapse:collapse; width:80%;\" >\n" +
                "   <thead style=\"font-weight: bold;color: #ffffff;background-color: #ff8c00;\" >" +
                "      <tr>\n" +
                "         <td width=\"20%\" >"+ I18nUtil.getString("jobinfo_field_jobgroup") +"</td>\n" +
                "         <td width=\"10%\" >"+ I18nUtil.getString("jobinfo_field_id") +"</td>\n" +
                "         <td width=\"20%\" >"+ I18nUtil.getString("jobinfo_field_jobdesc") +"</td>\n" +
                "         <td width=\"10%\" >"+ I18nUtil.getString("jobconf_monitor_alarm_title") +"</td>\n" +
                "         <td width=\"40%\" >"+ I18nUtil.getString("jobconf_monitor_alarm_content") +"</td>\n" +
                "      </tr>\n" +
                "   </thead>\n" +
                "   <tbody>\n" +
                "      <tr>\n" +
                "         <td>{0}</td>\n" +
                "         <td>{1}</td>\n" +
                "         <td>{2}</td>\n" +
                "         <td>"+ I18nUtil.getString("jobconf_monitor_alarm_type") +"</td>\n" +
                "         <td>{3}</td>\n" +
                "      </tr>\n" +
                "   </tbody>\n" +
                "</table>";

        return mailBodyTemplate;
    }

}
