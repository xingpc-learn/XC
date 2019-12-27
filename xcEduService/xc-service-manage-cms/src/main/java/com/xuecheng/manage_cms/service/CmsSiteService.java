package com.xuecheng.manage_cms.service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.service
 * @ClassName: CmsSiteRepositoryService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/25 13:11
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 站点服务
* */
@Service
public class CmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    public QueryResponseResult getSiteList(){
//        调用方法查询站点列表
        List<CmsSite> siteList = cmsSiteRepository.findAll();
//        将查询结果封装为QueryResponseResult
        QueryResult<CmsSite> queryResult=new QueryResult<>();
        queryResult.setList(siteList);
        queryResult.setTotal(siteList.size());
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
