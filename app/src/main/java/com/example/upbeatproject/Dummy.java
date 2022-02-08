//package com.example.upbeatproject;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class Dummy extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {
//    private static final String MyTag = "MyTag";
//    Context context;
//    ArrayList<ChatMessage> chatMessageArrayList;
//
//    public ChatMessageAdapter(Context context, ArrayList<ChatMessage> chatMessageArrayList) {
//        this.context = context;
//        this.chatMessageArrayList = chatMessageArrayList;
//    }
//
//    @NonNull
//    @Override
//    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Trying new Layout
////        View view = LayoutInflater.from(context).inflate(R.layout.chat_msg_item,parent,false);
//
//        View view = LayoutInflater.from(context).inflate(R.layout.msglist, parent, false);
//        return new ChatMessageViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
//        ChatMessage chatMessage = chatMessageArrayList.get(position);
//        Log.d(MyTag, "msg: " + chatMessage.getChatMessage());
//        Log.d(MyTag, "image: " + chatMessage.getImage());
//        if (chatMessage.getSender() == 1) {
//            //BOT response on left side
//            if (chatMessage.getChatMessage() != null)
//                holder.leftTextView.setText(chatMessage.getChatMessage());
//            else if (chatMessage.getImage() != null)
//                holder.leftTextView.setText(chatMessage.getImage());
//
//            holder.rightTextView.setVisibility(View.GONE);
//            holder.leftTextView.setVisibility(View.VISIBLE);
//        } else if (chatMessage.getSender() == 0) {
//            //USER response on right side
//            holder.rightTextView.setText(chatMessage.getChatMessage());
//            holder.leftTextView.setVisibility(View.GONE);
//            holder.rightTextView.setVisibility(View.VISIBLE);
//        }
//        LinearLayoutManager linearLayoutManager = new
//                LinearLayoutManager(holder.botBtnRecyclerView.getContext(),
//                RecyclerView.VERTICAL,
//                false);
//        holder.botBtnRecyclerView.setLayoutManager(linearLayoutManager);
//        BotButtonAdapter botButtonAdapter = new BotButtonAdapter(chatMessage.getButtonList());
//        holder.botBtnRecyclerView.setAdapter(botButtonAdapter);
//    }
//
//    @Override
//    public int getItemCount() {
//        return chatMessageArrayList.size();
//    }
//
//    public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
//        // Trying new layout
////        LinearLayout chatMessageLayout;
//        TextView leftTextView, rightTextView;
//        RecyclerView botBtnRecyclerView;
//
//        public ChatMessageViewHolder(@NonNull View itemView) {
//            super(itemView);
//            // Trying new layout
////            chatMessageLayout = itemView.findViewById(R.id.chatMessageLayout);
//            leftTextView = itemView.findViewById(R.id.leftTextView);
//            rightTextView = itemView.findViewById(R.id.rightTextView);
//            botBtnRecyclerView = itemView.findViewById(R.id.rvBotBtnList);
//        }
//    }
//}
//
////from line 52
//// Trying new layout
////This block of code lies inside onBindViewHolder func.
//
////        if(chatMessage.getSender()==1){
////            //this for bot's message
////            FrameLayout view = getBotChatMessageLayout();
////            holder.chatMessageLayout.addView(view);
////            TextView textView = view.findViewById(R.id.tvBotMsg);
////            textView.setText(chatMessage.getChatMessage());
////        }
////        else if(chatMessage.getSender()==0){
////            //this is fro user's message
////            FrameLayout view = getUserChatMessageLayout();
////            holder.chatMessageLayout.addView(view);
////            TextView textView = view.findViewById(R.id.tvUserMsg);
////            textView.setText(chatMessage.getChatMessage());
////        }
//
////This block of code lies inside onBindViewHolder
//
//// Trying new layout
////    private FrameLayout getUserChatMessageLayout() {
////        View inflater=LayoutInflater.from(context).inflate(R.layout.user_msg_box,null);
////        return (FrameLayout) inflater;
////    }
////
////    private FrameLayout getBotChatMessageLayout() {
////        View inflater=LayoutInflater.from(context).inflate(R.layout.bot_msg_box,null);
////        return (FrameLayout) inflater;
////    }
