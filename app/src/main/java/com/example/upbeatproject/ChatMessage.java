package com.example.upbeatproject;

import java.util.List;

public class ChatMessage {
    private int sender;
    private String chatMessage;
    private String image;
    private List<BotButton> buttonList;

    public ChatMessage(int sender, String chatMessage, String image, List<BotButton> buttonList) {
        this.sender = sender;
        this.chatMessage = chatMessage;
        this.image = image;
        this.buttonList = buttonList;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<BotButton> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<BotButton> buttonList) {
        this.buttonList = buttonList;
    }
}
