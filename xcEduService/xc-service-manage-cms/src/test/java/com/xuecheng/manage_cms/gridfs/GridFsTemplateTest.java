package com.xuecheng.manage_cms.gridfs;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.manage_cms.gridfs
 * @ClassName: GridFsTemplateTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/7/28 16:04
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTemplateTest {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    /**
    * grid存储文件
    * */
    @Test
    public void testGridFs() throws FileNotFoundException {
//        要存储的模板
        File file=new File("D:\\course.ftl");
//        定义输入流
        FileInputStream inputStream=new FileInputStream(file);
//        向grids存储文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "课程详情模板");
//        达到文件id
        String fileId = objectId.toString();
        System.out.println(fileId);
    }

    /**
    * grid下载文件,GridFsBucket用于打开下载流对象
    * */
    @Test
    public void testGridFsDownLoad() throws IOException {
        String fileId="5d3d59a12b7de42738f48064";
//        根据id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
//        打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
//        创建gridFsResource，用于获取流对象
        GridFsResource gridFsResource=new GridFsResource(gridFSFile,gridFSDownloadStream);
//        获取流中数据
        String s = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(s);
    }

    /**
    * 删除gridFS文件,根据文件id，删除fs.files和fs.chunks中的记录
    * */
    @Test
    public void testDeleteGridFs(){
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5d3d59a12b7de42738f48064")));
    }

}
