package com.cse5914backend.service.impl;

import com.cse5914backend.elasticSearch.impl.DataSearch1;
import com.cse5914backend.elasticSearch.IDataSearch;
import com.cse5914backend.imageSearch.ISearch;
import com.cse5914backend.domain.*;
import com.cse5914backend.domain.Record;
import com.cse5914backend.imageSearch.impl.Search1;
import com.cse5914backend.imageSearch.impl.Search2;
import com.cse5914backend.service.IDataService;
import com.cse5914backend.service.IGraphService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Component("GraphService")
public class GraphService implements IGraphService {
    ISearch iSearch=new Search1();
    boolean readFile;
    @Override
    public List<Thing> getResults(String filePath) {
        readFile = iSearch.sendImage(filePath);
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getThings();
    }

    @Override
    public List<LocalizedObject> getDetails(String filePath) {
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getLocalizedObjects();
    }

    @Override
    public List<Text> getTexts(String filePath) {
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getTexts();
    }

    @Override
    public List<String> getTranslations(String filePath) {
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getTranslations();
    }

    @Override
    public List<Label> getLabels(String filePath) {
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getLabels();
    }

    @Override
    public List<WebResource> getResources(String filePath) {
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iSearch.getResources();
    }




//    @Override
//    public List<Record> getHistory() {
//        IDataService dataService = new DataService();
//        return dataService.getSearchHistory();
//    }
}
