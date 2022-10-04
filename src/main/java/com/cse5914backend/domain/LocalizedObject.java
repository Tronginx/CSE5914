package com.cse5914backend.domain;

import lombok.Data;

import java.util.List;

@Data
public class LocalizedObject {
    String name;
    List<String> info;
    int x1, y1;
    int x2, y2;
    int x3, y3;
    int x4, y4;
}
