package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_course.exception
 * @ClassName: CustomExceptionCatch
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/26 19:39
 * @Version: 1.0
 */
@ControllerAdvice
public class CustomExceptionCatch extends ExceptionCatch {

    static {
        //除了customException以外的异常类型及对应的错误代码在这里定义，如果不定义则统一返回固定的错误信息
        builder.put(AccessDeniedException.class,CommonCode.UNAUTHORISE);
    }

}
