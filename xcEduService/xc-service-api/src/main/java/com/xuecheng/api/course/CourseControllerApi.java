package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.course
 * @ClassName: CourseControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 10:19
 * @Version: 1.0
 */
@Api(value="course页面管理接口",description="course页面管理接口，提供课程的增、删、改、查")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程基础信息")
    public AddCourseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("根据id获取课程基础信息")
    public CourseBase getCourseBaseById(String courseId);

    @ApiOperation("修改课程基础信息")
    public ResponseResult updateCourseBase(String courseId,CourseBase courseBase);

    @ApiOperation("查询课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("修改课程营销信息")
    public ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket);

    @ApiOperation("添加课程图片关联信息到数据库")
    public ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("查询课程图片")
    public CoursePic getCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("查询课程视图")
    public CourseView getCourseView(String courseId);

    @ApiOperation("课程预览")
    public CoursePublishResult preview(String courseId);

    @ApiOperation("课程发布")
    public CoursePublishResult publish(String courseId);

    @ApiOperation("保存媒资信息")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia);

}
