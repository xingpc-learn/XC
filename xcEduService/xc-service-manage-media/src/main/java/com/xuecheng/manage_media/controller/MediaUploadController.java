package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_media.controller
 * @ClassName: MediaUploadController
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/17 19:58
 * @Version: 1.0
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    MediaUploadService mediaUploadService;

    /**
    * 文件上传注册
    * */
    @Override
    @PostMapping("/register")
    public ResponseResult register(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName")String fileName, @RequestParam("fileSize") Long fileSize, @RequestParam("mimetype") String mimeType, @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.register(fileMd5,fileName ,fileSize , mimeType,fileExt );
    }

    /**
    * 文件分块检查
    * */
    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult checkChunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk")Integer chunk, @RequestParam("chunkSize") Integer chunkSize) {
        return mediaUploadService.checkChunk(fileMd5,chunk ,chunkSize );
    }

    /**
    * 文件分块上传
    * */
    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("chunk") Integer chunk, @RequestParam("fileMd5")String fileMd5) {
        return mediaUploadService.uploadChunk(file,chunk ,fileMd5 );
    }

    /**
    * 分块合并
    * */
    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergeChunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName, @RequestParam("fileSize") Long fileSize, @RequestParam("mimetype")String mimeType, @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.mergeChunk(fileMd5,fileName , fileSize, mimeType, fileExt);
    }
}
