package com.xuecheng.learning.client;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.learning.client
 * @ClassName: CourseSearchClient
 * @Author: Administrator
 * @Description: 学习服务调用搜索服务的客户端接口
 * @Date: 2019/8/20 17:10
 * @Version: 1.0
 */
@FeignClient(value = "xc-service-search")
public interface CourseSearchClient {

    @GetMapping(value = "/search/course/getmedia/{teachplanId}")
    public TeachplanMediaPub getMedia(@PathVariable("teachplanId") String teachplanId);
}
