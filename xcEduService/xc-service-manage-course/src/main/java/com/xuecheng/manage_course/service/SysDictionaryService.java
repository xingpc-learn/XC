package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.SysDictionaryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.service
 * @ClassName: SysDictionaryRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 23:29
 * @Version: 1.0
 */
@Service
public class SysDictionaryService {

    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    /**
    * 课程添加页面，字典查询列表，根据类型查询
    * */
    public SysDictionary getDictionaryList(String type){
        if (StringUtils.isEmpty(type)){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //调用方法查询数据
        return sysDictionaryRepository.findByDType(type);
    }

}
