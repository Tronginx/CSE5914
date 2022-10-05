package com.cse5914backend.service;

import com.cse5914backend.domain.LocalizedObject;
import com.cse5914backend.domain.Record;
import com.cse5914backend.domain.Thing;

import java.util.List;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
public interface IGraphService {
    List<Thing> getResults(String filePath);
    List<LocalizedObject> getDetails(String filePath);
//    List<Record> getHistory();
}
