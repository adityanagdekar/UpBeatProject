package com.example.upbeatproject;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class LearnActivityAdapter extends RecyclerView.Adapter<LearnActivityAdapter.LearnActivityViewHolder> {
    private ArrayList<LearnActivityItem> learnActivityList=new ArrayList<>();
    LearnActivityClicked learnActivityClickedlistener;
    public static final String MyTag="MyTag";
    @NonNull
    @Override
    public LearnActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_learn_activity,parent,false);
        LearnActivityAdapter.LearnActivityViewHolder viewHolder = new LearnActivityViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (learnActivityClickedlistener!=null && position!=RecyclerView.NO_POSITION)
                    learnActivityClickedlistener.onLearnActivityClick(learnActivityList.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LearnActivityViewHolder holder, int position) {
        LearnActivityItem learnActivityItem=learnActivityList.get(position);
        Log.d(MyTag,learnActivityItem.getTitle());
        holder.titleTextView.setText(learnActivityItem.getTitle());
        holder.authorTextView.setText("by "+learnActivityItem.getAuthor());
        holder.urlTextView.setText(learnActivityItem.getUrl());

        Glide.with(holder.imageView)
                .load(learnActivityItem.getImageUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return learnActivityList.size();
    }

    public void setLearnActivityList(ArrayList<LearnActivityItem> list){
        learnActivityList.clear();
        learnActivityList.addAll(list);
        notifyDataSetChanged();
    }

    public static class LearnActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView,authorTextView,urlTextView,imageurlTextView;
        private ImageView imageView;

        public LearnActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView =itemView.findViewById(R.id.title);
            authorTextView=itemView.findViewById(R.id.author);
            urlTextView=itemView.findViewById(R.id.url);
            imageView=itemView.findViewById(R.id.newsImage);
//            imageurlTextView=itemView.findViewById(R.id.imageurl);
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position=getAdapterPosition();
//                        if (learnActivityClickedlistener!=null && position!=RecyclerView.NO_POSITION)
//                            learnActivityClickedlistener.onLearnActivityClick(learnActivityList.get(position));
//                    }
//                });
        }
    }

    interface LearnActivityClicked{
        public void onLearnActivityClick(LearnActivityItem learnActivityItem);
    }

    public void setLearnActivityListener(LearnActivityClicked learnActivityClickedlistener){
        this.learnActivityClickedlistener=learnActivityClickedlistener;
    }
}

