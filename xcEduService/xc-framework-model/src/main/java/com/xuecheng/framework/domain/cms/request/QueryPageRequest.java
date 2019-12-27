package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.framework.domain.cms.request
 * @ClassName: QueryPageRequest
 * @Author: Administrator
 * @Description: 查询页面请求
 * @Date: 2019/7/21 16:43
 * @Version: 1.0
 */
@Data
public class QueryPageRequest extends RequestData {

    //站点ID
    @ApiModelProperty("站点id")
    private String siteId;
    //模版ID
    @ApiModelProperty("模版ID")
    private String templateId;
    //页面ID
    @ApiModelProperty("页面ID")
    private String pageId;
    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;
    //别名
    @ApiModelProperty("页面别名")
    private String pageAliase;
    //页面类型
    @ApiModelProperty("页面类型")
    private String pageType;
    //.....
}
