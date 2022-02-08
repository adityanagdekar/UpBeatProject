package com.example.upbeatproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyTaskActivity extends AppCompatActivity {
    public static final int ADD_TASK_CODE = 1;
    public static final int EDIT_TASK_CODE = 0;
    CardView cdLearnTask, cdMeditateTask;
    TextView tvcdLearnTask, tvcdMeditateTask;
    FloatingActionButton fabAddDailyTask;
    RecyclerView rvDailyTask;
    DailyTaskAdapter dailyTaskAdapter;
    DailyTaskViewModel dailyTaskViewModel;

    //dummy counter
    static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);

        cdLearnTask = findViewById(R.id.cdLearnTask);
        cdMeditateTask = findViewById(R.id.cdMeditateTask);
        tvcdLearnTask = findViewById(R.id.tvcdLearnTask);
        tvcdMeditateTask = findViewById(R.id.tvcdMeditateTask);
        fabAddDailyTask = findViewById(R.id.fabAddDailyTask);
        rvDailyTask = findViewById(R.id.rvDailyTask);


        // added code for getting current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        tvcdLearnTask.setText(formattedDate);
        tvcdMeditateTask.setText(formattedDate);

        cdLearnTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyTaskActivity.this, LearnActivity.class);
                startActivity(intent);
                Toast.makeText(DailyTaskActivity.this, "Taking you to the Learn Section", Toast.LENGTH_SHORT).show();
            }
        });

        cdMeditateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyTaskActivity.this, MeditateActivity.class);
                startActivity(intent);
                Toast.makeText(DailyTaskActivity.this, "Taking you to the Meditate Section", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvDailyTask.setLayoutManager(linearLayoutManager);
        List<DailyTask> dailyTaskList = new ArrayList<>();
        dailyTaskAdapter = new DailyTaskAdapter(this, dailyTaskList);

        rvDailyTask.setAdapter(dailyTaskAdapter);

        dailyTaskViewModel = new ViewModelProvider(DailyTaskActivity.this).get(DailyTaskViewModel.class);
        dailyTaskViewModel.init(DailyTaskActivity.this);
        dailyTaskViewModel.loadFromFireBase().observe(DailyTaskActivity.this, new Observer<List<DailyTask>>() {
            @Override
            public void onChanged(List<DailyTask> dailyTaskList) {
                Log.d("My", "inside DailyTaskActivity fireBaseViewModel onChanged");
                dailyTaskAdapter.setDailyTaskList(dailyTaskList);
            }
        });

        dailyTaskAdapter.setDailyTaskClickedListener(new DailyTaskAdapter.DailyTaskClicked() {
            @Override
            public void onDailyTaskClick(DailyTask dailyTask) {
                //TODO goto AddEditActivity for DailyTask
                Intent intent = new Intent(DailyTaskActivity.this, Add_EditDailyTaskActivity.class);
                intent.putExtra(Add_EditDailyTaskActivity.TASK_ID, dailyTask.getId());
                intent.putExtra(Add_EditDailyTaskActivity.TASK_DESC, dailyTask.getDescription());
                intent.putExtra(Add_EditDailyTaskActivity.TASK_PRIORITY, dailyTask.getPriority());
                intent.putExtra(Add_EditDailyTaskActivity.TASK_DATE, dailyTask.getDate());
                startActivityForResult(intent, EDIT_TASK_CODE);
            }
        });

        fabAddDailyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO goto AddEditActivity for DailyTask
                Intent intent = new Intent(DailyTaskActivity.this, Add_EditDailyTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_CODE);
            }
        });

        // here we are passing
        // our ItemTouchHelper.SimpleCallback
        // and attaching our RecyclerView to it
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvDailyTask);

    }

    // ItemTouchHelper.SimpleCallback this helps
    // in handling swipe events and
    // working on our logic after doing swipes
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Log.d("My", "inside onSwiped");
            int position = viewHolder.getAdapterPosition();
            DailyTask dailyTask = dailyTaskAdapter.getDailyTaskAt(position);
            switch (direction) {
                case ItemTouchHelper.RIGHT:
                    if (dailyTask.getStatus() == 0) {
                        dailyTask.setStatus(1);
                        Log.d("My", "onSwiped task id:" + dailyTask.getId());
                        dailyTaskViewModel.updateToFireBase(dailyTask);
                    } else {
                        dailyTask.setStatus(0);
                        Log.d("My", "onSwiped task id:" + dailyTask.getId());
                        dailyTaskViewModel.updateToFireBase(dailyTask);
                    }
                    // TODO Show task status updated successfully
                    Snackbar innerSnackBar = Snackbar.make(rvDailyTask,
                            "Task status updated successfully", Snackbar.LENGTH_SHORT);
                    innerSnackBar.show();
                    break;
                case ItemTouchHelper.LEFT:
                    Log.d("My", "onSwiped task id:" + dailyTask.getId());
                    dailyTaskViewModel.deleteFromFireBase(dailyTask, position);
                    Snackbar snackbar = Snackbar.make(rvDailyTask,
                            "Task is deleted",
                            Snackbar.LENGTH_LONG);

                    snackbar.setAction("undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO Show restored item successfully message
                            dailyTaskViewModel.restoreToFireBase(dailyTask, position);
                            dailyTaskAdapter.notifyItemInserted(position);
                            Snackbar innerSnackBar = Snackbar.make(v, "Task restored successfully", Snackbar.LENGTH_SHORT);
                            innerSnackBar.show();
                        }
                    });
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int f = 0;
        if (requestCode == ADD_TASK_CODE && resultCode == RESULT_OK) {

            f = 1;
            String description = data.getStringExtra(Add_EditDailyTaskActivity.TASK_DESC);
            int priority = data.getIntExtra(Add_EditDailyTaskActivity.TASK_PRIORITY, 1);
            int status = 0;
            String formattedDate = data.getStringExtra(Add_EditDailyTaskActivity.TASK_DATE);

            DailyTask dailyTask = new DailyTask(description, priority, status, formattedDate);
//            dailyTaskViewModel.insert(dailyTask);

            dailyTaskViewModel.addToFireBase(dailyTask, counter);
            counter++;

            Log.d("My", "inside ADD_TASK_CODE");
            Log.d("My", "description:" + description + " priority:" + priority + " status:" + status + " id:" + dailyTask.getId() + " date:" + dailyTask.getDate());
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TASK_CODE && resultCode == RESULT_OK) {

            int id = data.getIntExtra(Add_EditDailyTaskActivity.TASK_ID, -1);
            String description = data.getStringExtra(Add_EditDailyTaskActivity.TASK_DESC);
            int priority = data.getIntExtra(Add_EditDailyTaskActivity.TASK_PRIORITY, 1);
            int status = 0;

            if (id != -1) {
                // added code for getting current date
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);

                DailyTask dailyTask = new DailyTask(description, priority, status, formattedDate);
                dailyTask.setId(id);
//                dailyTaskViewModel.update(dailyTask);
                dailyTaskViewModel.updateToFireBase(dailyTask);

                Log.d("My", "inside EDIT_TASK_CODE");
                Log.d("My", "description:" + description + " priority:" + priority + " status:" + status + " id:" + dailyTask.getId() + " date:" + dailyTask.getDate());
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
        } else {
            if (f == 1)
                Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dailytask_activity_menu, menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("My", "inside DailyTaskActivity's onOptionsItemSelected");
        int id = item.getItemId();
        if (R.id.helpIcon == id) {
            Snackbar innerSnackBar = Snackbar.make(rvDailyTask,
                    "Swipe Left to delete the task and swipe right to update the status of task",
                    Snackbar.LENGTH_LONG);
            innerSnackBar.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}