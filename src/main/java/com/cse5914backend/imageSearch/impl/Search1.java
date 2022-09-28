package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.Thing;
import com.cse5914backend.imageSearch.ISearch;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search1 implements ISearch {

    static private List<Thing> things = new ArrayList<>();

//    public static void detectLandmarks() throws IOException {
//        // TODO(developer): Replace these variables before running the sample.
//        String filePath = "/Users/tron/RealTron/Bachelor/AU22/CSE5914/MagicPart/visionExample/src/main/resources/eiffel.jpeg";
//        detectLandmarks(filePath);
//    }

    // Detects landmarks in the specified local image.
    public static void detectLandmarks(String filePath) throws IOException {
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
                    things.add(tmp);
                    System.out.format("Landmark: %s%n %s%n", annotation.getDescription(), info.getLatLng());
                }
            }
        }
    }

    @Override
    public boolean sendImage(String path) {
        try {
            Search1.detectLandmarks(path);
            return true;
        } catch (IOException e) {
            System.out.println("ERROR:" + e);
            return false;
        }
    }

    @Override
    public List<Thing> getThings() {
        return things;
    }
}
