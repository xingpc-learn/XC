package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator.
 * 符合jpa规范接口，继成连个接口
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase,String>, JpaSpecificationExecutor<CourseBase> {
}
