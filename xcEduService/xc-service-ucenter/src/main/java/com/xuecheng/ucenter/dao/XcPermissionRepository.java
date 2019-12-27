package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcPermissionRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/26 21:10
 * @Version: 1.0
 */
public interface XcPermissionRepository extends JpaRepository<XcPermission,String> {
}
