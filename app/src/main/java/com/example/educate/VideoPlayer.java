package com.example.educate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private static final String TAG = "";
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    YouTubePlayer ytp;

    Button p;
    String data = "";
    TimerCounter tc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getNotificationColor();

        tc = new TimerCounter();
        tc.start();
        Log.d(TAG,"onCreate! Starting:");
        data = getIntent().getStringExtra("link");


        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(data);
                youTubePlayer.setShowFullscreenButton(false);
                ytp = youTubePlayer;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youTubePlayerView.initialize(YoutubeConfig.getApikey(),onInitializedListener);



    }

    void getNotificationColor()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.black_mate));
        }
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        if(b) {
            youTubePlayer.loadVideo(data);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();

        //tc.start();
        Toast.makeText(VideoPlayer.this, ""+tc.sec, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Toast.makeText(this, ""+ytp.getCurrentTimeMillis()/1000, Toast.LENGTH_SHORT).show();

        Toast.makeText(this, ""+tc.sec, Toast.LENGTH_SHORT).show();

        ytp.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        });


        ytp.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });
        tc.stopThread();

    }
}
class TimerCounter extends Thread
{
    int sec = 0;

    boolean exit = true;

    public void run(){
        while(exit)
        {
            try {
                sleep(1000);

                sec++;
                Log.d(String.valueOf(sec),String.valueOf(sec));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread()
    {
        exit = false;
    }

    public void startAgain()
    {
        exit = true;
    }
}