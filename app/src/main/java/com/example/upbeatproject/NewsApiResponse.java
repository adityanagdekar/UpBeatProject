package com.example.upbeatproject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsApiResponse {
    @SerializedName("articles")
    private List<LearnActivityItem> learnActivityItemList;

    public List<LearnActivityItem> getLearnActivityItemList() {
        return learnActivityItemList;
    }

    public void setLearnActivityItemList(List<LearnActivityItem> learnActivityItemList) {
        this.learnActivityItemList = learnActivityItemList;
    }
}
