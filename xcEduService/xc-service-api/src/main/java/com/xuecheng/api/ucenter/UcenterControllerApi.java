package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.ucenter
 * @ClassName: UcenterControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/23 14:16
 * @Version: 1.0
 */
@Api(value = "用户中心",description = "用户中心管理")
public interface UcenterControllerApi {

    /**
    * 校验用户是否存在
    * */
    public XcUserExt getUserext(String username);

}
