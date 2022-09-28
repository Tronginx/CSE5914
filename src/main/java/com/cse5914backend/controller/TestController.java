package com.cse5914backend.controller;

import com.cse5914backend.controller.utils.R;
import com.cse5914backend.domain.Thing;
import com.cse5914backend.service.IGraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
public class TestController {

    /**
     * 上传地址
     */
    @Value("${file.upload.path}")
    private String filePath;

    @Autowired
    private IGraphService iGraphService;

    // 跳转上传页面
    @RequestMapping("test")
    public String test() {
        return "Page";
    }

    // 执行上传
//    @RequestMapping("upload")
//    public String upload(@RequestParam("file") MultipartFile file, Model model) {
//        // 获取上传文件名
//        String filename = file.getOriginalFilename();
//        // 定义上传文件保存路径
//        String path = filePath + "rotPhoto/";
//        // 新建文件
//        File filepath = new File(path, filename);
//        // 判断路径是否存在，如果不存在就创建一个
//        if (!filepath.getParentFile().exists()) {
//            filepath.getParentFile().mkdirs();
//        }
//        try {
//            // 写入文件
//            file.transferTo(new File(path + File.separator + filename));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        // 将src路径发送至html页面
//        model.addAttribute("filename", "/images/rotPhoto/" + filename);
//
//        //TODO: send path to ISearch engine
//        List<Thing> searchResult = iGraphService.getResults(filePath);
//        return "index";
//    }

    @RequestMapping("upload")
    public R upload(@RequestParam("file") MultipartFile file, Model model) {
        // 获取上传文件名
        String filename = file.getOriginalFilename();
        // 定义上传文件保存路径
        String path = filePath + "rotPhoto/";
        // 新建文件
        File filepath = new File(path, filename);
        // 判断路径是否存在，如果不存在就创建一个
        if (!filepath.getParentFile().exists()) {
            filepath.getParentFile().mkdirs();
        }
        try {
            // 写入文件
            file.transferTo(new File(path + File.separator + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将src路径发送至html页面
        model.addAttribute("filename", "/images/rotPhoto/" + filename);

        //TODO: send path to ISearch engine
        List<Thing> searchResult = iGraphService.getResults(filePath);
        return new R(true, searchResult);
    }

    //TODO: return search history
    @GetMapping("/getHistory")
    public R getHistory(){
        return new R(true, iGraphService.getHistory());
    }

    //Ajax Test:
    @RequestMapping("/Aupload")
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
        try {
            // 写入文件
            file.transferTo(new File(path + File.separator + newImgName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Thing> searchResult = iGraphService.getResults(path);
        return new R(true, searchResult);
    }
}
