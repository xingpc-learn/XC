package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: CourseMarketRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/1 10:38
 * @Version: 1.0
 */
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {
}
