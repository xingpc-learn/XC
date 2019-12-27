package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.GetMediaResult;
import com.xuecheng.learning.service.CourseLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.learning.controller
 * @ClassName: CourseLearningController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/20 16:49
 * @Version: 1.0
 */
@RestController
@RequestMapping("/course/learning")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    CourseLearningService courseLearningService;

    /**
    * 根据课程id和教学计划id查询视频播放路径
    * */
    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getMedia(@PathVariable("courseId") String courseId, @PathVariable("teachplanId") String teachplanId) {
        //获取课程学习地址
        return courseLearningService.getMedia(courseId,teachplanId);
    }
}
