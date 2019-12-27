package com.xuecheng.auth;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.auth
 * @ClassName: TestJwt
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 15:29
 * @Version: 1.0
 */

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
* 测试jwt令牌的生成和校验
* */
public class TestJwt {

    /**
    * 生成一个jwt令牌
    * */
    @Test
    public void testCreateJwt(){
        //证书文件
        String key_location="xc.keystore";
        //秘钥库密码
        String keystore_password="xuechengkeystore";
        //访问证书路径
        ClassPathResource resource=new ClassPathResource(key_location);
        //秘钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory=new KeyStoreKeyFactory(resource,keystore_password.toCharArray());
        //秘钥的密码，此密码和别名要匹配
        String key_password="xuecheng";
        //秘钥别名
        String alias="xckey";
        //秘钥对(秘钥和公钥)
        KeyPair keyPair=keyStoreKeyFactory.getKeyPair(alias,key_password.toCharArray());
        //私钥
        RSAPrivateKey rsaPrivateKey=(RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String,Object> tokenMap=new HashMap<>();
        tokenMap.put("id","123");
        tokenMap.put("name","mrt");
        tokenMap.put("roles","ro1,ro2");
        tokenMap.put("ext","1");
        //生成jwt令牌
        Jwt jwt= JwtHelper.encode(JSON.toJSONString(tokenMap),new RsaSigner(rsaPrivateKey));
        //取出jwt令牌
        String token=jwt.getEncoded();
        System.out.println("token="+token);
    }

    //资源服务使用公钥验证jwt的合法性，并对jwt解码
    @Test
    public void testVerify(){
        //jwt令牌
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1NjY4NzU2MzMsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwiY291cnNlX2dldF9iYXNlaW5mbyIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcGxhbiIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2UiLCJjb3Vyc2VfZmluZF9saXN0IiwieGNfdGVhY2htYW5hZ2VyIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9tYXJrZXQiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX3B1Ymxpc2giLCJjb3Vyc2VfZmluZF9waWMiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX2FkZCJdLCJqdGkiOiJiZWQ0ZWMyZS03YWNjLTRhMzMtODYzNC0yM2JkNTc0MzE2YjkiLCJjbGllbnRfaWQiOiJYY1dlYkFwcCJ9.ZqWZZ8T0xtMt_Fvwwz-6xdKC3V0y1c6R98aLK6nzD_zMPbaU9cHpkjzdiOGgQz8lJSVUnfFz2AroqtTK-Ilv7kN5yb-rO6ZZsR9i09dC9X2-ejSD1RyKm4tTtgnr3WmwgeHSwIesUDrySRhMCZQfUPgnLzFSiHKekCs4RBM5JZL0snw3G4d_qw_z_Vpp-0nA1OkTSU1kflqLTDutkmghWtvOS2wqJabIaU_0jPqXLrf0a2T8qNqvbkD8pS0pG95FYqTusMtbEtu6rpb3H4qb0lVbQqqfgMxvs2KCR1HWq9WcnRWpoLvTRABGiXki5eU_BrLk7T2DLkAo7gk7ppr-qg";
        //公钥
        String publicKey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";
        //检验jwt
        Jwt jwt=JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        //取出jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

}
