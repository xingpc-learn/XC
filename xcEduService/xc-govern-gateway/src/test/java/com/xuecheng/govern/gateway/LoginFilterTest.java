package com.xuecheng.govern.gateway;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.govern.gateway
 * @ClassName: LoginFilterTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 14:07
 * @Version: 1.0
 */
@Component
public class LoginFilterTest extends ZuulFilter {

    private static final Logger LOGGER= LoggerFactory.getLogger(LoginFilterTest.class);

    @Override
    public String filterType() {
        //pre,routing,post,error
        return "pre";
    }

    @Override
    public int filterOrder() {
        //int值定义过滤器执行顺序，数值越小优先级越高
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //定义该过滤器是否需要执行
        return true;
    }

    @Override
    /**
    * 定义过滤执行逻辑
    * */
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        //取出头部信息Authorization
        String authorization=request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            //拒绝访问
            currentContext.setSendZuulResponse(false);
            //设置响应状态码
            currentContext.setResponseStatusCode(200);
            ResponseResult unauthenticated =new ResponseResult(CommonCode.UNAUTHENTICATED);
            //转成json字符串
            String jsonString = JSON.toJSONString(unauthenticated);
            currentContext.setResponseBody(jsonString);
            currentContext.getResponse().setContentType("application/json;charset=utf-8");
            return null;
        }
        return null;
    }
}
