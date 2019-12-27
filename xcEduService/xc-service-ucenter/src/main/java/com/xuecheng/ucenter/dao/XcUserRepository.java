package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcUserRepository
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/23 16:11
 * @Version: 1.0
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {

    /**
    * 通过用户名查找用户是否存在
    * */
    XcUser findXcUserByUsername(String username);
}
