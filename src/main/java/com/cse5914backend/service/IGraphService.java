package com.cse5914backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cse5914backend.domain.Thing;

import java.util.List;

public interface IGraphService {
    List<Thing> getResults(String filePath);
}
