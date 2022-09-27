package com.cse5914backend.service;

import com.cse5914backend.domain.Record;
import com.cse5914backend.domain.Thing;

import java.util.List;

public interface IGraphService {
    List<Thing> getResults(String filePath);
    List<Record> getHistory();
}
