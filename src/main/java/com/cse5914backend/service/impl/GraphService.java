package com.cse5914backend.service.impl;

import com.cse5914backend.DataSearch1;
import com.cse5914backend.IDataSearch;
import com.cse5914backend.ISearch;
import com.cse5914backend.Search1;
import com.cse5914backend.domain.*;
import com.cse5914backend.domain.Record;
import com.cse5914backend.service.IGraphService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService implements IGraphService {
    ISearch iSearch=new Search1();
    @Override
    public List<Thing> getResults(String filePath) {
        boolean readFile = iSearch.sendImage(filePath);
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        List<Thing> result = iSearch.getThings();
        return result;
    }

    @Override
    public List<Record> getHistory() {
        IDataSearch dataSearch = new DataSearch1();
        return dataSearch.getSearchHistory();
    }
}
