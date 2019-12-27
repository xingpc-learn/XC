package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.dao
 * @ClassName: CmsTemplateRepositoryService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/25 12:22
 * @Version: 1.0
 */
/**
* 模板接口
* */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
