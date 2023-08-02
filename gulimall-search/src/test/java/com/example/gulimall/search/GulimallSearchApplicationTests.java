package com.example.gulimall.search;

import cn.hutool.json.JSONUtil;
import com.example.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import net.minidev.json.JSONObject;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;
    @Test
    public void indexData() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("2");
        User user = new User();
        user.setId("02");
        user.setName("Leo");
        String s = JSONUtil.toJsonStr(user);
        indexRequest.source(s, XContentType.JSON);
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Test
    public void searchData() throws IOException {
        //1. 创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("users");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1.1 构建检索条件
        searchSourceBuilder.query(QueryBuilders.matchQuery("xxx","xxx"));
        //打印检索条件
        System.out.println(searchSourceBuilder.toString());

        searchRequest.source(searchSourceBuilder);

        //执行检索
        SearchResponse search = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        //分析结果

        System.out.println(search);
    }


    @Data
    class User{
        private String id;
        private String name;

    }
}
