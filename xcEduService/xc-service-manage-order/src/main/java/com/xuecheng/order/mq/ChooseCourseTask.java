package com.xuecheng.order.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.config.RabbitMQConfig;
import com.xuecheng.order.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.order.mq
 * @ClassName: ChooseCourseTask
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 14:17
 * @Version: 1.0
 */
@Component
public class ChooseCourseTask {

    private static final Logger LOGGER= LoggerFactory.getLogger(ChooseCourseTask.class);

    @Autowired
    TaskService taskService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
    * 接收选课响应结果
    * */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE})
    public void receiveFinishChoosecourseTask(XcTask xcTask,Message message,Channel channel){
        LOGGER.info("receiveChoosecourseTask..{}", xcTask.getId());
        //接收到的消息
        String id = xcTask.getId();
        //删除任务，添加历史任务
        taskService.finishTask(id);
    }

    /**
    * 每隔一分钟扫描消息表，向mq发送消息
    * */
    @Scheduled(fixedDelay =60000 )
    public void sendChoosecourseTask(){
        //取出当前时间1分钟之前的时间
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(GregorianCalendar.MINUTE,-1);
        Date time=calendar.getTime();
        List<XcTask> taskList=taskService.findTaskList(time,1000);

        //遍历任务列表
        for (XcTask xcTask : taskList) {
            //任务id
            String id = xcTask.getId();
            //版本号
            Integer version = xcTask.getVersion();
            //调用乐观锁方法校验任务是否可以执行
            if (taskService.getTask(id,version)>0){
                // 发送选课消息
                taskService.publish(xcTask,xcTask.getMqExchange() , xcTask.getMqRoutingkey());
                LOGGER.info("send choose course task id:{}",xcTask.getId());
            }
        }
    }

//    @Scheduled(cron = "0/3 * * * * *")//每隔三秒执行
//    @Scheduled(fixedRate = 5000)//上次开始时间后5秒执行
//    @Scheduled(fixedDelay = 5000)//上次执行完毕后5秒执行
//    @Scheduled(initialDelay = 3000,fixedRate = 5000)//第一次3秒后边每隔5秒
    public void task1(){
        LOGGER.info("==================测试定时任务1开始=================");
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        LOGGER.info("==================测试定时任务1结束=================");
    }

//    @Scheduled(fixedRate = 5000)//上次开始时间后5秒执行
    public void task2(){
        LOGGER.info("==================测试定时任务2开始=================");
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        LOGGER.info("==================测试定时任务2结束=================");
    }

}
