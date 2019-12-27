package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.response.LearningCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.dao.XcLearningCourseRepository;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.order.service
 * @ClassName: XcLearningCourseService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 23:44
 * @Version: 1.0
 */
@Service
public class XcLearningCourseService {

    @Autowired
    XcLearningCourseRepository xcLearningCourseRepository;

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    /**
    * 完成选课
    * */
    @Transactional
    public ResponseResult addCourse(String userId, String courseId, String valid, Date startTime, Date endTime, XcTask xcTask){
        if (StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        if (StringUtils.isEmpty(userId)){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_USERISNULL);
        }
        if (xcTask==null||StringUtils.isEmpty(xcTask.getId())){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }
        //查询历史任务
        Optional<XcTaskHis> xcTaskHisOptional = xcTaskHisRepository.findById(xcTask.getId());
        if (xcTaskHisOptional.isPresent()){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        //历史任务不存在，添加课程，添加入历史记录
        XcLearningCourse course = xcLearningCourseRepository.findXcLearningCourseByUserIdAndCourseId(userId, courseId);
        if (course==null){
            //没有选课记录则添加
            course=new XcLearningCourse();
            course.setUserId(userId);
            course.setCourseId(courseId);
            course.setValid(valid);
            course.setStartTime(startTime);
            course.setEndTime(endTime);
            course.setStatus("501001");
            xcLearningCourseRepository.save(course);
        }else {
            //有课程记录则更新日期
            course.setValid(valid);
            course.setStartTime(startTime);
            course.setEndTime(endTime);
            course.setStatus("501001");
            xcLearningCourseRepository.save(course);
        }
        //向历史记录插入记录
        Optional<XcTaskHis> optional = xcTaskHisRepository.findById(xcTask.getId());
        if (!optional.isPresent()){
            //添加历史任务
            XcTaskHis xcTaskHis=new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

}
