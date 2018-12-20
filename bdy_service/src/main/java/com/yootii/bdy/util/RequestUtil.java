package com.yootii.bdy.util;

import java.io.File;
import java.io.IOException;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.yootii.bdy.common.ReturnInfo;


public class RequestUtil {

	public static Object returnObject(HttpServletRequest request, Object rtnInfo) {
		/*
		if (request != null) {
			// 获取callback的回调函数名
			String callbackFunctionName = request.getParameter("callback");
			if (callbackFunctionName != null) {
				MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(
						rtnInfo);
				mappingJacksonValue.setJsonpFunction(callbackFunctionName);
				return mappingJacksonValue;
			}
		}*/
		return rtnInfo;
	}

	
	
	
	public static ResponseEntity<byte[]> export(String filePath, String fileName)
			throws IOException {
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", fileName);
		File file = new File(filePath);
		if (!file.exists() ) {
			if ( file.getCanonicalPath().contains("null")) 
			{
				throw new IOException("文件'"+file.getName()+"'因未知原因没有找到，建议删除该附件后再重新上传一次!");
			} else {
				throw new IOException("文件'"+file.getName()+"'因未知原因没有找到!");
			}	
		}
		byte[] outputByte = FileUtils.readFileToByteArray(file);
		
//		return new ResponseEntity<byte[]>(outputByte, headers,
//				HttpStatus.CREATED);
		
		return new ResponseEntity<byte[]>(outputByte, headers,
				HttpStatus.OK);
	}
	
	
	
	public static ResponseEntity<ReturnInfo>  returnJsonObject(ReturnInfo rtnInfo)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8); 
        return new ResponseEntity<ReturnInfo>(rtnInfo, headers,
				HttpStatus.OK);
	}
	
	
	

	public static ResponseEntity<String>  returnHtmlObject(Object rtnInfo)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);  
		String outPutString = rtnInfo.toString();
        return new ResponseEntity<String>(outPutString, headers,
				HttpStatus.OK);
	}
}
