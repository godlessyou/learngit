package com.yootii.bdy.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.yootii.bdy.common.Constants;
import com.yootii.bdy.tmcase.model.TradeMarkCaseGuanWen;

public class PdfBoxUtil {

	public static Logger logger = Logger.getLogger(PdfBoxUtil.class);

	public static TradeMarkCaseGuanWen readPdf(String filePath,	String gongzhangFile, String erweimaFile) {

		TradeMarkCaseGuanWen tradeMarkCaseGuanWen=null;
		try {
			

			File file = new File(filePath);
			if (!file.exists()) {
				logger.info(filePath + " is not exist.");
				return null;
			}
			
//			String outputPath= Constants.app_dir+Constants.guanwen_dir;
//			FileUtil.createFolderIfNotExists(outputPath);

			InputStream inputStream = new BufferedInputStream(
					new FileInputStream(file));
			// PDFParser parser = new PDFParser( inputStream );
			// parser.parse();
			PDDocument pdfDocument = PDDocument.load(inputStream);
			PDFTextStripper stripper = new PDFTextStripper();

			// 从PDF文档对象中剥离文本
			// PDDocument pdfDocument = parser.getPDDocument();
			// StringWriter writer = new StringWriter();
			// stripper.writeText(pdfDocument, writer);
			// String contents = writer.getBuffer().toString();

			String contents = stripper.getText(pdfDocument);

			logger.info(contents);

			tradeMarkCaseGuanWen=getContent(contents);
			
			if (tradeMarkCaseGuanWen==null){
				return null;
			}

			/** 文档页面信息 **/
			PDDocumentCatalog cata = pdfDocument.getDocumentCatalog();

			PDPageTree pageTree = cata.getPages();

			Iterator<PDPage> list = pageTree.iterator();

			while (list.hasNext()) {
				PDPage page = (PDPage) list.next();
				if (null == page) {
					continue;
				}

				PDResources resources = page.getResources();
				int num = 0;
				for (COSName name : resources.getXObjectNames()) {
					PDXObject xobject = resources.getXObject(name);
					if (!(xobject instanceof PDImageXObject)) {
						continue;
					}
					num++;
					PDImageXObject imageObject = (PDImageXObject) xobject;
					String suffix = imageObject.getSuffix();
					if (suffix == null) {
						continue;
					}
					
//					String appName=tradeMarkCaseGuanWen.getAppCnName();
					
					String appNumber=tradeMarkCaseGuanWen.getAppNumber();
					
					String outputPath= Constants.app_dir+Constants.guanwen_dir+"/"+appNumber+"/";
					
					FileUtil.createFolderIfNotExists(outputPath);

					String outputFile = outputPath+ "image" + num + ".jpg";

					BufferedImage bufferedImage = imageObject.getImage();

					ImageSimilarity fp1 = new ImageSimilarity(bufferedImage);
					ImageSimilarity fp2 = new ImageSimilarity(
							ImageIO.read(new File(gongzhangFile)));

					// 判断图片是否是公章的图片
					float compResult = fp1.compare(fp2);
					if (compResult > 0.8) {
						logger.info("this image is gong zhang ");
						continue;
					}

					ImageSimilarity fp3 = new ImageSimilarity(
							ImageIO.read(new File(erweimaFile)));
					// 判断图片是否是二维码的图片
					float compResult2 = fp1.compare(fp3);
					if (compResult2 > 0.8) {
						logger.info("this image is er wei ma ");
						continue;
					}

					// 保存图片到指定目录
					OutputStream outputStream = new FileOutputStream(outputFile);
					boolean writeOK = ImageIO.write(bufferedImage, "jpg",
							outputStream);
					if (writeOK) {
						String sourceFile = outputFile;
						String tempFile = outputPath + "image" + num + "-1.jpg";
						String destFile = outputPath + "image" + num + "-2.jpg";
						int limit=300;
						// 将图片的白边去掉，并且将图片大小进行压缩
						 boolean result = ImageUtils
						 .compressImage(sourceFile,
						 tempFile, destFile, limit);
						 if (result){
							 tradeMarkCaseGuanWen.setImagePath(destFile);
							 return tradeMarkCaseGuanWen;
						 }
					}
				}
				// 获取页面图片信息
			}

		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		return tradeMarkCaseGuanWen;
	}
	
	
	//从文中获取商标信息
	public static TradeMarkCaseGuanWen getContent(String contents) {
		String agentNum = getAgentNume(contents);
//		logger.info(agentNum);
		String appCnName=getApplicantName(contents);
//		logger.info(appCnName);
		String appNumber = getAppNumber(contents);
//		logger.info(appNumber);
		String dateString = getAppDate(contents);
//		logger.info(dateString);
		String goodClasses = getGoodClasses(contents);
//		logger.info(goodClasses);		
		String docDate = getDocDate(contents);
		logger.info(docDate);
		
		TradeMarkCaseGuanWen tradeMarkCaseGuanWen=new TradeMarkCaseGuanWen();
		
		tradeMarkCaseGuanWen.setAgentNum(agentNum);
		tradeMarkCaseGuanWen.setAppCnName(appCnName);
		tradeMarkCaseGuanWen.setAppNumber(appNumber);
		tradeMarkCaseGuanWen.setGoodClasses(goodClasses);
		tradeMarkCaseGuanWen.setDocDate(docDate);
				
		Date appDate=DateTool.StringToDate(dateString);
		tradeMarkCaseGuanWen.setAppDate(appDate);
		
		return tradeMarkCaseGuanWen;

	}
	
	
	

	public static String getAgentNume(String str) {

		// 邮箱验证规则
		String regEx = "(?is)(TM)(.*)(?=发文编号：)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String result = matcher.group(0);
			String result2=result.trim();
			return result2;
		}
		return null;

	}

	public static String getApplicantName(String str) {
		// 验证规则
		String regEx = "(?is)(?<=[\\u4e00-\\u9fa5A-Za-z0-9]\\r\\n)(.*)(?=：\\s*?\\r\\n\\s+?)";
//		String regEx = "[\\u4e00-\\u9fa5A-Za-z0-9]\\r\\n";
//		String regEx = "(?is)(?<=[\\u4e00-\\u9fa5A-Za-z0-9]\\r\\n{2})(.*)(?=：\\s*?\\r\\n\\s+?)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String tempStr = matcher.group(0);
			String result=null;
			while(tempStr!=null){
				tempStr=getAppName(tempStr);
				if (tempStr!=null){
					result=tempStr;
				}
			}
			return result;	
		}
		
		return null;

	}
	
	
	
	public static String getAppName(String str) {
		// 验证规则
		String regEx = "(?is)(?<=[\\u4e00-\\u9fa5A-Za-z0-9]\\r\\n)(.*)";

		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String result = matcher.group(0);
			return result;
		}
		return null;
	}


	public static String getAppNumber(String str) {

		// 验证规则
		String regEx = "(?is)(?<=申请日期:)(.*)(?=申请号)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String result = matcher.group(0);
			String result2=result.trim();
			return result2;			
		}
		return null;

	}

	public static String getAppDate(String str) {

		// 验证规则
//		String regEx = "(?is)(?<=申请号:)\\d{4}年\\d{1,2}月\\d{1,2}日  || (?is)(?<=申请日期:)\\d{4}年\\d{1,2}月\\d{1,2}日";
//		String regEx = "(?is)(?<=申请号:)(.*)(?=日)";
		String regEx = "(?is)(?<=申请号:)\\d{4}年\\d{1,2}月\\d{1,2}日";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String dateString = matcher.group(0);
			if (dateString.indexOf("年")>-1){
				dateString=dateString.replaceAll("年", "-");
				dateString=dateString.replaceAll("月", "-");
				dateString=dateString.replaceAll("日", "");
			}
			String result=dateString;
			return result;
		}
		return null;

	}

	public static String getGoodClasses(String str) {

		// 验证规则
//		String regEx = "(?is)(?<=申请号:)\\d{4}年\\d{1,2}月\\d{1,2}日  || (?is)(?<=申请日期:)\\d{4}年\\d{1,2}月\\d{1,2}日";
//		String regEx = "(?is)(?<=申请号:)(.*)(?=日)";
		String regEx = "(?is)(?<=类别：第)(.*)(?=类)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String result = null;
			int count = matcher.groupCount();
			for (int i = 0; i < count; i++) {
				String s = matcher.group(i);
				if (result == null) {
					result = s;
				} else {
					result = result + ";" + s;
				}
			}
			return result;

		}
		return null;

	}
	
	
	public static String getDocDate(String str) {

		// 验证规则
		String regEx = "(?is)(?<=北京万慧达知识产权代理有限公司\\r\\n)(.*)(?=\\r\\n邮政编码:\\r\\n)";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		// 忽略大小写的写法
		Matcher matcher = pattern.matcher(str);
		
		// 字符串是否与正则表达式相匹配
		if (matcher.find()) {
			String dateString = matcher.group(0);
			
			if (dateString.indexOf("年")>-1){
				dateString=dateString.replaceAll("年", "-");
				dateString=dateString.replaceAll("月", "-");
				dateString=dateString.replaceAll("日", "");
			}
			String result=dateString;
			return result;
		}
		return null;

	}
	
	
	//读取word 生成一个pdf文件
	public static void convertToPDF(){
		
		String workDir = "C:\\bdydoc\\template\\TM2018016220\\200\\1541407670895\\测试用例.doc";
		
		try{
			Document document =new Document();
			File file = new File("C:\\bdydoc.pdf");
			File fileInput = new File(workDir);
			OutputStream outputStream = new FileOutputStream(file);
			InputStream inputStream = new FileInputStream(fileInput);
			
			
			PdfWriter.getInstance(document, outputStream);
			document.open();
			Paragraph paragraph = new Paragraph("hello word"+"this is pdf");
			document.add(paragraph);
			document.close();
			outputStream.close();
			System.err.println("创建成功");
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 *pdf转图片  生成多张
	 */
	public static void pdfToImg(){
		try{
			File file =new File("C:\\bdydoc.pdf");
			//
			InputStream input = new FileInputStream(file);
			PDDocument pdDocument = PDDocument.load(input);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			int pageCount = pdDocument.getNumberOfPages();
			System.err.println(pageCount);
			for(int i=0;i<pageCount ;i++){
				File file2  = new File("C:\\sss.jpg");
				BufferedImage bufferedImage = renderer.renderImageWithDPI(i,130,ImageType.RGB);
				ImageIO.write(bufferedImage,"jpg", file2);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//
	public static void pdf2multiImage(String pdfFile, String outpath) {
		try {
			InputStream is = new FileInputStream(pdfFile);
			PDDocument pdf = PDDocument.load(is);
			List<BufferedImage> piclist = new ArrayList<BufferedImage>();
			File file =new File(pdfFile);
			InputStream input = new FileInputStream(file);
			PDDocument pdDocument = PDDocument.load(input);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			int pageCount = pdDocument.getNumberOfPages();
			System.err.println(pageCount);
			for(int i=0;i<pageCount ;i++){
				BufferedImage bufferedImage = renderer.renderImageWithDPI(i,130,ImageType.RGB);
				piclist.add(bufferedImage);
			}
			yPic(piclist, outpath);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//处理图片  合成 单张的图片
	public static void yPic(List<BufferedImage> piclist, String outPath) {// 纵向处理图片
		if (piclist == null || piclist.size() <= 0) {
			System.out.println("图片数组为空!");
			return;
		}
		try {
			int height = 0, // 总高度
			width = 0, // 总宽度
			_height = 0, // 临时的高度 , 或保存偏移高度
			__height = 0, // 临时的高度，主要保存每个高度
			picNum = piclist.size();// 图片的数量
			File fileImg = null; // 保存读取出的图片
			int[] heightArray = new int[picNum]; // 保存每个文件的高度
			BufferedImage buffer = null; // 保存图片流
			List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
			int[] _imgRGB; // 保存一张图片中的RGB数据
			for (int i = 0; i < picNum; i++) {
				buffer = piclist.get(i);
				heightArray[i] = _height = buffer.getHeight();// 图片高度
				if (i == 0) {
					width = buffer.getWidth();// 图片宽度
				}
				height += _height; // 获取总高度
				_imgRGB = new int[width * _height];// 从图片中读取RGB
				_imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
				imgRGB.add(_imgRGB);
			}
			_height = 0; // 设置偏移高度为0
			// 生成新图片
			BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < picNum; i++) {
				__height = heightArray[i];
				if (i != 0) _height += __height; // 计算偏移高度
				imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
			}
			File outFile = new File(outPath);
			ImageIO.write(imageResult, "jpg", outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//供外部调用生成 图片
	public static void conversion2Img(String workDir,String filePath,String fileName){
		
		try{
			File file = new File(filePath);
			PDDocument pdDocument = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			int pageCount = pdDocument.getNumberOfPages();
			System.err.println(pageCount);
			for(int i=0;i<pageCount ;i++){
				File file2  = new File(workDir+"\\"+fileName+i+".jpg");
				BufferedImage bufferedImage = renderer.renderImageWithDPI(i,130,ImageType.RGB);
				ImageIO.write(bufferedImage,"jpg", file2);
			}
			
		}catch (Exception e) {
			e.getMessage();
		}
	}
	
	
	
	

	public static void main(String[] args) {

		// String str="申请日期: 26401047申请号:2017年9月14日";

		// String str="摩恩公司";
		//
		// PdfBoxUtil.getApplicantName(str);

		// String
		// filePath="C:/work/biaodeyi/官文自动提取/北京万慧达知识产权代理有限公司/商标注册申请受理通知书-26376255-莱雅公司.pdf";
		//convertToPDF();
		//pdfToImg();
		String pdfFile = "C:\\A.pdf";
		String outpath = "C:\\pp.jpg";
		pdf2multiImage(pdfFile,outpath);
		/*String filePath = "C:\\work\\biaodeyi\\官文自动提取\\北京万慧达知识产权代理有限公司\\商标注册申请受理通知书-26401047-摩恩公司.pdf";
		String gongzhangFile = "C:\\work\\biaodeyi\\官文自动提取\\gongzhang.jpg";
		String erweimaFile = "C:\\work\\biaodeyi\\官文自动提取\\erweima.jpg";
		if (filePath.indexOf("/") < 0) {
			filePath = filePath.replaceAll("\\\\", "/");
		}

		int pos = filePath.lastIndexOf("/");

		String fileName = filePath.substring(pos + 1);

		pos = fileName.lastIndexOf(".");
		String path = "";
		if (pos > -1) {
			path = fileName.substring(0, pos);
		}

		String outputPath = "C:/work/biaodeyi/官文自动提取/" + path + "/";

		FileUtil.createFolderIfNotExists(outputPath);

		PdfBoxUtil.readPdf(filePath,  gongzhangFile, erweimaFile);*/

	}

}
