package com.cse5914backend.imageSearch.impl;

import com.cse5914backend.domain.Label;
import com.cse5914backend.domain.Text;
import com.cse5914backend.domain.WebResource;
import com.cse5914backend.imageSearch.IExtractWord;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractWord1 implements IExtractWord {

    static private String bestGuesses = "";

    public static String extractInfo(String filePath) throws IOException {
        String output = "";
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.WEB_DETECTION).build();
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
                    return null;
                }

                // Search the web for usages of the image. You could use these signals later
                // for user input moderation or linking external references.
                // For a full list of available annotations, see http://g.co/cloud/vision/docs
                WebDetection annotation = res.getWebDetection();

                List<WebDetection.WebLabel> webLabels = annotation.getBestGuessLabelsList();
                if (webLabels.size() > 0){
                    output = webLabels.get(0).getLabel();
                }
                for (WebDetection.WebLabel label : annotation.getBestGuessLabelsList()) {
                    System.out.format("%nBest guess label: %s", label.getLabel());
                }
            }
        }
        return output;
    }

    @Override
    public boolean sendImage(String path) {
        try {
            bestGuesses = ExtractWord1.extractInfo(path);
            return true;
        } catch (IOException e) {
            System.out.println("ERROR:" + e);
            return false;
        }
    }

    @Override
    public String getBestGuess() {
        return bestGuesses;
    }
}
