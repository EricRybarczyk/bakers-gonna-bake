package com.example.ericr.bakersgonnabake.model;

public class Step {
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription.trim();
        this.description = description.trim();
        this.videoURL = videoURL.trim();
        this.thumbnailURL = thumbnailURL.trim();
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() { return shortDescription; }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
