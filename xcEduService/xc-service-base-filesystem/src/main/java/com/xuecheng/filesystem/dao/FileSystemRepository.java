package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.filesystem.dao
 * @ClassName: FileSystemRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/2 17:10
 * @Version: 1.0
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
