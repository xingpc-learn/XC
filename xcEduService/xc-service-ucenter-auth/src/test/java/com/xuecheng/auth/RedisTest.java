package com.xuecheng.auth;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth
 * @ClassName: RedisTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 20:02
 * @Version: 1.0
 */

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
* redis连接测试，用于保存token令牌信息
* */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    //注入redis模板
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        //定义key
        String key="user_token:9734b68f-cf5e-456f-9bd6-df578c711390";
        //定义map
        Map<String,String> mapValue=new HashMap<>();
        mapValue.put("id","101");
        mapValue.put("username","itcast");
        String value= JSON.toJSONString(mapValue);
        //向redis中存储字符串
        stringRedisTemplate.boundValueOps(key).set(value,60, TimeUnit.SECONDS);
        //读取过期时间，已过期返回-2
        Long expire = stringRedisTemplate.getExpire(key);
        //根据key获取value
        String s=stringRedisTemplate.opsForValue().get(key);
        System.out.println(s);
    }
}
