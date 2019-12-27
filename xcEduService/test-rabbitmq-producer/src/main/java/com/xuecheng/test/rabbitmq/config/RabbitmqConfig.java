package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.rabbitmq.config
 * @ClassName: RabbitmqConfig
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/29 20:38
 * @Version: 1.0
 */
@Configuration
public class RabbitmqConfig {
    
    //队列名称
    public static final String QUEUE_INFORM_EMAIL="queue_inform_email";
    public static final String QUEUE_INFORM_SMS="queue_inform_sms";
    //声明交换机
    public static final String EXCHANGE_TOPIC_INFORM_SPRING="exchange_topic_inform_springboot";
    //声明routingkey,#可以匹配一个或多个，*配置一个，也可以为空
    public static final String ROUTING_KEY_EMAIL="inform.#.email.#";
    public static final String ROUTING_KEY_SMS="inform.#.sms.#";

    //配置队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }

    //配置交换机
    @Bean(EXCHANGE_TOPIC_INFORM_SPRING)
    public Exchange EXCHANGE_TOPIC_INFORM(){
        //durable,持久化。消息队列重启后仍然存在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC_INFORM_SPRING).durable(true).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue,@Qualifier(EXCHANGE_TOPIC_INFORM_SPRING) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_EMAIL).noargs();
    }
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,@Qualifier(EXCHANGE_TOPIC_INFORM_SPRING) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_SMS).noargs();
    }

}
