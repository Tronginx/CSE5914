package com.cse5914backend.service.impl;

import com.cse5914backend.domain.Thing;
import com.cse5914backend.imageSearch.ISearch;
import com.cse5914backend.imageSearch.impl.Search1;

public class test {
    public static void main(String[] args) {
        ISearch is = new Search1();
        is.sendImage("/Users/yz/Documents/22fall/CSE5914images/2b6d921d-502f-4212-943f-2d9a089367a5.jpeg");
        for (Thing t : is.getThings()) {
            System.out.println(t.getName());
        }
    }
}
