package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.Thing;
import com.cse5914backend.imageSearch.ISearch;

import java.util.ArrayList;
import java.util.List;

public class Search2 implements ISearch {
    @Override
    public boolean sendImage(String path) {
        return true;
    }

    @Override
    public List<Thing> getThings() {
        List<Thing> list = new ArrayList<>();
        Thing thing1 = new Thing();
        thing1.setName("object 1");
        thing1.setX1(0);
        thing1.setX2(1);
        thing1.setLocations(new ArrayList<>());
        thing1.getLocations().add("Tower1");
        Thing thing2 = new Thing();
        thing2.setName("object 2");
        thing2.setX1(2);
        thing2.setX2(3);
        thing2.setLocations(new ArrayList<>());
        thing2.getLocations().add("Hall1");
        list.add(thing1);
        list.add(thing2);
        return list;
    }
}
