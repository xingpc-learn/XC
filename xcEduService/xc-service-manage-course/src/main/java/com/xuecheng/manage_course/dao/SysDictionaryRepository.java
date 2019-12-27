package com.xuecheng.manage_course.dao;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.dao
 * @ClassName: SysDictionaryRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/31 23:27
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
* 查询mongoDB数据库，创建符合jpa规范的接口
* */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {

    public SysDictionary findByDType(String type);
}
