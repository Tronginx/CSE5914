package com.cse5914backend.imageProcess;

import com.cse5914backend.domain.Rectangle;

import java.util.List;


public interface IDraw {
    void sendImageAndCoordination(String imagePath, List<Rectangle> rectangleList);
    String getNewImage();
}
