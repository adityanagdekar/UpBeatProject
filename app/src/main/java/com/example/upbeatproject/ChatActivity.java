package com.example.upbeatproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    private int USER = 0;
    private int BOT = 1;
    private FloatingActionButton fabSend;
    private EditText etMessage;
    private RecyclerView recyclerView;
    private List<ChatMessage> chatMessageArrayList;
    private ChatMessageAdapter chatMessageAdapter;
    private static final String MyTag = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatMessageArrayList = new ArrayList<ChatMessage>();

        recyclerView = findViewById(R.id.rvMessageList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setHasFixedSize(true);

        // Instantiating ChatMessageAdapter
        // And most important implementing BotButtonAdapter.BotButtonClicked() INTERFACE
        // Here the INTERFACE'S onClick() is overridden by our CUSTOM LOGIC
        // our goal here is to send the BotButton object's  payLoad to the sendMessage(message,flag)
        chatMessageAdapter = new ChatMessageAdapter(this, chatMessageArrayList, new BotButtonAdapter.BotButtonClicked() {
            @Override
            public void onBotButtonClick(BotButton botButton) {
                Log.d(MyTag, "Clicked: " + botButton.getTitle());

//                if(!botButton.getTitle().toLowerCase().equals("bye")) {
//                    Toast.makeText(ChatActivity.this, "Taking you to Learn Section ", Toast.LENGTH_SHORT).show();
//                }
                if (botButton.getTitle().toLowerCase().equals("yes")) {
                    Toast.makeText(ChatActivity.this, "Taking you to Learn Section ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChatActivity.this, LearnActivity.class);
                    startActivity(intent);
                }
                if(botButton.getTitle().toLowerCase().equals("meditate")){
                    Toast.makeText(ChatActivity.this, "Taking you to Meditate Section", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChatActivity.this, MeditateActivity.class);
                    startActivity(intent);
                }
                sendMessage(botButton.getPayload(), 1);
            }
        });
        recyclerView.setAdapter(chatMessageAdapter);

        etMessage = findViewById(R.id.etMessage);
        fabSend = findViewById(R.id.fabSend);
        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().toString().trim().isEmpty())
                    Toast.makeText(ChatActivity.this, "Empty Message", Toast.LENGTH_SHORT).show();
                else {
                    sendMessage(etMessage.getText().toString().trim(), 0);
                    chatMessageAdapter.notifyItemInserted(chatMessageArrayList.size() - 1);
//                    chatMessageAdapter.notifyDataSetChanged();
                }
                etMessage.setText("");
                etMessage.requestFocus();
            }
        });

    }

    public void sendMessage(String message, int flag) {
        //Sending user message
        // Here the LOGIC OF FLAG is
        // if it is 0 then it is explicitly sent by the USER
        // And if it is 1 then it is implicitly sent by the USER
        // In 2nd case we just don't want a separate chat message from USER'S SIDE
        // And in former case we want a separate chat message from USER'S SIDE
        ChatMessage chatMessage = new ChatMessage(USER, message, null, null);
        if (flag == 0)
            chatMessageArrayList.add(chatMessage);
        else
            Log.d(MyTag, "message:" + message);
//        ChatMessage chatMessage1=new ChatMessage(BOT,message);
//        chatMessageArrayList.add(chatMessage1);

        //Added retrofit and gson below this comment is the newly added code
        //Getting bot response
        // baseUrl => "https://8c3b16900cb9.ngrok.io/webhooks/rest/"
        // this baseUrl keeps changing
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("here your ngrok url will come")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RasaChatBotInterface rasaChatBotInterface = retrofit.create(RasaChatBotInterface.class);
        MessageSender messageSender = new MessageSender("USER", message);
        Call<List<BotResponse>> call = rasaChatBotInterface.sendGetMessages(messageSender);
        call.enqueue(new Callback<List<BotResponse>>() {
            @Override
            public void onResponse(Call<List<BotResponse>> call, Response<List<BotResponse>> response) {
                if (response.isSuccessful()) {
                    Log.d(MyTag, "Inside onResponse");

                    List<BotResponse> botResponseList = response.body();
                    Log.d(MyTag, "botResponseList.size(): " + botResponseList.size());

                    if (botResponseList.size() >= 1) {
                        for (BotResponse botResponse : botResponseList) {
                            Log.d(MyTag, "text: " + botResponse.getText());
                            Log.d(MyTag, "image: " + botResponse.getImage());

                            chatMessageArrayList.add(new ChatMessage(BOT, botResponse.getText(), botResponse.getImage(), botResponse.getButtons()));
                            chatMessageAdapter.notifyItemInserted(chatMessageArrayList.size() - 1);

//                            }
                        }
                        chatMessageAdapter.notifyDataSetChanged();
                    } else if (botResponseList.size() == 0) {
                        Log.d(MyTag, "inside else if(botResponseList.size()==0)");
//                        List<BotButton> dummyList = new ArrayList<>();
//                        dummyList.add(new BotButton("affirm", "yes"));
//                        dummyList.add(new BotButton("deny", "no"));
//                        dummyList.add(new BotButton("bye", "bye"));
                        chatMessageArrayList.add(new ChatMessage(BOT, "Sorry getting back to you",
                                null, null));
                        chatMessageAdapter.notifyItemInserted(chatMessageArrayList.size() - 1);
                    }
                    recyclerView.smoothScrollToPosition(chatMessageArrayList.size()-1);
                } else {
                    String error = "Response Code: " + response.code() + "\n" +
                            "response.message(): " + response.message() + "\n\n";
                    Log.d(MyTag, "error: " + error);

                    /*Taking Response from dummy_bot_response.json file
                    String jsonString=null;
                    InputStream inputStream=getResources().openRawResource(R.raw.dummy_bot_response);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte buf[] = new byte[1024];
                    int len;
                    try {
                        while ((len = inputStream.read(buf)) != -1) {
                            byteArrayOutputStream.write(buf, 0, len);
                        }
                        byteArrayOutputStream.close();
                        inputStream.close();
                        jsonString = byteArrayOutputStream.toString();
                    } catch (IOException e) {
                        jsonString = e.getMessage();
                    }

                    Gson gson=new Gson();
                    But everything worked fine*/


                    List<BotButton> dummyList = new ArrayList<>();
                    dummyList.add(new BotButton("affirm", "yes"));
                    dummyList.add(new BotButton("deny", "meditate"));
                    Log.d(MyTag, "dummyList.size():" + dummyList.size());
                    chatMessageArrayList.add(new ChatMessage(BOT, error, null, dummyList));
                    chatMessageAdapter.notifyItemInserted(chatMessageArrayList.size() - 1);
                    recyclerView.smoothScrollToPosition(chatMessageArrayList.size()-1);
                }
            }

            @Override
            public void onFailure(Call<List<BotResponse>> call, Throwable t) {
                Log.d(MyTag, "onFailure");
                String error = "Error: " + t.getMessage() + "\n\n";
                Log.d(MyTag, "error: " + error);
                List<BotButton> dummyList = new ArrayList<>();
                dummyList.add(new BotButton("affirm", "yes"));
                dummyList.add(new BotButton("deny", "no"));
                Log.d(MyTag, "dummyList.size():" + dummyList.size());
                chatMessageArrayList.add(new ChatMessage(BOT, error, null, dummyList));
                recyclerView.smoothScrollToPosition(chatMessageArrayList.size()-1);
            }
        });
    }


}
// Line 95
//                    if(botResponseList.size()==1) {
//                        Log.d(MyTag,"imageurl "+botResponseList.get(0).getImageurl());
//                        chatMessageArrayList.add(new ChatMessage(BOT, botResponseList.get(0).text));
////                        chatMessageAdapter.notifyItemInserted(chatMessageArrayList.size()-1);
//                        chatMessageAdapter.notifyDataSetChanged();
//                    }

// Line 98
//                            if(botResponse.getImageurl().length()>0) {
//                                chatMessageArrayList.add(new ChatMessage(BOT, botResponse.imageurl));
////                                chatMessageAdapter.notifyItemInserted(chatMessageArrayList.size()-1);
//                            }
//                            if (!botResponse.getText().isEmpty()) {

// Line 100
//                                if(botResponse.getImageurl()!=null)
//                                    chatMessageArrayList.add(new ChatMessage(BOT, botResponse.imageurl.toString()));
