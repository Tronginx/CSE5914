package com.cse5914backend.elasticSearch;

import com.cse5914backend.domain.Record;

import java.util.List;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
public interface IDataSearch {
    boolean sendHistory(Record record);
    List<Record> getSearchHistory();
}
