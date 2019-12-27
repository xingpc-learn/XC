package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms_client.dao
 * @ClassName: CmsSiteRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/30 13:44
 * @Version: 1.0
 */
/**
* 根据站点id，获取站点物理地址，组成下载的服务地址
* */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
