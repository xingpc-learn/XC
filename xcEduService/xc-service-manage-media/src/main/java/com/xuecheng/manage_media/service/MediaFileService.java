package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_media.service
 * @ClassName: MediaFileService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/19 15:11
 * @Version: 1.0
 */
@Service
public class MediaFileService {

    private static Logger logger= LoggerFactory.getLogger(MediaFileService.class);

    @Autowired
    MediaFileRepository mediaFileRepository;

    /**
    * 文件列表分页查询
    * */
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest){
        //查询条件
        MediaFile mediaFile=new MediaFile();
        if (queryMediaFileRequest==null){
            queryMediaFileRequest=new QueryMediaFileRequest();
        }
        //查询条件匹配器,默认精确匹配
        ExampleMatcher matcher=ExampleMatcher.matching()
                //标签模糊匹配
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains())
                //媒资文件原始名模糊匹配
                .withMatcher("fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains())
                //处理状态精确匹配(默认)
                .withMatcher("processStaus",ExampleMatcher.GenericPropertyMatchers.exact());
        //查询条件对象
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())){
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }
        //定义exmaple
        Example<MediaFile> example=Example.of(mediaFile,matcher);
        //定义分页参数
        page=page-1;
        //使用分页插件，定义分页参数
        Pageable pageable=new PageRequest(page,size );
        //分页查询
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        QueryResult<MediaFile> mediaFileQueryResult=new QueryResult<>();
        mediaFileQueryResult.setList(all.getContent());
        mediaFileQueryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS,mediaFileQueryResult);

    }
}
