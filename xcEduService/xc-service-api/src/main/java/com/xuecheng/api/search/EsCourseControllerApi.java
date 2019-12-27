package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.search
 * @ClassName: EsCourseControllerApi
 * @Author: Administrator
 * @Description: 课程搜索接口
 * @Date: 2019/8/16 0:16
 * @Version: 1.0
 */
@Api(value = "课程搜索",description = "课程搜索",tags = {"课程搜索"})
public interface EsCourseControllerApi {

    @ApiOperation("课程搜索")
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);

    @ApiOperation("根据课程id查询课程信息")
    public Map<String,CoursePub> getAll(String courseId);

    @ApiOperation("根据课程计划查询媒资信息")
    public TeachplanMediaPub getMedia(String teachplanId);
}
