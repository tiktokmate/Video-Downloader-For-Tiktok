package com.leetlab.videodownloaderfortiktok.model;

public class IntroModel {
    private int image;
    private String title;
    private String description;
    private int demo;

    public IntroModel(int image, String title, String description, int demo) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.demo = demo;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}