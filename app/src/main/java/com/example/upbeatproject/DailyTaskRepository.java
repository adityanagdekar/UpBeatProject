package com.example.upbeatproject;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DailyTaskRepository {
    private DailyTaskDaoInterface dailyTaskDao;
    private LiveData<List<DailyTask>> dailyTasks;
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mReference = mFirebaseDatabase.getReference("user");
    private Context context;
    private MediatorLiveData<List<DailyTask>> mediatorLiveData = new MediatorLiveData<>();

    private MutableLiveData<List<DailyTask>> items = new MutableLiveData<>();
    private List<DailyTask> fireBaseItemList = new ArrayList<>();
    public static DailyTaskRepository instance;

    public DailyTaskRepository() {

    }

    public static DailyTaskRepository getInstance() {
        if (instance == null) {
            instance = new DailyTaskRepository();
        }
        return instance;
    }

//    public DailyTaskRepository(Application application) {
//        this.context = application.getApplicationContext();
//        DailyTaskDatabase dailyTaskDatabase = DailyTaskDatabase.getInstance(application);
//        dailyTaskDao = dailyTaskDatabase.getDailyTaskDao();
//        dailyTasks = dailyTaskDao.getAllDailyTasks();
//    }

//    public LiveData<List<DailyTask>> getAllDailyTasks() {
//
//        mediatorLiveData.addSource(dailyTasks, new Observer<List<DailyTask>>() {
//            @Override
//            public void onChanged(List<DailyTask> dailyTaskList) {
//                if (dailyTaskList == null || dailyTaskList.isEmpty()) {
//                    mReference = mFirebaseDatabase.getReference("user");
//                    mReference = mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    List<DailyTask> fireBaseItemList = new ArrayList<>();
//
//                    mReference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Log.d("My", "inside getDailyTasksFromFireBase addChildValueEventListener onChildAdded");
////                fireBaseItemList.clear();
//                            if (snapshot != null) {
//                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    Log.d("My", "dataSnapshot.key: " + dataSnapshot.getKey());
//                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                        Log.d("My", "data.key: " + data.getKey());
//                                        HashMap<String, Object> hm = (HashMap<String, Object>) data.getValue();
//
//                                        // here the object that i'm getting is in the form of hashmap
//                                        // so i have retrieved it in a hashmap
//                                        // then i have extracted individual KV pair
//                                        // and then i have passed it into DailyTask constructor
//                                        // MAIN REASON FOR THIS IS:
//                                        // because i have id stored in firebase but that ids are causing conflicts
//                                        // in the local database
//
//                                        String description = (String) hm.get("description");
//                                        int priority = Integer.parseInt(hm.get("priority").toString());
//                                        int status = Integer.parseInt(hm.get("status").toString());
//                                        String date = (String) hm.get("date");
//
//                                        DailyTask dailyTask = new DailyTask(description, priority, status, date);
//                                        fireBaseItemList.add(dailyTask);
//                                    }
//                                }
//
//                            }
//                            for (DailyTask fireBaseDailyTask : fireBaseItemList) {
//                                Log.d("My", "now firing the insert query");
//                                insertDailyTask(fireBaseDailyTask);
//                                Log.d("My", "after insertDailyTask");
//                            }
//                            mediatorLiveData.setValue(fireBaseItemList);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.d("My", "inside getDailyTasksFromFireBase addChildValueEventListener onCancelled");
//                        }
//                    });
//                } else {
//                    mediatorLiveData.removeSource(dailyTasks);
//                    mediatorLiveData.setValue(dailyTaskList);
//                }
//            }
//        });
//
//        if (mediatorLiveData.getValue() != null)
//            Log.d("My", "Total dailyTasks " + dailyTasks.getValue().size() + " tasks have been downloaded");
//
//        return mediatorLiveData;
//    }
//
//    public LiveData<List<DailyTask>> getDailyTasksFromFireBase() {
//        Log.d("My", "inside dailyTaskRepository getDailyTasksFromFireBase");
//
//        MutableLiveData<List<DailyTask>> fireBaseData = new MutableLiveData<>();
//        mReference = mFirebaseDatabase.getReference("user");
//        mReference = mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        List<DailyTask> fireBaseItemList = new ArrayList<>();
//
//        mReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("My", "inside getDailyTasksFromFireBase addChildValueEventListener onChildAdded");
////                fireBaseItemList.clear();
//                if (snapshot != null) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        Log.d("My", "dataSnapshot.key: " + dataSnapshot.getKey());
//                        for (DataSnapshot data : dataSnapshot.getChildren()) {
//                            Log.d("My", "data.key: " + data.getKey());
//                            HashMap<String, Object> hm = (HashMap<String, Object>) data.getValue();
//
//                            // here the object that i'm getting is in the form of hashmap
//                            // so i have retrieved it in a hashmap
//                            // then i have extracted individual KV pair
//                            // and then i have passed it into DailyTask constructor
//                            // MAIN REASON FOR THIS IS:
//                            // because i have id stored in firebase but that ids are causing conflicts
//                            // in the local database
//
//                            String description = (String) hm.get("description");
//                            int priority = Integer.parseInt(hm.get("priority").toString());
//                            int status = Integer.parseInt(hm.get("status").toString());
//                            String date = (String) hm.get("date");
//
//                            DailyTask dailyTask = new DailyTask(description, priority, status, date);
//                            fireBaseItemList.add(dailyTask);
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("My", "inside getDailyTasksFromFireBase addChildValueEventListener onCancelled");
//            }
//        });
//
//        for (DailyTask fireBaseDailyTask : fireBaseItemList) {
//            Log.d("My", "now firing the insert query");
//            insertDailyTask(fireBaseDailyTask);
//            Log.d("My", "after insertDailyTask");
//        }
//
//        fireBaseData.setValue(fireBaseItemList);
//
//        if (fireBaseData.getValue() != null)
//            Log.d("My", "Total " + fireBaseData.getValue().size() + " tasks have been downloaded");
//        else if (fireBaseData.getValue() == null)
//            Log.d("My", "fireBaseData is null");
//        return fireBaseData;
//    }
//
//    public void insertDailyTask(DailyTask dailyTask) {
//        new InsertAsyncTask(dailyTaskDao).execute(dailyTask);
//    }
//
//    private static class InsertAsyncTask extends AsyncTask<DailyTask, Void, Void> {
//        private DailyTaskDaoInterface dailyTaskDao;
//
//        private InsertAsyncTask(DailyTaskDaoInterface dailyTaskDao) {
//            this.dailyTaskDao = dailyTaskDao;
//        }
//
//        @Override
//        protected Void doInBackground(DailyTask... dailyTasks) {
//            Log.d("My", "desc:" + dailyTasks[0].getDescription()
//                    + " date:" + dailyTasks[0].getDate() + " status:" + dailyTasks[0].getStatus());
//            dailyTaskDao.insert(dailyTasks[0]);
//            return null;
//        }
//    }
//
//    public void updateDailyTask(DailyTask dailyTask) {
//        new UpdateAsyncTask(dailyTaskDao).execute(dailyTask);
//    }
//
//    private static class UpdateAsyncTask extends AsyncTask<DailyTask, Void, Void> {
//        private DailyTaskDaoInterface dailyTaskDao;
//
//        public UpdateAsyncTask(DailyTaskDaoInterface dailyTaskDao) {
//            this.dailyTaskDao = dailyTaskDao;
//        }
//
//        @Override
//        protected Void doInBackground(DailyTask... dailyTasks) {
//            dailyTaskDao.update(dailyTasks[0]);
//            return null;
//        }
//    }
//
//    public void deleteDailyTask(DailyTask dailyTask) {
//        new DeleteAsyncTask(dailyTaskDao).execute(dailyTask);
//    }
//
//    private static class DeleteAsyncTask extends AsyncTask<DailyTask, Void, Void> {
//        private DailyTaskDaoInterface dailyTaskDao;
//
//        public DeleteAsyncTask(DailyTaskDaoInterface dailyTaskDao) {
//            this.dailyTaskDao = dailyTaskDao;
//        }
//
//        @Override
//        protected Void doInBackground(DailyTask... dailyTasks) {
//            dailyTaskDao.delete(dailyTasks[0]);
//            return null;
//        }
//    }
//
//    public void deleteAllDailyTask() {
//        new DeleteAllAsyncTask(dailyTaskDao).execute();
//    }
//
//    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
//        private DailyTaskDaoInterface dailyTaskDao;
//
//        public DeleteAllAsyncTask(DailyTaskDaoInterface dailyTaskDao) {
//            this.dailyTaskDao = dailyTaskDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            dailyTaskDao.deleteAllNotes();
//            return null;
//        }
//    }
//
//    public void insertDailyTaskFirebase(DailyTask dailyTask) {
//        Log.d("My", "inside insertDailyTaskFirebase");
//
//        insertDailyTask(dailyTask);
//
//        mReference = mFirebaseDatabase.getReference("user");
//        //newly added line
//        mReference = mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        mReference = mReference.child(dailyTask.getDate()).
//                child(dailyTask.getId() + "");
//        Log.d("My", "key:" + mReference.getKey());
//        mReference.setValue(dailyTask).
//                addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d("My", "added successfully");
//                    }
//                });
//    }

    // ok now here on there will be only firebase methods
    // all crud operations will be done on firebase

    public MutableLiveData<List<DailyTask>> loadFromFireBase() {
        Log.d("My", "inside DailyTaskRepository loadFromFireBase");
        mReference = mFirebaseDatabase.getReference("user");
        mReference = mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("My", "inside loadFromFireBase addChildValueEventListener onChildAdded");
                fireBaseItemList.clear();
                if (snapshot != null) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        Log.d("My", "dataSnapshot.key: " + dataSnapshot.getKey());
//                        for (DataSnapshot data :
//                                snapshot.getChildren()) {
//                            Log.d("My", "data.key: " + data.getKey());
//                            for (DataSnapshot d : data.getChildren()) {
//
//                                HashMap<String, Object> hm = (HashMap<String, Object>) d.getValue();
//
//                                String desc = (String) hm.get("description");
//                                int id = Integer.parseInt(hm.get("id").toString());
//                                int priority = Integer.parseInt(hm.get("priority").toString());
//                                int status = Integer.parseInt(hm.get("status").toString());
//                                String date = (String) hm.get("date");
//
//                                Log.d("My", "desc:" + desc + " priority:" + priority
//                                        + " status:" + status + " date:" + date);
//                                DailyTask dailyTask = new DailyTask(desc, priority, status, date);
//                                dailyTask.setId(id);
//                                fireBaseItemList.add(dailyTask);
//                            }
//                        }
//                    }
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("My", "dataSnapshot.key: " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Log.d("My", "data.key: " + data.getKey());
                            DailyTask dt = data.getValue(DailyTask.class);
                            assert dt != null;
                            DailyTask readDailyTask = new DailyTask(dt.getDescription(),dt.getPriority(),dt.getStatus(),dt.getDate());
                            readDailyTask.setId(dt.getId());
                            fireBaseItemList.add(readDailyTask);
                        }
                    }
                    items.setValue(fireBaseItemList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("My", "inside getDailyTasksFromFireBase addChildValueEventListener onCancelled");
            }
        });
        return items;
    }

    public void addToFireBase(DailyTask dailyTask, int counter) {
        Log.d("My", "inside DailyTaskRepository addToFireBase");

        dailyTask.setId(1 + counter);
        Log.d("My","task id:"+dailyTask.getId());
        //newly added line
        mReference = mFirebaseDatabase.getReference("user");
        mReference = mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference = mReference.child(dailyTask.getDate()).child(dailyTask.getId() + "");
        Log.d("My", "key:" + mReference.getKey());
        mReference.setValue(dailyTask).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("My", "added successfully");
                    }
                });
    }

    public void updateToFireBase(DailyTask dailyTask) {
        Log.d("My", "inside DailyTaskRepository updateToFireBase");
        Log.d("My","task id:"+dailyTask.getId());
        DailyTask updatedDailyTask = new DailyTask(dailyTask.getDescription(),
                dailyTask.getPriority(),
                dailyTask.getStatus(),
                dailyTask.getDate());
        updatedDailyTask.setId(dailyTask.getId());

        //newly added line
        mReference = mFirebaseDatabase.getReference("user");
        mReference = mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mReference = mReference.child(updatedDailyTask.getDate());
        mReference = mReference.child(String.valueOf(updatedDailyTask.getId()));

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mReference.setValue(updatedDailyTask);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteFromFireBase(DailyTask dailyTask, int position) {
        Log.d("My", "inside DailyTaskRepository deleteFromFireBase");
        Log.d("My","task id:"+dailyTask.getId()+" desc:"+dailyTask.getDescription());
        //newly added line
        mReference = mFirebaseDatabase.getReference("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(dailyTask.getDate())
                .child(String.valueOf(dailyTask.getId()));
        fireBaseItemList.remove(position);
        mReference.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("My", "deleted successfully");
                    }
                });
    }

    public void restoreToFireBase(DailyTask dailyTask, int position) {
        Log.d("My", "inside DailyTaskRepository restoreToFireBase");
        Log.d("My","task id:"+dailyTask.getId());
        //newly added line
        mReference = mFirebaseDatabase.getReference("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(dailyTask.getDate())
                .child(String.valueOf(dailyTask.getId()));

        if (fireBaseItemList.size() > 0 && fireBaseItemList.size() > position)
            fireBaseItemList.add(position, dailyTask);
        else
            fireBaseItemList.add(0, dailyTask);

        Log.d("My", "restored key: " + mReference.getKey());
        mReference.setValue(dailyTask)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("My", "restored successfully");
                    }
                });
    }

}
