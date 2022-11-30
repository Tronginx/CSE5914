package com.cse5914backend.imageSearch;
import java.io.IOException;
import java.util.List;

public interface IExtractWord {

    //send image, return true if able to open
    boolean sendImage(String path);
    String getBestGuess();
}
