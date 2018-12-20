package com.yootii.bdy.docTitle.test;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.downloadapplicant.model.DocTitleWithBLOBs;
import com.yootii.bdy.downloadapplicant.service.DocTitleService;
import com.yootii.bdy.security.model.Token;
import com.yootii.bdy.security.service.AuthenticationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-mybatis.xml" })
public class TestDocTitleService {
	
	@Resource
	private AuthenticationService authenticationService;
	@Resource
	private DocTitleService docTitleService;
	
	private static final Logger logger = Logger.getLogger(TestDocTitleService.class);
//	@Test
	public void createTest() {
		DocTitleWithBLOBs docTitle = new DocTitleWithBLOBs();
		docTitle.setDocTypeId(1);
		docTitle.setLevel(1);
		docTitle.setOrderNum("1");
		docTitle.setTitle("理由概述");
		
		Token token = new Token();
		token.setUser(true);
		token.setUserID(2);
		try {
			ReturnInfo result = docTitleService.createDocTitle(docTitle, token);
			logger.info("result="+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void queryTest() {
		DocTitleWithBLOBs docTitle = new DocTitleWithBLOBs();
		docTitle.setDocTypeId(1);
//		docTitle.setLevel(1);
//		docTitle.setOrderNum("1");
//		docTitle.setTitle("理由概述");
		
		Token token = new Token();
//		token.setUser(true);
		token.setUserID(3);
		try {
			ReturnInfo result = docTitleService.queryDocTitle(docTitle, token);
			List<DocTitleWithBLOBs> docTitles = (List<DocTitleWithBLOBs>)result.getData();
			for(DocTitleWithBLOBs dt : docTitles){
				System.out.println("1"+dt.getTitle());
				System.out.println(dt.getContent());
				List<DocTitleWithBLOBs> docTitles2 = dt.getChildren();
				for(DocTitleWithBLOBs dt2:docTitles2){
					System.out.println("----2"+dt2.getTitle());
					List<DocTitleWithBLOBs> docTitles3 = dt2.getChildren();
					for(DocTitleWithBLOBs dt3:docTitles3){
						System.out.println("------3"+dt3.getTitle());
						List<DocTitleWithBLOBs> docTitles4 = dt3.getChildren();
						for(DocTitleWithBLOBs dt4:docTitles4){
							System.out.println("--------4"+dt4.getTitle());
							List<DocTitleWithBLOBs> docTitles5 = dt4.getChildren();
							for(DocTitleWithBLOBs dt5:docTitles5){
								System.out.println("--------5"+dt5.getTitle());
							}
						}
					}
				}
			}
			logger.info("result="+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@Test
	public void deleteTest() {
		DocTitleWithBLOBs docTitle = new DocTitleWithBLOBs();
		docTitle.setDocTypeId(1);
		docTitle.setLevel(1);
		docTitle.setOrderNum("1");
		docTitle.setTitle("理由概述");
		
		Token token = new Token();
//		token.setUser(true);
		token.setUserID(2);
		try {
			ReturnInfo result = docTitleService.deleteDocTitle(7, token);
			logger.info("result="+result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
