package com.zsp.Service;

import com.alibaba.fastjson.JSON;
import com.zsp.pojo.GoodList;
import com.zsp.utils.WebParse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoodListService {
    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;
    public List parseJD(String keyword) throws Exception {

        try {
            return WebParse.parseJD(keyword);
        } catch (Exception e) {
            throw new Exception("获取数据失败");
        }
    }
    public Boolean parseGoodList(String keyword) throws Exception {

        List<GoodList> goodLists = WebParse.parseJD(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (GoodList goodList : goodLists) {
            bulkRequest.add(new IndexRequest("jd_goods")
                            .source(JSON.toJSONString(goodList), XContentType.JSON)
            );
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }
    public Boolean parseSexList(String keyword) throws Exception {

        List<GoodList> goodLists = WebParse.parseSex(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (GoodList goodList : goodLists) {
            bulkRequest.add(new IndexRequest("jd_goods")
                    .source(JSON.toJSONString(goodList), XContentType.JSON)
            );
        }
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }
    public List<Map<String,Object>> serchPage(String keyword,int pageNo,int pageSize) throws IOException {
        if (pageNo<=1){pageNo=1;} //规定分页默认值
        if(pageSize<=1){pageSize=15;}//规定分页默认值
//        1、创建搜索请求
        SearchRequest searchRequest = new SearchRequest("jd_goods");
//        2、建立对象
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo); //给定分页数据
        sourceBuilder.size(pageSize); //给定分页数据
//        3、给定搜索的方式

        MatchQueryBuilder query = QueryBuilders.matchQuery("title",keyword);
//        4、资源和对象放入
        sourceBuilder.query(query);  //搜索方式放入
        searchRequest.source(sourceBuilder);//搜索对象放入
//        5、获取响应数据
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
//        数据封装成对象
        SearchHits hits = search.getHits();
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        List<Map<String, Object>> result = list.stream().distinct().collect(Collectors.toList());
        return result;
    }
    public List<Map<String,Object>> searchHighLight(String keyword,int pageNo,int pageSize) throws IOException {
        if (pageNo<=1){pageNo=1;} //规定分页默认值
        if(pageSize<=1){pageSize=15;}//规定分页默认值
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder
                .from(pageNo)
                .size(pageSize); //分页
        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        高亮配置
        highlightBuilder
                .field("title") //高亮字段
                .requireFieldMatch(false)//是否开启统一标题内全部高亮
                .preTags("<span style ='color:red'>") //高亮前缀
                .postTags("</span>"); //高亮后缀
        sourceBuilder.highlighter(highlightBuilder); //将高亮放入对象中
        //使用搜索工具获取搜索结果
        MatchQueryBuilder query = QueryBuilders.matchQuery("title", keyword);
        sourceBuilder.query(query);//将搜索结果放入
        searchRequest.source(sourceBuilder);//将对象放入请求中
        //获取请求的回调对象
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields(); //获取高亮字段
            HighlightField title = highlightFields.get("title");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (title!=null){
                Text[] fragments = title.fragments();
                String newTitle ="";
                for (Text fragment : fragments) {
                newTitle+=fragment;
                }
                sourceAsMap.put("title",newTitle);
            }
            list.add(sourceAsMap);

        }
        List<Map<String, Object>> result = list.stream().distinct().collect(Collectors.toList());
        return result;
    }

}
