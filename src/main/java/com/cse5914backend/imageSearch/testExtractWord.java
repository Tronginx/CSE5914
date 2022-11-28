package com.cse5914backend.imageSearch;

import com.cse5914backend.imageSearch.impl.ExtractWord1;

public class testExtractWord {
    public static void main(String[] args) {
        IExtractWord iExtractWord = new ExtractWord1();
        String imagePath = "put your test image path here";
        String result = iExtractWord.extractInfo(imagePath);
        System.out.println("info from this picture: "+ result);
    }
}
