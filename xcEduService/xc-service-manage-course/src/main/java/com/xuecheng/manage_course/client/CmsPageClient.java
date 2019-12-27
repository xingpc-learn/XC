package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.client
 * @ClassName: CmsPageClient
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/3 11:51
 * @Version: 1.0
 */
@FeignClient(value = "XC-SERVICE-MANAGE-CMS")
public interface CmsPageClient {

    @GetMapping("/cms/page/get/{id}")
    public CmsPage findById(@PathVariable("id") String pageId);

    /**
    * 远程调用的接口
    * */
    @PostMapping("/cms/page/save")
    public CmsPageResult save(CmsPage cmsPage);

    /**
    * 远程调用接口：课程一键发布
    * */
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult publish(@RequestBody CmsPage cmsPage);


}
