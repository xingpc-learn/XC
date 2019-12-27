package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.course.ext
 * @ClassName: CourseView
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/3 19:29
 * @Version: 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class CourseView {

    //课程基本信息
    CourseBase courseBase;
    //课程营图片
    CoursePic coursePic;
    //课程营销信息
    CourseMarket courseMarket;
    //教学计划
    TeachplanNode teachplanNode;

}
