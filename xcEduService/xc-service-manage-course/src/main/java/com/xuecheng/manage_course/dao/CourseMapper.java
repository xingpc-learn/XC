package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {

   /**
   * 根据id查询课程
   * */
   CourseBase findCourseBaseById(String id);

   /**
    *分页查询课程列表和对应图片
    **/
   Page<CourseInfo> findCourseListPage(CourseListRequest courseListRequest);
}
