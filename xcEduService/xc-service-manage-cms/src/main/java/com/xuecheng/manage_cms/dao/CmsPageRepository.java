package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.dao
 * @ClassName: CmsPageRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/21 20:40
 * @Version: 1.0
 */
/**
* cms dao层接口继承mongoRepository,提供一系列方法
* */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    /**
    * 测试mongodb方法名约定进行查询
    * */
    public CmsPage findByPageId(String pageId);

    /**
    * 根据页面名称，站点id，页面webpath查询页面是否存在
    * */
    public CmsPage findByPageNameAndPageWebPathAndSiteId(String pageName,String pageWebPath,String siteId);
}
