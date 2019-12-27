package com.xuecheng.api.category;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.course
 * @ClassName: CategoryControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 21:39
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 课程分类接口
* */
@Api(value="课程分类管理接口",description="课程分类管理",tags = {"课程分类管理"})
public interface CategoryControllerApi {

    @ApiOperation("课程分类查询")
    public CategoryNode findList();
}
