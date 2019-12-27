package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: TeachplanMediaPubRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/20 10:17
 * @Version: 1.0
 */
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub,String> {

    /**根据课程id删除课程计划媒资信息*/
    long deleteByCourseId(String courseId);
}
