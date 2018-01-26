package com.yl.Mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import com.yl.bean.Mail;

public class MailUtil {
	// protected final Logger logger = Logger.getLogger(getClass());
	private static Logger log = Logger.getLogger(MailUtil.class);

	public static boolean send(Mail mail) {
		// 发送email
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
			email.setHostName(mail.getHost());
			// 字符编码集的设置
			email.setCharset(Mail.ENCODEING);
			// 收件人的邮箱
			email.addTo(mail.getReceiver());
			// 发送人的邮箱
			email.setFrom(mail.getSender(), mail.getName());
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
			email.setAuthentication(mail.getUsername(), mail.getPassword());
			// 要发送的邮件主题
			email.setSubject(mail.getSubject());
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(mail.getMessage());
			// 发送
			email.send();
			if (log.isDebugEnabled()) {
				log.debug(mail.getSender() + " 发送邮件到 " + mail.getReceiver());
			}
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			log.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 失败");
			return false;
		}
	}
	
	public static void main (String args[]){
		     Mail mail = new Mail();  
	        mail.setHost(MailConfig.host); // 设置邮件服务器,如果不用163的,自己找找看相关的  
	        mail.setSender(MailConfig.sender);  
	        mail.setReceiver(MailConfig.receiver); // 接收人  
	        mail.setUsername(MailConfig.sender); // 登录账号,一般都是和邮箱名一样吧  
	        mail.setPassword(MailConfig.password); // 发件人邮箱的登录密码  
	        mail.setSubject("aaaaaaaaa");  
	        mail.setMessage("bbbbbbbbbbbbbbbbb");  
	       send(mail);  
//	       MailSenderInfo mailInfo = new MailSenderInfo();
//           mailInfo.setMailServerHost(MailConfig.host);
//           mailInfo.setMailServerPort("25");
//           mailInfo.setValidate(true);
//           mailInfo.setUserName(MailConfig.sender);
//           mailInfo.setPassword(MailConfig.password);
//           mailInfo.setFromAddress(MailConfig.sender);
//           mailInfo.setToAddress(MailConfig.receiver);
//           mailInfo.setContent("ddddd");
//           // 这个类主要来发送邮件
//           SimpleMailSender sms = new SimpleMailSender();
//           //sms.sendTextMail(mailInfo);// 发送文体格式
//           sms.sendHtmlMail(mailInfo);// 发送html格式
	}
	
}
