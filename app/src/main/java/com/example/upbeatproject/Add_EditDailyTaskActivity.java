package com.example.upbeatproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_EditDailyTaskActivity extends AppCompatActivity {
    public static final String TASK_ID = "com.example.upbeatproject.TASK_ID";
    public static final String TASK_DESC = "com.example.upbeatproject.TASK_DESC";
    public static final String TASK_PRIORITY = "com.example.upbeatproject.TASK_PRIORITY";
    public static final String TASK_DATE = "com.example.upbeatproject.TASK_DATE";
    EditText etDailyTaskDescription;
    NumberPicker numberPickerPriority;
    Button btnDailyTaskReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__edit_daily_task);

        etDailyTaskDescription = findViewById(R.id.etDailyTaskDescription);
        numberPickerPriority = findViewById(R.id.numberPickerPriority);
        btnDailyTaskReminder = findViewById(R.id.btnDailyTaskReminder);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        Intent intent = getIntent();
        if (intent.hasExtra(TASK_ID)) {
            Log.d("My", "TASK_DESC: " + intent.getStringExtra(TASK_DESC) +
                    " TASK_PRIORITY: " + intent.getIntExtra(TASK_PRIORITY, 1) +
                    "TASK_DATE: " + intent.getStringExtra(TASK_DATE));
            etDailyTaskDescription.setText(intent.getStringExtra(TASK_DESC));
            numberPickerPriority.setValue(intent.getIntExtra(TASK_PRIORITY, 1));
        }

        btnDailyTaskReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etDailyTaskDescription.getText().toString().isEmpty()
                        && !(numberPickerPriority.getValue() < 1)) {
                    Intent calendarIntent = new Intent(Intent.ACTION_INSERT);
                    calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                    int priority = 10 - numberPickerPriority.getValue();
                    calendarIntent.putExtra(CalendarContract.Events.TITLE, "" + priority + etDailyTaskDescription.getText().toString());
                    calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, etDailyTaskDescription.getText().toString());
                    calendarIntent.putExtra(CalendarContract.Events.ALLOWED_REMINDERS, true);

                    if (calendarIntent.resolveActivity(getPackageManager()) != null){
                        startActivity(calendarIntent);
                    }
                    else{
                        Toast.makeText(Add_EditDailyTaskActivity.this, "You don't have Google Calendar App", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Add_EditDailyTaskActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_dailytask_activity_menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("My", "inside Add_EditDailyTaskActivity's onOptionsItemSelected");
        int id = item.getItemId();
        if (R.id.dailyTaskSaveIcon == id) {
            saveDailyTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveDailyTask() {
        String description = etDailyTaskDescription.getText().toString().trim();
        int priority = numberPickerPriority.getValue();
        if (description.isEmpty()) {
            Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // added code for getting current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        Intent dataIntent = new Intent();
        dataIntent.putExtra(TASK_DESC, description);
        dataIntent.putExtra(TASK_PRIORITY, priority);
        dataIntent.putExtra(TASK_DATE, formattedDate);

        int id = getIntent().getIntExtra(TASK_ID, -1);
        if (id != -1) {
            dataIntent.putExtra(TASK_ID, id);
        }
        setResult(RESULT_OK, dataIntent);
        finish();
    }
}