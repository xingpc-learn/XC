package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.filesystem.service
 * @ClassName: FileSystemService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/2 17:12
 * @Version: 1.0
 */
@Service
public class FileSystemService {

    private static final Logger LOGGER= LoggerFactory.getLogger(FileSystemService.class);

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;

    /**
    * fast环境初始化,加载fdfs配置
    * */
    public void initFdfsConfig(){
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常报告异常
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }

    /**
    * 上传文件到fastDFS返回文件id
    * */
    public String fast_upload(MultipartFile multipartFile){
        //fast环境初始化
        initFdfsConfig();
        try {
            //创建tracker客户端
            TrackerClient tc=new TrackerClient();
            //获取tracker连接服务
            TrackerServer ts = tc.getConnection();
            //获取storage服务
            StorageServer ss = tc.getStoreStorage(ts);
            //创建storage客户端
            StorageClient1 sc=new StorageClient1(ts,ss);
            //获取文件字节码
            byte[] fileBytes = multipartFile.getBytes();
            //获取文件格式
            String originalFilename = multipartFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //上传文件
            return sc.upload_file1(fileBytes, extName, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
    *文件上传
    * */
    public UploadFileResult upload(MultipartFile multipartFile,String filetag,String businesskey,String metadata){
        if (multipartFile==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //保存文件到文件系统fastDFS，返回保存的文件id
        String fileId = fast_upload(multipartFile);
        //保存文件信息到MongoDB数据库
        FileSystem fileSystem=new FileSystem();
        //设置信息
        fileSystem.setFileId(fileId);
        if (fileId==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //文件id
        fileSystem.setFilePath(fileId);
        //文件在文件系统中的路径
        fileSystem.setFiletag(filetag);
        //业务标识代码
        fileSystem.setBusinesskey(businesskey);
        //元数据
        if (StringUtils.isNotEmpty(metadata)){
            try {
                Map map = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //文件名字
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        //文件大小
        fileSystem.setFileSize(multipartFile.getSize());
        //文件类型
        fileSystem.setFileType(multipartFile.getContentType());
        //保存到数据库
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }




















}
