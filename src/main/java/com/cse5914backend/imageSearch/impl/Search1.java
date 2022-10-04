package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.LocalizedObject;
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
    static private List<LocalizedObject> objects = new ArrayList<>();

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

    /**
     * Detects localized objects in a remote image on Google Cloud Storage.
     *
     * @param gcsPath The path to the remote file on Google Cloud Storage to detect localized objects
     *     on.
     * @throws Exception on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void detectLocalizedObjectsGcs(String gcsPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();

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
            client.close();
            // Display the results
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }
                // TODO: Change over here
                for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
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
    public boolean sendImage(String path) {
        try {
            Search1.detectLandmarks(path);
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
    public List<LocalizedObject> getLocalizedObjects() {
        return objects;
    }
}
