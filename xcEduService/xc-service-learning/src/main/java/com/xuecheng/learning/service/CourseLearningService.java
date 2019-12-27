package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.GetMediaResult;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.CourseSearchClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.learning.service
 * @ClassName: CourseLearningService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/20 16:59
 * @Version: 1.0
 */
@Service
public class CourseLearningService {

    @Autowired
    CourseSearchClient courseSearchClient;

    /**
    * 根据课程id和课程计划id，远程调用课程管理服务、媒资管理服务获取课程学习地址
    * */
    public GetMediaResult getMedia(String courseId,String teachplanId){
        //校验学生的学习权限，是否付费等

        //调用课程搜索服务查询
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getMedia(teachplanId);
        if (teachplanMediaPub==null|| StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())){
            //获取视频播放地址出错
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        return new GetMediaResult(CommonCode.SUCCESS,teachplanMediaPub.getMediaUrl());
    }

}
