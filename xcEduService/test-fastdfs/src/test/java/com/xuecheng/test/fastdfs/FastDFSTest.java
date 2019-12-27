package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.test.fastdfs
 * @ClassName: FastDFSTest
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/2 13:52
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FastDFSTest {

    /**
    * 测试上传
    * */
    @Test
    public void testUpload(){
        try {
            //加载fastdfs配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient trackerClient=new TrackerClient();
            //获取,连接tracker服务
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取storage服务
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //创建一个storage客户端
            StorageClient1 storageClient=new StorageClient1(trackerServer,storageServer);
            //上传文件
            String filePath="E:\\uploads\\vue.jpg";
            String fileId = storageClient.upload_file1(filePath, "jpg", null);
            //输出存储地址 group1/M00/00/00/wKhlgF1D1Y-Ab4ltAAKh6ZHHvUA088.jpg
            System.out.println(fileId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
    * 测试下载
    * */
    @Test
    public void testDownload(){
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient trackerClient=new TrackerClient();
            //获取连接tracker服务
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取storage服务
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //获取storage客户端
            StorageClient1 storageClient1=new StorageClient1(trackerServer,storageServer);
            //下载图片
            String fileId="group1/M00/00/00/wKhlgF1D1Y-Ab4ltAAKh6ZHHvUA088.jpg";
            byte[] file1 = storageClient1.download_file1(fileId);
            //用输出流存储图片
            FileOutputStream fileOutputStream=new FileOutputStream(new File("E:\\Download\\1.jpg"));
            fileOutputStream.write(file1);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    /**
     * 测试查询
     * */
    @Test
    public void testQuery(){
        try {
            //加载配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //创建tracker客户端
            TrackerClient trackerClient=new TrackerClient();
            //获取连接tracker服务
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取storage服务
            StorageServer storageServer = null;
            //获取storage客户端
            StorageClient1 storageClient1=new StorageClient1(trackerServer,storageServer);
            //查询
            FileInfo fileInfo = storageClient1.query_file_info1("group1/M00/00/00/wKhlgF1D1Y-Ab4ltAAKh6ZHHvUA088.jpg");
            System.out.println(fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testChar(){
        char c='A';
        char c1='a';
        char c2='1';
        char c3='0';
        System.out.println("A"+":"+(int)c);
        System.out.println("a"+":"+(int)c1);
        System.out.println("1"+":"+(int)c2);
        System.out.println("0"+":"+(int)c3);
    }

}
