package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.auth
 * @ClassName: AuthContollerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 20:25
 * @Version: 1.0
 */
@Api(value = "用户认证",description = "用户认证接口")
public interface AuthContollerApi {

    /**
    * 登录认证
    * */
    @ApiOperation("登录认证")
    public LoginResult login(LoginRequest loginRequest);

    /**
    * 退出认证
    * */
    @ApiOperation("退出认证")
    public ResponseResult logout();

    /**
    * 根据cookie中的jti查询redis中的token
    * */
    @ApiOperation("查询userjwt令牌")
    public JwtResult userJwt();

}
