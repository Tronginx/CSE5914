package com.cse5914backend.domain;

import lombok.Data;

@Data
public class Record {
    String Id;
    String location; //predicted location
    String filePath;
    String latitude, longitude;
}
