package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.exception
 * @ClassName: ExceptionCatch
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/27 11:56
 * @Version: 1.0
 */
/**
* 使用@ControllerAdvice和@ExceptionHandler注解来捕获指定异常
* */
@ControllerAdvice
public class ExceptionCatch {

//    声明日志
    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

//    声明不可预知错误异常映射map，使用ImmutableMap的特点是一旦创建不可改变，并且线程安全
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
//    使用builder来创建一个异常类型和错误代码异常
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder=ImmutableMap.builder();

//    捕获自定义异常CustomExcption
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catch exception : {}\r\nexception:",e.getMessage(),e);
        ResultCode resultCode=e.getResultCode();
        return new ResponseResult(resultCode);
    }

//    捕获不可预知异常Excption
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        LOGGER.error("catch exception : {}\r\nexception:",e.getMessage(),e);
        if (EXCEPTIONS==null) {
            EXCEPTIONS = builder.build();
        }
        final ResultCode resultCode=EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if (resultCode !=null){
            responseResult=new ResponseResult(resultCode);
        }else {
            responseResult=new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }

    static {
//        定义异常类型对应的异常代码
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
    }
}
