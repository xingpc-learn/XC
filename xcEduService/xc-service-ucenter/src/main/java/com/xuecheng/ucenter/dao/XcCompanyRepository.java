package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompany;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcCompanyRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/26 21:08
 * @Version: 1.0
 */
public interface XcCompanyRepository extends JpaRepository<XcCompany,String> {
}
