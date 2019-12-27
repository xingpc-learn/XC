package com.xuecheng.manage_cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms
 * @ClassName: ManageCmsApplication
 * @Author: Administrator
 * @Description: cms微服务引导类
 * @Date: 2019/7/21 19:25
 * @Version: 1.0
 */
//表明是一个eureka客户端
@EnableDiscoveryClient
//表明此类为引导类
@SpringBootApplication
//扫描cms微服务实体
@EntityScan("com.xuecheng.framework.domain.cms")
//扫描cms微服务接口
@ComponentScan(basePackages = {"com.xuecheng.api"})
//扫描cms项目下所有类
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})
//扫描common工程下的类
@ComponentScan(basePackages = {"com.xuecheng.framework"})
public class ManageCmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCmsApplication.class,args);
    }

    //配置远程请求访问http，resttemplate
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
