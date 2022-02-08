package com.example.upbeatproject;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DailyTaskViewModel extends ViewModel {
    private DailyTaskRepository dailyTaskRepository;
    private LiveData<List<DailyTask>> dailyTasks;

    //firebase variables
    private LiveData<List<DailyTask>> items;
    public void init(Context context) {

        if (items != null) {
            return;
        }
        dailyTaskRepository = DailyTaskRepository.getInstance();
        items = dailyTaskRepository.loadFromFireBase();

    }

//    public DailyTaskViewModel(@NonNull Application application) {
////        super(application);
//        dailyTaskRepository = new DailyTaskRepository(application);
//        dailyTasks = dailyTaskRepository.getAllDailyTasks();
//    }
//
//    public LiveData<List<DailyTask>> getDailyTasks() {
//        return dailyTasks;
//    }
//
//    public LiveData<List<DailyTask>> getDailyTasksFromFireBase() {
//        MutableLiveData<List<DailyTask>> fireBaseData = new MutableLiveData<>();
//        fireBaseData.setValue(dailyTaskRepository.getDailyTasksFromFireBase().getValue());
//        return fireBaseData;
//    }
//
//    public void insert(DailyTask dailyTask) {
//        dailyTaskRepository.insertDailyTask(dailyTask);
//    }
//
//    public void update(DailyTask dailyTask) {
//        dailyTaskRepository.updateDailyTask(dailyTask);
//    }
//
//    public void delete(DailyTask dailyTask) {
//        dailyTaskRepository.deleteDailyTask(dailyTask);
//    }
//
//    public void deleteAllDailyTask() {
//        dailyTaskRepository.deleteAllDailyTask();
//    }
//
//    public void insertDailyTaskFirebase(DailyTask dailyTask) {
//        dailyTaskRepository.insertDailyTaskFirebase(dailyTask);
//    }

    // ok now here on there will be only firebase methods
    // all crud operations will be done on firebase

    public LiveData<List<DailyTask>> loadFromFireBase(){
        return items;
    }

    public void addToFireBase(DailyTask dailyTask, int counter){
        Log.d("My","inside addToFireBase");
        dailyTaskRepository.addToFireBase(dailyTask,counter);
    }

    public void updateToFireBase(DailyTask dailyTask){
        Log.d("My","inside updateToFireBase");
        dailyTaskRepository.updateToFireBase(dailyTask);
    }

    public void deleteFromFireBase(DailyTask dailyTask, int position){
        Log.d("My","inside deleteFromFireBase");
        dailyTaskRepository.deleteFromFireBase(dailyTask,position);
    }

    public void restoreToFireBase(DailyTask dailyTask, int position){
        Log.d("My","inside restoreToFireBase");
        dailyTaskRepository.restoreToFireBase(dailyTask,position);
    }
}
