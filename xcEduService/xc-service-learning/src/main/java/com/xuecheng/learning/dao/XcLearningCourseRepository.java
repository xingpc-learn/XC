package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.order.dao
 * @ClassName: XcLearningCourseRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 23:38
 * @Version: 1.0
 */
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse,String> {

    /**根据用户和课程查询选课记录，用于判断是否添加选课*/
    XcLearningCourse findXcLearningCourseByUserIdAndCourseId(String userId,String courseId);



}
