package com.cse5914backend.domain;

import lombok.Data;

/**
 * DON'T MODIFY THIS BEFORE SPEAK TO Daniel
 */
@Data
public class Record {
    String location; //predicted location
    String filePath;
    String latitude, longitude;
}
