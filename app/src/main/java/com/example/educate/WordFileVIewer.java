package com.example.educate;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.webkit.WebView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WordFileVIewer extends AppCompatActivity {

    WebView wv;
    String topic_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_file_v_iewer);

        topic_id = getIntent().getStringExtra("topicid");
        String url = getIntent().getStringExtra("link");
        wv = findViewById(R.id.webview);
        String doc="http://docs.google.com/viewer?url="+url;
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
}