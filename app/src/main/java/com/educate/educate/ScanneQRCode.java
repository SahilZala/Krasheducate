package com.educate.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanneQRCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    ZXingScannerView zXingScannerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);

        zXingScannerView = new ZXingScannerView(this);
        setContentView(zXingScannerView);


    }

    @Override
    public void handleResult(Result result) {

        //MainActivity.textView.setText(result.getText().toString());

        if(!result.getText().toString().equals(getOUid())) {
            checkReferClass(result.getText().toString());
        }
        else{
            Toast.makeText(getApplicationContext(), "same id not allowed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        zXingScannerView.stopCamera();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();


        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    void checkReferClass(String referbyid)
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("ReferClass").child(getOUid());

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReferClass rc = null;
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    rc = ds.getValue(ReferClass.class);
                }

                if(rc != null)
                {
                    if(rc.getReferbyid().equals(referbyid)){
                        Toast.makeText(getApplicationContext(), "You already referd by him", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        setData(getOUid(),referbyid);
                    }
                }
                else{
                    setData(getOUid(),referbyid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setData(String referid,String referbyid)
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("ReferClass").child(referid);
        String id = dref.push().getKey();
        ReferClass rc = new ReferClass(referid,referbyid,"time","date",id);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child(referbyid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child(referid);

                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserData ud = snapshot.getValue(UserData.class);
                            if(ud.getReferdone().equals("0"))
                            {
                                dref.child(id).setValue(rc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();



                                        getReferIdUserData(referbyid);
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Multiple ref not allowed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "wrong ref id", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    UserData referelUserData,referbyUserData;

    void getReferIdUserData(String referbyid)
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);
                referelUserData = ud;

                double i = Double.parseDouble(String.valueOf(ud.getPoints()));
                i = i + 20;


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
                ref.child("points").setValue(String.valueOf(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
                        ref.child("referdone").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                getReferByIdUserData(referbyid);
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void getReferByIdUserData(String referbyid)
    {
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("UserData").child(referbyid);

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);
                referbyUserData  = ud;

                double i = Double.parseDouble(String.valueOf(ud.getPoints()));
                i = i+10;

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserData").child(referbyid);
                ref.child("points").setValue(String.valueOf(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        double total_ref = Double.parseDouble(String.valueOf(ud.getReferby()));
                        total_ref = total_ref + 1;

                        ref.child("referby").setValue(String.valueOf(total_ref));
                        Toast.makeText(getApplicationContext(), "referencing done succesfully", Toast.LENGTH_SHORT).show();


                        onBackPressed();
                      //  getCurrentUserData();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(getApplicationContext(),NewProfileActivity.class));
        finish();
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
