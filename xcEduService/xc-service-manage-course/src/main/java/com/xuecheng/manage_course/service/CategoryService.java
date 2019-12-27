package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.service
 * @ClassName: CategoryService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 22:51
 * @Version: 1.0
 */
@Service
public class CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
    * 查询课程分类列表
    * */
    public CategoryNode getCategoryList(){
        return categoryMapper.getCategoryList();
    }
}
