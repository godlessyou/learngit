package com.yootii.bdy.util;

//import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.poi.POIXMLDocumentPart;
//import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackageRelationshipCollection;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFVMLDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ExcelUtil {
	// private static Logger logger = Logger.getLogger(ExcelUtil.class);

	// 默认单元格内容为数字时格式
	private static DecimalFormat df = new DecimalFormat("0");
	// 默认单元格格式化日期字符串
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	// 格式化数字
	private static DecimalFormat nf = new DecimalFormat("0.00");

	// 提取excel表格中的数据
	public static List<Object> getCellData(
			Map<Integer, List<ExcelRow>> sheetList,
			Map<Integer, String> propertyNames, String className) {

		List<Object> tmcList = new ArrayList<Object>();
		

		try {
			Iterator<Entry<Integer, List<ExcelRow>>> iter = sheetList
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, List<ExcelRow>> entry = (Map.Entry<Integer, List<ExcelRow>>) iter
						.next();
//				Integer sheetNumber = entry.getKey();
				List<ExcelRow> excelContent = entry.getValue();
				int size = 0;
				if (excelContent != null) {
					size = excelContent.size();
				}

				for (int i = 1; i < size; i++) {
					ExcelRow excelRow = excelContent.get(i);
					
					int rowNumber=excelRow.getRowNumber();
					
					List<Object> obj=excelRow.getColList();
					
					int colSize = obj.size();

//					System.out.println("row: " + i);

					// 通过反射机制，创建对象实例
					Class<?> clazz = Class.forName(className);
					Object bean=clazz.newInstance();
					
					for (int j = 0; j < colSize; j++) {
						Object objValue = obj.get(j);
					
						if (objValue == null) {
							continue;
						}
						
						String value =  objValue.toString();
						Iterator<Entry<Integer, String>> iter222 = propertyNames
								.entrySet().iterator();
						while (iter222.hasNext()) {
							Map.Entry<Integer, String> entry222 = (Map.Entry<Integer, String>) iter222
									.next();
							Integer colId = entry222.getKey();

							// 获取每一列的属性值，并根据每一列的序号与对象属性名的对应关系
							// 形成属性名值对，设置到对象中。
							if (colId.intValue() == j) {
								String propertyName = entry222.getValue();
								ObjectUtil.setProperty(bean, propertyName,
										value);
							}
						}
						
					}
					
					
					//设置该对象对应的excel表格中的行号
					//便于后面进行数据正确性检查时，返回错误数据所在的行号
					String lineNo=rowNumber+"";
					ObjectUtil.setProperty(bean, "lineNo", lineNo);
					
					tmcList.add(bean);

					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
//		System.out.println(count);
		return tmcList;
	}
	
	
	
	// 提取excel表格中的数据
	/*public static List<Object> getCellDataOfCustomField(
				Map<Integer, List<ExcelRow>> sheetList,
				Map<Integer, CustomField> propertyNames, String className) {

		List<Object> tmcList = new ArrayList<Object>();
		

		try {
			Iterator<Entry<Integer, List<ExcelRow>>> iter = sheetList
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, List<ExcelRow>> entry = (Map.Entry<Integer, List<ExcelRow>>) iter
						.next();
//					Integer sheetNumber = entry.getKey();
				List<ExcelRow> excelContent = entry.getValue();
				int size = 0;
				if (excelContent != null) {
					size = excelContent.size();
				}

				for (int i = 1; i < size; i++) {
					ExcelRow excelRow = excelContent.get(i);
					
					int rowNumber=excelRow.getRowNumber();
					
					List<Object> obj=excelRow.getColList();
					
					int colSize = obj.size();

//						System.out.println("row: " + i);
					
					ObjectValueList objectValueList=new ObjectValueList();

					
					for (int j = 0; j < colSize; j++) {
						Object objValue = obj.get(j);
						if (objValue == null) {
							continue;
						}
						String value =  objValue.toString();
						Iterator<Entry<Integer, CustomField>> iter222 = propertyNames
								.entrySet().iterator();
						while (iter222.hasNext()) {
							Map.Entry<Integer, CustomField> entry222 = (Map.Entry<Integer, CustomField>) iter222
									.next();
							Integer colId = entry222.getKey();

							// 获取每一列的属性值，并根据每一列的序号与对象属性名的对应关系
							// 形成属性名值对，设置到对象中。
							if (colId.intValue() == j) {
								CustomField field = entry222.getValue();
								Integer cfId=field.getCfId();
								String dataType=field.getDataType();
								
								//根据数据类型获取属性名
								String propertyName=getPropertyName(dataType);
								
								// 通过反射机制，创建对象实例								
								Class<?> clazz = Class.forName(className);
								Object bean=clazz.newInstance();
								
								ObjectUtil.setProperty(bean, propertyName,
										value);
							
								//设置cfId属性值
								ObjectUtil.setProperty(bean, "cfId",
										cfId.toString());
								
								objectValueList.getList().add(bean);
								
							}
						}
						
					}
					
					
					//设置该对象对应的excel表格中的行号
					//便于后面进行数据正确性检查时，返回错误数据所在的行号
					String lineNo=rowNumber+"";
					ObjectUtil.setProperty(objectValueList, "lineNo", lineNo);
					
					tmcList.add(objectValueList);

					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
//			System.out.println(count);
		return tmcList;
	}*/
	
	
	
	
	//根据数据类型获取属性名
	private static String getPropertyName(String dataType){
		
		String propertyName=null;
		if (dataType.equals("string")){
			propertyName="stringValue";
		}
		else if (dataType.equals("integer")){
			propertyName="numberValue";
		}
		else if (dataType.equals("date")){
			propertyName="dateValue";
		}
		else if (dataType.equals("text")){
			propertyName="textValue";
		}
		
		return propertyName;
	}
	
	
	

	public static Map<Integer, List<ExcelRow>> readExcel(File file,
			boolean readAllSheet) {
		if (file == null) {
			return null;
		}
		if (file.getName().endsWith("xlsx")) {
			// 处理ecxel2007
			return readExcel2007(file, readAllSheet);
		} else {
			// 处理ecxel2003
			return readExcel2003(file, readAllSheet);
		}
	}

	/*
	 * @return 将返回结果存储在ArrayList内，存储结构与二位数组类似
	 * lists.get(0).get(0)表示过去Excel中0行0列单元格
	 */
	public static Map<Integer, List<ExcelRow>> readExcel2003(File file,
			boolean readAllSheet) {
		try {
			Map<Integer, List<ExcelRow>> sheetList = new HashMap<Integer, List<ExcelRow>>();
			List<ExcelRow> rowList = new ArrayList<ExcelRow>();
			List<Object> colList;
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));

			int sheetNumber = wb.getNumberOfSheets();

			for (int k = 0; k < sheetNumber; k++) {

				if (!readAllSheet) {
					if (k > 0) {
						break;
					}
				}

				HSSFSheet sheet = wb.getSheetAt(0);
				HSSFRow row;
				HSSFCell cell;
				Object value;
				for (int i = sheet.getFirstRowNum(); i< sheet.getLastRowNum(); i++) {
//				for (int i = sheet.getFirstRowNum(), rowCount = 0; rowCount < sheet
//						.getPhysicalNumberOfRows(); i++) {
					row = sheet.getRow(i);
					colList = new ArrayList<Object>();
					if (row == null) {
						// 当读取行为空时
//						if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
//							rowList.add(colList);
//						}
						continue;
					} else {
//						rowCount++;
					}
					
					ExcelRow excelRow=new ExcelRow();
					excelRow.setRowNumber(i);
					
					
					for (int j = row.getFirstCellNum(); j <= row
							.getLastCellNum(); j++) {
						cell = row.getCell(j);
						if (cell == null
								|| cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
							// 当该单元格为空
							if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
								colList.add("");
							}
							continue;
						}
						switch (cell.getCellType()) {
						case XSSFCell.CELL_TYPE_STRING:
							// System.out.println(i + "行" + j +
							// " 列 is String type");
							value = cell.getStringCellValue();
							break;
						case XSSFCell.CELL_TYPE_NUMERIC:
							if ("@".equals(cell.getCellStyle()
									.getDataFormatString())) {
								value = df.format(cell.getNumericCellValue());
							} else if ("General".equals(cell.getCellStyle()
									.getDataFormatString())) {
								value = nf.format(cell.getNumericCellValue());
							} else {
								value = sdf
										.format(HSSFDateUtil.getJavaDate(cell
												.getNumericCellValue()));
							}
							// System.out.println(i + "行" + j
							// + " 列 is Number type ; DateFormt:"
							// + value.toString());
							break;
						case XSSFCell.CELL_TYPE_BOOLEAN:
							// System.out.println(i + "行" + j +
							// " 列 is Boolean type");
							value = Boolean.valueOf(cell.getBooleanCellValue());
							break;
						case XSSFCell.CELL_TYPE_BLANK:
							// System.out.println(i + "行" + j +
							// " 列 is Blank type");
							value = "";
							break;
						default:
							System.out.println(i + "行" + j
									+ " 列 is default type");
							value = cell.toString();
						}// end switch
						colList.add(value);
					}// end for j
					
					excelRow.setColList(colList);
					rowList.add(excelRow);
					
				}// end for i

			}// end for k
			sheetList.put(0, rowList);
			return sheetList;
		} catch (Exception e) {
			return null;
		}
	}

	public static Map<Integer, List<ExcelRow>> readExcel2007(
			File excelFile, boolean readAllSheet) {
		try {

			Map<Integer, List<ExcelRow>> sheetList = new HashMap<Integer, List<ExcelRow>>();
			List<ExcelRow> rowList = new ArrayList<ExcelRow>();

			XSSFWorkbook workbook = null;
			InputStream in = null;
			try {
				in = new FileInputStream(excelFile);
				workbook = new XSSFWorkbook(in);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			if (readAllSheet) {
				int sheetNumber = workbook.getNumberOfSheets();
				for (int k = 0; k < sheetNumber; k++) {
					rowList = readExcelContent(workbook, k);
					sheetList.put(k, rowList);
				}
			} else {
				rowList = readExcelContent(workbook, 0);
				sheetList.put(0, rowList);
			}

			return sheetList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<ExcelRow> readExcelContent(XSSFWorkbook workbook,
			int sheetNumber) {
		List<ExcelRow> rowList = new ArrayList<ExcelRow>();
		List<Object> colList;
		try {

			XSSFSheet sheet = workbook.getSheetAt(sheetNumber);
			XSSFRow row;
			XSSFCell cell;
			
//			for (int i = sheet.getFirstRowNum(), rowCount = 0; rowCount < sheet
//					.getPhysicalNumberOfRows(); i++) {
			for (int i = sheet.getFirstRowNum(); i<= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				colList = new ArrayList<Object>();
				if (row == null) {
					// 当读取行为空时
//					if (i != sheet.getPhysicalNumberOfRows()) {// 判断是否是最后一行
//						rowList.add(colList);
//					}					
					continue;
				} else {
//					rowCount++;
				}

				ExcelRow excelRow=new ExcelRow();
				excelRow.setRowNumber(i);
				
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					if (j < 0) {
						break;
					}
					// System.out.println("cell number is: " + j);
					cell = row.getCell(j);
					if (cell == null
							|| cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						// 当该单元格为空
						if (j != row.getLastCellNum()) {// 判断是否是该行中最后一个单元格
							colList.add("");
						}
						continue;
					}

					// 读取表格中的值
					Object value = readCell(cell);					

					colList.add(value);
				}// end for j
				
				excelRow.setColList(colList);
				
				rowList.add(excelRow);
			}// end for i

			return rowList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Object readCell(XSSFCell cell) {
		Object value = null;
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			// System.out.println(i + "行" + j +
			// " 列 is String type");
			value = cell.getStringCellValue();
			String s = (String) value;
			if (s.indexOf("(") > -1) {
				s = s.replace("(", "（");
			}
			if (s.indexOf(")") > -1) {
				s = s.replace(")", "）");
			}
			value = s;
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			if ("@".equals(cell.getCellStyle().getDataFormatString())) {
				value = df.format(cell.getNumericCellValue());
			} else if ("General".equals(cell.getCellStyle()
					.getDataFormatString())) {
				//value = nf.format(cell.getNumericCellValue());
				value = df.format(cell.getNumericCellValue());
			} else {
				value = sdf.format(HSSFDateUtil.getJavaDate(cell
						.getNumericCellValue()));
			}
			// System.out.println(i + "行" + j
			// + " 列 is Number type ; DateFormt:"
			// + value.toString());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			// System.out.println(i + "行" + j +
			// " 列 is Boolean type");
			value = Boolean.valueOf(cell.getBooleanCellValue());
			break;
		case XSSFCell.CELL_TYPE_BLANK:
			value = "";
			break;
		default:
			// System.out.println(i + "行" + j +
			// " 列 is default type");
			value = cell.toString();
		}// end switch

		return value;
	}

	/**
	 * 获取一个表内所有的图片，并根据标签号进行图片的分类存储
	 */
	public static void getSheetPictrues2007(String excelfilePath,
			String imagePath, int colNumber, int sheetNumber) {
		File excelFile = new File(excelfilePath);
		if (!excelFile.exists())
			return;
		XSSFWorkbook workbook = null;
		InputStream in = null;
		try {
			in = new FileInputStream(excelFile);
			workbook = new XSSFWorkbook(in);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		int sheetCount = 0;
		// 如果sheetNumber<0,处理所有sheet中的图片
		if (sheetNumber < 0) {
			sheetCount = workbook.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				getSheetPictrues(workbook, imagePath, colNumber, i);
			}
		} else {
			getSheetPictrues(workbook, imagePath, colNumber, sheetNumber);
		}

	}

	/**
	 * 获取一个表的sheet内所有的图片，并根据商标注册号进行图片的分类存储
	 */
	public static void getSheetPictrues(XSSFWorkbook workbook,
			String imagePath, int colNumber, int sheetNumber) {

		String key = "注册号";
		int keyLen = key.length();
		int count = 0;
		XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

		boolean getRegNumberCol = false;
		if (colNumber < 0) {
			getRegNumberCol = true;
		}

		int number = 0;

		for (POIXMLDocumentPart dr : sheet.getRelations()) {
			String picName = null;

			PackagePart pPart = dr.getPackagePart();

			String contentType = pPart.getContentType();
			// application/vnd.openxmlformats-officedocument.drawing+xml
			// application/vnd.openxmlformats-officedocument.vmlDrawing
			if (contentType.endsWith(".drawing+xml")
					|| contentType.endsWith(".vmlDrawing")) {
				XSSFPictureData xpd = null;
				if (dr instanceof XSSFVMLDrawing) {// emf图片会进入这个分支
					XSSFVMLDrawing drawing = (XSSFVMLDrawing) dr;

					List<POIXMLDocumentPart> shapes = drawing.getRelations();

					for (POIXMLDocumentPart shape : shapes) {
						count++;
						if (shape instanceof XSSFPictureData) {
							xpd = (XSSFPictureData) shape;
							// 可以得到护展名,但没办法得到图片的起始位置.

							picName = sheetNumber + "_" + count;
							if (picName != null) {
								// 保存图片数据到文件
								savePicture(picName, imagePath, xpd);
							}
						}

					}
				} else if (dr instanceof XSSFDrawing) {
					XSSFDrawing drawing = (XSSFDrawing) dr;
					java.util.List<XSSFShape> shapes = drawing.getShapes();

					for (XSSFShape shape : shapes) {

						count++;
						XSSFPicture pic = (XSSFPicture) shape;
						xpd = pic.getPictureData();

						XSSFClientAnchor anchor = pic.getPreferredSize();
						org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker ctMarker = anchor
								.getFrom();//

						int rowNumber = ctMarker.getRow();
						int col = -1;
						// 如果没有给定注册号/申请号所在的列的序号，那么，取当前图片所在的列的序号
						if (getRegNumberCol) {
							col = ctMarker.getCol();
						} else {
							col = colNumber;
						}

						XSSFCell cell = sheet.getRow(rowNumber).getCell(col);
						String fileNameDesc = "";
						if (cell != null) {
							// 读取表格中的值
							Object value = readCell(cell);
							fileNameDesc = (String) value;
							if (fileNameDesc == null || fileNameDesc.equals("")
									|| fileNameDesc.equals("暂无")) {
								// 如果注册号/申请号为空，那么取sheet号+"_"+行号作为文件名

								fileNameDesc = sheetNumber + "_" + rowNumber;
							}
						} else {
							// 如果注册号/申请号为空，那么取sheet号+"_"+行号作为文件名
							fileNameDesc = sheetNumber + "_" + rowNumber;
						}

						picName = fileNameDesc;

						// System.out.println("fileNameDesc: "+ fileNameDesc);

						if (fileNameDesc.indexOf(key) > -1) {
							int pos = fileNameDesc.indexOf("，");
							if (pos > -1) {
								String temp = fileNameDesc.substring(0, pos);
								int pos2 = temp.indexOf(key);
								if (pos2 > -1) {
									picName = temp.substring(pos2 + keyLen);
								}
							}
						}

						if (picName != null) {
							// System.out.println("savePicture, picName: "+
							// picName);
							// 保存图片数据到文件
							savePicture(picName, imagePath, xpd);

						}

					}
				}
			} else if (contentType
					.equals("application/vnd.openxmlformats-officedocument.oleObject")) {

				System.out.println("Unknown Embedded Document: " + contentType);
				try {
					InputStream inputStream = pPart.getInputStream();

					PackageRelationshipCollection prc = pPart
							.getRelationships();

					String fileName = "mytest" + number;

					String filePath = imagePath + "/" + fileName;

					FileUtil.saveFile(filePath, inputStream);

					number++;

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// System.out.println("count: "+ count);

		}
	}

	// 保存图片数据到文件
	public static void savePicture(String picName, String imagePath,
			XSSFPictureData xpd) {

		// 对于文件命中包含/的，为了避免形成子目录形式，出现java.io.FileNotFoundException，用-符号替换
		if (picName.indexOf("/") > -1) {
			picName = picName.replaceAll("/", "-");
		}

		// String picName = "image" + count + ".jpg";
		String savePath = imagePath + File.separator + picName + ".jpg";

		FileOutputStream fos = null;
		try {
			File picFile = new File(savePath);

			if (!picFile.exists()) {
				fos = new FileOutputStream(savePath);

				fos.write(xpd.getData());

				System.out.println("savePicture: " + savePath);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/*
	 * 保存excel中的图片(以文件形式保存，或者存入数据库)
	 * 
	 * basePath:应用所在路径,附件存放路径: 参数：is是上传的附件文件流
	 */

	/*
	 * public static void saveSheetImgByFile(String excelPath, String imagePath)
	 * throws Exception { File excelFile = new File(excelPath); if
	 * (!excelFile.exists()) return; XSSFWorkbook workbook = new
	 * XSSFWorkbook(new FileInputStream(excelFile)); XSSFSheet sheet =
	 * workbook.getSheetAt(1); // getSheetPictrues07(sheet, workbook,
	 * imagePath);
	 * 
	 * // FileOutputStream os = null; // List<XSSFPictureData> pictures =
	 * workbook.getAllPictures(); // int i = 0; // for (XSSFShape shape :
	 * sheet.getDrawingPatriarch().getChildren()) { // XSSFClientAnchor anchor =
	 * (XSSFClientAnchor) shape.getAnchor(); // // if (shape instanceof
	 * XSSFPicture) { // XSSFPicture pic = (XSSFPicture) shape; // int row =
	 * anchor.getRow1(); // System.out.println(i + "--->" + anchor.getRow1() +
	 * ":" // + anchor.getCol1()); // int pictureIndex =
	 * pic.getPictureIndex()-1; // XSSFPictureData picData =
	 * pictures.get(pictureIndex); // // System.out.println(i + "--->" +
	 * pictureIndex); // savePic(row, picData); // } // i++; // }
	 * 
	 * }
	 */
	/*
	 * private static void savePic(int i, PictureData pic) throws Exception {
	 * 
	 * String ext = pic.suggestFileExtension();
	 * 
	 * byte[] data = pic.getData(); if (ext.equals("jpeg")) { FileOutputStream
	 * out = new FileOutputStream( "D:\\Users\\Fancy1_Fan\\桌面\\work\\pict" + i +
	 * ".jpg"); out.write(data); out.close(); } if (ext.equals("png")) {
	 * FileOutputStream out = new FileOutputStream(
	 * "D:\\Users\\Fancy1_Fan\\桌面\\work\\pict" + i + ".png"); out.write(data);
	 * out.close(); } }
	 */

/*	public static void writeExcel(ArrayList<ArrayList<Object>> result)
			throws Exception {
		writeExcel(result, null, "");
	}

	public static void writeExcel(ArrayList<ArrayList<Object>> result,
			String excelFilePath) throws Exception {
		writeExcel(result, excelFilePath, "");
	}*/

	/*public static void writeExcel(ArrayList<ArrayList<Object>> result,
			String excelFilePath, String dataType) throws Exception {

		// String imagePath = null;
		if (result == null) {
			return;
		}
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("sheet1");
		// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
		HSSFPatriarch patriarch = null;

		boolean insertImage = false;

		// String imagePath = null;

		// String destFile = null;

		// 第一行数据是各个列的标题
		ArrayList<Object> firstRowData = result.get(0);
		if (firstRowData != null && firstRowData.size() > 0) {
			String title = (String) firstRowData.get(0);
			if (title.equals("商标图样")) {
				insertImage = true;
				patriarch = sheet.createDrawingPatriarch();

				// int pos = excelFilePath.lastIndexOf(File.separator);
				// String workDir = excelFilePath.substring(0, pos + 1);
				// imagePath = workDir + "image001.jpg";
				// destFile = workDir + "image002.jpg";
			}
			HSSFRow firstRow = sheet.createRow(0);
			HSSFCell cell0 = firstRow.createCell(0);
			cell0.setCellValue(title);

			short col1 = 0; // 第一列
			short col2 = 1; // 第二列
			if (insertImage) {
				// HSSFCell cell1 = firstRow.createCell(1);

				// 因为图片占据了两个单元格，所以，需要把第一列和第二列合并
				// 单元格合并
				// 四个参数分别是：起始行，起始列，结束行，结束列
				sheet.addMergedRegion(new Region(0, col1, 0, col2));
			}

			for (int j = 1; j < firstRowData.size(); j++) {
				String columnTile = (String) firstRowData.get(j);
				int cellNumber = j;
				// 因为图片占据了两个单元格，所以，需要从第三列开始写其他数据的标题
				if (insertImage) {
					cellNumber = j + 1;
				}
				HSSFCell cell = firstRow.createCell(cellNumber);
				cell.setCellValue(columnTile);
			}

			// 因为第一列是图片数据，所以从第二列开始获取其它数据
			int startColumn = 0;
			if (insertImage) {
				startColumn = 1;
			}
			for (int i = 1; i < result.size(); i++) {
				HSSFRow row = sheet.createRow(i);
				if (result.get(i) != null) {
					// 现在第一列插入图片
					if (insertImage) {
						// 列宽
						// sheet1.setColumnWidth((short)1,(short)2500);
						// sheet1.setColumnWidth((short)2,(short)2500);

						// 行高
						row.setHeight((short) 1500);

						short row1 = (short) i; // 每一行
						boolean creatFile = false; // 插入图片，不立即不生成excel文件，等所有数据插入后再生成

						// 从数据库获取图片数据
						String decondeString = (String) result.get(i).get(0);

						if (decondeString != null) {
							int len = decondeString.length();
							int beginIndex = Constants.base64ImageStr.length();
							// 去掉data:image/png;base64,
							if (len > beginIndex) {
								String base64String = decondeString
										.substring(beginIndex);

								
								 * int aLen=base64String.length();
								 * 
								 * // 将Base64位编码的图片进行解码，并保存到指定目录
								 * ImageUtils.decodeBase64ToImage(base64String,
								 * imagePath);
								 * 
								 * boolean
								 * success=ImageUtils.cutImage(imagePath,
								 * destFile); String filePath = imagePath; if
								 * (success){ filePath = destFile; String
								 * test=ImageUtils
								 * .encodeImgageToBase64(destFile);
								 * 
								 * int blen =test.length();
								 * 
								 * System.out.println("aLen:"+aLen+ " blen:" +
								 * blen);
								 * 
								 * }
								 * 
								 * // 将图片插入每一行的第一列
								 * insertImageToCell(wb,patriarch,filePath,
								 * col1, row1, excelFilePath, creatFile);
								 

								// 优化上述实现，不再生成临时的图片文件，直接使用图片的字节数组
								// 将Base64位编码的图片进行解码，获取字节数组
								byte[] decoderBytes = ImageUtils
										.getImagebytes(base64String);
								// // 将图片插入每一行的第一列
								insertImageByteToCell(wb, patriarch,
										decoderBytes, col1, row1,
										excelFilePath, creatFile);
							}
						}

						// 因为图片占据了两个单元格，所以，需要把第一列和第二列合并
						// 单元格合并
						// 四个参数分别是：起始行，起始列，结束行，结束列
						sheet.addMergedRegion(new Region(row1, col1, row1, col2));

					}
					ArrayList<Object> otherRowData = result.get(i);
					int colunmSize = otherRowData.size();
					for (int j = startColumn; j < colunmSize; j++) {
						int cellNumber = j;
						// 因为图片占据了两个单元格，所以，需要从第三列开始写其他数据的标题
						if (insertImage) {
							cellNumber = j + 1;
						}
						HSSFCell cell = row.createCell(cellNumber);
						cell.setCellValue(otherRowData.get(j).toString());
					}
				}
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			wb.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}

		byte[] content = os.toByteArray();
		File file = new File(excelFilePath);// Excel文件生成后存储的位置。
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/

	public static void insertImageToCell(String imageFile, short col1,
			short row1, String excelFile, boolean creatFile) {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			File file = new File(imageFile);
			if (file.exists()) {
				bufferImg = ImageIO.read(file);
				ImageIO.write(bufferImg, "png", byteArrayOut);

				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet1 = wb.createSheet("test picture");
				// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
				HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
				// anchor主要用于设置图片的属性
				// col1 图片的左上角放在第几个列cell，
				// row1 图片的左上角放在第几个行cell，
				//
				// col2 图片的右下角放在第几个列cell，
				// row2 图片的右下角放在第几个行cell，
				// 列宽
				// sheet1.setColumnWidth((short)1,(short)2500);
				// sheet1.setColumnWidth((short)2,(short)2500);

				// 行高
				HSSFRow row = sheet1.createRow(row1);
				row.setHeight((short) 1200);
				short col2 = col1;
				col2++;

				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,
						(short) col1, row1, (short) col2, row1);
				anchor.setAnchorType(3);
				// 插入图片
				patriarch.createPicture(anchor, wb.addPicture(
						byteArrayOut.toByteArray(),
						HSSFWorkbook.PICTURE_TYPE_JPEG));
				if (creatFile) {
					fileOut = new FileOutputStream(excelFile);
					// 写入excel文件
					wb.write(fileOut);
					System.out.println("----Excle文件已生成------");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void insertImageByteToCell(HSSFWorkbook wb,
			HSSFPatriarch patriarch, byte[] imageBytes, short col1, short row1,
			String excelFile, boolean creatFile) {
		FileOutputStream fileOut = null;
		try {
			// anchor主要用于设置图片的属性
			// col1 图片的左上角放在第几个列cell，
			// row1 图片的左上角放在第几个行cell，
			//
			// col2 图片的右下角放在第几个列cell，
			// row2 图片的右下角放在第几个行cell，

			short col2 = col1;
			col2++;

			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,
					(short) col1, row1, (short) col2, row1);
			anchor.setAnchorType(3);
			// 插入图片
			patriarch.createPicture(anchor,
					wb.addPicture(imageBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
			if (creatFile) {
				fileOut = new FileOutputStream(excelFile);
				// 写入excel文件
				wb.write(fileOut);
				System.out.println("----Excle文件已生成------");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void insertImageToCell(HSSFWorkbook wb,
			HSSFPatriarch patriarch, String imageFile, short col1, short row1,
			String excelFile, boolean creatFile) {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			File file = new File(imageFile);
			if (file.exists()) {
				bufferImg = ImageIO.read(file);
				ImageIO.write(bufferImg, "jpg", byteArrayOut);

				// anchor主要用于设置图片的属性
				// col1 图片的左上角放在第几个列cell，
				// row1 图片的左上角放在第几个行cell，
				//
				// col2 图片的右下角放在第几个列cell，
				// row2 图片的右下角放在第几个行cell，

				short col2 = col1;
				col2++;

				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,
						(short) col1, row1, (short) col2, row1);
				anchor.setAnchorType(3);
				// 插入图片
				patriarch.createPicture(anchor, wb.addPicture(
						byteArrayOut.toByteArray(),
						HSSFWorkbook.PICTURE_TYPE_JPEG));
				if (creatFile) {
					fileOut = new FileOutputStream(excelFile);
					// 写入excel文件
					wb.write(fileOut);
					System.out.println("----Excle文件已生成------");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void insertImageToCell(String imageFile, String excelFile) {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		// 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			File file = new File(imageFile);
			if (file.exists()) {
				bufferImg = ImageIO.read(file);
				ImageIO.write(bufferImg, "png", byteArrayOut);

				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet1 = wb.createSheet("test picture");
				// 画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
				HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
				// anchor主要用于设置图片的属性
				// col1 图片的左上角放在第几个列cell，
				// row1 图片的左上角放在第几个行cell，
				//
				// col2 图片的右下角放在第几个列cell，
				// row2 图片的右下角放在第几个行cell，
				// 列宽
				// sheet1.setColumnWidth((short)1,(short)2500);
				// sheet1.setColumnWidth((short)2,(short)2500);
				// 行高
				HSSFRow row = sheet1.createRow(1);
				row.setHeight((short) 1200);
				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,
						(short) 0, 1, (short) 1, 1);
				anchor.setAnchorType(3);
				// 插入图片
				patriarch.createPicture(anchor, wb.addPicture(
						byteArrayOut.toByteArray(),
						HSSFWorkbook.PICTURE_TYPE_JPEG));
				fileOut = new FileOutputStream(excelFile);
				// 写入excel文件
				wb.write(fileOut);
				System.out.println("----Excle文件已生成------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static DecimalFormat getDf() {
		return df;
	}

	public static void setDf(DecimalFormat df) {
		ExcelUtil.df = df;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		ExcelUtil.sdf = sdf;
	}

	public static DecimalFormat getNf() {
		return nf;
	}

	public static void setNf(DecimalFormat nf) {
		ExcelUtil.nf = nf;
	}

	
	
	// 提取excel表格中的数据
	public static List<Object> getCellData(
			Map<Integer, List<ExcelRow>> sheetList,
			Map<Integer, String> propertyNames, String className, String imagePath) {

		List<Object> list = new ArrayList<Object>();		

		try {

			Iterator<Entry<Integer, List<ExcelRow>>> iter = sheetList
					.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<Integer, List<ExcelRow>> entry = (Map.Entry<Integer, List<ExcelRow>>) iter
						.next();
//						Integer sheetNumber = entry.getKey();
				List<ExcelRow> excelContent = entry.getValue();
				getObjectFromCell(excelContent, propertyNames, className,imagePath, list);				

			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
//				System.out.println(count);
		return list;
	}
	
	
	
	
	// 提取excel表格中的数据
	public static void getObjectFromCell(List<ExcelRow> excelContent,
			Map<Integer, String> propertyNames, String className, String imagePath,List<Object> list) {
		
		try {				
		
				int size = 0;
				if (excelContent != null) {
					size = excelContent.size();
				}
				

				for (int i = 1; i < size; i++) {
					ExcelRow excelRow = excelContent.get(i);
					
					int rowNumber=excelRow.getRowNumber();
					
					List<Object> rowData=excelRow.getColList();
					
					int colSize = rowData.size();

//								System.out.println("row: " + i);

					// 通过反射机制，创建对象实例
					Class<?> clazz = Class.forName(className);
					Object bean=clazz.newInstance();
					
					for (int j = 0; j < colSize; j++) {
						Object colValue = rowData.get(j);
					
						if (colValue == null) {
							continue;
						}
						
						String value =  colValue.toString();
						if(value.equals("")){
							continue;
						}
						
						Iterator<Entry<Integer, String>> iter222 = propertyNames
								.entrySet().iterator();
						while (iter222.hasNext()) {
							Map.Entry<Integer, String> entry222 = (Map.Entry<Integer, String>) iter222
									.next();
							Integer colId = entry222.getKey();

							// 获取每一列的属性值，并根据每一列的序号与对象属性名的对应关系
							// 形成属性名值对，设置到对象中。
							if (colId.intValue() == j) {
								String propertyName = entry222.getValue();									
									ObjectUtil.setProperty(bean, propertyName,
												value);
								
							}
						}
						
					}
					
					
					//设置该对象对应的excel表格中的行号
					//便于后面进行数据正确性检查时，返回错误数据所在的行号
					int lineNumber=rowNumber+1;
					String lineNo=lineNumber+"";
					ObjectUtil.setProperty(bean, "lineNo", lineNo);		
					
					list.add(bean);						
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	
	}
		
	
	
		
		
		
	public static void main(String[] args) {
		File file = new File(
				"C:\\work\\huiguanjia\\data\\咪咕文化科技有限公司\\咪咕互动娱乐有限公司.xlsx");
		Map<Integer, List<ExcelRow>> result = ExcelUtil.readExcel(file,
				false);
		// for(int i = 0 ;i < result.size() ;i++){
		// for(int j = 0;j<result.get(i).size(); j++){
		// System.out.println(i+"行 "+j+"列  "+ result.get(i).get(j).toString());
		// }
		// }
		// ExcelUtil.writeExcel(result,"C:/work/huiguanjia/data/bb.xls");
		// String imageFile = "C:/work/huiguanjia/data/taikang/20883960.png";
		// String excelFile = "C:/work/huiguanjia/data/taikang/test1.xls";
		// short col1 = 0;
		// short row1 = 1;
		// boolean creatFile = true;
		// ExcelUtil
		// .insertImageToCell(imageFile, col1, row1, excelFile, creatFile);

		// String excelfilePath =
		// "C:\\work\\huiguanjia\\data\\taikang\\5\\tm1.xlsx";
		String excelfilePath = "C:\\support\\waibucasedata\\yunnandata2.xlsx";
		String imagePath = "C:\\support\\waibucasedata";
		try {
			getSheetPictrues2007(excelfilePath, imagePath, -1, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
		

}