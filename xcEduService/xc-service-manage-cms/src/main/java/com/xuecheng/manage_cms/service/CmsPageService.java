package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.service
 * @ClassName: CmsPageService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/21 22:20
 * @Version: 1.0
 */
@Service
public class CmsPageService {

    /**
     * 注入dao层
     */
    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    /**
    * service层分页，页码从1开始
    * */
    public QueryResponseResult findPageList(int page, int size, QueryPageRequest queryPageRequest){
        if (queryPageRequest==null){
            queryPageRequest=new QueryPageRequest();
        }
//        自定义条件处查询
        ExampleMatcher exampleMatcher=ExampleMatcher.matching().withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
                CmsPage cmsPage=new CmsPage();
//                站点条件
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
//        模板条件
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
//        别名模糊条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
//        页面类型
        if (StringUtils.isNotEmpty(queryPageRequest.getPageType())){
            cmsPage.setPageType(queryPageRequest.getPageType());
        }
        Example<CmsPage> example=Example.of(cmsPage,exampleMatcher);

//        判断参数
        if (page<=0){
            page=1;
        }
        page=page-1;

        if (size<=0){
            size=10;
        }

//      分页参数
        Pageable pageable = PageRequest.of(page, size);
//        分页查询,添加分页条件
        Page<CmsPage> pages = cmsPageRepository.findAll(example,pageable);
        QueryResult<CmsPage> queryResult=new QueryResult<>();
        queryResult.setList(pages.getContent());
        queryResult.setTotal(pages.getTotalElements());
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS, queryResult);
//        返回分页查询结果
        return queryResponseResult;
    }

    /**
    * 添加页面
    * */
    public CmsPageResult add(CmsPage cmsPage){

        if (cmsPage==null){
//            如果参数为空，非法参数异常
        }

//        判断添加页面数据库中是否存在,三个字段唯一性
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(cmsPage.getPageName(),cmsPage.getPageWebPath(),cmsPage.getSiteId());
        if (cmsPage1!=null){
//            检测页面存在，抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTS);
        }
/*//        已经存在返回信息添加失败
        return new CmsPageResult(CommonCode.FAIL,null);*/
//            id设置为空，让主键由spring data自动生成
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
//            返回结果
        CmsPageResult cmsPageResult=new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        return cmsPageResult;
    }

    /**
    * 根据id查询页面
    * */
    public CmsPage getById(String id){
        Optional<CmsPage> cmspage1 = cmsPageRepository.findById(id);
        if (cmspage1.isPresent()){
            return cmspage1.get();
        }
        return null;
    }

    /**
    * 更新页面数据
    * */
    public CmsPageResult update(String id,CmsPage cmsPage){
//        查询id的页面
        CmsPage page = this.getById(id);
//        判断是否为空，更新数据
        if (page!=null){
            //更新模板
            page.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            page.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            page.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            page.setPageName(cmsPage.getPageName());
            //更新访问路径
            page.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            page.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新数据Url
            page.setDataUrl(cmsPage.getDataUrl());
//            执行更新页面
            CmsPage save = cmsPageRepository.save(page);
            if (save!=null){
                CmsPageResult cmsPageResult=new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }
        }
        //保存失败，返回失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
    * 根据页面id删除页面
    * */
    public ResponseResult delete(String id){
//        先查询页面
        Optional<CmsPage> cmsPage = cmsPageRepository.findById(id);
        if (cmsPage.isPresent()){
            CmsPage cmsPage1 = cmsPage.get();
            cmsPageRepository.delete(cmsPage1);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    /**
    * 页面静态化
    * 1、填写页面DataUrl在编辑cms页面信息界面填写DataUrl，将此字段保存到cms_page中。
     * 2、静态化程序获取页面的DataUrl
     * 3、静态化程序远程请求DataUrl获取数据模型。
     * 4、静态化程序获取页面的模板信息
     * 5、执行页面静态化
    * */
    public String getPageHtml(String pageId){
//        根据pageId获取cmsPage对象，获取数据模型dataUrl和模板id
//        获取模型数据
        Map model = this.getModleByPageId(pageId);
        if (model==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
//        获取页面模板
        String template = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
//        页面静态化
        String html = this.generateHtml(model, template);
        if (StringUtils.isEmpty(html)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /**
    * 根据pageId获取模型数据
    * */
    private Map getModleByPageId(String id){
        //查询页面信息
        CmsPage cmsPage = this.getById(id);
        if (cmsPage==null){
            //页面不存在抛出异常
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_CMSPAGENULL);
        }
        //页面存在获取dataUrl
        String dataUrl=cmsPage.getDataUrl();
        if (dataUrl==null){
            //dataUrl不存在，抛出异常
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //获取数据模型
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        return forEntity.getBody();
    }


    /**
     * 根据pageId获取页面模板
     * */
    private String getTemplateByPageId(String id){
        //获取页面信息
        CmsPage cmsPage = this.getById(id);
        if (cmsPage==null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_CMSPAGENULL);
        }
        //页面存在获取templateFileId
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            //页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            //模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建gridFsResources
            GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(),"utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 页面静态化
     * */
    private String generateHtml(Map model,String template){
        try {
        //生成配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //模板加载器
        StringTemplateLoader stringTemplateLoader =new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",template);
        //配置模板类加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        Template template1 = configuration.getTemplate("template");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * 页面发布
    * */
    public ResponseResult postPage(String pageId){
        /**
        * 页面发布生产方步骤：
         * 1.根据页面id，进行页面静态化
         * 2.将页面存储gridfs，并更新cmsPage的htmlFileId
         * 3.发送消息，pageId，routingkey：站点id
        * */
        //1.根据页面id，进行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        if (StringUtils.isEmpty(pageHtml)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //2.将页面存储gridfs，并更新cmsPage的htmlFileId
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        //3.发送消息，pageIdroutingkey：站点id
        this.sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
    * 给rabbitmq发送消息,消息格式为json类型
    * */
    private void sendPostPage(String pageId){
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_CMSPAGENULL);
        }
        Map<String,String> msgMap=new HashMap<>();
        msgMap.put("pageId",pageId);
        //将消息内容转成json类型
        String msg = JSON.toJSONString(msgMap);
        //获取站点id作为routingkey
        String siteId = cmsPage.getSiteId();
        this.rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId,msg);
    }

    /**
    * 保存静态页面内容
    * */
    private CmsPage saveHtml(String pageId,String pageHtml){
        //查询页面
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_CMSPAGENULL);
        }
        CmsPage cmsPage = optional.get();
        //存储之前先删除
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotEmpty(htmlFileId)){
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        //保存html文件至gridfs
        InputStream inputStream = IOUtils.toInputStream(pageHtml);
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        //文件id
        String fileId = objectId.toString();
        //将文件id存储至cmsPage中
        cmsPage.setHtmlFileId(fileId);
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    /**
    * 保存页面，如果页面存在则更新，不存在则添加
    * */
    public CmsPageResult save(CmsPage cmsPage) {
        //判断页面是否存在
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(cmsPage.getPageName(), cmsPage.getPageWebPath(), cmsPage.getSiteId());
        if (cmsPage1==null){
            return this.add(cmsPage);
        }
        return this.update(cmsPage1.getPageId(),cmsPage);
    }

    /**
    * 页面一键发布
    * */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        //查询页面是否存在保存或添加
        CmsPageResult cmsPageResult = this.save(cmsPage);
        if (!cmsPageResult.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        //获取页面的id
        CmsPage pageSave = cmsPageResult.getCmsPage();
        String pageId = pageSave.getPageId();
        //发布页面
        ResponseResult responseResult = this.postPage(pageId);
        if (!responseResult.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        //拼接发布的页面url=cmsSite.siteDomain+cmsSite.siteWebPath+ cmsPage.pageWebPath + cmsPage.pageName
        //获取页面站点id
        String siteId = pageSave.getSiteId();
        CmsSite cmsSite = this.getCmsSiteById(siteId);
        //页面url
        String pagePath=cmsSite.getSiteDomain()+cmsSite.getSiteWebPath()+pageSave.getPageWebPath()+pageSave.getPageName();
        return new CmsPostPageResult(CommonCode.SUCCESS,pagePath);
    }

    public CmsSite getCmsSiteById(String siteId){
        Optional<CmsSite> siteOptional = cmsSiteRepository.findById(siteId);
        if (siteOptional.isPresent()){
            return siteOptional.get();
        }
        return null;
    }
}
