package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    TextInputEditText username,mobileno;
    MaterialButton verify;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        //

        username = findViewById(R.id.user_name_forget_pass);
        mobileno = findViewById(R.id.mobile_no_forget);
        verify = findViewById(R.id.submit_button_forget);
        progressBar = findViewById(R.id.spin_kit);

        //

        initToolbar();
        getNotificationColor();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                checkData();
            }
        });





    }

    @Override
    public void onBackPressed() {

        finish();
    }
    private Toolbar toolbar;

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#040507'>Forget Password</font>"));
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


    UserData userData;
    DatabaseReference dref;
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
                    if(ud.getMobileno().equals(mobileno.getText().toString()) && ud.getUsername().toString().equals(username.getText().toString()))
                    {
                       // Toast.makeText(ForgotPassword.this, " ", Toast.LENGTH_SHORT).show();
                        userData = ud;
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                if(userData != null)
                {
                    if(userData.getMobileno().equals(mobileno.getText().toString()) && userData.getUsername().toString().equals(username.getText().toString()))
                    {
                        passwordUpdate();
                    }

                    //Toast.makeText(getApplicationContext(), "Mobile number already present,\n Use different number", Toast.LENGTH_LONG).show();
                    // startActivity(new Intent(getApplicationContext(),Dashbord.class).putExtra("username",userData.getUsername()).putExtra("userid",userData.userid));
                }
                else
                {
                    Toast.makeText(ForgotPassword.this, "No Matching Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    DatabaseReference password_change_ref;

    private void passwordUpdate() {

        ImageButton cancel_dialog;
        MaterialButton update;
        TextInputEditText password;


        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.password_change_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        password = dialog.findViewById(R.id.password_update);
        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        update = dialog.findViewById(R.id.password_update_button);


        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().length() >= 8) {

                    progressBar.setVisibility(View.VISIBLE);
                    password_change_ref = FirebaseDatabase.getInstance().getReference("UserData").child(userData.getUserid()).child("password");
                    password_change_ref.setValue(password.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            progressBar.setVisibility(View.INVISIBLE);
                            dialog.cancel();
                            Toast.makeText(ForgotPassword.this, "Password change succesfuly", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            dialog.cancel();
                            Toast.makeText(ForgotPassword.this, "Something Problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(ForgotPassword.this, "Lenght of password should be greater then 8", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

}