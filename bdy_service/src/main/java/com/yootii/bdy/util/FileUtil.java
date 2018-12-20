package com.yootii.bdy.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.yootii.bdy.common.Constants;
public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);

	// 临时excel文件被打开时形成的临时文件名是以~$开头
	private static String tempFileHead = "~$";

	// 将图片保存到本地目录
	public static void saveImageWithData(String imagePath, byte[] b)
			throws Exception {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(imagePath);
			out.write(b);
			out.close();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	
	
	// 将文件保存到本地目录
	public static void saveFile(String filePath, InputStream in)
			throws Exception {
		FileOutputStream out = null;
		try {

			out = new FileOutputStream(filePath);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) != -1) {
				out.write(b, 0, len);
				out.flush();
			}

		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void saveAsFile(String file, String content) {

		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(file);
			fwriter.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	/**
	 * Creates a folder to desired location if it not already exists
	 * 
	 * @param dirName
	 *            - full path to the folder
	 * @throws SecurityException
	 *             - in case you don't have permission to create the folder
	 */
	public static void createFolderIfNotExists(String dirName)
			throws SecurityException {
		File theDir = new File(dirName);
		mkDir(theDir);
	}

	public static void mkDir(File file) {
		if (file.getParentFile().exists()) {
			file.mkdir();
		} else {
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}

	public static void deleteFile(String path) {
		File f = new File(path);
		if (f.exists()) {
			if (f.isFile()) {
				f.delete();
			} else {
				deleteDir(f);
			}
		}
	}

	public static boolean deleteDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(dir, children[i]);
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					deleteDir(file);
				}
			}
		}

		// The directory is now empty so now it can be smoked
		return dir.delete();
	}

	// 检查目录的时间戳，删除30分钟前创建的临时目录
	private static void checkAndDelete() {

		try {
			// 检查导出目录
			List<String> FilePathList = new ArrayList<String>();
			getDir(Constants.export_dir, FilePathList, 1);
			for (String filePath : FilePathList) {
				if (File.separator.equals("\\")) {
					if (filePath.indexOf("/") > -1) {
						filePath = filePath.replaceAll("/", "\\\\");
					}
				} else {
					if (filePath.indexOf("\\") > -1) {
						filePath = filePath.replaceAll("\\\\", "/");
					}
				}

				if (filePath.endsWith(File.separator)) {
					filePath = filePath.substring(0, filePath.length() - 1);
				}

				int pos = filePath.lastIndexOf(File.separator);
				if (pos < 0) {
					continue;
				}

				String dirName = filePath.substring(pos + 1);
				if (dirName == null || dirName.length() == 0) {
					continue;
				}

				boolean isNumber = StringUtils.isNum(dirName);
				if (!isNumber) {
					continue;
				}

				long startTime = Long.parseLong(dirName);
				long estimatedTime = System.nanoTime() - startTime;
				long estimatedSecond = estimatedTime / 1000000000;

				long duration = 1800;
				// 对于完成处理时间超过1800秒（30分钟）的临时目录进行删除
				if (estimatedSecond > duration) {
					File dir = new File(filePath);
					FileUtil.deleteDir(dir);
					// removedList.add(filePath);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 删除操作员目录下的邮件临时目录
	private static void doDelete() {
		try {
			File mailinfo_dir = new File(Constants.export_dir);
			if (mailinfo_dir.exists()) {
				String[] children = mailinfo_dir.list();
				for (int j = 0; j < children.length; j++) {
					File sub_dir = new File(mailinfo_dir, children[j]);
					FileUtil.deleteDir(sub_dir);
					System.out.println("delete " + sub_dir);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean deleteFilesOfDir(File dir) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				File file = new File(dir, children[i]);
				if (file.isFile()) {
					file.delete();
				} else if (file.isDirectory()) {
					deleteDir(file);
				}
			}
		}

		return true;
	}

	// 删除生成邮件的临时目录
	public static void deleteTempDir() {
		// 初始化之后，先执行删除临时目录的操作
		doDelete();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				// 假定邮件从生成到发送成功的间隔不会超过30分钟，所以删除30分钟前的邮件临时目录
				checkAndDelete();
			}
		}, 2000, 600 * 1000);// 指定任务task在指定延迟2000毫秒后，进行固定延迟600秒（10分钟）的执行。

	}

	public static void main(String[] args) {
//		System.setProperty("catalina.home", "C:/tomcat/apache-tomcat-7.0.65");
//		checkAndDelete();
		
		/*String sourceFile="c:/test/11.txt";
		String targetFile="c:/test/2016-02-25/11.txt";*/
		
		String sourceFile = "C:sss0.jpg";
		String targetFile = "C:ss.jpg";
	//	fileChannelCopy(sourceFile,targetFile);
		String aString = "abcdef";
		aString = aString.substring(1, aString.length());
		System.err.println(aString);
	}

	public static Properties readProperties(String file) {

		Properties properties = new Properties();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			if (loader == null) {
				loader = FileUtil.class.getClassLoader();
			}
			properties.load(loader.getResourceAsStream(file));

		} catch (Exception e) {
			StringBuilder sbInfo = new StringBuilder(
					"请将mailvm.properties放在下列目录：\n" + "Webapp/WEB-INF/classes\n"
							+ "Webapp/WEB-INF/lib\n");// 打印一些友好的tips

			logger.debug("没有找到mailvm.properties，将导致无法获得文件主题\n" + sbInfo);
		}

		return properties;

	}

	public ArrayList<String> getFileNames(String filePath) {
		ArrayList<String> fileNames = new ArrayList<String>();
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		for (int i = 0; i < listfile.length; i++) {
			String fileName = listfile[i].getPath().toString();
			fileNames.add(fileName);
		}

		return fileNames;

	}

	public static void getDir(String filePath, List<String> FilePathList,
			int subdir_jibie) {
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		if (listfile == null || listfile.length == 0)
			return;

		for (int i = 0; i < listfile.length; i++) {
			String subDirfilePath = listfile[i].toString();
			if (listfile[i].isDirectory()) {
				if (subdir_jibie > 0) {
					getDir(subDirfilePath, FilePathList, subdir_jibie - 1);
				} else {
					FilePathList.add(subDirfilePath);
				}
			}
		}
	}

	
	
	public static void getFile(String filePath, String[] extNames,
			List<String> FilePathList) {
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		if (listfile == null || listfile.length == 0)
			return;

		for (int i = 0; i < listfile.length; i++) {
			String subDirfilePath = listfile[i].toString();

			if (!listfile[i].isDirectory()) {
				if (extNames == null || extNames.equals("")) {
					FilePathList.add(subDirfilePath);
				} else {
					for (String extName : extNames) {
						// excel文件被打开时形成的临时文件名是以~$开头，在查找时需要排除这些文件。
						if (subDirfilePath.startsWith(tempFileHead)) {
							continue;
						}
						// 查找指定扩展名的文件
						if (subDirfilePath.endsWith(extName)) {

							FilePathList.add(subDirfilePath);
							System.out.println("****** = " + subDirfilePath);
							break;
						}
					}
				}
			} else {
				getFile(subDirfilePath, extNames, FilePathList);
			}
		}
	}
	
	
	
	public static void getSpecalFile(String filePath, String[] extNames,
			List<String> filePathList, String specailfileName) {
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		if (listfile == null || listfile.length == 0)
			return;

		for (int i = 0; i < listfile.length; i++) {
			String subDirfilePath = listfile[i].toString();

			if (!listfile[i].isDirectory()) {
				if (extNames == null || extNames.equals("")) {
					filePathList.add(subDirfilePath);
				} else {
					for (String extName : extNames) {						
						// 查找指定扩展名的文件
						if (subDirfilePath.endsWith(extName)) {
							if (subDirfilePath.indexOf(specailfileName) > -1) {
	    						filePathList.add(subDirfilePath);
	    						System.out.println("****** = " + subDirfilePath);
	    					}						
							break;
						}
					}
				}
			} else {
				getSpecalFile(subDirfilePath, extNames, filePathList, specailfileName);
			}
		}
	}

	public static ArrayList<String> getSubDir(String filePath) {
		ArrayList<String> subDirs = new ArrayList<String>();
		File file = new File(filePath);
		File[] listfile = file.listFiles();
		for (int i = 0; i < listfile.length; i++) {
			if (listfile[i].isDirectory()) {
				subDirs.add(listfile[i].getAbsolutePath());
			}
		}

		return subDirs;

	}

	/**
	 * 使用文件通道的方式复制文件
	 * 
	 * @param s
	 *            源文件
	 * @param t
	 *            复制到的新文件
	 */
	public static void fileChannelCopy(String sourceFile, String targetFile) {
		
		File s = new File(sourceFile);
		if (s.exists()){
			File t = new File(targetFile);
			FileInputStream fi = null;
			FileOutputStream fo = null;
			FileChannel in = null;
			FileChannel out = null;
			try {
				fi = new FileInputStream(s);
				fo = new FileOutputStream(t);
				in = fi.getChannel();// 得到对应的文件通道
				out = fo.getChannel();// 得到对应的文件通道
				in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					
					in.close();
					fi.close();
					out.close();
					fo.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// 将图片保存到本地目录
	public static String saveImage(String workDir, String trademarkImage) throws Exception {

		try {
			String imagePath = workDir + File.separator + "image001.jpg";
			String cutImageFile = workDir + File.separator + "image002.jpg";
			FileUtil.createFolderIfNotExists(workDir);
			//从ftp下载图样
			FtpTool.fileDownload(trademarkImage, imagePath);
			// 2016-10-12, 对于超过一定高度(目前设置为300像素)的图片,去掉图片上下的空白
			int limit = 300;
			int basesize = 50;
//			ImageUtil imageUtil = new ImageUtil();
			boolean result = ImageUtils.cutImage(imagePath, cutImageFile, limit);
			if (result) {
				imagePath = cutImageFile;
			}
			return imagePath;
		}catch (Exception e) {
			throw e;
		}

	}
	public static void fileDownLoad(HttpServletRequest request, HttpServletResponse response,String filename,String fullFileName) throws ServletException, IOException {  
		// TODO Auto-generated method stub  
		File file = new File(fullFileName);
		if (file.exists()) { // 文件存在
			/* 第二步：根据已存在的文件，创建文件输入流 */
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			/* 第三步：创建缓冲区，大小为流的最大字符数 */
			byte[] buffer = new byte[inputStream.available()]; // int available() 返回值为流中尚未读取的字节的数量
			/* 第四步：从文件输入流读字节流到缓冲区 */
			inputStream.read(buffer);
			/* 第五步： 关闭输入流 */
			inputStream.close();

			String fileName = filename;// 获取文件名     iso8859-1
			response.reset();
			response.addHeader("Content-Disposition",
					"attachment;filename=" +URLEncoder.encode(fileName, "UTF-8"));
			response.addHeader("Content-Length", "" + file.length());

			/* 第六步：创建文件输出流 */
			OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			/* 第七步：把缓冲区的内容写入文件输出流 */
			outputStream.write(buffer);
			/* 第八步：刷空输出流，并输出所有被缓存的字节 */
			outputStream.flush();
			/* 第九步：关闭输出流 */
			outputStream.close();

			//end  if (file.exists())
		} else {

			return;
		}
	}
	public static Integer getFileSize(String filePath) {  
		File file = new File(filePath);
		if(file.exists()&&file.isFile()){
			return (int)file.length();
		}
		return null;
	}
	
	
	// 拷贝文件
	public static void copyFile(String oldPath, String newPath) {
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				inStream = new FileInputStream(oldPath);
				fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			// System.out.println("error  ");
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	
}
