package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.search.controller
 * @ClassName: EsCourseController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/16 1:10
 * @Version: 1.0
 */
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    EsCourseService esCourseService;

    @Override
    @GetMapping(value="/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,@PathVariable("size") int size, CourseSearchParam courseSearchParam) {
        return esCourseService.list(page,size ,courseSearchParam );
    }

    /**
    * 根据课程id查询所有课程信息
    * */
    @Override
    @GetMapping("/getall/{id}")
    public Map<String, CoursePub> getAll(@PathVariable("id") String courseId) {
        return esCourseService.getAll(courseId);
    }

    /**
    *根据教学计划查询媒资信息
    * */
    @Override
    @GetMapping(value = "/getmedia/{teachplanId}")
    public TeachplanMediaPub getMedia(@PathVariable("teachplanId") String teachplanId) {
        //将课程计划id放在数组中，为调用service做准备
        String[] teachplanIds=new String[]{teachplanId};
        //调用service方法获取课程媒资信息
        QueryResponseResult<TeachplanMediaPub> mediaPubQueryResponseResult = esCourseService.getMedia(teachplanIds);
        QueryResult<TeachplanMediaPub> queryResult = mediaPubQueryResponseResult.getQueryResult();
        if (queryResult!=null&&queryResult.getList()!=null&&queryResult.getList().size()>0){
            //返回课程计划对应的课程媒资
            return queryResult.getList().get(0);
        }
        return new TeachplanMediaPub();
    }
}
