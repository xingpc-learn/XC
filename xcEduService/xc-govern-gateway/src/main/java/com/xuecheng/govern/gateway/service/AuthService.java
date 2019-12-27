package com.xuecheng.govern.gateway.service;

import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.govern.gateway.service
 * @ClassName: AuthService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 15:32
 * @Version: 1.0
 */
@Service
public class AuthService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
    * 查询身份令牌
    * */
    public String getTokenFromCookie(HttpServletRequest request){
        Map<String, String> cookieMap = CookieUtil.readCookie(request,"uid");
        String jti_token = cookieMap.get("uid");
        if (StringUtils.isEmpty(jti_token)){
            return null;
        }
        return jti_token;
    }

    /**
    * 从header中查询jwt令牌
    * */
    public String getJwtFromHeader(HttpServletRequest request){
        String authorization=request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            //拒绝访问
            return null;
        }
        if (!authorization.startsWith("Bearer ")){
            //拒绝访问
            return null;
        }
        return authorization;
    }

    /**
    * 查询令牌的有效期
    * */
    public long getExpire(String jti_token){
        //token在redis中的key
        String key="user_token:"+jti_token;
        return stringRedisTemplate.getExpire(key);
    }

}
