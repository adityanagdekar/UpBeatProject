package com.example.upbeatproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DailyTaskDaoInterface {
    @Insert
    void insert(DailyTask dailyTask);

    @Update
    void update(DailyTask dailyTask);

    @Delete
    void delete(DailyTask dailyTask);

    @Query("DELETE FROM daily_task_table")
    void deleteAllNotes();

    @Query("SELECT * FROM daily_task_table ORDER BY priority DESC")
    LiveData<List<DailyTask>> getAllDailyTasks();
}
