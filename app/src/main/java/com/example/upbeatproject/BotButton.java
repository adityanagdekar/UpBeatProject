package com.example.upbeatproject;

public class BotButton {
    private String payload;
    private String title;

    public BotButton(String payload, String title) {
        this.payload = payload;
        this.title = title;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
