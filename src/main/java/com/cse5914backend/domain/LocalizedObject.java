package com.cse5914backend.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LocalizedObject {
    String name;
    Float confidence;
    List<Map.Entry<String, Float>>vertex;
//    int x1, y1;
//    int x2, y2;
//    int x3, y3;
//    int x4, y4;
}
