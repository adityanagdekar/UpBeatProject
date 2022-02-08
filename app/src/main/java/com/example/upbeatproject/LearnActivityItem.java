package com.example.upbeatproject;

import com.google.gson.annotations.SerializedName;

public class LearnActivityItem {
    //Making changes
    //Adding @SerializedName("") for Retrofit
    //previous version never used @SerializedName()
    //if code breaks remove the @SerializedName("")
    //this change is added on 21/02/2021
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String author;
    @SerializedName("url")
    private String url;
    @SerializedName("urlToImage")
    private String imageUrl;

    public LearnActivityItem(String title, String author, String url, String imageUrl) {
        this.title = title;
        this.author = author;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
