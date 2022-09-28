package com.cse5914backend.elasticSearch.impl;

import java.util.ArrayList;
import java.util.List;
import com.cse5914backend.domain.Record;
import com.cse5914backend.elasticSearch.IDataSearch;

/**
 * This is used to test front-end
 *      without the need to know elastic search
 */
public class DataSearch1 implements IDataSearch {

    @Override
    public boolean sendHistory(Record record) {
        return false;
    }

    public List<Record> getSearchHistory() {
        List<Record> list = new ArrayList<>();
        Record r1 = new Record();
        Record r2 = new Record();
        list.add(r1);
        list.add(r2);
        r1.setLocation("Paris");
        r2.setLocation("Beijing");
        return list;
    }
}
