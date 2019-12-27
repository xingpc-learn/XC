package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: CategoryMapper
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 21:52
 * @Version: 1.0
 */
@Mapper
public interface CategoryMapper {

    /**
    * 获取课程分类信息
    * */
    public CategoryNode getCategoryList();
}
