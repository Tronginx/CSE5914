package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.LocalizedObject;
import com.cse5914backend.domain.Thing;
import com.cse5914backend.imageSearch.ISearch;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.MapEntry;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Search1 implements ISearch {

    static private List<Thing> things = new ArrayList<>();
    static private List<LocalizedObject> objects = new ArrayList<>();
    static private List<Thing> pictureLandmarks = new ArrayList<>();
    static private List<LocalizedObject> pictureDetails = new ArrayList<>();

//    public static void detectLandmarks() throws IOException {
//        // TODO(developer): Replace these variables before running the sample.
//        String filePath = "/Users/tron/RealTron/Bachelor/AU22/CSE5914/MagicPart/visionExample/src/main/resources/eiffel.jpeg";
//        detectLandmarks(filePath);
//    }

    // Detects landmarks in the specified local image.
    public static void detectLandmarks(String filePath) throws IOException {
        pictureLandmarks.clear();
        List<AnnotateImageRequest> requests = new ArrayList<>();
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LANDMARK_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLandmarkAnnotationsList()) {
                    LocationInfo info = annotation.getLocationsList().listIterator().next();
                    Thing tmp = new Thing();
                    tmp.setLocations(new ArrayList<>());
                    tmp.setName(annotation.getDescription());
                    tmp.getLocations().add(info.getLatLng().toString());
                    pictureLandmarks.add(tmp);
                    things.add(tmp);
                    System.out.format("Landmark: %s%n %s%n", annotation.getDescription(), info.getLatLng());
                }
            }
        }
    }

    /**
     * Detects localized objects in the specified local image.
     *
     * @param filePath The path to the file to perform localized object detection on.
     * @throws Exception on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void detectLocalizedObjects(String filePath) throws IOException {
        pictureDetails.clear();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION))
                        .setImage(img)
                        .build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            // Perform the request
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            // Display the results
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
                    LocalizedObject tmp = new LocalizedObject();
                    tmp.setName(entity.getName());
                    tmp.setConfidence(entity.getScore());
                    tmp.setVertex(new ArrayList<>());
                    for (NormalizedVertex v : entity.getBoundingPoly().getNormalizedVerticesList()) {
                        tmp.getVertex().add(v.getX());
                        tmp.getVertex().add(v.getY());
                    }
                    pictureDetails.add(tmp);
                    objects.add(tmp);
                    System.out.format("Object name: %s%n", entity.getName());
                    System.out.format("Confidence: %s%n", entity.getScore());
                    System.out.format("Normalized Vertices:%n");
                    entity
                            .getBoundingPoly()
                            .getNormalizedVerticesList()
                            .forEach(vertex -> System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY()));
                }
            }
        }
    }

    @Override
    public boolean sendImage1(String path) {
        try {
            Search1.detectLandmarks(path);
            return true;
        } catch (IOException e) {
            System.out.println("ERROR:" + e);
            return false;
        }
    }

    @Override
    public boolean sendImage2(String path) {
        try {
            Search1.detectLocalizedObjects(path);
            return true;
        } catch (IOException e) {
            System.out.println("ERROR:" + e);
            return false;
        }
    }

    @Override
    public List<Thing> getThings() {
        return pictureLandmarks;
    }
    public List<LocalizedObject> getLocalizedObjects() {
        return pictureDetails;
    }

    @Override
    public List<Thing> getHistory() { return pictureLandmarks; }
}
