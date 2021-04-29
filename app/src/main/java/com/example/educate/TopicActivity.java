package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    RecyclerView topic_recycler_view;
    ArrayList<TopicClass> topicClassArrayList;

    ProgressBar progressBar1;

    String subjectid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        getNotificationColor();
        subjectid = getIntent().getStringExtra("subjectid");

        progressBar1 = findViewById(R.id.spin_kit);


        //declaration

        topic_recycler_view = findViewById(R.id.topic_recycler_view);
        topicClassArrayList = new ArrayList<>();


        //
        initToolbar();
       fetchData();

    }


    void setAdapter()
    {

        progressBar1.setVisibility(View.INVISIBLE);

        topic_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        topic_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        AdapterTopicList topicAdapter = new AdapterTopicList(this,topicClassArrayList);
        topic_recycler_view.setAdapter(topicAdapter);

        topic_recycler_view.scrollToPosition(0);
//        topic_scrollView.getParent().requestChildFocus(topic_scrollView,topic_scrollView);

        topicAdapter.setOnItemClickListener1(new AdapterTopicList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TopicClass obj, int position) {
               descDialog(obj.getDescription());
            }
        });

        topicAdapter.setOnItemClickListener(new AdapterTopicList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TopicClass obj, int position) {
                if(obj.type.equals("link")) {
                  //  progressBar1.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(), VideoPlayer.class).putExtra("topicname",obj.getName()).putExtra("topicid",obj.getTopicid()).putExtra("link",obj.getLink()));
                }
                else if(obj.type.equalsIgnoreCase("pdf")){
                    if(!obj.link.equalsIgnoreCase("wait uploading is in progress") && !obj.link.equalsIgnoreCase("none")) {
                        startActivity(new Intent(getApplicationContext(), PDFViewerActivity.class).putExtra("subjectid", obj.getSubjectid()).putExtra("topicid", obj.getTopicid()));
                    }
                    else{
                        Toast.makeText(TopicActivity.this, "Not loaded file", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if(!obj.link.equalsIgnoreCase("wait uploading is in progress") && !obj.link.equalsIgnoreCase("none")) {
                        startActivity(new Intent(getApplicationContext(), WordFileVIewer.class).putExtra("topicname",obj.getName()).putExtra("topicid", obj.getTopicid()).putExtra("link", obj.getLink()));
                        // progressBar1.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Toast.makeText(TopicActivity.this, "Not loaded file", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    DatabaseReference topic_dref;
    void fetchData()
    {
        topic_dref = FirebaseDatabase.getInstance().getReference("Topic").child(subjectid);

        topic_dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                topicClassArrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    TopicClass tc = ds.getValue(TopicClass.class);
                    topicClassArrayList.add(tc);
                }
             //   progressBar1.setVisibility(View.INVISIBLE);
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#040507'>Topic List</font>"));
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


    private void descDialog(String description) {

        ImageButton cancel_dialog;

        TextView desc;

        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.description_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);

        desc = dialog.findViewById(R.id.desc_dialog);

        desc.setText(description);
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();
    }



}