package com.cse5914backend.imageSearch;

import com.cse5914backend.imageSearch.impl.ExtractWord1;

import java.io.IOException;

public class testExtractWord {
    public static void main(String[] args) throws IOException {
        IExtractWord iExtractWord = new ExtractWord1();
        String imagePath = "/Users/young_y2m/Desktop/CSE/Tower.jpg";
        String result = iExtractWord.extractInfo(imagePath);
        System.out.println("\ninfo from this picture: "+ result);
    }
}
