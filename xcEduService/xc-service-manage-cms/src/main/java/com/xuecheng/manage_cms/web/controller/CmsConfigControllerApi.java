package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.web.controller
 * @ClassName: CmsConfigControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 14:06
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigControllerApi implements com.xuecheng.api.cms.CmsConfigControllerApi {

    @Autowired
    CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id) {
        return cmsConfigService.getConfigById(id);
    }
}
