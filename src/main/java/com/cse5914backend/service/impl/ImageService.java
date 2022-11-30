package com.cse5914backend.service.impl;

import com.cse5914backend.imageSearch.IExtractWord;
import com.cse5914backend.imageSearch.impl.ExtractWord1;
import com.cse5914backend.service.IImageService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Component("ImageService")
public class ImageService implements IImageService {

    IExtractWord iExtractWord = new ExtractWord1();
    boolean readFile;

    @Override
    public String getBestGuesses(String filePath) {
        readFile = iExtractWord.sendImage(filePath);
        if(!readFile) {
            System.out.println("Failed to read file.");
            return null;
        }
        return iExtractWord.getBestGuess();
    }
}
