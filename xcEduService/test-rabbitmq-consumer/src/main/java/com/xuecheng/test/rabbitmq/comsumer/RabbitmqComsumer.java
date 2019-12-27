package com.xuecheng.test.rabbitmq.comsumer;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.rabbitmq.comsumer
 * @ClassName: RabbitmqComsumer
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/29 21:35
 * @Version: 1.0
 */
@Component
public class RabbitmqComsumer {

    //监听队列消息
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receive_sms(String msg, Message message, Channel channel){
        System.out.println("receive message is:"+msg);
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(String msg, Message message, Channel channel){
        System.out.println("receive message is:"+msg);
    }
}
