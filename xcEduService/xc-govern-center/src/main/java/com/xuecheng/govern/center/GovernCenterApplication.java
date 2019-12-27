package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.govern.center
 * @ClassName: GovernCenterApplication
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/2 23:44
 * @Version: 1.0
 */
/**
* Eureka单机服务环境搭建
 * Eureka单机服务启动类
* */
@SpringBootApplication
//标识这是一个Eureka服务
@EnableEurekaServer
public class GovernCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GovernCenterApplication.class,args);
    }
}
