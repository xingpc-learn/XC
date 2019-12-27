package com.xuecheng.auth.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.auth.AuthToken;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth.service
 * @ClassName: AuthService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 21:19
 * @Version: 1.0
 */
@Service
public class AuthService {

    private static final Logger LOGGER= LoggerFactory.getLogger(AuthService.class);

    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
    * 认证方法
    * */
    public AuthToken login(String username,String password,String clientId,String clientSecret){
        //申请令牌
        AuthToken authToken=applyToken(username,password ,clientId , clientSecret);
        if (authToken==null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //将token存储到redis
        String jti_token=authToken.getJti_token();
        String content= JSON.toJSONString(authToken);
        boolean saveTokenResult = saveToken(jti_token, content, tokenValiditySeconds);
        if (!saveTokenResult){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }

    /**
    * 存储令牌到redis
    * */
    public boolean saveToken(String jti_token,String content,long ttl){
        //令牌名称
        String name="user_token:"+jti_token;
        //保存到令牌到redis
        stringRedisTemplate.boundValueOps(name).set(content,ttl, TimeUnit.SECONDS);
        //获取过期时间
        Long expire = stringRedisTemplate.getExpire(name);
        return expire>0;
    }

    /**
    * 认证方法，申请令牌
    * */
    private AuthToken applyToken(String username,String password,String clientId,String clientSecret){
        //采用客户端负载均衡，从eureka获取认证服务的ip和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        if (serviceInstance==null){
            LOGGER.error("choose an auth instance fail");
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_AUTHSERVER_NOTFOUND);
        }
        //获取令牌的url
        //http://localhost:40400/auth/oauth/token,申请令牌的url
        String authUrl=serviceInstance.getUri()+"/auth/oauth/token";
        //请求的内容分两部分body
        //1、header信息，包含了http basic认证信息
        MultiValueMap<String,String> headers= new LinkedMultiValueMap<String,String>();
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization",httpBasic(clientId,clientSecret));
        //2、包括grant_type,usrname,password
        MultiValueMap<String,String> body=new LinkedMultiValueMap<String,String>();
        body.add("grant_type","password" );
        body.add("username",username );
        body.add("password",password );
        HttpEntity<MultiValueMap<String,String>> multiValueMapHttpEntity=new HttpEntity<MultiValueMap<String, String>>(body,headers);
        Map map=null;
        try {
            //指定resttemplate当遇到400或者401响应时不抛出异常也正常返回值
            ((RestTemplate)restTemplate).setErrorHandler(new DefaultResponseErrorHandler(){
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    //当响应的值为400或401时候也要正常响应，不要抛出异常
                    if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                        super.handleError(response);
                    }
                }
            });
            //远程调用申请令牌
            //String url, HttpMethod method,@Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables
            ResponseEntity<Map> mapResponseEntity = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
            map = mapResponseEntity.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            LOGGER.error("request oauth_token_password error: {}",e.getMessage());
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //jti是jwt令牌的唯一标识作为用户身份令牌
        if (map==null||map.get("access_token")==null||map.get("refresh_token")==null||map.get("jti")==null){
            //获取springsecurity返回的错误信息
            String error_description=(String) map.get("error_description");
            if (StringUtils.isNotEmpty(error_description)){
                if (error_description.equals("坏的凭证")){
                    ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }else if (error_description.indexOf("UserDetailsService returned null")>=0){
                    ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }
            }
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        AuthToken authToken=new AuthToken();
        //访问令牌jwt
        String jwt_token = (String) map.get("access_token");
        //刷新令牌jwt
        String refresh_token = (String) map.get("refresh_token");
        //jti，作为用户的身份标识
        String jti_token = (String) map.get("jti");
        authToken.setJti_token(jti_token);
        authToken.setJwt_token(jwt_token);
        authToken.setRefresh_token(refresh_token);
        return authToken;

    }

    /**
    * 申请令牌需要的httpbasic连接注册的服务应用,获取httpbasic认证串
    * */
    private String httpBasic(String clientId, String clientSecret) {
        //将客户端id和密码凭借，按“客户端id:客户端密码”
        String string=clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

    /**
    * 从Redis中查询令牌
    * */
    public AuthToken getUserToken(String jti_token){
        String userToken="user_token:"+jti_token;
        String userTokenString=stringRedisTemplate.opsForValue().get(userToken);
        if (userTokenString!=null){
            AuthToken authToken=null;
            try {
                authToken=JSON.parseObject(userTokenString,AuthToken.class);
            } catch (Exception e) {
                LOGGER.error("getUserToken from redis and execute JSON.parseObject error:{}",e.getMessage());
                e.printStackTrace();
            }
            return authToken;
        }
        return null;
    }


    /**
     * 从redis中删除令牌
     * */
    public boolean delToken(String jti_token){
        String name="user_token:"+jti_token;
        stringRedisTemplate.delete(name);
        return true;
    }

}
