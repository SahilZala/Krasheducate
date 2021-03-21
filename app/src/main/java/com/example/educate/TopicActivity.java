package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    TextView topic_name,back;
    RecyclerView topic_recycler_view;
    ArrayList<TopicClass> topicClassArrayList;
    NestedScrollView topic_scrollView;

    String subjectid = "";

    String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        subjectid = getIntent().getStringExtra("subjectid");

        type = getIntent().getStringExtra("type");

        //declaration

        topic_name = findViewById(R.id.topic_name);
        back = findViewById(R.id.topic_back);
        topic_recycler_view = findViewById(R.id.topic_recycler_view);
        topic_scrollView = findViewById(R.id.topic_nested_scrollview);
        topicClassArrayList = new ArrayList<>();



        //

        topic_name.setText(type);
       fetchData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    void setAdapter()
    {

        topic_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        topic_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        AdapterTopicList topicAdapter = new AdapterTopicList(this,topicClassArrayList);
        topic_recycler_view.setAdapter(topicAdapter);

        topic_recycler_view.scrollToPosition(0);
        topic_scrollView.getParent().requestChildFocus(topic_scrollView,topic_scrollView);

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
                setAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}