package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoPlayer extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private static final String TAG = "";
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    YouTubePlayer ytp;

    Button p;
    String data = "";
    TimerCounter vp,st;
    String userid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        getNotificationColor();

        vp = new TimerCounter();
        st = new TimerCounter();
        st.start();
        Log.d(TAG,"onCreate! Starting:");
        data = getIntent().getStringExtra("link");

        userid = getOUid();

        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                youTubePlayer.loadVideo(data);
                youTubePlayer.setShowFullscreenButton(false);
                ytp = youTubePlayer;

                youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                    @Override
                    public void onPlaying() {

                        vp.startAgain();
                       // Toast.makeText(VideoPlayer.this, "onPlaying() "+vp.sec, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaused() {

                        vp.pauseNode();
                      //  Toast.makeText(VideoPlayer.this, "onPaued()", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStopped() {

                        //vp.stopThread();
                        //Toast.makeText(VideoPlayer.this, "onStoped()", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onBuffering(boolean b) {
                       // Toast.makeText(VideoPlayer.this, "onBuffering()", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSeekTo(int i) {
                        //Toast.makeText(VideoPlayer.this, "onSeekedTo()", Toast.LENGTH_SHORT).show();
                    }
                });
                youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
//                        Toast.makeText(VideoPlayer.this, "onVideoLoading()", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoaded(String s) {

                        //Toast.makeText(VideoPlayer.this, "onLoading()", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdStarted() {
                       // Toast.makeText(VideoPlayer.this, "onAddStarted()", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onVideoStarted() {

                        vp.start();
                        //Toast.makeText(VideoPlayer.this, "onVideoStarted() "+vp.sec, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVideoEnded() {
                        vp.pauseNode();
                        Toast.makeText(VideoPlayer.this, "video ended", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                        vp.pauseNode();
                        Toast.makeText(VideoPlayer.this, "onVideoError()", Toast.LENGTH_SHORT).show();
                    }
                });




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
    public void onBackPressed() {
        super.onBackPressed();

        Log.d("tag","Screen time = "+String.valueOf(st.sec));
        Log.d("tag","video playing = "+String.valueOf(vp.sec));
        Log.d("tag","Video State = "+String.valueOf(ytp.getCurrentTimeMillis()/1000));

     //   Toast.makeText(this, "Screen time = "+String.valueOf(st.sec), Toast.LENGTH_SHORT).show();

        double v = (Double.parseDouble(String.valueOf(st.sec)) - (1-(Double.parseDouble(String.valueOf(vp.sec))/(Double.parseDouble(String.valueOf(ytp.getCurrentTimeMillis()/1000))))))/100;


        st.stopThread();
        vp.stopThread();

        putDataInFirebase(v);

       Toast.makeText(this, "Point score = "+String.valueOf(v), Toast.LENGTH_SHORT).show();

       finish();

    }


    DatabaseReference dref;

    void putDataInFirebase(double da)
    {
        dref = FirebaseDatabase.getInstance().getReference("UserData").child(userid);
        dref.addListenerForSingleValueEvent(new ValueEventListener() {

            double val = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);
                String point = ud.getPoints();

                val = Double.parseDouble(point);

                if(da > 0) {
                    dref.child("points").setValue(String.valueOf(val + da));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    SQLiteDatabase db;
    String getOUid()
    {
        Cursor c = null;
        String i = "";
        db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
        db.execSQL("create table if not exists userdata (aid text,val text,uid text,uemai text,uname text,umobile text,utype text);");
        c = db.rawQuery("select * from userdata;", null);
        c.moveToFirst();
        for (int ii = 0; c.moveToPosition(ii); ii++) {
            i = c.getString(0);
        }
        return i;
    }

}
class TimerCounter extends Thread
{
    int sec = 0;

    boolean exit = true;

    boolean ispaused = true;

    public void run(){
        while(exit)
        {
            try {
                if(ispaused) {
                    sleep(1000);

                    sec++;
                    Log.d(String.valueOf(sec), String.valueOf(sec));
                }
                else
                {
                    Log.d(String.valueOf(sec), String.valueOf(sec));
                }
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
        ispaused = true;
    }
    public void pauseNode()
    {
       ispaused = false;
    }
}
