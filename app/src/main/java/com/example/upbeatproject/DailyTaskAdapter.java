package com.example.upbeatproject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DailyTaskAdapter extends RecyclerView.Adapter<DailyTaskAdapter.DailyTaskViewHolder> {
    private Context context;
    private DailyTaskClicked dailyTaskClickedListener;
    private List<DailyTask> dailyTaskList;

    public DailyTaskAdapter(Context context, List<DailyTask> dailyTaskList) {
        this.context = context;
        this.dailyTaskList = dailyTaskList;
    }

    @NonNull
    @Override
    public DailyTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_task_item, parent, false);
        DailyTaskViewHolder dailyTaskViewHolder = new DailyTaskViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = dailyTaskViewHolder.getAdapterPosition();
                dailyTaskClickedListener.onDailyTaskClick(dailyTaskList.get(position));
            }
        });
        return dailyTaskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTaskViewHolder holder, int position) {
        DailyTask dailyTask = dailyTaskList.get(position);
        // TODO setting the task description
        holder.tvDailyTaskDescription.setText(dailyTask.getDescription());
        // TODO setting the task date
        holder.tvDailyTaskDate.setText(dailyTask.getDate());
        // TODO using if else for setting the task status
        if (dailyTask.getStatus()==1) holder.chkBoxDailyTask.setChecked(true);
        else holder.chkBoxDailyTask.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return dailyTaskList.size();
    }

    public void setDailyTaskList(List<DailyTask> dailyTaskList) {
        Log.d("My","inside DailyTaskAdapter setDailyTaskList");
        this.dailyTaskList = dailyTaskList;
        notifyDataSetChanged();
    }

    public DailyTask getDailyTaskAt(int position) {
        return dailyTaskList.get(position);
    }

    public static class DailyTaskViewHolder extends RecyclerView.ViewHolder {
        CardView cdDailyTaskItem;
        TextView tvDailyTaskDescription,tvDailyTaskDate;
        CheckBox chkBoxDailyTask;

        public DailyTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cdDailyTaskItem = itemView.findViewById(R.id.cdDailyTaskItem);
            tvDailyTaskDescription = itemView.findViewById(R.id.tvDailyTaskDescription);
            tvDailyTaskDate = itemView.findViewById(R.id.tvDailyTaskDate);
            chkBoxDailyTask = itemView.findViewById(R.id.chkBoxDailyTask);
        }
    }

    public interface DailyTaskClicked {
        public void onDailyTaskClick (DailyTask dailyTask);
    }

    public void setDailyTaskClickedListener(DailyTaskClicked dailyTaskClickedListener) {
        this.dailyTaskClickedListener = dailyTaskClickedListener;
    }

}
