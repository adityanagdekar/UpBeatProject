package com.example.upbeatproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {
    public static final String MyTag = "MyTag";
    Context context;
    List<ChatMessage> chatMessageList;
    BotButtonAdapter.BotButtonClicked botButtonClickedListener;

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessageList, BotButtonAdapter.BotButtonClicked botButtonClickedListener) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        this.botButtonClickedListener=botButtonClickedListener;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msglist, parent, false);
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessageList.get(position);

        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(holder.botBtnRecyclerView.getContext(),
                RecyclerView.HORIZONTAL,
                false);
        holder.botBtnRecyclerView.setLayoutManager(linearLayoutManager);
        List<BotButton> botButtonList = new ArrayList<>();
        BotButtonAdapter botButtonAdapter = new BotButtonAdapter(context,botButtonList,botButtonClickedListener);
        holder.botBtnRecyclerView.setAdapter(botButtonAdapter);
        if (chatMessage.getButtonList() == null)
            Log.d(MyTag, "null list");
        else {
            holder.rightTextView.setVisibility(View.GONE);
            holder.leftTextView.setVisibility(View.GONE);

            for (BotButton botButton :
                    chatMessage.getButtonList()) {
                botButtonList.add(botButton);
                botButtonAdapter.notifyItemInserted(botButtonList.size() - 1);
            }
        }

        if (chatMessage.getSender() == 1) {
            //BOT response on left side
            if (chatMessage.getChatMessage() != null && !chatMessage.getChatMessage().trim().isEmpty() ) {
                //This for message from Bot's side
                holder.leftTextView.setText(chatMessage.getChatMessage());
                holder.leftTextView.setVisibility(View.VISIBLE);
                holder.rightTextView.setVisibility(View.GONE);
            } else if ( chatMessage.getImage() != null && !chatMessage.getImage().trim().isEmpty() ) {
                //This for image from Bot's side
                holder.leftTextView.setText(chatMessage.getImage());
                holder.leftTextView.setVisibility(View.VISIBLE);
                holder.rightTextView.setVisibility(View.GONE);
            } else if ( (chatMessage.getChatMessage() == null && chatMessage.getImage() == null) ) {
                Log.d(MyTag,"inside else");
                holder.rightTextView.setVisibility(View.GONE);
                holder.leftTextView.setVisibility(View.GONE);
            }
        } else if (chatMessage.getSender() == 0) {
            //USER response on left side
            holder.rightTextView.setText(chatMessage.getChatMessage());
            holder.rightTextView.setVisibility(View.VISIBLE);
            holder.leftTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView leftTextView, rightTextView;
        RecyclerView botBtnRecyclerView;
//        ImageView botResponseImageView;
        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftTextView = itemView.findViewById(R.id.leftTextView);
            rightTextView = itemView.findViewById(R.id.rightTextView);
            botBtnRecyclerView = itemView.findViewById(R.id.rvBotBtnList);
//            botResponseImageView = itemView.findViewById(R.id.botResponseImageView);
        }
    }
}
