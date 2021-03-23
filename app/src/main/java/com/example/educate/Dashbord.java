package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashbord extends AppCompatActivity {

    RecyclerView subject_recycler_view;
    Button notes,video;
    int is_not = 0;

    NestedScrollView scrollView;


    String userid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        getNotificationColor();

        //decalration

        userid = getIntent().getStringExtra("userid");
        subject_recycler_view = findViewById(R.id.subject_recycler_view);

        notes = findViewById(R.id.notes_button);
        video = findViewById(R.id.video_button);

        scrollView = findViewById(R.id.nested_scrollbar);

        //

        fetchDataFromFirebase();

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_not == 0)
                {
                    notes.setBackgroundColor(Color.rgb(56,56,186));
                    notes.setTextColor(Color.WHITE);
                    video.setBackgroundColor(Color.rgb(4,5,7));
                    video.setTextColor(Color.rgb(81,75,74));

                    is_not = 1;
                    adaptNotes();
                }
                else
                {
                    notes.setBackgroundColor(Color.rgb(4,5,7));
                    notes.setTextColor(Color.rgb(81,75,74));
                    video.setBackgroundColor(Color.rgb(56,56,186));
                    video.setTextColor(Color.WHITE);

                    is_not = 0;
                    adaptVideo();
                }
            }
        });


        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_not == 1)
                {
                    video.setBackgroundColor(Color.rgb(56,56,186));
                    video.setTextColor(Color.WHITE);
                    notes.setBackgroundColor(Color.rgb(4,5,7));
                    notes.setTextColor(Color.rgb(81,75,74));


                    is_not = 0;
                    adaptVideo();
                }
                else
                {
                    video.setBackgroundColor(Color.rgb(4,5,7));
                    video.setTextColor(Color.rgb(81,75,74));
                    notes.setBackgroundColor(Color.rgb(56,56,186));
                    notes.setTextColor(Color.WHITE);


                    is_not = 1;

                    adaptNotes();
                }
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

    void adaptVideo()
    {
        subject_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        subject_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        AdapterSubjectList subjectAdapter = new AdapterSubjectList(this,v);
        subject_recycler_view.setAdapter(subjectAdapter);

        subject_recycler_view.scrollToPosition(0);

        scrollView.getParent().requestChildFocus(scrollView,scrollView);

        subjectAdapter.setOnItemClickListener(new AdapterSubjectList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SubjectClass obj, int position) {
                startActivity(new Intent(getApplicationContext(),TopicActivity.class).putExtra("subjectid",obj.getSubjectid()).putExtra("type","Video").putExtra("userid",userid));
            }
        });
    }
    void adaptNotes()
    {


        subject_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        subject_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        AdapterSubjectList subjectAdapter = new AdapterSubjectList(this,n);
        subject_recycler_view.setAdapter(subjectAdapter);

        subject_recycler_view.scrollToPosition(0);
        scrollView.getParent().requestChildFocus(scrollView,scrollView);


        subjectAdapter.setOnItemClickListener(new AdapterSubjectList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SubjectClass obj, int position) {
                startActivity(new Intent(getApplicationContext(),TopicActivity.class).putExtra("subjectid",obj.getSubjectid()).putExtra("type","Notes").putExtra("userid",userid));
            }
        });
    }


    DatabaseReference dref;
    ArrayList<SubjectClass> v,n;
    void fetchDataFromFirebase()
    {
        v = new ArrayList<>();
        n = new ArrayList<>();


        dref = FirebaseDatabase.getInstance().getReference("Subject");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                v.clear();
                n.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    SubjectClass sc = ds.getValue(SubjectClass.class);

                    if(sc.type.equals("link"))
                    {
                        v.add(sc);
                    }
                    else
                    {
                        n.add(sc);
                    }

                }
                if(is_not == 0) {
                    adaptVideo();
                }
                else{
                    adaptNotes();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}