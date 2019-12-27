package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: CoursePicRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/2 20:51
 * @Version: 1.0
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    long deleteByCourseid(String courseId);
}
