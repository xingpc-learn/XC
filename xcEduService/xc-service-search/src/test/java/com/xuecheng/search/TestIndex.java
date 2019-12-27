package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: xcEduService
 * @Package: com.xuecheng.search
 * @ClassName: TestIndex
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/4 23:09
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    /**
    * 测试删除索引库
    * */
    @Test
    public void testDeleteIndex() throws IOException {
        //创建删除索引库请求对象
        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest("xc_course");
        //创建删除索引的操作客户端
        IndicesClient indices = client.indices();
        DeleteIndexResponse deleteIndexResponse = indices.delete(deleteIndexRequest);
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
     * 测试创建索引库和映射
     * */
    @Test
    public void testCreateIndex() throws IOException {
        //创建创建索引请求对象
        CreateIndexRequest createIndexRequest=new CreateIndexRequest("xc_course");
        //设置索引参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1).put("number_of_replicas",0));
        //设置映射
        createIndexRequest.mapping("doc", " {\n" +
                " \t\"properties\": {\n" +
                "           \"name\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"description\": {\n" +
                "              \"type\": \"text\",\n" +
                "              \"analyzer\":\"ik_max_word\",\n" +
                "              \"search_analyzer\":\"ik_smart\"\n" +
                "           },\n" +
                "           \"studymodel\": {\n" +
                "              \"type\": \"keyword\"\n" +
                "           },\n" +
                "           \"price\": {\n" +
                "              \"type\": \"float\"\n" +
                "           }\n" +
                "        }\n" +
                "}", XContentType.JSON);
        //创建操作索引客户端
        IndicesClient indices = client.indices();
        //创建索引
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        //操作响应结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    /**
    * 测试添加文档
    * */
    @Test
    public void testCreateDocIndex() throws IOException {
        //准备json数据
        Map<String,Object> jsonMap=new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud基础入门 3.实战Spring Boot 4.注册中心eureka。");
        jsonMap.put("studymodel", "201001");
        jsonMap.put("price", 5.6f);
        //索引请求对象
        IndexRequest indexRequest =new IndexRequest("xc_course","doc");
        //指定索引文档内容
        indexRequest.source(jsonMap);
        //索引响应对象
        IndexResponse index = client.index(indexRequest);
        //索引响应结果
        DocWriteResponse.Result result = index.getResult();
        System.out.println(result);
    }

    /**
    * 查询文档
    * */
    @Test
    public void testGetDoc() throws IOException {
        //创建查询请求对象
        GetRequest getRequest=new GetRequest("xc_course","doc","p1teXWwBAB4rftxSXW74");
        //客户端调用方法
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    /**
    * 文档更新
    * */
    @Test
    public void testUpdate() throws IOException {
        UpdateRequest updateRequest=new UpdateRequest("xc_course","doc" ,"p1teXWwBAB4rftxSXW74" );
        Map<String,Object> jsonMap=new HashMap<>();
        //更新局部
        jsonMap.put("name","spring cloud实战" );
        updateRequest.doc(jsonMap);
        UpdateResponse updateResponse = client.update(updateRequest);
        RestStatus status = updateResponse.status();
        System.out.println(status);
    }

    /**
    * 删除文档
    * */
    @Test
    public void testDeleteDoc() throws IOException {
        //删除文档id
        String id="p1teXWwBAB4rftxSXW74";
        //删除索引请求对象
        DeleteRequest deleteRequest=new DeleteRequest("xc_course","doc",id);
        //客户端调用删除
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        //响应结果
        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }


}
