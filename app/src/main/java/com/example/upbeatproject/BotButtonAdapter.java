package com.example.upbeatproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BotButtonAdapter extends RecyclerView.Adapter<BotButtonAdapter.BotButtonViewHolder> {
    public static final String MyTag="MyTag";
    private BotButtonClicked botButtonClickedListener;
    private Context context;
    private List<BotButton> buttonList;

    public BotButtonAdapter(Context context,List<BotButton> buttonList,BotButtonClicked botButtonClickedListener) {
        this.context=context;
        this.buttonList = buttonList;
        this.botButtonClickedListener=botButtonClickedListener;
    }

    @NonNull
    @Override
    public BotButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_btn_list_item,parent,false);
        BotButtonViewHolder botButtonViewHolder = new BotButtonViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = botButtonViewHolder.getAdapterPosition();
                Log.d(MyTag,"position: "+position);
                botButtonViewHolder.payLoadBtn.setBackgroundResource(R.drawable.bot_button_item_list_clicked);
                botButtonViewHolder.payLoadBtn.setTextColor(Color.BLACK);
                botButtonClickedListener.onBotButtonClick(buttonList.get(position));
            }
        });
        return botButtonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BotButtonViewHolder holder, int position) {
        BotButton botButton=buttonList.get(position);
        holder.payLoadBtn.setText(botButton.getTitle());
    }

    @Override
    public int getItemCount() {
        return buttonList.size();
    }

    public class BotButtonViewHolder extends RecyclerView.ViewHolder {
        TextView payLoadBtn;
        public BotButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            payLoadBtn=itemView.findViewById(R.id.payLoadBtn);
        }
    }

    interface BotButtonClicked{
        public void onBotButtonClick(BotButton botButton);
    }

    public void setBotButtonClickedListener(BotButtonClicked botButtonClickedListener){
        this.botButtonClickedListener=botButtonClickedListener;
    }
}
