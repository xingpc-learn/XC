package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.learning
 * @ClassName: CourseLearningControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/20 16:43
 * @Version: 1.0
 */
@Api(value = "录播课程学习管理",description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    /**
    * 根据课程id和学习计划id查询播放路径地址，课程id将来用于权限控制
    * */
    @ApiOperation("获取课程学习地址")
    public GetMediaResult getMedia(String courseId,String teachplanId);
}
