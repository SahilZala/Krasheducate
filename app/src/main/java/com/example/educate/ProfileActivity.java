package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView mail,mobile,uname;
    ImageView profile;
    ImageButton update_username,update_email,update_mobileno,logout_profile2;

    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);


        uname = findViewById(R.id.user_name_profile2);
        mail = findViewById(R.id.user_mail_profile2);
        mobile = findViewById(R.id.user_mobileno_profile2);
        profile = findViewById(R.id.user_image_profile2);
        logout_profile2 = findViewById(R.id.logout_profile2);

        update_username = findViewById(R.id.update_username_profile2);
        update_email = findViewById(R.id.email_update_profile2);
        update_mobileno = findViewById(R.id.mobileno_update_profile2);

        getNotificationColor();
        initToolbar();

        logout_profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        update_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSignout();
            }
        });

        update_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailUpdateDialog();
            }
        });

        update_mobileno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMobileUpdateDialog();
            }
        });



        getData();
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

    DatabaseReference user_data_ref;

    void getData()
    {
        user_data_ref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());

        user_data_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);
                uname.setText(ud.username);
                mobile.setText(ud.mobileno);
                mail.setText(ud.mail);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showDialogSignout() {

        ImageButton cancel_dialog;
        MaterialButton update;
        TextInputEditText username;

    //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.username_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        username = dialog.findViewById(R.id.user_name_update);
        update = dialog.findViewById(R.id.username_update);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty())
                {
                    Toast.makeText(ProfileActivity.this, "Please eneter some text!", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
                    dref.child("username").setValue(username.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getData();
                            dialog.cancel();
                            Toast.makeText(ProfileActivity.this, "User name updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


//        Picasso.get().load(getImageUrl()).into(profile);
//        username.setText(getUserName());
//        usergmail.setText(getUserGmail());

//        ((Button) dialog.findViewById(R.id.sign_out)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressBar.setVisibility(View.VISIBLE);
//                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful())
//                        {
//                            db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
//                            db.execSQL("drop table userdata;");
//
//
//
//                            progressBar.setVisibility(View.INVISIBLE);
//                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                            finish();
//                            Toast.makeText(getApplicationContext(), "Button Sign Out Clicked", Toast.LENGTH_SHORT).show();
//                        }else
//                        {
//                            progressBar.setVisibility(View.INVISIBLE);
//                            Toast.makeText(getApplicationContext(), "!Opps,Sorry", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        });
//
//        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.cancel();
//                Toast.makeText(getApplicationContext(), "Button Cancel Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });


        dialog.show();
    }


    private void showEmailUpdateDialog() {

        ImageButton cancel_dialog;
        MaterialButton update;
        TextInputEditText mail;

        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.update_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        mail = dialog.findViewById(R.id.user_email_update);
        update = dialog.findViewById(R.id.useremail_update);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mail.getText().toString().isEmpty())
                {
                    Toast.makeText(ProfileActivity.this, "Please eneter some text!", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
                    dref.child("mail").setValue(mail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getData();
                            dialog.cancel();
                            Toast.makeText(ProfileActivity.this, "email updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }


    private void showMobileUpdateDialog() {

        ImageButton cancel_dialog;
        MaterialButton update;
        TextInputEditText mobileno;

        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.mobileno_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        mobileno = dialog.findViewById(R.id.user_mobileno_update);
        update = dialog.findViewById(R.id.mobileno_update);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mobileno.getText().toString().isEmpty())
                {
                    Toast.makeText(ProfileActivity.this, "Please eneter some text!", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
                    dref.child("mobileno").setValue(mobileno.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getData();
                            dialog.cancel();
                            Toast.makeText(ProfileActivity.this, "Mobileno updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }


    private void logout() {

        ImageButton cancel_dialog;
        MaterialButton update;


        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.logout_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        update = dialog.findViewById(R.id.logout);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
                db.execSQL("drop table userdata;");

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        dialog.show();
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

}