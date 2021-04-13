package com.zsp.Service;

import com.alibaba.fastjson.JSON;
import com.zsp.pojo.GoodList;
import com.zsp.utils.WebParse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Map<String,Object>> serchPage(String keyword,int pageNo,int pageSize) throws IOException {
        if (pageNo<=1){pageNo=1;}
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
        TermQueryBuilder termQuery = QueryBuilders.termQuery("title", keyword);
        sourceBuilder.query(termQuery);
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

}
