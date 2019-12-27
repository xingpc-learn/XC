package com.xuecheng.auth;

import com.sun.jersey.core.util.Base64;
import com.xuecheng.framework.client.XcServiceList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth
 * @ClassName: TestClient
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 20:36
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestClient {

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    RestTemplate restTemplate;

    /**
    * 客户端申请令牌测试
    * */
    @Test
    public void testClient(){
        //采用客户端负载均衡，从eureka获取认证服务的ip和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        URI uri=serviceInstance.getUri();
        //http://localhost:40400/auth/oauth/token,申请令牌的url
        String authUrl=uri+"/auth/oauth/token";
        //请求的内容分两部分
        //1、header信息，包含了http basic认证信息
        MultiValueMap<String,String> headers= new LinkedMultiValueMap<String,String>();
        String httpbasic=httpBasic("XcWebApp","XcWebApp");
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        headers.add("Authorization",httpbasic);
        //2、包括grant_type,usrname,password
        MultiValueMap<String,String> body=new LinkedMultiValueMap<String,String>();
        body.add("grant_type","password" );
        body.add("username","itcast" );
        body.add("password","123" );
        HttpEntity<MultiValueMap<String,String>> multiValueMapHttpEntity=new HttpEntity<MultiValueMap<String, String>>(body,headers);
        //指定resttemplate当遇到400或者401响应时不抛出异常也正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
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
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }

    private String httpBasic(String clientId, String clientSecret) {
        //将客户端id和密码凭借，按“客户端id:客户端密码”
        String string=clientId+":"+clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic "+new String(encode);
    }
}
