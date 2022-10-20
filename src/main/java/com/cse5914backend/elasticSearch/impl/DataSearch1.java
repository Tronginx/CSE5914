package com.cse5914backend.elasticSearch.impl;

import java.util.ArrayList;
import java.util.List;
import com.cse5914backend.domain.Record;
import com.cse5914backend.elasticSearch.IDataSearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

//demo version
public class DataSearch1 implements IDataSearch {


    //The config parameters for the connection
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";

    private static RestHighLevelClient restHighLevelClient;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String INDEX = "record";
    private static final String TYPE = "_doc";

    /**
     * Implemented Singleton pattern here
     * so that there is just one connection at a time.
     * @return RestHighLevelClient
     */
    static synchronized RestHighLevelClient makeConnection() {

        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }

        return restHighLevelClient;
    }
    static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }
    public DataSearch1(){

    }
    @Override
    public boolean sendHistory(Record record) {

            makeConnection();
        record.setId(UUID.randomUUID().toString());
        System.out.println("------"+record.getId());
        String latitude = record.getLatitude();
        String longitude = record.getLongitude();
        String path = record.getFilePath();
        String location = record.getLocation();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("filePath", path);
        dataMap.put("location", location);
        dataMap.put("latitude", latitude);
        dataMap.put("longitude", longitude);
        dataMap.put("ID",record.getId());


        IndexRequest indexRequest = new IndexRequest(INDEX)
                .id(record.getId()).source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        try {
            closeConnection();
        }catch (java.io.IOException x)
        {
            x.getLocalizedMessage();
        }
        return true;
    }

    public List<Record> getSearchHistory(){
        makeConnection();
        List<Record> res = new ArrayList<>();

        List<Map<String,Object>> m = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(10000);
        final SearchRequest source = searchRequest.source(searchSourceBuilder);
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        res = getSC(response);
        try {
            closeConnection();
        }catch (java.io.IOException x)
        {
            x.getLocalizedMessage();
        }
        return res;
    }
    @Override
    public List<Record> searchByName(String key, String value) {
        List<Record> res = new ArrayList<>();
        makeConnection();
        // 支持通配符查询，*表示任意字符，?表示任意单个字符
        WildcardQueryBuilder wildcardQuery = QueryBuilders.wildcardQuery(key, value);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(wildcardQuery);

        SearchRequest request = new SearchRequest(INDEX);
        request.source(sourceBuilder);
        SearchResponse search = null;
        try {
            search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("-------------");
        System.out.println(search);
        res = getSC(search);
        try {
            closeConnection();
        }catch (java.io.IOException x)
        {
            x.getLocalizedMessage();
        }
        return res;
    }


    private List<Record> getSC(SearchResponse sr) {
        List<Record> m = new ArrayList<>();
        for (SearchHit hit : sr.getHits()) {
            Map<String, Object> source = hit.getSourceAsMap();
            Record temp = new Record();
            String location = (String) source.get("location");
            String ID = (String) source.get("ID");
            String latitude = (String) source.get("latitude");
            String longitude = (String) source.get("longitude");
            String filePath = (String) source.get("filePath");
            temp.setLatitude(latitude);
            temp.setLocation(location);
            temp.setId(ID);
            temp.setLongitude(longitude);
            temp.setFilePath(filePath);
            m.add(temp);
        }
        return  m;
    }

    public void deleteRecordById(String id) throws IOException {
        makeConnection();
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (java.io.IOException e) {
            e.getLocalizedMessage();
        }
        try {
            closeConnection();
        }catch (java.io.IOException x)
        {
            x.getLocalizedMessage();
        }
    }


}
