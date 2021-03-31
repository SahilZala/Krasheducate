package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class PDFViewerActivity extends AppCompatActivity {

    PDFView pdfView;

    String subjectid,topicid;
    ProgressBar progressBar;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);

        getNotificationColor();
        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        FoldingCube foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        progressBar.setVisibility(View.VISIBLE);

        subjectid = getIntent().getStringExtra("subjectid");
        topicid = getIntent().getStringExtra("topicid");

        pdfView = findViewById(R.id.pdfview);

        pdfView.setBackgroundColor(R.color.black_mate_2);

        getPdfData();



    }

    void setCountData(String data)
    {

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("TopicCount").child(getOUid());

        TopicCount tc = new TopicCount(getOUid(),data,"nots");

        dref.child(data).setValue(tc);
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

    class RetiveStream extends AsyncTask<String,Void, InputStream>
    {

        @Override
        protected InputStream doInBackground(String... strings) {


            InputStream inputStream = null;
            try{

                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if(httpURLConnection.getResponseCode()==200)
                {
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    setCountData(topicid);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).load();
        }
    }

    DatabaseReference pdfref;

    void getPdfData()
    {
        progressBar.setVisibility(View.VISIBLE);

        pdfref = FirebaseDatabase.getInstance().getReference("Topic").child(subjectid).child(topicid);

        pdfref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TopicClass tc = dataSnapshot.getValue(TopicClass.class);


                progressBar.setVisibility(View.VISIBLE);
                new RetiveStream().execute(tc.getLink());
                //        progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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


}
