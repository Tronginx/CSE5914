package com.cse5914backend.elasticSearch.impl;
import com.cse5914backend.domain.Record;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.cse5914backend.elasticSearch.impl.DataSearch1.closeConnection;
import static com.cse5914backend.elasticSearch.impl.DataSearch1.makeConnection;

public class test {

    public static void main(String[] args) throws IOException {
        DataSearch1 es = new DataSearch1();
        List<Record> m =es.getSearchHistory();
        for(int a=0;a<m.size();a++){
            es.deleteRecordById(m.get(a).getId());
        }
        m=es.getSearchHistory();
        System.out.println(m);

    }

}
