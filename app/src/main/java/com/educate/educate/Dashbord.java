package com.educate.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashbord extends AppCompatActivity {

    RecyclerView subject_recycler_view;
    //Button notes,video;
    int is_not = 0;

    NestedScrollView scrollView;


    TextView u_name;
    String userid="";

    ImageButton profile;

    ProgressBar progressBar,progressBar1;

    TextView score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_dashbord);

        getNotificationColor();

        //decalration

        progressBar1 = (ProgressBar)findViewById(R.id.spin_kit);
        FoldingCube foldingCube = new FoldingCube();
        progressBar1.setIndeterminateDrawable(foldingCube);



        progressBar1.setVisibility(View.VISIBLE);



        score = findViewById(R.id.score);
        progressBar = findViewById(R.id.progress_bar);
        String uname = getIntent().getStringExtra("username");

        profile = findViewById(R.id.profile);

        userid = getIntent().getStringExtra("userid");


        subject_recycler_view = findViewById(R.id.subject_recycler_view);

//        notes = findViewById(R.id.notes_button);
//        video = findViewById(R.id.video_button);

        scrollView = findViewById(R.id.nested_scrollbar);

        u_name = findViewById(R.id.uname);
        //
        u_name.setText(uname);

        fetchDataFromFirebase();

        fetchData();


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("userid",userid).putExtra("username",uname));
                finish();
            }
        });

//        notes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(is_not == 0)
//                {
//                    notes.setBackgroundColor(Color.rgb(56,56,186));
//                    notes.setTextColor(Color.WHITE);
//                    video.setBackgroundColor(Color.rgb(4,5,7));
//                    video.setTextColor(Color.rgb(81,75,74));
//
//                    is_not = 1;
//                    adaptNotes();
//                }
//                else
//                {
//                    notes.setBackgroundColor(Color.rgb(4,5,7));
//                    notes.setTextColor(Color.rgb(81,75,74));
//                    video.setBackgroundColor(Color.rgb(56,56,186));
//                    video.setTextColor(Color.WHITE);
//
//                    is_not = 0;
//                    adaptVideo();
//                }
//            }
//        });
//
//
//        video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(is_not == 1)
//                {
//                    video.setBackgroundColor(Color.rgb(56,56,186));
//                    video.setTextColor(Color.WHITE);
//                    notes.setBackgroundColor(Color.rgb(4,5,7));
//                    notes.setTextColor(Color.rgb(81,75,74));
//
//
//                    is_not = 0;
//                    adaptVideo();
//                }
//                else
//                {
//                    video.setBackgroundColor(Color.rgb(4,5,7));
//                    video.setTextColor(Color.rgb(81,75,74));
//                    notes.setBackgroundColor(Color.rgb(56,56,186));
//                    notes.setTextColor(Color.WHITE);
//
//
//                    is_not = 1;
//
//                    adaptNotes();
//                }
//            }
//        });
//


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


    void adaptData()
    {
        subject_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        subject_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        AdapterSubjectList subjectAdapter = new AdapterSubjectList(this,data);
        subject_recycler_view.setAdapter(subjectAdapter);

        subject_recycler_view.scrollToPosition(0);
        scrollView.getParent().requestChildFocus(scrollView,scrollView);


        subjectAdapter.setOnItemClickListener(new AdapterSubjectList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SubjectClass obj, int position) {
                startActivity(new Intent(getApplicationContext(),TopicActivity.class).putExtra("subjectid",obj.getSubjectid()).putExtra("type",obj.getName()).putExtra("userid",userid));
            }
        });
    }


    DatabaseReference dref;
    ArrayList<SubjectClass> data;
    void fetchDataFromFirebase()
    {
        data = new ArrayList<>();


        dref = FirebaseDatabase.getInstance().getReference("Subject");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();

                for(DataSnapshot ds:snapshot.getChildren()){
                    SubjectClass sc = ds.getValue(SubjectClass.class);

                    data.add(sc);

                }


                progressBar1.setVisibility(View.INVISIBLE);
                adaptData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    DatabaseReference fetchdata;
    void fetchData()
    {
        fetchdata = FirebaseDatabase.getInstance().getReference("UserData").child(userid);
        fetchdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);
                int p = (int)Double.parseDouble(ud.points);
                progressBar.setProgress(p);
                score.setText(String.valueOf(p)+"%");
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