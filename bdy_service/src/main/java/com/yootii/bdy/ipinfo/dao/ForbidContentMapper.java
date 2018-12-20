package com.yootii.bdy.ipinfo.dao;

import com.yootii.bdy.ipinfo.model.ForbidContent;

public interface ForbidContentMapper {
    
    ForbidContent selectByContent(String content);
    
}