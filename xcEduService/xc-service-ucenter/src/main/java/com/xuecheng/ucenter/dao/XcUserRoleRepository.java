package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcUserRoleRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/26 21:11
 * @Version: 1.0
 */
public interface XcUserRoleRepository extends JpaRepository<XcUserRole,String> {
}
