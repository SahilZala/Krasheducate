package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    TextInputEditText name,mob;

    String userid,uname;

    MaterialButton submit;

    TextView point;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userid = getIntent().getStringExtra("userid");
        uname = getIntent().getStringExtra("username");

        name = findViewById(R.id.user_name_profile);
        mob = findViewById(R.id.mobile_no_profile);

        submit = findViewById(R.id.submit_button_profile);

        point = findViewById(R.id.points);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() || !mob.getText().toString().isEmpty())
                {
                    dref = FirebaseDatabase.getInstance().getReference("UserData").child(userid);
                    dref.child("username").setValue(name.getText().toString());

                    dref.child("mobileno").setValue(mob.getText().toString());

                    startActivity(new Intent(getApplicationContext(),Dashbord.class).putExtra("userid",userid).putExtra("username",name.getText().toString()));
                    finish();
                }
            }
        });



        getData();
    }
    DatabaseReference dref;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Dashbord.class).putExtra("userid",userid).putExtra("username",name.getText().toString()));
        finish();
    }

    void getData()
    {
        dref = FirebaseDatabase.getInstance().getReference("UserData").child(userid);

        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);

                name.setText(ud.username);
                mob.setText(ud.mobileno);


                long p = (long)Double.parseDouble(ud.points);
                point.setText(String.valueOf(p));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}