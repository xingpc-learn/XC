package com.xuecheng.framework.domain.learning;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.learning
 * @ClassName: GetMediaResult
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/20 16:39
 * @Version: 1.0
 */
@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {

    //媒资文件播放地址
    private String fileUrl;

    public GetMediaResult(ResultCode resultCode,String fileUrl){
        super(resultCode);
        this.fileUrl=fileUrl;
    }
}
