package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {

    RecyclerView subject_recycler_view;
    //Button notes,video;



    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        initToolbar();
        getNotificationColor();


        fetchDataFromFirebase();

        progressBar1 = findViewById(R.id.spin_kit);

        subject_recycler_view = findViewById(R.id.subject_recycler_view);


    }

    @Override
    public void onBackPressed() {

        finish();
    }
    private Toolbar toolbar;

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#040507'>Subject List</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black_mate_2), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    void getNotificationColor()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.sky_blue));
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
        //scrollView.getParent().requestChildFocus(scrollView,scrollView);


        subjectAdapter.setOnItemClickListener(new AdapterSubjectList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SubjectClass obj, int position) {
                startActivity(new Intent(getApplicationContext(),TopicActivity.class).putExtra("subjectid",obj.getSubjectid()));
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

}