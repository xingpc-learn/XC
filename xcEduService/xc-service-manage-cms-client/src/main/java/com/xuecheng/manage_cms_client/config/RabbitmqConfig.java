package com.xuecheng.manage_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    //声明首页队列名称
    public static final String HOME_QUEUE_CMS_POSTPAGE="home_queue_cms_postpage";
    //声明课程发布队列名称
    public static final String COURSE_QUEUE_CMS_POSTPAGE="course_queue_cms_postpage";
    //声明交换机名称
    public static final String EX_ROUTING_CMS_POSTPAGE="ex_routing_cms_postpage";
    //队列的名称，配置队列和routingkey定义注入
    @Value("${xuecheng.mq.home_queue}")
    public String home_queue_cms_postpage_name;
    @Value("${xuecheng.mq.home_routingKey}")
    public String homeRoutingKey;
    @Value("${xuecheng.mq.course_queue}")
    public String course_queue_cms_postpage_name;
    @Value("${xuecheng.mq.course_routingKey}")
    public String courseRoutingKey;

    //队列配置
    @Bean(HOME_QUEUE_CMS_POSTPAGE)
    public Queue HOME_QUEUE_CMS_POSTPAGE(){
        return new Queue(home_queue_cms_postpage_name);
    }

    @Bean(COURSE_QUEUE_CMS_POSTPAGE)
    public Queue COURSE_QUEUE_CMS_POSTPAGE(){
        return new Queue(course_queue_cms_postpage_name);
    }

    //交换机配置
    @Bean(EX_ROUTING_CMS_POSTPAGE)
    public Exchange EX_ROUTING_CMS_POSTPAGE(){
        return ExchangeBuilder.directExchange(EX_ROUTING_CMS_POSTPAGE).durable(true).build();
    }

    //队列和交换机绑定
    @Bean
    public Binding BINDING_HOME_QUEUE_CMS_POSTPAGE(@Qualifier(HOME_QUEUE_CMS_POSTPAGE) Queue queue,@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(homeRoutingKey).noargs();
    }
    @Bean
    public Binding BINDING_COURSE_QUEUE_CMS_POSTPAGE(@Qualifier(COURSE_QUEUE_CMS_POSTPAGE) Queue queue,@Qualifier(EX_ROUTING_CMS_POSTPAGE) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(courseRoutingKey).noargs();
    }

}
