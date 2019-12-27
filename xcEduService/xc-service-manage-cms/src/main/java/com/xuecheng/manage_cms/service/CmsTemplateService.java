package com.xuecheng.manage_cms.service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.service
 * @ClassName: CmsTemplateRepositoryService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/25 13:11
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
* 模板服务
* */
@Service
public class CmsTemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    public QueryResponseResult getTemplateList(){
//        调用方法查询站点列表
        List<CmsTemplate> templateList = cmsTemplateRepository.findAll();
//        将查询结果封装为QueryResponseResult
        QueryResult<CmsTemplate> queryResult=new QueryResult<>();
        queryResult.setList(templateList);
        queryResult.setTotal(templateList.size());
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

}
