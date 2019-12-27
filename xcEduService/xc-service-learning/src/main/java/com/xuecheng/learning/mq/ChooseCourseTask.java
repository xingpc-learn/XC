package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.XcLearningCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
    XcLearningCourseService xcLearningCourseService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
    * 接收选课任务
    * */
    @RabbitListener(queues = {RabbitMQConfig.XC_LEARNING_ADDCHOOSECOURSE})
    public void receiveChoosecourseTask(XcTask xcTask, Message message, Channel channel) throws ParseException {
        LOGGER.info("receive choose course task,taskId:()", xcTask.getId());
        //接收到的消息id
        String id = xcTask.getId();
        try {

            //添加选课
            String requestBody = xcTask.getRequestBody();
            Map map = JSON.parseObject(requestBody, Map.class);
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (map.get("startTime") != null) {
                startTime = sdf.parse((String) map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                endTime = sdf.parse((String) map.get("endTime"));
            }
            //选课添加
            ResponseResult addCourse = xcLearningCourseService.addCourse(userId, courseId, valid, startTime, endTime, xcTask);
            //选课成功发送响应消息
            if (addCourse.isSuccess()) {
                //向mq发送响应消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE, RabbitMQConfig.XC_LEARNING_FINISHADDCHOOSECOURSE_KEY, xcTask);
                LOGGER.info("send finish choose course taskId:{}", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("send finish choose course taskId:{}",id);
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
