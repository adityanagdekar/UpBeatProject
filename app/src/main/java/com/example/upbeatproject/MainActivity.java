package com.example.upbeatproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    CardView cardViewLearn,cardViewMeditate,cardViewChat, cardViewDailyTask;
    static final int PERMIT_CODE=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Below function checks our permissions
        checkAllPermissions();

        //This card view takes to ChatActivity
        cardViewChat=findViewById(R.id.cardView_chat);
        cardViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });
        //This card view takes to LearnActivity
        cardViewLearn=findViewById(R.id.cardView_learn);
        cardViewLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LearnActivity.class);
                startActivity(intent);
            }
        });
        //This card view takes to MeditateActivity
        cardViewMeditate=findViewById(R.id.cardView_meditate);
        cardViewMeditate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MeditateActivity.class);
                startActivity(intent);
            }
        });
        //This card view takes to JournalActivity
        cardViewDailyTask =findViewById(R.id.cardView_dailyTask);
        cardViewDailyTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,DailyTaskActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAllPermissions() {
        Log.d("My","Inside checkInternetPermission()");
        String [] permissions = {Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE};
        if( (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED) &&
        (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_GRANTED)){
            Log.d("My","Inside checkAllPermissions() permissions granted");
            //DO SOMETHING
        }
        else{
            Log.d("My","calling requestAllPermissions()");
            requestAllPermissions(permissions);
        }
    }

    private void requestAllPermissions(String [] permissions) {
        Log.d("My","Inside requestAllPermissions()");
        Log.d("My","Permissions: "+permissions[0]);

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.INTERNET) &&
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_NETWORK_STATE)){
            Log.d("My","inside if calling showDialog()");
            showDialog(permissions);
        }
        else{
            Log.d("My","inside else calling ActivityCompat.requestPermissions()");
            ActivityCompat.requestPermissions(this, permissions, PERMIT_CODE);
        }
    }

    public void showDialog(final String [] permissions) {
        Log.d("My","inside showDialog()");
        AlertDialog alertDialog =  new AlertDialog.Builder(MainActivity.this)
                .setTitle("Permission Needed")
                .setMessage("We need to access internet to work smoothly")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("My","inside +ve btn");
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMIT_CODE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("My","inside -ve btn");
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean b = false;
        if (requestCode == PERMIT_CODE){
            if (grantResults.length>0 ){
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i]==PackageManager.PERMISSION_GRANTED){
                        b=true;
                    }
                    else{
                        gotoApplicationSettingsDialog();
                    }
                }
                if(b){
                    Log.d("My","Inside onRequestPermissionsResult() now calling loadMeme()");
                    //DO SOMETHING
                }
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                gotoApplicationSettingsDialog();
            }
        }
    }

    private void gotoApplicationSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",this.getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void gotoApplicationSettingsDialog(){
        Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        if ( !(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.INTERNET)  &&
                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.ACCESS_NETWORK_STATE)) ){
            Log.d("My", "inside if calling gotoApplicationSettings()");

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission Needed")
                    .setMessage("We need to access internet to work smoothly")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gotoApplicationSettings();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("My","inside MainActivity's onOptionsItemSelected");
        int id = item.getItemId();
//        if (R.id.userProfileIcon == id){
//            Toast.makeText(this, "Taking you to User Profile", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        if (R.id.userLogout == item.getItemId()) {
            FirebaseAuth.getInstance().signOut();
            if (FirebaseAuth.getInstance().getCurrentUser() == null)
                Toast.makeText(this, "Logged Out successfully", Toast.LENGTH_SHORT).show();

            // emptying entire dailyTasks db once user logs out

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}