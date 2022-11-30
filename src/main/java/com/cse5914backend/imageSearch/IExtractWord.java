package com.cse5914backend.imageSearch;
import java.io.IOException;

public interface IExtractWord {
    /**
     * Extract information (place, words, or sentence)
     * @param filePath
     * @return the best guess info
     */
    String extractInfo(String filePath) throws IOException;
}
