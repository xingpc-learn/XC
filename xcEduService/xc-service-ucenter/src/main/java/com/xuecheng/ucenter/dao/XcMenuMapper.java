package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.ucenter.dao
 * @ClassName: XcMenuMapper
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/26 20:35
 * @Version: 1.0
 */
@Mapper
public interface XcMenuMapper {

    /**
    * 根据userId查询权限信息
    * */
    List<XcMenu> selectPermissionByUserId(String userid);
}
