package com.cse5914backend.service.impl;

import com.cse5914backend.domain.Record;
import com.cse5914backend.domain.Thing;
import com.cse5914backend.elasticSearch.IDataSearch;
import com.cse5914backend.elasticSearch.impl.DataSearch1;
import com.cse5914backend.imageSearch.ISearch;
import com.cse5914backend.imageSearch.impl.Search2;
import com.cse5914backend.service.IGraphService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is for front end to test on their own code
 * withouth the need to configure any external service
 */
@Service
public class GraphService_FrontEnd implements IGraphService {
    ISearch iSearch=new Search2();
    @Override
    public List<Thing> getResults(String filePath) {
        boolean readFile = iSearch.sendImage(filePath);
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getThings();
    }

    @Override
    public List<Record> getHistory() {
        IDataSearch dataSearch = new DataSearch1();
        return dataSearch.getSearchHistory();
    }
}
