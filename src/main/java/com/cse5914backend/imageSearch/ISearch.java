package com.cse5914backend.imageSearch;

import com.cse5914backend.domain.Thing;

import java.util.List;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
public interface ISearch {

    //send image, return true if able to open
    boolean sendImage(String path);

    /**
     * return things searched only for current search
     * Remember to clean list
     * @return
     */
    List<Thing> getThings();

}