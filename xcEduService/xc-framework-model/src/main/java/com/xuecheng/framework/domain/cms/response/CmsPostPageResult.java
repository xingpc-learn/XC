package com.xuecheng.framework.domain.cms.response;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.cms.response
 * @ClassName: CmsPostPageResult
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/4 11:02
 * @Version: 1.0
 */

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
* 页面一键发布
* */
@Data
@ToString
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {

    String pageUrl;
    public CmsPostPageResult(ResultCode resultCode,String pageUrl){
        super(resultCode);
        this.pageUrl=pageUrl;
    }

}
