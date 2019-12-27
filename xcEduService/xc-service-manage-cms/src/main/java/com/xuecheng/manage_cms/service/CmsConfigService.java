package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.service
 * @ClassName: CmsConfigService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 13:57
 * @Version: 1.0
 */
@Service
public class CmsConfigService {

    @Autowired
    CmsConfigRepository cmsConfigRepository;

    //根据id查询配置管理信息
    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> config = cmsConfigRepository.findById(id);
        if (config.isPresent()){
            return config.get();
        }
        return null;
    }
}
