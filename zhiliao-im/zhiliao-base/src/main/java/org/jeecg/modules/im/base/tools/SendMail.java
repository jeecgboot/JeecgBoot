package org.jeecg.modules.im.base.tools;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * 邮件发送
 * @author junko
 * 
 * 描述：使用javamail出现java.net.SocketException: Network is unreachable: connect异常 解决方法 
 * 
 * 1. main方法加入System.setProperty("java.net.preferIPv4Stack", "true");  
 * 2. tomcat服务器加上启动参数 -Djava.net.preferIPv4Stack=true   
 */
//public class ToolMail {
//
////	private static Logger log = Logger.getLogger(ToolMail.class);
//
//	public static final String sendType_text = "text";
//
//	public static final String sendType_html = "html";
//
//	/**
//	 * 发送文本邮件
//	 * @param host
//	 * @param port
//	 * @param validate
//	 * @param userName
//	 * @param password
//	 * @param from
//	 * @param to
//	 * @param subject
//	 * @param content
//	 * @param attachFileNames
//	 */
//	public static void sendTextMail(
//			String host, String port, boolean validate, String userName, String password,
//			String from, List<String> to,
//			String subject, String content, String[] attachFileNames) {
////		log.info("发送文本邮件");
////		SendMail mail = new SendMail(sendType_text, host, port, validate, userName, password, from, to, subject, content, attachFileNames);
////		mail.start();
//	}
//
//	/**
//	 * 发送html邮件
//	 * @param host
//	 * @param port
//	 * @param validate
//	 * @param userName
//	 * @param password
//	 * @param from
//	 * @param to
//	 * @param subject
//	 * @param content
//	 * @param attachFileNames
//	 */
//	public static void sendHtmlMail(
//			String host, String port, boolean validate, String userName, String password,
//			String from, List<String> to,
//			String subject, String content, String[] attachFileNames) {
////		log.info("发送html邮件");
////		SendMail mail = new SendMail(sendType_html, host, port, validate, userName, password, from, to, subject, content, attachFileNames);
////		mail.start();
//	}
//
//	public static void main(String[] args) {
//		String host = "smtp.gmail.com";		// 发送邮件的服务器的IP
//		String port = "587";	// 发送邮件的服务器的端口
//
//		String from = "junkoder2015@gmail.com";		// 邮件发送者的地址
//		String userName = "junkoder2015@gmail.com";	// 登陆邮件发送服务器的用户名
//		String password = "woSHI:junGAO2012";	// 登陆邮件发送服务器的密码
//
//		List<String> to = new ArrayList<String>();			// 邮件接收者的地址
//		to.add("317394310@qq.com");
//
//		boolean validate = true;	// 是否需要身份验证
//
//		String subject = "标题test111";		// 邮件标题
//		String content = "内容test111";		// 邮件的文本内容
////		String[] attachFileNames = new String[]{"D:/code.jpg"};	// 邮件附件的文件名
//
//		sendTextMail(host, port, validate, userName, password, from, to, subject, content, null);
//	}
//
//}

/**
 * 异步发送邮件线程
 * @author junko  317394310@qq.com
 */
public class SendMail extends Thread {
	public static final String sendType_text = "text";

	public static final String sendType_html = "html";

	private String sendType;		// 发送邮件的类型：text 、html

	private String host;		// 发送邮件的服务器的IP
	private String port = "25";	// 发送邮件的服务器的端口

	private String from;		// 邮件发送者的地址
	private String userName;	// 登陆邮件发送服务器的用户名
	private String password;	// 登陆邮件发送服务器的密码

	private List<String> to;			// 邮件接收者的地址

	private boolean validate = false;	// 是否需要身份验证

	private String subject;		// 邮件标题
	private String content;		// 邮件的文本内容
	private String[] attachFileNames;	// 邮件附件的文件名
	private EmailSendCallBack callBack;

	public SendMail(String sendType,
			String host, String port, boolean validate, String userName, String password,
			String from, List<String> to,
			String subject, String content, String[] attachFileNames,EmailSendCallBack callBack) {
		this.sendType = sendType;
		this.host = host;
		this.port = port;
		this.from = from;
		this.userName = userName;
		this.password = password;
		this.to = to;
		this.validate = validate;
		this.subject = subject;
		this.content = content;
		this.attachFileNames = attachFileNames;
		this.callBack = callBack;
		// tomcat服务器加上启动参数 -Djava.net.preferIPv4Stack=true
		System.setProperty("java.net.preferIPv4Stack", "true");
	}

	public static void main(String[] args) {

//		String host = "smtp.vip.163.com";
//		String port = "465";
//		List list = new ArrayList();
//		list.add("317394310@qq.com");
//		SendMail sendMail = new SendMail(SendMail.sendType_html,host,port,true,"aa164897745@vip.163.com","XTJKJNGTIDRVBIZN","aa164897745@vip.163.com",list,"测试","测试邮件",null,null);
//		sendMail.start();


		String host = "smtp.qq.com";
		String port = "587";
		List list = new ArrayList();
		list.add("317394310@qq.com");
		SendMail sendMail = new SendMail(SendMail.sendType_html,host,port,true,"332249996@qq.com","tbcbodlhlrjncaah","332249996@qq.com",list,"测试","测试邮件",null,null);
		sendMail.start();

	}


	@Override
	public void run() {
		boolean result = false;
		if(sendType.equals(sendType_text)){
			result = sendTextMail(callBack);
		} else if(sendType.equals(sendType_html)){
			result = sendHtmlMail(callBack);
		} else {
//			log.error("发送邮件参数sendType不能为空");
		}
		if(!result){

		}
	}

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties(){
		Properties prop = new Properties();
		prop.put("mail.smtp.host", this.host);
		prop.put("mail.smtp.port", this.port);
		prop.put("mail.smtp.auth", validate ? "true" : "false");
		prop.put("mail.smtp.starttls.enable", "true");

		if(this.host.indexOf("smtp.gmail.com") >= 0){
			prop.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			prop.setProperty("mail.smtp.socketFactory.fallback", "false");
			prop.setProperty("mail.smtp.port", "465");
			prop.setProperty("mail.smtp.socketFactory.port", "465");
		}

		return prop;
	}

	/**
	 * 以文本格式发送邮件
	 */
	public boolean sendTextMail(EmailSendCallBack callBack) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = getProperties();
		if (isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MailAuthenticator(getUserName(), getPassword());
		}

		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getInstance(pro, authenticator);// getDefaultInstance

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);

			// 创建邮件发送者地址
			Address from = new InternetAddress(getFrom());

			// 设置邮件消息的发送者
			mailMessage.setFrom(from);

			// 创建邮件的接收者地址，并设置到邮件消息中
			Address[] tos = new InternetAddress[to.size()];
			for(int i=0; i<to.size(); i++){
				String receive = to.get(i);
				if(null != receive && !receive.isEmpty()){
					tos[i] = new InternetAddress(receive);
				}
			}

			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, tos);

			// 设置邮件消息的主题
			mailMessage.setSubject(getSubject());

			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			Multipart multipart = new MimeMultipart();

			// 正文
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setText(getContent());
            multipart.addBodyPart(mbp);

			// 设置邮件消息的主要内容
			mailMessage.setContent(multipart);

			// 发送邮件
			Transport.send(mailMessage);
			if(callBack!=null)
				callBack.success();
			return true;
		} catch (MessagingException e) {
//			log.error("发送文本邮件异常：" + e.getMessage());
			e.printStackTrace();
			if(callBack!=null)
				callBack.fail();
			return false;
		}
	}

	/**
	 * 以HTML格式发送邮件
	 */
	public boolean sendHtmlMail(EmailSendCallBack callBack) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (isValidate()) {
			authenticator = new MailAuthenticator(getUserName(), getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getInstance(pro, authenticator);// getDefaultInstance
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(getFrom());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address[] tos = new InternetAddress[to.size()];
			for(int i=0; i<to.size(); i++){
				String receive = to.get(i);
				if(null != receive && !receive.isEmpty()){
					tos[i] = new InternetAddress(receive);
				}
			}
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, tos);

			// 设置邮件消息的主题
			mailMessage.setSubject(getSubject());

			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart multipart = new MimeMultipart();

			// 正文
			BodyPart mbp = new MimeBodyPart();
			mbp.setContent(getContent(), "text/html; charset=utf-8");// 设置HTML内容
			multipart.addBodyPart(mbp);

			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(multipart);
			// 发送邮件
			Transport.send(mailMessage);
			if(callBack!=null)
				callBack.success();
			return true;
		} catch (MessagingException e) {
//			log.error("发送html邮件异常：" + e.getMessage());
			e.printStackTrace();
			if(callBack!=null)
				callBack.fail();
			return false;
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] attachFileNames) {
		this.attachFileNames = attachFileNames;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

}

class MailAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MailAuthenticator() {
	}

	public MailAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

	public static void main(String[] args) {
		String host = "smtp.exmail.qq.com";		// 发送邮件的服务器的IP
		String port = "25";	// 发送邮件的服务器的端口

		String from = "noreply@depothelp.com";		// 邮件发送者的地址
		String userName = "noreply@depothelp.com";	// 登陆邮件发送服务器的用户名
		String password = "Abb123456!";	// 登陆邮件发送服务器的密码

		List<String> to = new ArrayList<String>();			// 邮件接收者的地址
		to.add("317394310@qq.com");

		boolean validate = true;	// 是否需要身份验证

		String subject = "标题test111";		// 邮件标题
		String content = "内容test111";		// 邮件的文本内容
//		String[] attachFileNames = new String[]{"D:/code.jpg"};	// 邮件附件的文件名
		SendMail mail = new SendMail(SendMail.sendType_html, host, port, validate, userName, password, from, to, subject, content, null,new EmailSendCallBack(){
			@Override
			public void fail() {
				System.out.println("发送失败");
			}

			@Override
			public void success() {
				System.out.println("发送成功");
			}
		});
		mail.start();
	}
}
