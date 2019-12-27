package com.xuecheng.framework.domain.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.auth
 * @ClassName: AuthToken
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/22 21:16
 * @Version: 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class AuthToken {

    //jwt令牌
    String jwt_token;
    //刷新令牌
    String refresh_token;
    //身份令牌
    String jti_token;

}
