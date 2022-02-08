package com.example.upbeatproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MeditateModelAdapter extends RecyclerView.Adapter<MeditateModelAdapter.MeditateModelViewHolder>{
    Context context;
    ArrayList<MeditateActivityItem> meditateActivityItemArrayList;
    meditateModelClicked meditateModelClickedListener;

    public MeditateModelAdapter(Context context, ArrayList<MeditateActivityItem> meditateActivityItemArrayList) {
        this.context = context;
        this.meditateActivityItemArrayList = meditateActivityItemArrayList;
    }

    @NonNull
    @Override
    public MeditateModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meditate_activity,parent,false);
        MeditateModelViewHolder viewHolder = new MeditateModelViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (meditateModelClickedListener!=null && position!=RecyclerView.NO_POSITION)
                    meditateModelClickedListener.onMeditateModelClick(meditateActivityItemArrayList.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MeditateModelViewHolder holder, int position) {
        MeditateActivityItem meditateActivityItem = meditateActivityItemArrayList.get(position);
        holder.textView.setText(meditateActivityItem.getTitle());
        holder.imageView.setImageResource(meditateActivityItem.getImageId());
    }

    @Override
    public int getItemCount() {
        return meditateActivityItemArrayList.size();
    }

    public static class MeditateModelViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MeditateModelViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.ivImage);
            textView=itemView.findViewById(R.id.tvTitle);
        }
    }

//     This interface is created tohandle click events
//    Future plan is to add play/pause animation to play/pause music
    interface meditateModelClicked {
        public void onMeditateModelClick(MeditateActivityItem meditateActivityItem);
    }

    public void setMediateModelListener(meditateModelClicked listener){
        meditateModelClickedListener = listener;
    }

}
