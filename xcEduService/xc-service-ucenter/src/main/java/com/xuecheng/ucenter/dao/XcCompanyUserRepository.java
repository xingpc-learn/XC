package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcCompanyUserRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/23 16:12
 * @Version: 1.0
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {
    /**根据用户id查询所属企业id*/
    XcCompanyUser findByUserId(String userId);
}
