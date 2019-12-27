package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.dao
 * @ClassName: CmsSiteRepositoryService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/25 12:22
 * @Version: 1.0
 */
/**
* 站点接口
* */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
