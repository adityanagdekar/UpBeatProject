package com.example.upbeatproject;

import java.util.List;

public class BotResponse {
    String recipent_id;
    String text;
    String image;
    List<BotButton> buttons;

    public BotResponse(String recipent_id, String text, String image, List<BotButton> buttons) {
        this.recipent_id = recipent_id;
        this.text = text;
        this.image = image;
        this.buttons = buttons;
    }

    public String getRecipent_id() {
        return recipent_id;
    }

    public void setRecipent_id(String recipent_id) {
        this.recipent_id = recipent_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<BotButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<BotButton> buttons) {
        this.buttons = buttons;
    }
}
