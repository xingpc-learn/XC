package com.xuecheng.manage_cms.resttemplate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.resttemplate
 * @ClassName: RestTemplateTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 14:24
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RestTemplateTest {

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRestTemplate(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        System.out.println(forEntity);
    }
}
