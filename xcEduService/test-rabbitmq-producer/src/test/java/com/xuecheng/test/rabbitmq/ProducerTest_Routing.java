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

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
* rabbitMQ消息队列，生产者测试，发送消息
 * 工作模式：路由模式routing
* */
public class ProducerTest_Routing {

    //队列名称
    private static final String QUEUE_INFORM_EMAIL="queue_inform_email";
    private static final String QUEUE_INFORM_SMS="queue_inform_sms";
    //声明交换机
    private static final String EXCHANGE_ROUTING_INFORM="exchange_routing_inform";
    //声明routingkey
    private static final String ROUTING_KEY_EMAIL="routing_key_email";
    private static final String ROUTING_KEY_SMS="routing_key_sms";
    private static final String ROUTING_KEY_COMMON="routing_key_common";

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
        //声明交换机 String exchange, BuiltinExchangeType type
        /**
        * exchange:交换机名称
         * type：交换机类型，fanout(pulish/subscrible模式),topic(topic模式),direct(routing),headers（headers模式）
        * */
        channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT );

        /**
        * 声明队列，如果rabbit中没有此队列将自动创建,参数
         * String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments
         * queue:队列名称
         * durable：是否持久化
         * exclusive：队列是否独占此链接
         * autoDelete：队列不使用时是否自动删除此队列
         * arguments：队列参数
        * */
        channel.queueDeclare(QUEUE_INFORM_EMAIL,true , false,false ,null );
        channel.queueDeclare(QUEUE_INFORM_SMS,true , false,false ,null );
        //交换机和队列绑定String queue, String exchange, String routingKey
        /**
        * queue；队列名称
         * exchange：交换机名称
         * routingKey:路由key
        * */
        channel.queueBind(QUEUE_INFORM_EMAIL, EXCHANGE_ROUTING_INFORM,ROUTING_KEY_EMAIL);
        channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM ,ROUTING_KEY_SMS);
        /**
        * 消息发布
         * String exchange, String routingKey, BasicProperties props, byte[] body
         * exchange:交换机名称，没有指定使用默认
         * routingKey：消息的路由key，用于交换机将消息发到指定的消息队列
         * props：消息包含的属性
         * body：消息体
         * 这里没有指定交换机，消息将发送给默认交换机，每个队列也会去绑定那个默认的交换机，但是不能显示绑定或解除，默认交换机，routingkey等于队列名称
        * */
        for (int i = 0; i < 10; i++) {
            String message="infor to user,"+ i;
            if (i<5){
                channel.basicPublish(EXCHANGE_ROUTING_INFORM, ROUTING_KEY_EMAIL, null, message.getBytes());
            }else {
            channel.basicPublish(EXCHANGE_ROUTING_INFORM,ROUTING_KEY_SMS ,null ,message.getBytes());
            }
            System.out.println("send message is:'"+message+"'");
        }
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
