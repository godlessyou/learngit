package com.yootii.bdy.mail;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

public class SendMailUtil {

	private final Logger logger = Logger.getLogger(SendMailUtil.class);

	private long awaitTime = 10;
	
	public Boolean sendmail(MailSenderInfo mailInfo) throws Exception {

		// 设置邮件服务器信息
		mailInfo.setValidate(true);
		
		// 在独立的线程中发送邮件
		Boolean ret = sendMailWithTask(mailInfo);

		logger.info("邮件发送完毕");
		return ret;
	}

	public void sendMailSynchronize(MailSenderInfo mailInfo) throws Exception {

		// 设置邮件服务器信息
		mailInfo.setValidate(true);

		SimpleMailSender.sendHtmlMail(mailInfo);

		logger.info("邮件发送完毕");
	}

	public void sendmail(String[] args) {

		String host = args[0];
		String port = args[1];
		String username = args[2];
		String password = args[3];
		String from = username;
		String to = args[4];
		String subject = args[5];
		String mailContent = args[6];

		// 设置邮件服务器信息
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(host);
		mailInfo.setMailServerPort(port);
		mailInfo.setValidate(true);

		// 邮箱用户名
		mailInfo.setUserName(username);
		// 邮箱密码
		mailInfo.setPassword(password);
		// 发件人邮箱
		mailInfo.setFromAddress(from);
		// 收件人邮箱
		mailInfo.setToAddress(to);
		// 邮件主题
		mailInfo.setSubject(subject);
		// 邮件内容
		mailInfo.setContent(mailContent);

		// 发送html格式
		SimpleMailSender.sendHtmlMail(mailInfo);
		System.out.println("邮件发送完毕");
	}



	// 在独立的线程中发送邮件
	public Boolean sendMailWithTask(
			MailSenderInfo mailInfo) throws Exception {

		ExecutorService exec = Executors.newCachedThreadPool();

		MailTask task = new MailTask();
		task.mailInfo = mailInfo;

		String failReason = null;
		Future<Boolean> future =   exec.submit(task); 
		Boolean taskResult = false; 
		
		try {  
            // 等待计算结果，最长等待timeout秒，timeout秒后中止任务  
           taskResult = future.get(awaitTime, TimeUnit.SECONDS);
           taskResult = true;
			
        } catch (InterruptedException e) {  
            failReason = "主线程在等待计算结果时被中断！";  
        } catch (ExecutionException e) {  
            failReason = "主线程等待计算结果，但计算抛出异常！";  
        } catch (TimeoutException e) {  
            failReason = "主线程等待计算结果超时，因此中断任务线程！";              
            exec.shutdownNow();  
        } finally{
        	if (failReason!=null){
        		logger.info("failReason : " + failReason);
        	}
        }
		
		return taskResult;

	}

	class MailTask implements  Callable<Boolean> {

		public MailSenderInfo mailInfo = null;
		public boolean sendOne = false;

		@Override
		public Boolean call() throws Exception {
			boolean result=false; 
			// TODO Auto-generated method stub
			// 发送html格式			
			SimpleMailSender.sendHtmlMail(mailInfo);
			result=true;
			
	        return result;
		}

	}
	

	public static void main(String[] args) {
//		if (args == null || args.length == 0) {
//			args = new String[7];
//			args[0] = "smtp.126.com";
//			args[1] = "25";
//			args[2] = "michael2080@126.com";
//			args[3] = "Welcome2080";
//			args[4] = "2393270155@qq.com";
//			args[5] = "my subject";
//			args[6] = "this is a test";
//			
//		}
//		sendmail(args);
		
		MailSenderInfo mailInfo=new MailSenderInfo();
		mailInfo.setSubject("test");
		SendMailUtil sendMailUtil= new SendMailUtil();
		try {
			sendMailUtil.sendmail(mailInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
