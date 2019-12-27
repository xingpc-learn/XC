package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.dao
 * @ClassName: CmsConfigRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 13:54
 * @Version: 1.0
 */
/**
* 创建dao接口继承MongoDB，提供一系列方法
* */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
