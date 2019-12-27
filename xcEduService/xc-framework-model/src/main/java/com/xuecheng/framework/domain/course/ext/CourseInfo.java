package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/2/10.
 * 将课程信息和匹配的图片进行封装为一个对象，传递给前端
 */
@Data
@ToString
public class CourseInfo extends CourseBase {

    //课程图片
    private String pic;

}
