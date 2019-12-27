package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthContollerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.auth.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth.controller
 * @ClassName: AuthController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 22:24
 * @Version: 1.0
 */
@RestController
public class AuthController implements AuthContollerApi {

    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;
    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;
    @Autowired
    AuthService authService;

    @Override
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest) {
        //检查账号是否输入
        if (loginRequest==null|| StringUtils.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        //检查密码是否输入
        if (StringUtils.isEmpty(loginRequest.getPassword())) {
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        AuthToken authToken = authService.login(loginRequest.getUsername(), loginRequest.getPassword(), clientId, clientSecret);
        //将身份令牌写入cookie
        String jti_token=authToken.getJti_token();
        //将身份令牌存储到cookie
        saveCookie(jti_token);
        return new LoginResult(CommonCode.SUCCESS, jti_token);

    }

    /**
    * 将身份令牌存入cookie
    * */
    private void saveCookie(String token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //添加cookie认证令牌，最后一个参数设置为false，标识允许浏览器获取
        CookieUtil.addCookie(response,cookieDomain ,"/" ,"uid" ,token ,cookieMaxAge ,false );
    }

    @Override
    @PostMapping("/userlogout")
    public ResponseResult logout() {
        //取出身份令牌
        String jti_token=getTokenFromCookie();
        //删除redis中的token
        boolean flag = authService.delToken(jti_token);
        if(!flag){
            ExceptionCast.cast(AuthCode.AUTH_LOGOUT_FAIL);
        }
        //清除cookie
        clearCookie(jti_token);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
    * 清除cookie
    * */
    private void clearCookie(String jti_token){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain,"/" , "uid", jti_token,0 , false);
    }

    /**
    * 根据cookie中的jti查询redis中的token
    * */
    @Override
    @GetMapping("/userjwt")
    public JwtResult userJwt() {
        //取出cookie中的令牌
        String jti_token=getTokenFromCookie();
        //根据身份令牌从redis中查询jwt
        AuthToken authToken = authService.getUserToken(jti_token);
        if (authToken==null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        return new JwtResult(CommonCode.SUCCESS,authToken.getJwt_token());
    }

    /**
    * 从cookie中读取令牌
    * */
    private String getTokenFromCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> cookieMap = CookieUtil.readCookie(request, "uid");
        String jti_token = cookieMap.get("uid");
        return jti_token;
    }

}
