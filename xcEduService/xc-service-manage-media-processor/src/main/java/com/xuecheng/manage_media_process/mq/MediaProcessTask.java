package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_media_process.mq
 * @ClassName: MediaProcessTask
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 9:09
 * @Version: 1.0
 */
@Component
public class MediaProcessTask {

    //记录日志
    private static final Logger LOGGER= LoggerFactory.getLogger(MediaProcessTask.class);

    //注入mq相关信息
    //ffmpeg绝对路径
    @Value("${xc-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;

    //上传文件根目录
    @Value("${xc-service-manage-media.video-location}")
    String serverPath;

    @Autowired
    MediaFileRepository mediaFileRepository;

    /**
    * mq监听消息队列,接收routingkey
    * */
    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}",containerFactory = "customerContainerFactory")
    public void receiveMediaProcessTask(String msg){
        //解析接收到的消息为map类型
        Map msgMap = JSON.parseObject(msg, Map.class);
        //输出日志
        LOGGER.info("receive media process task msg:{}",msgMap);
        //解析消息，媒资文件id
        String mediaId = (String) msgMap.get("mediaId");
        //获取媒资文件信息
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if (!mediaFileOptional.isPresent()){
            return;
        }
        MediaFile mediaFile = mediaFileOptional.get();
        //获取文件类型，判断文件类型是否为avi类型，判断是否处理
        String fileType=mediaFile.getFileType();
        if (fileType==null||!fileType.equals("avi")){
            //如果类型不是avi，更改处理状态为不处理
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return;
        }else {
            //类型为avi，设置为未处理
            mediaFile.setProcessStatus("303001");
            mediaFileRepository.save(mediaFile);
        }
        //调用工具类生成MP4文件
        String viedo_path=serverPath+mediaFile.getFilePath()+mediaFile.getFileName();
        String mp4_name=mediaFile.getFileId()+".mp4";
        String mp4folder_path=serverPath+mediaFile.getFilePath();
        //创建工具类对象
        Mp4VideoUtil mp4VideoUtil=new Mp4VideoUtil(ffmpeg_path,viedo_path , mp4_name,mp4folder_path );
        String result = mp4VideoUtil.generateMp4();
        if (result==null||!result.equals("success")){
            //mp4转换操作失败，写入处理日志
            LOGGER.error("transfer .avi to .mp4 fail,mediaFile is:{}",mediaFile.getFileName());
            //更改处理状态为失败
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
            //设置处理失败信息
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //生成m3u8，在转换mp4工具类已经判断视频时长是否相等，转换是否成功
        //mpe4文件地址
        viedo_path=serverPath+mediaFile.getFilePath()+mp4_name;
        //m3u8文件名字
        String m3u8_name=mediaFile.getFileId()+".m3u8";
        //生成的m3u8文件存放路径
        String m3u8folder_path=serverPath+mediaFile.getFilePath()+"hls/";
        //创建工具类对象
        HlsVideoUtil hlsVideoUtil=new HlsVideoUtil(ffmpeg_path,viedo_path,m3u8_name ,m3u8folder_path );
        result = hlsVideoUtil.generateM3u8();
        if (result==null||!result.equals("success")){
            //转换失败写入日志
            LOGGER.error("media tranfer .mp4 to m3u8 fail,media is:{}",mediaFile.getFileName());
            //处理状态更改为失败
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //转换成功，获取ts列表，保存到MongoDB数据库
        List<String> ts_list=hlsVideoUtil.get_ts_list();
        //更新处理状态为处理成功
        mediaFile.setProcessStatus("303002");
        MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        //m3u8文件url
        mediaFile.setFileUrl(mediaFile.getFilePath()+"hls/"+m3u8_name);
        mediaFileRepository.save(mediaFile);
    }

}
