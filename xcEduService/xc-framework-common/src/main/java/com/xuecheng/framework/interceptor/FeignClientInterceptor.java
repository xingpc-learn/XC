package com.xuecheng.framework.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.interceptor
 * @ClassName: FeignClientInterceptor
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/27 1:11
 * @Version: 1.0
 */
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        try {
            //使用requestcontextholder工具获取request相关变量
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes!=null){
                //取出request
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames!=null){
                    while (headerNames.hasMoreElements()){
                        String name = headerNames.nextElement();
                        String value = request.getHeader(name);
                        if (name.equals("authorization")){
                            //将header向下传
                            template.header(name,value);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
