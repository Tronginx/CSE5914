package com.cse5914backend.service.impl;

import com.cse5914backend.domain.Record;
import com.cse5914backend.domain.Thing;
import com.cse5914backend.elasticSearch.IDataSearch;
import com.cse5914backend.elasticSearch.impl.DataSearch1;
import com.cse5914backend.service.IDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService implements IDataService {
    IDataSearch iDataSearch = new DataSearch1();
    @Override
    public boolean sendHistory(List<Thing> records, String filePath) {

        for(Thing thing: records) {
            Record record = new Record();
            record.setLocation(thing.getName());
            System.out.println(thing.getLatitude()+"--------"+thing.getLongitude()+"--------");
            record.setFilePath(filePath);
            record.setLatitude(String.valueOf(thing.getLatitude()));
            record.setLongitude(String.valueOf(thing.getLongitude()));
            iDataSearch.sendHistory(record);
        }
        return true;
    }

    @Override
    public List<Record> getSearchHistory() {
        return iDataSearch.getSearchHistory();
    }

    @Override
    public List<Record> searchByName(String key, String value){
        return iDataSearch.searchByName(key, value);
    }
}
