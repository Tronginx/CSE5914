package com.cse5914backend.elasticSearch.impl;
import com.cse5914backend.domain.Record;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.cse5914backend.elasticSearch.impl.DataSearch1.closeConnection;
import static com.cse5914backend.elasticSearch.impl.DataSearch1.makeConnection;

public class test {

    public static void main(String[] args) throws IOException {
        makeConnection();
        DataSearch1 es = new DataSearch1();
        Record r1 = new Record();
        r1.setId("1");
        r1.setLatitude("1456");
        r1.setLocation("Beijing");

        //es.sendHistory(r1);
        List<Record> m=es.getSearchHistory();
        closeConnection();

        System.out.println(m);
    }

}
