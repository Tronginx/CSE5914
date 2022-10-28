package com.cse5914backend.controller;

import com.cse5914backend.controller.utils.R;
import com.cse5914backend.domain.*;
import com.cse5914backend.domain.Record;
import com.cse5914backend.elasticSearch.IDataSearch;
import com.cse5914backend.elasticSearch.impl.DataSearch1;
import com.cse5914backend.service.IDataService;
import com.cse5914backend.service.IGraphService;
import com.cse5914backend.service.impl.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TimeBox1Controller {

    /**
     * 上传地址
     */
    @Value("${file.upload.path}")
    private String filePath;

    @Qualifier("GraphService")
    @Autowired
    private IGraphService iGraphService;

    @Autowired
    private IDataService dataService;

//    // 跳转上传页面
//    @RequestMapping("test")
//    public String test() {
//        return "Page";
//    }

    // 执行上传
    @RequestMapping("/upload")
    public R aUpload(@RequestParam("file") MultipartFile file) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        // 定义上传文件保存路径
        String path = filePath + "images/";
        //生成新的文件名称
        String newImgName = UUID.randomUUID().toString() + suffixName;
        // 新建文件
        File filepath = new File(path, newImgName);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        String newPath = path + File.separator + newImgName;
        try {
            // 写入文件
            file.transferTo(new File(newPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Thing> searchResult = iGraphService.getResults(newPath);
        List<LocalizedObject> detailResult = iGraphService.getDetails(newPath);
        List<Text> detailText = iGraphService.getTexts(newPath);
        List<String> translationText = iGraphService.getTranslations(newPath);
        List<Label> labelText = iGraphService.getLabels(newPath);
        List<WebResource> resourceResult = iGraphService.getResources(newPath);
        List<Object> result = new ArrayList<Object>();
        result.add(searchResult);
        result.add(detailResult);
        result.add(detailText);
        result.add(translationText);
        result.add(labelText);
        result.add(resourceResult);
        // store history to IDataSearch
        //TODO: maybe need a try catch?
        if(searchResult==null || searchResult.size()==0){
            return new R(false, result);
        }

        //only need one result
        while(searchResult.size()>1){
            searchResult.remove(searchResult.size()-1);
        }
        dataService.sendHistory(searchResult, newPath);
        return new R(true, result);
    }

    // return search history
    @GetMapping("/getHistory")
    public R getHistory(){
        return new R(true, dataService.getSearchHistory());
    }

    @GetMapping("/searchHistory/{key}/{value}")
    public R searchHistory(@PathVariable String key, @PathVariable String value){
        System.out.println("key: "+key);
        System.out.println("value: "+value);
        List<Record> result = dataService.searchByName(key, value);
        return new R(result!=null && result.size()>0 , result);
    }

}
