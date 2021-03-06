package com.zsp;

import com.alibaba.fastjson.JSON;
import com.zsp.pojo.User;
import lombok.AllArgsConstructor;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.support.SearchHitsUtil;
import org.springframework.data.querydsl.QuerydslUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootTest
class ElkStudyApplicationTests {

    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;

    @Test
    void testCreate() throws IOException {
//        ??????????????????
        CreateIndexRequest request = new CreateIndexRequest("zsp_index");
//        ??????????????????????????????????????????????????????
        CreateIndexResponse createIndexResponse =
                client.indices().create(request, RequestOptions.DEFAULT);
//        ??????
        System.out.println(createIndexResponse);
    }
    @Test
    void testExit() throws IOException {
        GetIndexRequest request = new GetIndexRequest("zsp_index");
        boolean exists = client.indices().exists(request,RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    @Test
    void testDelete() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("zsp_index");
        AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
    @Test
    void testPut() throws IOException {
        //????????????
        for (int i = 4; i <10 ; i++) {

            User user = new User("zsp"+i, 18+i);
            //????????????
            IndexRequest request = new IndexRequest("zsp_index");
            //??????????????????,id??????????????????????????????????????????json??????????????????
            request.id(""+i);
            request.source(JSON.toJSONString(user), XContentType.JSON);
            request.timeout("1s");

            IndexResponse index = client.index(request, RequestOptions.DEFAULT);
        }


    }
    @Test
    void testIsExit() throws IOException {
        GetRequest request = new GetRequest("zsp_index", "1");
//        ???????????????api
        boolean exists = client.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }
    @Test
    void testGetRequest() throws IOException {
//        ?????????
        GetRequest request = new GetRequest("zsp_index", "1");
//        ????????????
        GetResponse documentFields = client.get(request, RequestOptions.DEFAULT);
//        ??????????????????
        Map<String, Object> source = documentFields.getSource();
    }
    @Test
    void testDelRequest() throws IOException {
        DeleteRequest request = new DeleteRequest("zsp_index");
        request.id("9");
        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.status());
    }
    @Test
    void testUpdate() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest();
        User user = new User("ppp", 18);
        updateRequest.index("zsp_index").id("1").doc(JSON.toJSONString(user),XContentType.JSON);
        client.update(updateRequest,RequestOptions.DEFAULT);
    }
    @Test
    void testBluk() throws IOException {
//        ??????????????????
        BulkRequest bulkRequest = new BulkRequest();
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("psz",20));
        userList.add(new User("gg",21));
        userList.add(new User("ps",22));
        userList.add(new User("pz",23));
        for (User user : userList) {
            bulkRequest.add(new IndexRequest("zsp_index")
//                    .id()   //??????????????????id,?????????????????????
                        .source(JSON.toJSONString(user),XContentType.JSON));
        }
        client.bulk(bulkRequest,RequestOptions.DEFAULT);
    }
    @Test
    void testSearch() throws IOException {
//      1???????????????
        SearchRequest searchRequest = new SearchRequest("jd_goods");
//      2?????????????????????
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//      3???????????????????????????????????????????????????????????????
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title","java");
//        QueryBuilders.matchAllQuery();//????????????
//        sourceBuilder.from() //???xx?????? ??????
//        sourceBuilder.size() //???????????? ??????
//        sourceBuilder.highlighter();//??????
//      4??????????????????????????????
        sourceBuilder.query(termQueryBuilder);
//      5??????????????????????????????
        searchRequest.source(sourceBuilder);
//      6???????????????????????????ES
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit );
        }
    }
}
