package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import com.xuecheng.manage_cms.service.CmsSiteService;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.web.controller
 * @ClassName: CmsPageControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/21 19:54
 * @Version: 1.0
 */
@RestController
@RequestMapping("/cms/page")
public class CmsPageControllerApi implements com.xuecheng.api.cms.CmsPageControllerApi {

//    注入service层
    @Autowired
    CmsPageService cmsPageService;
    @Autowired
    CmsSiteService cmsSiteService;
    @Autowired
    CmsTemplateService cmsTemplateService;

    /**
    * 查询cms页面列表
    * */
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findPageList(@PathVariable("page") int page,@PathVariable("size") int size, QueryPageRequest queryPageRequest) {

       /* QueryResult<CmsPage> queryResult=new QueryResult<>();
        List<CmsPage> list=new ArrayList<>();
        CmsPage cmsPage=new CmsPage();
        cmsPage.setPageName("test page");
        cmsPage.setPageAliase("测试");
        list.add(cmsPage);
        queryResult.setList(list);
        queryResult.setTotal(1);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);*/
        return cmsPageService.findPageList(page,size ,queryPageRequest);
    }

    /**
     * 查询站点列表
     * */
    @Override
    @GetMapping("/site")
    public QueryResponseResult findSiteList(){
        return cmsSiteService.getSiteList();
    }

    /**
     * 查询模板列表
     * */
    @Override
    @GetMapping("/template")
    public QueryResponseResult findTemplateList(){
        return cmsTemplateService.getTemplateList();
    }

    /**
    * 添加页面
    * */
    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return cmsPageService.add(cmsPage);
    }

    /**
    * 根据页面id查找页面
    * */
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.getById(id);
    }

    /**
    * 编辑页面
    * */
    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.update(id,cmsPage);
    }

    /**
    * 删除页面
    * */
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable String id) {
        return cmsPageService.delete(id);
    }

    /**
    * 发布页面
    * */
    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return cmsPageService.postPage(pageId);
    }

    /**
    * 保存页面
    * */
    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }

    /**
    * 页面一键发布
    * */
    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return cmsPageService.postPageQuick(cmsPage);
    }
}
