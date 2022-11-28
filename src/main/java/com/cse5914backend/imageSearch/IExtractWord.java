package com.cse5914backend.imageSearch;

public interface IExtractWord {
    /**
     * Extract information (place, words, or sentence)
     * @param filePath
     * @return the best guess info
     */
    String extractInfo(String filePath);
}
