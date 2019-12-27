package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.web.controller
 * @ClassName: CourseController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 11:33
 * @Version: 1.0
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    //注入service
    @Autowired
    CourseService courseService;

    /**
    * 获取课程计划
    * */
    @Override
    @PreAuthorize("hasAuthority('course_teachplan_list')")
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId){
        return courseService.findTeachplanList(courseId);
    }

    /**
    * 添加课程计划
    * */
    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /**
    * 分页显示课程信息
    * */
    @Override
    @PreAuthorize("hasAuthority('course_find_list')")
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,@PathVariable("size") int size,CourseListRequest courseListRequest) {
        /*//先使用静态数据测试
        String companyId="1";*/
        //调用工具类取出用户信息
        XcOauth2Util xcOauth2Util=new XcOauth2Util();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        XcOauth2Util.UserJwt userJwt=xcOauth2Util.getUserJwtFromHeader(request);
        if (userJwt==null){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        return courseService.getCourseList(userJwt.getCompanyId(),page,size ,courseListRequest);
    }

    /**
    * 添加课程基础信息
    * */
    @Override
    @PostMapping("/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    /**
    * 根据id查询课程基础信息
    * */
    @Override
    @PreAuthorize("hasAuthority('course_get_baseinfo')")
    @GetMapping("/coursebasebyid/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseBaseById(courseId);
    }

    /**
    * 更新课程基础信息
    * */
    @Override
    @PutMapping("/updatecoursebase/{courseId}")
    public ResponseResult updateCourseBase(@PathVariable("courseId") String courseId, @RequestBody CourseBase courseBase) {
        return courseService.updateCoursebase(courseId,courseBase );
    }

    /**
    * 查询课程营销信息
    * */
    @Override
    @GetMapping("/coursemarket/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    /**
    * 更新课程营销信息
    * */
    @Override
    @PutMapping("/updatecoursemarket/{courseId}")
    public ResponseResult updateCourseMarket(@PathVariable("courseId") String courseId, @RequestBody CourseMarket courseMarket) {
        return courseService.updateCourseMarket(courseId,courseMarket);
    }

    /**
    * 添加课程图片
    * */
    @Override
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.addCoursePic(courseId,pic);
    }

    /**
    * 查询课程图片
    * */
    @Override
    @PreAuthorize("hasAuthority('course_find_pic')")
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic getCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.getCoursePic(courseId);
    }

    /**
    * 删除课程图片
    * */
    @Override
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    /**
    * 查询课程视图
    * */
    @Override
    @GetMapping("/courseview/{id}")
    public CourseView getCourseView(@PathVariable("id") String courseId) {
        return courseService.getCourseView(courseId);
    }

    /**
    * 预览课程
    * */
    @Override
    @PostMapping("/preview/{courseId}")
    public CoursePublishResult preview(@PathVariable("courseId") String courseId) {
        return courseService.preview(courseId);
    }

    /**
    * 课程发布
    * */
    @Override
    @PostMapping("/publish/{courseId}")
    public CoursePublishResult publish(@PathVariable("courseId") String courseId) {
        return courseService.publish(courseId);
    }

    /**
    * 保存媒资信息
    * */
    @Override
    @PostMapping("/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia) {
        return courseService.saveMedia(teachplanMedia);
    }

}
