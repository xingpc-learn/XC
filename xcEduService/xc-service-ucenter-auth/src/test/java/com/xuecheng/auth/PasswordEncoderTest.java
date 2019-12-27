package com.xuecheng.auth;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth
 * @ClassName: PasswordEncoderTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/23 23:11
 * @Version: 1.0
 */
public class PasswordEncoderTest {

    @Test
    public void testPasswordEncoder(){
        String password="222222";
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        for (int i = 0; i < 10; i++) {
            //每次计算出的hash值都不一样
            String hashPass=passwordEncoder.encode(password);
            System.out.println(hashPass);
            //虽然每次计算的密码hash值不一样但是校验是通过的
            boolean matches = passwordEncoder.matches(password, hashPass);
            System.out.println(matches);
        }
    }

}
