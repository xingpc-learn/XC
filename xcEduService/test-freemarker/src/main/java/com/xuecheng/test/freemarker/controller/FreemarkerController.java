package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.freemarker.controller
 * @ClassName: FreemarkerController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/27 21:25
 * @Version: 1.0
 */
/**
* 这里不使用restController,次注解返回json数据，这里需要输出成html格式
* */
@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/course")
    public String courseDetail(Map<String,Object> map){
        String dataUrl="http://localhost:31200/course/courseview/4028858162e0bc0a0162e0bfdf1a0000";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        //设置数据模型
        map.putAll(body);
        return "course";
    }

    @RequestMapping("/banner")
    public String indexBanner(Map<String,Object> map){
        String dataUrl="http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        //设置数据模型
        map.putAll(body);
        return "index_banner";
    }

    @RequestMapping("/test1")
    public String freemarker(Map<String,Object> map){
        //map就是freemarker使用的数据
        map.put("name", "黑马程序员");
        //创建数据，在map数据模型中放数据
        Student stu1=new Student();
        stu1.setName("小明");
        stu1.setAge(12);
        stu1.setMoney(125);
        stu1.setBirthday(new Date());
        Student stu2=new Student();
        stu2.setName("小红");
        stu2.setAge(22);
        stu2.setMoney(225);
        stu2.setBirthday(new Date());
        Student stu3=new Student();
        stu3.setName("阿郎");
        stu3.setAge(33);
        stu3.setMoney(335);
        stu3.setBirthday(new Date());
        List<Student> friends=new ArrayList<>();
        friends.add(stu1);
        friends.add(stu3);
        //给学生2设置朋友集合
        stu2.setFriends(friends);
        //给学生2设置最好朋友
        stu2.setBestFriend(stu1);
        //将学生保存到集合中,学生集合
        List<Student> stus=new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        stus.add(stu3);
        //向数据模型放数据
        map.put("stus", stus);
        //准备map数据
        HashMap<String,Student> stuMap=new HashMap<>();
//        stuMap.put("stu1",stu1 );
//        stuMap.put("stu2",stu2 );
        stuMap.put("stu3",stu3 );
        //向数据模型放数据
        map.put("stu1",stu1 );
        map.put("stu2",stu2 );
        map.put("stu3",stu3 );
        //向数据模型放数据
        map.put("stuMap",stuMap );

        map.put("point", 123456789);

        //返回templates路径，基于src/resources/templates路径
        return "test1";
    }
}
