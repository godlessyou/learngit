package com.yootii.bdy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class WordTool {

	public static Logger logger = Logger.getLogger(FileUtil.class);

	// word运行程序对象
	// private ActiveXComponent activeXComponent = null;

	// private IConverter converter = null;

	public WordTool() {
		// init();
	}

	/** */
	/**
	 * 创建一个新的word文档
	 * 
	 */
	public void insertImage(String imagePath, String toFileName) {
		ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word
		Dispatch doc = null;
		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();

			doc = Dispatch.call(docs, "Add").toDispatch();
			Dispatch selection = app.getProperty("Selection").toDispatch();
			Dispatch.call(Dispatch.get(selection, "InLineShapes").toDispatch(),
					"AddPicture", imagePath);
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
					toFileName, new Variant(17) }, new int[1]);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Variant f = new Variant(false);
			Dispatch.call(doc, "Close", f);
			app.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}

	}

	public static boolean wordToPdf(String sfileName, String toFileName) {

		System.out.println("启动 Word...");
		long start = System.currentTimeMillis();
		ActiveXComponent app = null;
		Dispatch doc = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			doc = Dispatch.call(docs, "Open", sfileName).toDispatch();
			System.out.println("打开文档..." + sfileName);
			System.out.println("转换文档到 PDF..." + toFileName);
			File tofile = new File(toFileName);
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.call(doc, "SaveAs", toFileName, // FileName
					17);
			long end = System.currentTimeMillis();
			System.out.println("转换完成..用时：" + (end - start) + "ms.");

		} catch (Exception e) {
			System.out.println("========Error:文档转换失败：" + e.getMessage());
		} finally {
			Dispatch.call(doc, "Close", false);
			System.out.println("关闭文档");
			if (app != null)
				app.invoke("Quit", new Variant[] {});
		}
		// 如果没有这句话,winword.exe进程将不会关闭
		ComThread.Release();

		return true;
	}
	public void htmlToWord(String html, String docFile) {
		ActiveXComponent app = new ActiveXComponent("Word.Application"); // 启动word
		Dispatch doc = null;
		try {
			app.setProperty("Visible", new Variant(false));
			Dispatch docs = app.getProperty("Documents").toDispatch();
			doc = Dispatch
					.invoke(docs,
							"Open",
							Dispatch.Method,
							new Object[] { html, new Variant(false),
									new Variant(true) }, new int[1])
					.toDispatch();
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {
					docFile, new Variant(1) }, new int[1]);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Variant f = new Variant(false);
			Dispatch.call(doc, "Close", f);
			app.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}
	}

	// 将多个word文档合并
	public static void uniteDoc(List<String> fileList, String savepaths) {
		if (fileList.size() == 0 || fileList == null) {
			return;
		}
		// 打开word
		ActiveXComponent app = new ActiveXComponent("Word.Application");// 启动word
		Dispatch doc = null;
		try {
			// 设置word不可见
			app.setProperty("Visible", new Variant(false));
			// 获得documents对象
			Object docs = app.getProperty("Documents").toDispatch();
			// 打开第一个文件
			doc = Dispatch
					.invoke((Dispatch) docs,
							"Open",
							Dispatch.Method,
							new Object[] { (String) fileList.get(0),
									new Variant(false), new Variant(true) },
							new int[3]).toDispatch();

			Dispatch selection = app.getProperty("Selection").toDispatch();

			// 换行
			// Dispatch.call(selection, "TypeParagraph");

			// 把插入点移动到文件末尾位置
			Dispatch.call(selection, "EndKey", new Variant(6));

			// 追加文件
			for (int i = 1; i < fileList.size(); i++) {
				Dispatch.invoke(app.getProperty("Selection").toDispatch(),
						"insertFile", Dispatch.Method, new Object[] {
								(String) fileList.get(i), "",
								new Variant(false), new Variant(false),
								new Variant(false) }, new int[3]);
			}
			// 保存新的word文件
			Dispatch.invoke((Dispatch) doc, "SaveAs", Dispatch.Method,
					new Object[] { savepaths, new Variant(1) }, new int[3]);

		} catch (Exception e) {
			throw new RuntimeException("合并word文件出错.原因:" + e);
		} finally {
			Variant f = new Variant(false);
			Dispatch.call(doc, "Close", f);
			app.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}
	}

	// public boolean convert2(String sfileName, String toFileName) {
	//
	// long start = System.currentTimeMillis();
	//
	// File wordFile = new File(sfileName), target = new File(toFileName);
	//
	// // IConverter converter = LocalConverter.make();
	// // Future<Boolean> conversion = converter
	// // .convert(wordFile).as(DocumentType.MS_WORD)
	// // .to(target).as(DocumentType.PDF)
	// // .prioritizeWith(1000) // optional
	// // .schedule();
	//
	// boolean conversion = converter.convert(wordFile)
	// .as(DocumentType.MS_WORD).to(target).as(DocumentType.PDF)
	// .execute();
	// long end = System.currentTimeMillis();
	// logger.info("转换完成..用时：" + (end - start) + "ms.");
	//
	// return conversion;
	// }

//	private void init() {

		// activeXComponent = new ActiveXComponent("Word.Application");
		//
		// activeXComponent.setProperty("Visible", new Variant(false));

		// String tempDir = "C:\\tomcat\\apache-tomcat-7.0.65\\temp";
		// converter = LocalConverter.builder()
		// .baseFolder(new File(tempDir))
		// .workerPool(20, 25, 2, TimeUnit.SECONDS)
		// .processTimeout(5, TimeUnit.SECONDS)
		// .build();
		// converter = LocalConverter.make();

//	}

	public static void testWordToPdf() {
		String wordFile = "C:\\bdydoc\\template\\TM2018016220\\200\\1541407670895\\测试用例.doc";
		//for (int i = 0; i < 10; i++) {
			String pdfFile = "C:\\bdydoc.pdf";
			wordToPdf(wordFile, pdfFile);
	//	}
		// for (int i=0; i<10; i++){
		// String pdfFile = "C:\\JavaProjects\\automail\\test"+ i +".pdf";
		// wordToPdf.convert2(wordToPdf,pdfFile);
		// }
	}

	public void testUniteDoc() {
		List<String> list = new ArrayList<String>();
		String file1 = "C:\\mailtest\\file1.doc";
		String file2 = "C:\\mailtest\\file2.doc";

		list.add(file1);
		list.add(file2);

		uniteDoc(list, "C:\\mailtest\\file.doc");
	}

	public static void main(String[] args) {
		WordTool wordTool = new WordTool();
//		// wordTool.testUniteDoc();
//		String imgFile = "C:/mailtest/test/1-14585408-阿兰德拉伯胶及图-PUBLICATION.jpg";
//		String destFile = "C:/mailtest/test/1-14585408-阿兰德拉伯胶及图-PUBLICATION.doc";
//
//		wordTool.insertImage(imgFile, destFile);
		testWordToPdf();
		//WordTool d = new WordTool();
       // d.wordToPdf("D:\\bdydoc\\654321.doc", "D:\\bdydoc\\1.pdf");
	}

}
