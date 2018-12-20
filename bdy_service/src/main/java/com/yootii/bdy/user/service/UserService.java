package com.yootii.bdy.user.service;



import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.model.User;
public interface UserService {  

   
    //判断用户角色
    public boolean checkUserPermission(String permission, String userId, String tokenID);

    //获取具备指定权限的用户集合
    public List<String> findUsersByPermission(String permission, String agencyId, String tokenID);
    
    public User getUserById(String userId, String tokenID) throws Exception;
    
    public User getUserByUserName(String username, String tokenID) throws Exception;
    
    public User queryAdminByAgencyId(String agencyId, String tokenID) throws Exception;
}  