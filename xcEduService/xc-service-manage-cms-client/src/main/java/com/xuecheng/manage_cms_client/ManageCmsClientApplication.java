package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms_client
 * @ClassName: ManageCmsClientApplication
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/30 9:25
 * @Version: 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
//扫描cms微服务实体
@EntityScan("com.xuecheng.framework.domain.cms")
//扫描common工程下的类
@ComponentScan(basePackages = {"com.xuecheng.framework"})
//扫描cms项目下所有类
@ComponentScan(basePackages = {"com.xuecheng.manage_cms_client"})
public class ManageCmsClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCmsClientApplication.class,args);
    }
}
