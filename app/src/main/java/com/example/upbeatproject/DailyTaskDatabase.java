package com.example.upbeatproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Database(entities = {DailyTask.class}, version = 2)
public abstract class DailyTaskDatabase extends RoomDatabase {
    private static DailyTaskDatabase dailyTaskDatabaseInstance;

    public abstract DailyTaskDaoInterface getDailyTaskDao();


    static Migration migrations = new Migration(1,2){

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'daily_task_table' ADD COLUMN 'date' VARCHAR");
        }
    };

    public static synchronized DailyTaskDatabase getInstance(Context context) {
        if (dailyTaskDatabaseInstance == null) {
            dailyTaskDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                    DailyTaskDatabase.class, "daily_task_table_database")
                    .addCallback(roomCallBack)
                    .addMigrations(migrations)
                    .build();
        }
        return dailyTaskDatabaseInstance;
    }

    private static Callback roomCallBack = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDbAsyncTask(dailyTaskDatabaseInstance);
            super.onCreate(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private DailyTaskDaoInterface dailyTaskDaoInterface;
        private PopulateDbAsyncTask(DailyTaskDatabase dailyTaskDatabase){
            dailyTaskDaoInterface = dailyTaskDatabase.getDailyTaskDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("My","inside doInBackground of PopulateDbAsyncTask");
            // added code for getting current date
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("current date dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);

            dailyTaskDaoInterface.insert(new DailyTask("This is Task Desc.",10,0,formattedDate));
            return null;
        }
    }
}
