package com.yootii.bdy.task.service.Impl;


import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;


import com.yootii.bdy.task.service.FindCandidateUserService;


@Service("findCandidateUserService")
public class FindCandidateUserServiceImpl implements FindCandidateUserService {

//	@Resource
//	private ProcessService processService;
	
	public List<String> findAgents(String permission, String agencyId){
		 List<String> users=new ArrayList<String>();
		 
		 return users;
	}
	



	
}