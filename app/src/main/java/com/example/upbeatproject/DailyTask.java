package com.example.upbeatproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "daily_task_table")
public class DailyTask {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private int priority;
    private int status;
    private String date;

    public DailyTask(){}

    public DailyTask(String description, int priority, int status, String date) {
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
