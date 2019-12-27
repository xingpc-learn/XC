package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.cms
 * @ClassName: CmsConfigControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 13:43
 * @Version: 1.0
 */
@Api(value="cms配置管理接口",description = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {

    @ApiOperation("根据id查询CMS配置信息")
    public CmsConfig getModel(String id);
}
