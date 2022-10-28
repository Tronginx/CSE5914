package com.cse5914backend.domain;

import lombok.Data;

import java.util.List;

@Data
public class WebResource {
    String name;
    Float confidence;
    List<Float> vertex;
}
