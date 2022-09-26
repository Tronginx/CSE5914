package com.cse5914backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cse5914backend.ISearch;
import com.cse5914backend.Search1;
import com.cse5914backend.domain.Thing;
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
}
