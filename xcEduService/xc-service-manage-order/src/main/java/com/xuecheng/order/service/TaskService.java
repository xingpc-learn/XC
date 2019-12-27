package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.order.service
 * @ClassName: TaskService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 19:12
 * @Version: 1.0
 */
@Service
public class TaskService {

    @Autowired
    XcTaskRepository xcTaskRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    XcTaskHisRepository xcTaskHisRepository;

    /**
    * 取出前n条任务，取出指定时间之前处理的任务
    * */
    public List<XcTask> findTaskList(Date updateTime,int n){
        //设置分页参数，取出前n条记录
        Pageable pageable=new PageRequest(0,n);
        Page<XcTask> xcTasks =xcTaskRepository.findByUpdateTimeBefore(pageable,updateTime);
        return xcTasks.getContent();
    }

    /**
    * 发送消息，xctask：任务对象，ex:交换机id，routingkey：路由键
    * */
    @Transactional
    public void publish(XcTask xcTask,String ex,String routingkey){
       //查询任务
       Optional<XcTask> taskOptional = xcTaskRepository.findById(xcTask.getId());
       if (taskOptional.isPresent()){
           xcTask=taskOptional.get();
           //rabbit发送消息
           rabbitTemplate.convertAndSend(ex,routingkey,xcTask);
           //更新任务时间为当前时间
           xcTask.setUpdateTime(new Date());
           xcTaskRepository.save(xcTask);
       }
    }

    /**
    * 使用乐观锁校验任务，获取任务
    * */
    @Transactional
    public int getTask(String taskId,int version){
        int i=xcTaskRepository.updateTaskVersion(taskId,version);
        return i;
    }

    /**
    * 删除任务
    * */
    @Transactional
    public void finishTask(String taskId){
        Optional<XcTask> taskOptional = xcTaskRepository.findById(taskId);
        if (taskOptional.isPresent()){
            XcTask xcTask=taskOptional.get();
            xcTask.setDeleteTime(new Date());
            XcTaskHis xcTaskHis=new XcTaskHis();
            BeanUtils.copyProperties(xcTask,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            xcTaskRepository.delete(xcTask);
        }
    }

}
