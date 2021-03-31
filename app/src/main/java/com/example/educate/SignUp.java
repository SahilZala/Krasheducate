package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    TextInputEditText username,usermobile,password;
    Button submit_button;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getNotificationColor();

        username = findViewById(R.id.user_name);
        usermobile = findViewById(R.id.mobile_no);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.spin_kit);

        submit_button = findViewById(R.id.submit_button);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().length() >= 8) {
                    if(usermobile.getText().toString().length() == 10) {

                        progressBar.setVisibility(View.VISIBLE);

                        checkData();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Enter proper mobileno", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Password should be at least greater than 8 length", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
    DatabaseReference dref;
    void pushUserData(){

        if(!username.getText().toString().isEmpty() && !usermobile.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            dref = FirebaseDatabase.getInstance().getReference("UserData");
            String user_id = dref.push().getKey();

            UserData ud = new UserData(user_id, username.getText().toString(), usermobile.getText().toString(), password.getText().toString(),"abc@gmail.com","url", "time", "date", "0.0", "true","0","0");
            dref.child(user_id).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignUp.this, "User Created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            });
        }
        else{
            Toast.makeText(this, "Complete all field", Toast.LENGTH_SHORT).show();
        }
    }

    UserData userData;
    void checkData()
    {
        userData = null;
        dref  = FirebaseDatabase.getInstance().getReference("UserData");
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    UserData ud = ds.getValue(UserData.class);
                    if(ud.getMobileno().equals(usermobile.getText().toString()))
                    {
                        userData = ud;
                    }
                }

                if(userData != null)
                {
                    Toast.makeText(getApplicationContext(), "Mobile number already present,\n Use different number", Toast.LENGTH_LONG).show();
                   // startActivity(new Intent(getApplicationContext(),Dashbord.class).putExtra("username",userData.getUsername()).putExtra("userid",userData.userid));
                }
                else
                {
                    pushUserData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}