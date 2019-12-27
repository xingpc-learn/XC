package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: TeachplanMapper
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 10:58
 * @Version: 1.0
 */
@Mapper
public interface TeachplanMapper {

    /**
    * 查询课程计划列表
    * */
    public TeachplanNode selectList(String courseId);

}
