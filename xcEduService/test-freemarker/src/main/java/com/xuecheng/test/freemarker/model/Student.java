package com.xuecheng.test.freemarker.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.freemarker.model
 * @ClassName: Student
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/27 21:18
 * @Version: 1.0
 */
@Data
@ToString
public class Student {

    //姓名
    private String name;
    //年龄
    private int age;
    //生日
    private Date birthday;
    //钱包
    private float money;
    //朋友列表
    private List<Student> friends;
    //最好朋友
    private Student bestFriend;
}
