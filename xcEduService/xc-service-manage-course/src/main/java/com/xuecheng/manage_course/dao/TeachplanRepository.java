package com.xuecheng.manage_course.dao;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: TeachplanRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 15:26
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
* 符合jpa的课程计划接口
* */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    //定义方法根据课程id和父节点id查询出节点列表，使用此方法查询节点
    public List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
