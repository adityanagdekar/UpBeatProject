package com.example.upbeatproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MeditateActivity extends AppCompatActivity {
    ArrayList<MeditateActivityItem> meditateActivityItemArrayList;
    MeditateModelAdapter meditateModelAdapter;
    ViewPager2 viewPager2;
    ViewPager2.OnPageChangeCallback onPageChangeCallback;
    int currentPagePosition;
    MediaPlayer mediaPlayer;
    int mediaPlayerFlag=0;
    FloatingActionButton fabPlayPause;
    public static final String MyTag = "MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditate);

        Log.d(MyTag,"Inside MeditateActivity");

        mediaPlayer = MediaPlayer.create(this,R.raw.meditation_relaxing);
        meditateActivityItemArrayList = new ArrayList<>();
        meditateActivityItemArrayList.add(new MeditateActivityItem("Meditation \n Quite the mind and the soul will speak",R.drawable.meditation));
        meditateActivityItemArrayList.add(new MeditateActivityItem("Title2",R.drawable.meditation));

        meditateModelAdapter = new MeditateModelAdapter(MeditateActivity.this, meditateActivityItemArrayList);
        viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(meditateModelAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPagePosition=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
                mediaPlayerFlag=0;
                changeSong();
                Log.d(MyTag,"inside onPageScrollStateChanged(int state) mediaPlayerFlag: "+mediaPlayerFlag+"\n"+
                        " mediaPlayer.isPlaying(): "+mediaPlayer.isPlaying());
            }
        };
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

//        Here we are using our interface and we override
//        it's method to play/pause music and also to show play/pause animation
        meditateModelAdapter.setMediateModelListener(new MeditateModelAdapter.meditateModelClicked() {
            @Override
            public void onMeditateModelClick(MeditateActivityItem meditateActivityItem) {
                Log.d(MyTag,"Clicked: "+meditateActivityItem.getTitle());
            }
        });
    }

    private void changeSong() {
        switch (meditateActivityItemArrayList.get(currentPagePosition).getTitle()){
            case "Title1":
                mediaPlayer=MediaPlayer.create(this,R.raw.meditation_relaxing);
                mediaPlayerFlag=0;
                break;
            case "Title2":
                mediaPlayer=MediaPlayer.create(this,R.raw.alert);
                mediaPlayerFlag=0;
                break;
        }
    }

    public void startMediaPlayer(View view) {
        if(mediaPlayerFlag==1 && !mediaPlayer.isPlaying())
        {
            mediaPlayer.start();
            Log.d(MyTag,"song has finished");
            ((FloatingActionButton)view).setImageResource(R.drawable.ic_play_btn);
        }
        if(mediaPlayerFlag==0 ) {
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
            mediaPlayerFlag=1;
            ((FloatingActionButton)view).setImageResource(R.drawable.ic_pause_btn);
        }
        else{
            mediaPlayer.pause();
            mediaPlayerFlag=0;
            ((FloatingActionButton)view).setImageResource(R.drawable.ic_play_btn);
        }
        Log.d(MyTag,"mediaPlayerFlag: "+mediaPlayerFlag+
                " mediaPlayer.isPlaying(): "+mediaPlayer.isPlaying()+
                "\n"+"mediaPlayer.getDuration(): "+mediaPlayer.getDuration());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback);
        mediaPlayer.release();
        mediaPlayerFlag=0;
    }
}