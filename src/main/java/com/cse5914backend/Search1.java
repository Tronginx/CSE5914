package com.cse5914backend;

import com.cse5914backend.domain.Thing;

import java.util.ArrayList;
import java.util.List;

public class Search1 implements ISearch{
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
        Thing thing2 = new Thing();
        thing2.setName("object 2");
        thing2.setX1(2);
        thing2.setX2(3);
        list.add(thing1);
        list.add(thing2);
        return list;
    }
}
