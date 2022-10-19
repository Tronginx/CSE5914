package com.cse5914backend.service;

import com.cse5914backend.domain.Record;
import com.cse5914backend.domain.Thing;

import java.util.List;

public interface IDataService {
    boolean sendHistory(List<Thing> records, String filePath);
    List<Record> getSearchHistory();
    List<Record> searchByName(String key, String value);
}
