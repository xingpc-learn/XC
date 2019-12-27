package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms_client.dao
 * @ClassName: CmsPageRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/30 13:44
 * @Version: 1.0
 */
/**
* 获取页面信息，根据页面id获取htmlFileId，从gridfs下载html静态页面，页面物理地址
* */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}
