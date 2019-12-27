package com.xuecheng.api.filesystem;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.api.filesystem
 * @ClassName: FileSystemControllerApi
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/2 16:57
 * @Version: 1.0
 */

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
* 文件管理系统
* */
@Api(value="文件系统管理接口",description="为子系统提供文件的管理")
public interface FileSystemControllerApi {

    /**
    *multipartFile:上传文件
     *  filetag：文件所属子系统标签
     *  businesskey：文件所属子系统业务代码
     *  metadata：元信息，json格式
    * */
    @ApiOperation("文件上传")
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata);
}
