package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.exception
 * @ClassName: ExceptionCast
 * @Author: Administrator
 * @Description: 自定义异常抛出类
 * @Date: 2019/7/27 11:54
 * @Version: 1.0
 */
public class ExceptionCast {

//    使用静态方法抛出自定义异常
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
