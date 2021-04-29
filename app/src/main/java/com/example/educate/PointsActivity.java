package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PointsActivity extends AppCompatActivity {

    ProgressBar progressBar;

    TextView score;

    MaterialButton request;
    String activation = "";
    int points = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        getNotificationColor();
        initToolbar();

        fetchData();

        score = findViewById(R.id.point_score);
        progressBar = findViewById(R.id.point_progress_bar);
        request = findViewById(R.id.send_request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequestToEarn();
            }
        });

    }

    @Override
    public void onBackPressed() {

        finish();
    }
    private Toolbar toolbar;

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#040507'>Points</font>"));
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


    UserData userData = null;
    DatabaseReference name_ref;
    void fetchData()
    {
        name_ref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());

        name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);

                userData = ud;

                int p = (int)Double.parseDouble(ud.points);
                        progressBar.setProgress(p);
                score.setText(String.valueOf(p)+"%");

                points = p;

               activation =  ud.getActivation();

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

    void sendRequestToEarn()
    {
        if(!activation.equalsIgnoreCase("notnotice")) {
            if(points >= 100) {
                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Request");

                String id = dref.push().getKey();

                PointsRequest pr = new PointsRequest(id,getOUid(),SystemTool.getCurrent_date(),SystemTool.getCurrent_time(),"true");

                dref.child(id).setValue(pr);

                Toast.makeText(PointsActivity.this, "request sended to make money", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Points should be more then 100%", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "You cant send request", Toast.LENGTH_SHORT).show();
        }
    }



}