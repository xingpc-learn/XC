package com.xuecheng.auth.client;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth.client
 * @ClassName: UserClient
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/23 17:06
 * @Version: 1.0
 */
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {

    @GetMapping("/ucenter/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username);

}
