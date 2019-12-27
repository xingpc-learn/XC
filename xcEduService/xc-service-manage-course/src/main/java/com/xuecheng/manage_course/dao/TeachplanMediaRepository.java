package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: TeachplanMediaRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 18:52
 * @Version: 1.0
 */
@Repository
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia,String> {

    /**根据课程id查询课程计划媒资信息*/
    List<TeachplanMedia> findByCourseId(String courseId);
}
