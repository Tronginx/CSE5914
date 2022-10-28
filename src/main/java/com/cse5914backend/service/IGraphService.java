package com.cse5914backend.service;

import com.cse5914backend.domain.*;
import com.cse5914backend.domain.Record;

import java.util.List;
import java.util.Map;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
public interface IGraphService {
    List<Thing> getResults(String filePath);
    List<LocalizedObject> getDetails(String filePath);
    List<Text> getTexts(String filePath);
    List<String> getTranslations(String filePath);
    List<Label> getLabels(String filePath);
//    List<Thing> getHistory(String filePath);
//    List<Record> getHistory();
}
