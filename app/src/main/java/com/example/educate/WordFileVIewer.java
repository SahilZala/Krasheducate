package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WordFileVIewer extends AppCompatActivity {

    WebView wv;
    String topic_id;
    String topicname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_file_v_iewer);

        topic_id = getIntent().getStringExtra("topicid");
        String url = getIntent().getStringExtra("link");
        topicname = getIntent().getStringExtra("topicname");
        wv = findViewById(R.id.webview);
        String doc="http://docs.google.com/viewer?url="+url;

        getAddress();

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.loadUrl(doc);

        setCountData(topic_id);


    //    wv.loadData( doc , "text/html",  "UTF-8");

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

    void createHistory(String changes_in,String changes_in_id,String message,String address)
    {
        DatabaseReference hist = FirebaseDatabase.getInstance().getReference("History");
        String histid = hist.push().getKey();

        History h = new History(histid,getOUid(),changes_in,changes_in_id,message,SystemTool.getCurrent_time(),SystemTool.getCurrent_date(),"true",address);


        hist.child(getOUid()).child(histid).setValue(h);
    }

    void getAddress()
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Address").child(getOUid()).child("address");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AddressClass ac = snapshot.getValue(AddressClass.class);

                createHistory("Topic",topic_id,"Reading word file "+topicname,ac.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}