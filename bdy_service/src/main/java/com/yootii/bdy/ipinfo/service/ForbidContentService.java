package com.yootii.bdy.ipinfo.service;



import com.yootii.bdy.common.GeneralCondition;
import com.yootii.bdy.common.ReturnInfo;
import com.yootii.bdy.trademark.model.Trademark;


public interface ForbidContentService {  
           
    
    public ReturnInfo checkForbidContent(String content);
    
    public ReturnInfo checkSameTm(String tmName,String tmType,String localSearch, GeneralCondition gcon);
    
    
    public ReturnInfo querySameTm(String regNumber,  GeneralCondition gcon) ;
    
    
    public ReturnInfo queryTm(String regNumber,  String tmType, GeneralCondition gcon) ;
    
}  