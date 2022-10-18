package com.cse5914backend.domain;

import lombok.Data;

import java.util.List;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
@Data
public class Thing {
    String name;
    //rectangle coordination
    // x, y
    List<String> locations; //list.get(0) is longitude, (1)is latitude
    double latitude, longitude;
    int x1, y1;
    int x2, y2;
    int x3, y3;
    int x4, y4;
}
