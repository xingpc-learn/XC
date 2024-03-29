package com.xuecheng.api.system;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.system
 * @ClassName: SysDictionaryControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 23:21
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
* 数据字典接口
* */
@Api(value="数据字典接口",description="提供数据字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {

    //数据字典
    @ApiOperation("数据字典查询接口")
    public SysDictionary getByType(String type);

}
