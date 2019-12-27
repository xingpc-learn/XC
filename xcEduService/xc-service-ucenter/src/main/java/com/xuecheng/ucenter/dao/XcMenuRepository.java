package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcMenuRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/26 21:09
 * @Version: 1.0
 */
public interface XcMenuRepository extends JpaRepository<XcMenu,String> {
}
