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
//        Record r1 = new Record();
//        r1.setLatitude("892342");
//        r1.setLocation("QWERT");
//        r1.setFilePath("C//Desktop");
//        r1.setLongitude("8638378");
//
//        es.sendHistory(r1);



        List<Record> m =es.getSearchHistory();
        System.out.println(m);
    }

}
