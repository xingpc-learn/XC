package com.xuecheng.manage_cms_client.service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms_client.service
 * @ClassName: CmsPageService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/30 14:04
 * @Version: 1.0
 */

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
* 定义消费端方法，在service中定义方法
 * 1.组装服务地址=站点地址+页面物理地址+页面名称
 * 2.从gridfs下载静态文件
 * 3.将读入的文件流保存至服务站点物理路径
* */
@Service
public class CmsPageService {

    //日志记录
    private static final Logger LOGGER= LoggerFactory.getLogger(CmsPageService.class);

    //注入页面dao
    @Autowired
    CmsPageRepository cmsPageRepository;
    //注入站点dao
    @Autowired
    CmsSiteRepository cmsSiteRepository;
    //注入gridfs模板
    @Autowired
    GridFsTemplate gridFsTemplate;
    //注入下载源对象
    @Autowired
    GridFSBucket gridFSBucket;

    //将页面html保存到页面物理地址
    public void savePageToPath(String pageId){

        //获取页面物理地址
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_CMSPAGENULL);
            LOGGER.error("findById cmspage is null,pageId:{}",pageId);
        }
        CmsPage cmsPage = optional.get();
        //获取页面所属站点的站点物理地址
        CmsSite cmsSite = this.getCmsSiteById(cmsPage.getSiteId());
        //拼接服务物理地址=站点地址+页面物理地址+页面名称
        String pagePath=cmsSite.getSitePhysicalPath()+cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        //查询页面静态文件
        String htmlFileId = cmsPage.getHtmlFileId();
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
            LOGGER.error("getFileById inpustream is null,htmlFileId:{}",htmlFileId);
        }
        //将读入的文件流输出到服务物理地址
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(new File(pagePath));
            //将文件内容保存至服务物理路径
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
    * 根据htmlFildId获取文件内容
    * */
    public InputStream getFileById(String htmlFileId){
        try {
            //获取文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
            //打开下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //得到文件源
            GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);
            //返回输入流
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    * 根据页面id获取站点
    * */
    public CmsSite getCmsSiteById(String siteId){
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    /**
    * 根据pageid获取page页面
    * */
    public CmsPage getCmsPageById(String pageId){
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_CMSPAGENULL);
        }
        return optional.get();
    }

}
