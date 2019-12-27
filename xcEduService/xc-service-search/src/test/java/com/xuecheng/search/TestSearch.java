package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class TestSearch {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    /**
    * 查询所有
    * */
    @Test
    public void testSearch() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置数据源查询方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询数量
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 分页查询
     * */
    @Test
    public void testSearchPage() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=2;
        int size=1;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //设置数据源查询方式
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * termQuery查询，termQuery精确查询；termsQuery根据ids查询
     * */
    @Test
    public void testTermQuery() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //设置数据源查询方式，termquery精确匹配
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        //根据id查询，注意查询方法使用termsQuery
        //id数组
        String[] ids={"1","2"};
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id",ids));
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 全文检索查询(先分词再查询)，matchQuery，minmum_should_match
     * */
    @Test
    public void testMatchQuery() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //设置数据源查询方式，termquery精确匹配
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        //根据id查询，注意查询方法使用termsQuery
        //operators
//        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发").operator(Operator.OR));
        //operators
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","前台页面开发框架").minimumShouldMatch("80%"));
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 全文检索查询,multiMatchQuery多域查询，minmum_should_match
     * */
    @Test
    public void testMultiMatchQuery() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //设置数据源查询方式，termquery精确匹配
//        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));
        //根据id查询，注意查询方法使用termsQuery
        //operators
//        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发").operator(Operator.OR));
        //operators
//        searchSourceBuilder.query(QueryBuilders.matchQuery("description","前台页面开发框架").minimumShouldMatch("80%"));
        //多域查询，boost提升
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring框架","name","description").field("name",10 ));
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 布尔查询BoolQuery
     * */
    @Test
    public void testBoolQuery() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //设置数据源查询方式，先设置一个termquery精确匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");

        //再设置一个多域查询，boost提升
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架", "name", "description").field("name", 10);

        //设置一个boolquery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 过滤器,在boolQuery中使用,使用过滤器和查询相结合可以提高性能
     * */
    @Test
    public void testFilter() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        //设置一个多域查询，boost提升
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring java", "name", "description");
        //设置一个boolquery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //设置过滤器
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gt(30).lt(100));
        boolQueryBuilder.must(multiMatchQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 排序，sort，对于keyword，float，date可以添加排序，在searchSourceBuilder对象调用
     * */
    @Test
    public void testSort() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //设置一个boolquery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //设置过滤器
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gt(30).lt(100));
        searchSourceBuilder.query(boolQueryBuilder);
        //增加排序
        searchSourceBuilder.sort("price", SortOrder.ASC );
        searchSourceBuilder.sort("studymodel", SortOrder.ASC );
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            //文档主键
            String id = searchHit.getId();
            float score = searchHit.getScore();
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String sourceAsString = searchHit.getSourceAsString();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }

    /**
     * 高亮显示
     * */
    @Test
    public void testLight() throws IOException, ParseException {
        //创建查询请求对象
        SearchRequest searchRequest=new SearchRequest("xc_course");
        //设置查询类型
        searchRequest.types("doc");
        //创建查找数据源对象
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        //设置分页参数
        int page=1;
        int size=3;
        //起始索引位置
        int from=(page-1)*size;
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        //匹配关键字
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发", "name", "description");
        //设置一个boolquery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //设置过滤器
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gt(30).lt(100));
        searchSourceBuilder.query(boolQueryBuilder);
        //增加排序
        searchSourceBuilder.sort("price", SortOrder.ASC );
        searchSourceBuilder.sort("studymodel", SortOrder.ASC );
        //设置过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","timestamp","price"}, new String[]{});
        //高亮显示
        HighlightBuilder highlightBuilder=new HighlightBuilder();
        //设置前缀
        highlightBuilder.preTags("<tag>");
        //设置后缀
        highlightBuilder.postTags("</tag>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
//        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);
        //查询请求设置数据源
        searchRequest.source(searchSourceBuilder);
        //客户端调用查询
        SearchResponse searchResponse = client.search(searchRequest);
        //数据响应查询符合条件数量
        SearchHits hits = searchResponse.getHits();
        //命中的分数高的文档
        SearchHit[] searchHits = hits.getHits();
        //日期格式
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit searchHit : searchHits) {
            //文档内容
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            //名称
            String name = (String) sourceAsMap.get("name");
            //取出高亮字段内容
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            if (highlightFields!=null){
                HighlightField nameField = highlightFields.get("name");
                if (nameField!=null){
                    Text[] fragments = nameField.getFragments();
                    StringBuffer stringBuffer=new StringBuffer();
                    for (Text fragment : fragments) {
                        stringBuffer.append(fragment.string());
                    }
                    name=stringBuffer.toString();
                }
            }
            String index = searchHit.getIndex();
            String type = searchHit.getType();
            String id = searchHit.getId();
            float score = searchHit.getScore();
            String sourceAsString = searchHit.getSourceAsString();

            String studymodel = (String) sourceAsMap.get("studymodel");
            //无法获取到，由于数据源字段没有
//            String description = (String) sourceAsMap.get("description");
            Date timestamp = sdf.parse((String) sourceAsMap.get("timestamp"));
            Double price = (Double) sourceAsMap.get("price");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(timestamp);
            System.out.println(price);
        }
    }



}
