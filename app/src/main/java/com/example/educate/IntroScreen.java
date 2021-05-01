package com.example.educate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntroFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IntroScreen extends com.github.paolorotolo.appintro.AppIntro {
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      // setContentView(R.layout.activity_intro_screen);
        getNotificationColor();
        String v = checkOut();




        if(v.equals("1"))
        {
            Intent i = new Intent(IntroScreen.this, NewDashbord.class);
            startActivity(i);
            finish();
            //getUserData();

        }
        else{
            addSlide(AppIntroFragment.newInstance("Welcome To Education","bottom","A Product By KRASH IT SERVICE for DIGITAL STUDENTS(THINK DIGITAL)","",R.drawable.onlinelearning,ContextCompat.getColor(getApplicationContext(), R.color.mate_red),ContextCompat.getColor(getApplicationContext(), R.color.white),ContextCompat.getColor(getApplicationContext(), R.color.white)));
            addSlide(AppIntroFragment.newInstance("Authentication","bottom","One Time Login With Mobile number and OTP Verification","",R.drawable.athentication,ContextCompat.getColor(getApplicationContext(), R.color.mate_purple),ContextCompat.getColor(getApplicationContext(), R.color.white),ContextCompat.getColor(getApplicationContext(), R.color.white)));
            addSlide(AppIntroFragment.newInstance("Earn Money","bottom","Make Money By Making Points It Will Helps you Learning","",R.drawable.earnmoney,ContextCompat.getColor(getApplicationContext(), R.color.mate_yelloe),ContextCompat.getColor(getApplicationContext(), R.color.white),ContextCompat.getColor(getApplicationContext(), R.color.white)));

        }
    }



    @Override

    public void onDonePressed(Fragment currentFragment){

        super.onDonePressed(currentFragment);

        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
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

    void getNotificationColor()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.sky_blue));
        }
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



}

