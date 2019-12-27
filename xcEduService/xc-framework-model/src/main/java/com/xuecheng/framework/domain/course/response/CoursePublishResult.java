package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.course.response
 * @ClassName: CoursePublishResult
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/3 22:56
 * @Version: 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class CoursePublishResult extends ResponseResult {

    String previewUrl;
    public CoursePublishResult(ResultCode resultCode,String previewUrl){
        super(resultCode);
        this.previewUrl=previewUrl;
    }
}
