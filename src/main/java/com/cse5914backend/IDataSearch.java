package com.cse5914backend;

import com.cse5914backend.domain.Record;

import java.util.List;

public interface IDataSearch {
    boolean sendHistory(Record record);
    List<Record> getSearchHistory();
}
