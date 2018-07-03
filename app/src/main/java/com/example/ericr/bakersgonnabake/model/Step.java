package com.example.ericr.bakersgonnabake.model;

public class Step {
    private int id;
    private String shortDescription;
    private String description;
    private String videoSource;
    private String thumbnameImage;

    public Step(int id, String shortDescription, String description, String videoSource, String thumbnameImage) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoSource = videoSource;
        this.thumbnameImage = thumbnameImage;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() { return shortDescription; }

    public String getDescription() {
        return description;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public String getThumbnameImage() {
        return thumbnameImage;
    }
}
