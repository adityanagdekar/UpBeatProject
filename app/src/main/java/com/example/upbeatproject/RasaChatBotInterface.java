package com.example.upbeatproject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RasaChatBotInterface {

    @POST("webhook")
    Call<List<BotResponse>> sendGetMessages(@Body MessageSender messageSender);
}
