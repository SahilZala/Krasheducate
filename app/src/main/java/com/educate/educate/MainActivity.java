package com.educate.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    Button login,signup;

    DatabaseReference dref;

    TextInputEditText mobile,pass;

    TextInputLayout til;

    ProgressBar progressBar;

    TextView forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        FoldingCube foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);


        forget = findViewById(R.id.forget);
        progressBar.setVisibility(View.INVISIBLE);


        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
            }
        });


        if (checkOut().equals("1")){

            startActivity(new Intent(getApplicationContext(),NewDashbord.class).putExtra("username",getName()).putExtra("userid",getOUid()));
            finish();
        }
        else{
            getNotificationColor();

            signup = findViewById(R.id.sign_up_button);
            login = findViewById(R.id.log_in);

            mobile = findViewById(R.id.login_mobile_no);
            pass = findViewById(R.id.login_password);

            til = findViewById(R.id.mobile_layout);

            mobile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(mobile.getText().toString().length() == 10)
                    {
                        til.setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                    }
                    else{

                        til.setEndIconDrawable(null);
                    }
                    //til.setEndIconMode(TextInputLayout.END_ICON_NONE);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mobile.getText().toString().length() <= 0 || pass.getText().toString().length() <=0 )
                    {
                        Toast.makeText(MainActivity.this, "complete all field", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.VISIBLE);
                        loginData();
                    }
                }
            });

            signup.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SignUp.class)));
        }


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

    UserData userData = null;

    void loginData()
    {
        userData = null;
        dref  = FirebaseDatabase.getInstance().getReference("UserData");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    UserData ud = ds.getValue(UserData.class);
                    if(ud.getMobileno().equals(mobile.getText().toString()) && ud.getPassword().equals(pass.getText().toString()))
                    {
                        userData = ud;
                    }
                }

                if(userData != null )
                {

                    if(userData.getActivation().equalsIgnoreCase("active") || userData.getActivation().equalsIgnoreCase("true") || userData.getActivation().equalsIgnoreCase("notnotice")) {
                        progressBar.setVisibility(View.INVISIBLE);

                        insertData(userData.getUserid(), "1", userData.getUserid(), userData.getMobileno(), userData.getUsername(), userData.getMobileno(), "student");
                        startActivity(new Intent(getApplicationContext(), NewDashbord.class).putExtra("username", userData.getUsername()).putExtra("userid", userData.userid));
                        finish();
                    }
                    else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(MainActivity.this, "Your Block by admin", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(userData == null)
                {

                    progressBar.setVisibility(View.INVISIBLE);

                    Toast.makeText(MainActivity.this, "Data not match", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    void insertData(String aid,String flage,String ui,String ue,String un,String um,String type)
    {
        progressBar.setVisibility(View.INVISIBLE);
        Cursor c = null;

        String val="1";

        db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);

        String sql = "create table if not exists userdata (aid text,val text,uid text,uemai text,uname text,umobile text,utype text);";
        db.execSQL(sql);


        ContentValues values = new ContentValues();
        values.put("aid",aid);
        values.put("val","0");
        values.put("uid",ui);
        values.put("uemai",ue);
        values.put("uname",un);
        values.put("umobile",um);
        values.put("utype",type);


        db.insert("userdata",null,values);

        db.execSQL("update userdata set val='" + val + "' where aid='" + aid + "';");



        // String q = "insert into usdata values('"+aid+"','1')";

        //  db.execSQL(q);

    }


    SQLiteDatabase db;
    String checkOut()
    {
        Cursor c = null;
        String i = "";
        db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
        db.execSQL("create table if not exists userdata (aid text,val text,uid text,uemai text,uname text,umobile text,utype text);");
        c = db.rawQuery("select * from userdata;", null);
        c.moveToFirst();
        for (int ii = 0; c.moveToPosition(ii); ii++) {
            i = c.getString(1);
        }
        return i;
    }

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
    String getName()
    {
        Cursor c = null;
        String i = "";
        db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
        db.execSQL("create table if not exists userdata (aid text,val text,uid text,uemai text,uname text,umobile text,utype text);");
        c = db.rawQuery("select * from userdata;", null);
        c.moveToFirst();
        for (int ii = 0; c.moveToPosition(ii); ii++) {
            i = c.getString(4);
        }
        return i;
    }
}