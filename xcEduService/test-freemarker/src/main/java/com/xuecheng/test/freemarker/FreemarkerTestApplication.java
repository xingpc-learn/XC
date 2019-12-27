package com.xuecheng.test.freemarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.freemarker
 * @ClassName: FreemarkerTestApplication
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/27 21:32
 * @Version: 1.0
 */
@SpringBootApplication
public class FreemarkerTestApplication {
    public static void main(String[] args){
        SpringApplication.run(FreemarkerTestApplication.class,args);
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
