package com.xuecheng.ucenter.service;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.service
 * @ClassName: UserService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/23 16:25
 * @Version: 1.0
 */
@Service
public class UserService {

    @Autowired
    XcUserRepository xcUserRepository;

    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;

    @Autowired
    XcMenuMapper xcMenuMapper;

    /**
    * 根据用户账号查询用户信息
    * */
    public XcUser findXcUserByUsername(String username){
        return xcUserRepository.findXcUserByUsername(username);
    }

    /**
    * 根据账号查询用户的信息，返回用户扩展信息
    * */
    public XcUserExt getUserExt(String username){
        XcUser xcUser = this.findXcUserByUsername(username);
        if (xcUser==null){
            return null;
        }
        //用户id
        String userId = xcUser.getId();
        //根据用户id查询用户权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        XcUserExt xcUserExt=new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        //查询用户所属公司
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
        if (xcCompanyUser!=null){
            String companyId = xcCompanyUser.getCompanyId();
            xcUserExt.setCompanyId(companyId);
        }
        //用户权限
        xcUserExt.setPermissions(xcMenus);
        return xcUserExt;
    }

}
