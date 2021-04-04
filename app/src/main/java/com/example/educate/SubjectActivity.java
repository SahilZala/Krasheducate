package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity {

    RecyclerView subject_recycler_view,secondary_subject_recycler_view;

    //Button notes,video;

    TextView paid;


    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        fetchData();

        initToolbar();
        getNotificationColor();


        paid = findViewById(R.id.paid_view);

        progressBar1 = findViewById(R.id.spin_kit);

        subject_recycler_view = findViewById(R.id.subject_recycler_view);

        secondary_subject_recycler_view = findViewById(R.id.secondary_subject_recycler_view);


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

    void secondAdapter(ArrayList<SubjectClass> data)
    {
        secondary_subject_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        secondary_subject_recycler_view.setHasFixedSize(true);

        //set data and list adapter
        AdapterSubjectList subjectAdapter = new AdapterSubjectList(this,data);
        secondary_subject_recycler_view.setAdapter(subjectAdapter);

        secondary_subject_recycler_view.scrollToPosition(0);
        //scrollView.getParent().requestChildFocus(scrollView,scrollView);


        subjectAdapter.setOnItemClickListener(new AdapterSubjectList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, SubjectClass obj, int position) {
                subscribe_show();
                //Toast.makeText(SubjectActivity.this, "subscribe", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getApplicationContext(),TopicActivity.class).putExtra("subjectid",obj.getSubjectid()));
            }
        });
    }

    void adaptData(ArrayList<SubjectClass> data)
    {
        subject_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        subject_recycler_view.setHasFixedSize(false);

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
    ArrayList<SubjectClass> paid_data;
    void fetchDataFromFirebase()
    {
        data = new ArrayList<>();
        paid_data = new ArrayList<>();


        dref = FirebaseDatabase.getInstance().getReference("Subject");
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                paid_data.clear();

                for(DataSnapshot ds:snapshot.getChildren()){
                    SubjectClass sc = ds.getValue(SubjectClass.class);

                    if(sc.getActivation().equalsIgnoreCase("true")) {
                        if (userData != null) {
                            if (userData.getPaidunpaid().equalsIgnoreCase("paid")) {
                                data.add(sc);
                            } else if (userData.getPaidunpaid().equalsIgnoreCase("unpaid")) {
                                if (sc.getFtype().equalsIgnoreCase("free")) {
                                    data.add(sc);
                                }
                                else{
                                    paid_data.add(sc);
                                }
                            }

                        }
                    }
                }


                progressBar1.setVisibility(View.INVISIBLE);
                if(userData.getPaidunpaid().equalsIgnoreCase("paid")) {
                    paid.setText("");
                    adaptData(data);
                    secondAdapter(paid_data);
                }
                else{
                    paid.setText("Paid");
                    adaptData(data);
                    secondAdapter(paid_data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                fetchDataFromFirebase();

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


    private void subscribe_show() {

        ImageButton cancel_dialog;

        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.subscribe_alert_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });


        dialog.show();
    }

}
