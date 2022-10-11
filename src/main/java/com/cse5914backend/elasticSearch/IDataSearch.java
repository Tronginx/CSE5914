package com.cse5914backend.elasticSearch;

import com.cse5914backend.domain.Record;

import java.io.IOException;
import java.util.List;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
public interface IDataSearch {
    /**
     *
     * @param record: sent a record to es
     * @return successful or not
     */
    boolean sendHistory(Record record);

    /**
     *
     * @return all data in es
     * TODO: expand more than 10 result
     */
    List<Record> getSearchHistory();


    /**
     *
     * @param key : search key
     * @param value: search value
     * @return list of all matched Record
     */
    List<Record> searchByName(String key, String value);
}
