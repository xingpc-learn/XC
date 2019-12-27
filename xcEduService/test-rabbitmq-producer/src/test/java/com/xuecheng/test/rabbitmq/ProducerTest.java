package com.xuecheng.test.rabbitmq;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.rabbitmq
 * @ClassName: ProducerTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/29 10:21
 * @Version: 1.0
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
* rabbitMQ消息队列，生产者测试，发送消息
 * 工作模式：work_queue
* */
public class ProducerTest {

    //队列名称
    private static final String QUEUE="helloworld";

    public static void main(String[] args){
        Connection connection=null;
        Channel channel=null;
        try {
        //创建connection连接工厂，并设置参数
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        //rabbitmq默认虚拟机名称为"/"，虚拟机相当于一个独立的mq服务器
        factory.setVirtualHost("/");
        //创建于rabbitmq的tcp连接
        connection= factory.newConnection();
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
        channel.queueDeclare(QUEUE,true , false,false ,null );
        /**
        * 消息发布
         * String exchange, String routingKey, BasicProperties props, byte[] body
         * exchange:交换机名称，没有指定使用默认
         * routingKey：消息的路由key，用于交换机将消息发到指定的消息队列
         * props：消息包含的属性
         * body：消息体
         * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会去绑定那个默认的交换机，但是不能显示绑定或解除，默认交换机，routingkey等于队列名称
        * */
        String message="helloworld xingpc,"+ System.currentTimeMillis();
        channel.basicPublish("", QUEUE, null, message.getBytes());
        System.out.println("send message is:'"+message+"'");
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (channel!=null){
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }




}
