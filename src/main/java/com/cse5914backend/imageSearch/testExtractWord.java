package com.cse5914backend.imageSearch;

import com.cse5914backend.imageSearch.impl.ExtractWord1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class testExtractWord {
    public static void main(String[] args) throws IOException {
        IExtractWord iExtractWord = new ExtractWord1();
        List<String> imagePaths = new ArrayList<>();
        List<String> outputs = new ArrayList<>();
        String imagePath1 = "/Users/young_y2m/Desktop/CSE/Tower.jpg";
        String imagePath2 = "/Users/young_y2m/Desktop/CSE/TowerMap.png";
        String imagePath3 = "/Users/young_y2m/Desktop/CSE/Pole.jpg";
        String imagePath4 = "/Users/young_y2m/Desktop/CSE/PoleMap.png";
        String imagePath5 = "";
        imagePaths.add(imagePath1);
        imagePaths.add(imagePath2);
        imagePaths.add(imagePath3);
        imagePaths.add(imagePath4);
        imagePaths.add(imagePath5);
        for (String path : imagePaths) {
            if (path.length() > 0){
//                String result = iExtractWord.extractInfo(path);
                String result = "null";
                outputs.add(result);
            } else {
                outputs.add("Nothing found");
            }
        }
        for (String output : outputs){
            System.out.println("info from this picture: "+ output);
        }
    }
}
