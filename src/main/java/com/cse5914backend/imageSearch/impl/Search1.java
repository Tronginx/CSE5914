package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.*;
import com.cse5914backend.imageSearch.ISearch;
import com.google.cloud.ServiceOptions;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntry;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.WebDetection;
import com.google.cloud.vision.v1.WebDetection.WebEntity;
import com.google.cloud.vision.v1.WebDetection.WebImage;
import com.google.cloud.vision.v1.WebDetection.WebLabel;
import com.google.cloud.vision.v1.WebDetection.WebPage;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Search1 implements ISearch {

    static private List<Thing> things = new ArrayList<>();
    static private List<LocalizedObject> objects = new ArrayList<>();
    static private List<Thing> pictureLandmarks = new ArrayList<>();
    static private List<LocalizedObject> pictureDetails = new ArrayList<>();
    static private List<Text> texts = new ArrayList<>();
    static private List<Text> pictureTexts = new ArrayList<>();
    static private String projectId = ServiceOptions.getDefaultProjectId();
    static private String targetLanguage = "en";
    static private List<String> translations = new ArrayList<>();
    static private List<Label> labels = new ArrayList<>();
    static private List<WebResource> resources = new ArrayList<>();

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
                    double lat = info.getLatLng().getLatitude();
                    double lng = info.getLatLng().getLongitude();
                    tmp.setLatitude(lat);
                    tmp.setLongitude(lng);
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

    // Detects text in the specified image.
    public static void detectText(String filePath) throws IOException {
        pictureTexts.clear();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
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
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    Text tmp = new Text();
                    tmp.setDescription(annotation.getDescription());
                    tmp.setVertex(new ArrayList<>());
                    for (Vertex v: annotation.getBoundingPoly().getVerticesList()){
                        tmp.getVertex().add(v.getX());
                        tmp.getVertex().add(v.getY());
                    }
                    pictureTexts.add(tmp);
                    texts.add(tmp);
                    System.out.format("Text: %s%n", annotation.getDescription());
                    System.out.format("Position : %s%n", annotation.getBoundingPoly());
                }
            }
        }
    }

//     Translate text to target language.
    public static void translateText(String projectId, String targetLanguage, String text)
            throws IOException {

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // Supported Locations: `global`, [glossary location], or [model location]
            // Glossaries must be hosted in `us-central1`
            // Custom Models must use the same location as your model. (us-central1)
            LocationName parent = LocationName.of(projectId, "global");

            // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
            TranslateTextRequest request =
                    TranslateTextRequest.newBuilder()
                            .setParent(parent.toString())
                            .setMimeType("text/plain")
                            .setTargetLanguageCode(targetLanguage)
                            .addContents(text)
                            .build();

            TranslateTextResponse response = client.translateText(request);

            // Display the translation for each input text provided
            for (Translation translation : response.getTranslationsList()) {
                translations.add(translation.getTranslatedText());
                System.out.printf("Translated text: %s\n", translation.getTranslatedText());
            }
        }
    }

    // Detects labels in the specified local image.
    public static void detectLabels(String filePath) throws IOException {
        labels.clear();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
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
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    Label tmp = new Label();
                    String tmpDescribe = "google.cloud.vision.v1.EntityAnnotation.description";
                    String tmpScore = "google.cloud.vision.v1.EntityAnnotation.score";
                    for (Map.Entry<Descriptors.FieldDescriptor, Object> ele: annotation.getAllFields().entrySet()){
                        Descriptors.FieldDescriptor key = ele.getKey();
                        Object val = ele.getValue();
                        if (key.toString().equals(tmpDescribe)){
                            tmp.setDescription(val.toString());
                        }
                        if (key.toString().equals(tmpScore)){
                            tmp.setScore(Float.parseFloat(val.toString()));
                        }
                    }
                    labels.add(tmp);
                    annotation
                            .getAllFields()
                            .forEach((k, v) -> System.out.format("%s : %s%n", k, v.toString()));
                }
            }
        }
    }

    // Finds references to the specified image on the web.
    public static void detectWebDetections(String filePath) throws IOException {
        resources.clear();
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.WEB_DETECTION).build();
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

                // Search the web for usages of the image. You could use these signals later
                // for user input moderation or linking external references.
                // For a full list of available annotations, see http://g.co/cloud/vision/docs
                WebDetection annotation = res.getWebDetection();
                WebResource tmp = new WebResource();
                List<WebEntity> webEntities = annotation.getWebEntitiesList();
                if (webEntities.size() > 0){
                    tmp.setEntityDescription(webEntities.get(0).getDescription());
                    tmp.setEntityScore(webEntities.get(0).getScore());
                }
                for (WebEntity entity : annotation.getWebEntitiesList()) {
                    System.out.println(
                            entity.getDescription() + " : " + entity.getEntityId() + " : " + entity.getScore());
                }

                List<WebLabel> webLabels = annotation.getBestGuessLabelsList();
                if (webLabels.size() > 0){
                    tmp.setBestGuessLabel(webLabels.get(0).getLabel());
                }
                for (WebLabel label : annotation.getBestGuessLabelsList()) {
                    System.out.format("%nBest guess label: %s", label.getLabel());
                }

                List<Label> imgs = new ArrayList<>();
                List<WebImage> webFullyImage = annotation.getFullMatchingImagesList();
                int fullySize = 3;
                for (int i = 0; i < Math.min(fullySize, webFullyImage.size()); i++){
                    WebImage current = webFullyImage.get(i);
                    Label label = new Label();
                    label.setDescription(current.getUrl());
                    label.setScore(current.getScore());
                    imgs.add(label);
                }
                System.out.println("%nPages with fully matching images: Score%n==");
                for (WebImage image : annotation.getFullMatchingImagesList()) {
                    System.out.println(image.getUrl() + " : " + image.getScore());
                }

                List<WebImage> webPartialImage = annotation.getPartialMatchingImagesList();
                int partialSize = 3;
                for (int i = 0; i < Math.min(partialSize, webPartialImage.size()); i++){
                    WebImage current = webPartialImage.get(i);
                    Label label = new Label();
                    label.setDescription(current.getUrl());
                    label.setScore(current.getScore());
                    imgs.add(label);
                }
                System.out.println("%nPages with partially matching images: Score%n==");
                for (WebImage image : annotation.getPartialMatchingImagesList()) {
                    System.out.println(image.getUrl() + " : " + image.getScore());
                }

                List<WebImage> webSimilarImage = annotation.getVisuallySimilarImagesList();
                int visualSize = 3;
                for (int i = 0; i < Math.min(visualSize, webSimilarImage.size()); i++){
                    WebImage current = webSimilarImage.get(i);
                    Label label = new Label();
                    label.setDescription(current.getUrl());
                    label.setScore(current.getScore());
                    imgs.add(label);
                }
                System.out.println("%nPages with visually similar images: Score%n==");
                for (WebImage image : annotation.getVisuallySimilarImagesList()) {
                    System.out.println(image.getUrl() + " : " + image.getScore());
                }
                tmp.setImages(imgs);
                resources.add(tmp);
            }
        }
    }

    @Override
    public boolean sendImage(String path) {
        try {
            translations.clear();
            Search1.detectLandmarks(path);
            Search1.detectLocalizedObjects(path);
            Search1.detectText(path);
            for (Text t : pictureTexts){
                Search1.translateText(projectId, targetLanguage, t.getDescription());
            }
            Search1.detectLabels(path);
            Search1.detectWebDetections(path);
            return true;
        } catch (IOException e) {
            System.out.println("ERROR:" + e);
            return false;
        }
    }


    @Override
    public List<Thing> getThings() {
        List<Thing> res = pictureLandmarks;
        pictureLandmarks = new ArrayList<>();
        return res;
    }
    @Override
    public List<LocalizedObject> getLocalizedObjects() {
        return pictureDetails;
    }

    @Override
    public List<Text> getTexts(){
        return pictureTexts;
    }

    @Override
    public List<String> getTranslations(){
        return translations;
    }

    @Override
    public List<Label> getLabels(){
        return labels;
    }

    @Override
    public List<WebResource> getResources(){
        return resources;
    }


}
