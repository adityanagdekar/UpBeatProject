package com.example.upbeatproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends AppCompatActivity {
    private static final String VISIT_FLAG = "VISIT_FLAG";
    public static final String SPF_FILE="my_sharedPreferences_file";
    Button btn1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        btn1 = findViewById(R.id.btn1);

        sharedPreferences = getSharedPreferences(SPF_FILE, MODE_PRIVATE);
        editor = getSharedPreferences(SPF_FILE,MODE_PRIVATE).edit();
        if (sharedPreferences.getInt(VISIT_FLAG, 0)==0) {
            flag=0;
            editor.putInt(VISIT_FLAG, 1);
            editor.apply();
        }
        else if (sharedPreferences.getInt(VISIT_FLAG,0)==1){
            flag=1;
        }
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    Intent intent = new Intent(SplashActivity.this, GuideLinesActivity.class);
                    startActivity(intent);
                    finish();
                } else if (flag == 1) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}