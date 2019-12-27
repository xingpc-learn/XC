package com.xuecheng.test.freemarker;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.freemarker
 * @ClassName: FreemarkerTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 10:49
 * @Version: 1.0
 */

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
* freemarker生成静态html
* */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    /**
    * 基于模板文件生成静态化页面
    * */
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
//        创建配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
//        获取模板路径，设置模板路径
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));
//        设置配置类字符集
        configuration.setDefaultEncoding("utf-8");
//        加载配置文件
        Template template = configuration.getTemplate("test1.ftl");
//        获取数据模型
        Map map = this.getMap();
//        生成html（模板+数据模型）,静态化页面
        // 1.模板文件；
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        输出静态化页面
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream =new FileOutputStream(new File("d:/test1.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
        fileOutputStream.close();
        inputStream.close();
        // 2.模板字符串

    }

    /**
    * 基于模板文件（字符串）生成静态化页面
    * */
    @Test
    public void testGenetateHtmlByString() throws IOException, TemplateException {
//        创建配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //模板内容，这里测试时使用简单的字符串作为模板
        String templateString = "" +
                "<html>\n" +
                "<head></head>\n" +
                "<body>\n" +
                "名称：${name}\n" +
                "</body>\n" +
                "</html>";
//        加载模板加载器
        StringTemplateLoader stringTemplateLoader=new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateString);
        configuration.setTemplateLoader(stringTemplateLoader);
//        获取模板
        Template template = configuration.getTemplate("template", "utf-8");
//        获取数据模型
        Map map = this.getMap();
//        生成静态化页面
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        输出静态页面
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream fileOutputStream=new FileOutputStream(new File("d:/test1.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
        fileOutputStream.close();
        inputStream.close();
    }

    public Map getMap(){
        Map<String,Object> map=new HashMap<>();
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
        return map;
    }
}
