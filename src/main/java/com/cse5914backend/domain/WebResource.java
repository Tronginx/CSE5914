package com.cse5914backend.domain;

import lombok.Data;

import java.util.List;

@Data
public class WebResource {
    String entityDescription;
    Float entityScore;
    String bestGuessLabel;
    List<Label> images;
}
