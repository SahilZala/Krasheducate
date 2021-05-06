package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Notification extends AppCompatActivity {

    RecyclerView noti_recycler_view;

    ProgressBar progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        noti_recycler_view = findViewById(R.id.noti_recycler_view);

        progressBar1 = findViewById(R.id.spin_kit);
        notificationClasses = new ArrayList<>();

        getNotificationColor();
        fetchData();
        initToolbar();
    }

    ArrayList<NotificationClass> notificationClasses;


    void fetchData()
    {


        progressBar1.setVisibility(View.VISIBLE);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Notification");

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationClasses.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    NotificationClass nc = ds.getValue(NotificationClass.class);

                    notificationClasses.add(nc);
                }

                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        finish();
    }
    private Toolbar toolbar;

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#040507'>Notifications</font>"));
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

    void setAdapter()
    {
        Collections.reverse(notificationClasses);
        progressBar1.setVisibility(View.INVISIBLE);

        noti_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        noti_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        NotiAdapter topicAdapter = new NotiAdapter(this,notificationClasses);
        noti_recycler_view.setAdapter(topicAdapter);

        noti_recycler_view.scrollToPosition(0);
//        topic_scrollView.getParent().requestChildFocus(topic_scrollView,topic_scrollView);


        topicAdapter.setOnItemClickListener(new NotiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NotificationClass obj, int position) {
                Toast.makeText(Notification.this, ""+obj.head, Toast.LENGTH_SHORT).show();
            }
        });

    }

}