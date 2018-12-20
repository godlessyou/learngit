package com.yootii.bdy.util;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;



import org.springframework.stereotype.Component;

import com.yootii.bdy.common.Constants;

import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Properties;
@Component
public class FtpTool {

	private static String ftpServer = "192.168.0.8";
	private static String ftpUsername = "yaodou";
	private static String ftpPassword = "123456";

	private static Properties props = null;
	
	private static boolean initFlag = false;
	public static Logger logger = Logger.getLogger(FtpTool.class);

	public static void main(String[] args) throws Exception {
		
		// ftpTool.init();

		String remote = "/input/报官清单回执+商标注册申请书.pdf";
		String local = "C:/商标监测页面需求说明书.docx";

//		FtpTool.fileDownload("\\\\doc.wanhuida.cn\\WHDDOC\\2017\\1\\16\\PDF\\2147483609_0_3823_7986926.pdf", local);
		FtpTool.fileDownload("/material/1/173/267/1/商标监测页面需求说明书.docx", local);
		
		// String aa = "-A9";
		// getEndString(aa.toCharArray());
		// String remote ="/admin/pic/3.gif";
		// String local ="c:/down.gif";
		// FtpTool ftpTool = new FtpTool();
		// // ftpTool.fileUpload(remote, local);
		// ftpTool.fileDownload(remote, local);
	}

//	public static void init() {
//		
//		if (!initFlag){
//
//			props = FileUtil.readProperties(Constants.ftpConfigFile);
//	
//			server = props.getProperty("server");
//			username = props.getProperty("username");
//			password = props.getProperty("password");
//			
//			initFlag = true;
//		}
//
//	}

	public static String getEndString(char[] chars) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < chars.length; ++i) {
			if (Character.isDigit(chars[i])) {
				break;
			}
			sb.append(chars[i]);
		}

		String result = sb.toString();
		return result;
	}

	/**
	 * FTP上传n个文件测试
	 */
	// public static void fileUpload(String[] remote, String[] local) {
	// FTPClient ftpClient = new FTPClient();
	// FileInputStream fis = null;
	//
	// try {
	// ftpClient.connect("192.168.14.117");
	// ftpClient.login("admin", "123");
	//
	// File srcFile = new File("C:\\new.gif");
	// fis = new FileInputStream(srcFile);
	// // 设置上传目录
	// ftpClient.changeWorkingDirectory("/admin/pic");
	// ftpClient.setBufferSize(1024);
	// ftpClient.setControlEncoding("GBK");
	// // 设置文件类型（二进制）
	// ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	// ftpClient.storeFile("3.gif", fis);
	// } catch (IOException e) {
	// e.printStackTrace();
	// throw new RuntimeException("FTP客户端出错！", e);
	// } finally {
	//
	// try {
	// if (fis != null)
	// fis.close();
	// ftpClient.disconnect();
	// } catch (IOException e) {
	// e.printStackTrace();
	// throw new RuntimeException("关闭FTP连接发生异常！", e);
	// }
	// }
	// }

	/**
	 * FTP下载单个文件测试
	 */
	public static void fileDownload(String remote, String local)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fos = null;

		try {
			ftpClient.connect(ftpServer);
			ftpClient.login(ftpUsername, ftpPassword);
//			ftpClient.connect("192.168.0.8",21);
//			ftpClient.login("yaodou", "123456");
			boolean connected = ftpClient.isConnected();
			if (!connected) {
				throw new RuntimeException("连接FTP客户端出错！");
			}
			// for (int i=0; i<remote.length; i++){
			String remoteFileName = remote;

			// ftpClient.changeWorkingDirectory("/input");

			fos = new FileOutputStream(local);

			ftpClient.setBufferSize(1024);
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			String ftpFile = new String(remoteFileName.getBytes("GBK"),
					"iso-8859-1");
			boolean getFile = ftpClient.retrieveFile(ftpFile, fos);
			if (!getFile) {
				ftpFile = new String(remoteFileName.getBytes("UTF-8"),
						"iso-8859-1");
				getFile = ftpClient.retrieveFile(ftpFile, fos);
				if (!getFile) {
					throw new RuntimeException("从FTP服务器获取文件出错");
				}
			}

		} catch (Exception e) {
			logger.error("下载文件失败...",e);
			throw e;
		} finally {
			try {
				if (fos != null)
					fos.close();
				ftpClient.disconnect();
			} catch (IOException e) {
				logger.error("下载文件失败...",e);
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	public static String getFtpServer() {
		return ftpServer;
	}

	public static void setFtpServer(String ftpServer) {
		FtpTool.ftpServer = ftpServer;
	}

	public static String getFtpUsername() {
		return ftpUsername;
	}

	public static void setFtpUsername(String ftpUsername) {
		FtpTool.ftpUsername = ftpUsername;
	}

	public static String getFtpPassword() {
		return ftpPassword;
	}

	public static void setFtpPassword(String ftpPassword) {
		FtpTool.ftpPassword = ftpPassword;
	}
}