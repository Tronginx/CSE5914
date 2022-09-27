package com.cse5914backend;

import com.cse5914backend.domain.Thing;

import java.util.List;

public interface ISearch {

    //send image, return true if able to open
    boolean sendImage(String path);
    //return things disover on image
    List<Thing> getThings();

}