package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms_client.config
 * @ClassName: RabbitmqConfig
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/30 9:24
 * @Version: 1.0
 */
@Configuration
public class RabbitmqConfig {

    //声明交换机名称
    public static final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_postpage";

    //交换机配置
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

}
