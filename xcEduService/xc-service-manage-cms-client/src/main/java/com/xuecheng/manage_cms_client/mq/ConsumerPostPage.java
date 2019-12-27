package com.xuecheng.manage_cms_client.mq;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms_client.mq
 * @ClassName: ConsumerPostPage
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/30 14:50
 * @Version: 1.0
 */

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.CmsPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
* 消费客户端，监听发布队列的消息，收到消息后调用service方法，下载页面保存本地
* */
@Component
public class ConsumerPostPage {

    //日志记录
    private static final Logger LOGGER= LoggerFactory.getLogger(ConsumerPostPage.class);

    //注入pageService
    @Autowired
    CmsPageService cmsPageService;

    @RabbitListener(queues = {"${xuecheng.mq.home_queue}","${xuecheng.mq.course_queue}"})
    public void postHomePage(String msg){
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //取出页面id
        String pageId = (String) map.get("pageId");
        //查询页面
        CmsPage cmsPage = cmsPageService.getCmsPageById(pageId);
        if (cmsPage==null){
            LOGGER.error("receive cms post page,cmspage is null:{}",msg.toString());
            return;
        }
        //将页面保存到服务物理地址
        cmsPageService.savePageToPath(pageId);
    }

}
