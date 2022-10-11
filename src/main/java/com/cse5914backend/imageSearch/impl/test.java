package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.Thing;
import com.cse5914backend.imageSearch.ISearch;

import java.util.List;

public class test {
    public static void main(String[] args) {
        ISearch is = new Search1();
        is.sendImage("/Users/yz/Documents/22fall/CSE5914images/7db2c666-95e8-4e87-94d2-50586bcc3888.jpeg");
        for (Thing t : is.getThings()) {
            System.out.println(t.getName());
        }
    }
}