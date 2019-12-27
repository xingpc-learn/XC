package com.xuecheng.test.rabbit;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.rabbit
 * @ClassName: ConsumerTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/29 11:08
 * @Version: 1.0
 */

import com.rabbitmq.client.*;

import java.io.IOException;


/**
* 消费者接收队列消息
* */
public class ConsumerTest {

    //队列名称
    private static final String QUEUE = "helloworld";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            //创建connection连接工厂，并设置参数
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            //rabbitmq默认虚拟机名称为"/"，虚拟机相当于一个独立的mq服务器
            factory.setVirtualHost("/");
            //创建于rabbitmq的tcp连接
            connection = factory.newConnection();
            //创建与exchange的通道，每个连接可以创建多个通道，每个通道代表一个会话任务
            channel = connection.createChannel();
            /**
             * 声明队列，如果rabbit中没有此队列将自动创建,参数
             * String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments
             * queue:队列名称
             * durable：是否持久化
             * exclusive：队列是否独占此链接
             * autoDelete：队列不使用时是否自动删除此队列
             * arguments：队列参数
             * */
            channel.queueDeclare(QUEUE, true, false, false, null);

            /**
            * 消费方法声明
            * */
            DefaultConsumer consumer=new DefaultConsumer(channel){
                /**
                * 消费者就收消息调用此方法
                 * consumerTag：消费者标签，在chnnel.basiscConsume()指定
                 * envelope消息包的内容，可以获取消息id，消息routingkey，交换机，消息和重传标识（收到消息失败后是否需要重新发送）
                 * properties
                 * body
                * */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
                {
                   //交换机
                    String exchange = envelope.getExchange();
                    //路由key
                    String routingKey = envelope.getRoutingKey();
                    //消息id
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String msg = new String(body, "utf-8");
                    System.out.println("receive message:"+msg);
                }
            };

            /**
            * 监听队列
             * String queue, boolean autoAck, Consumer callback
             * queue:队列名称
             * autoAck：是否自动回复，设置为true表示消息就收到后自动向mq回复接收到，设置为false则需要手动回复
             * callback：消费消息方法，消费者接收到消息后调用此方法
            * */
            channel.basicConsume(QUEUE,true ,consumer );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

