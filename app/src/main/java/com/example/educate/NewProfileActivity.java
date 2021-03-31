package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

public class NewProfileActivity extends AppCompatActivity {

    CardView refer_earn_card,logout_card;

    TextView username,useremail,points;
    TextView video,nots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        refer_earn_card = findViewById(R.id.refer_earn);

        logout_card = findViewById(R.id.logout_card);
        username = findViewById(R.id.new_profile_user_name);
        useremail = findViewById(R.id.new_profile_user_email);
        points = findViewById(R.id.new_profile_user_points);

        video = findViewById(R.id.video_c);
        nots = findViewById(R.id.nots_c);

        refer_earn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refer_earn(getOUid());
            }
        });

        useremail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showEmailUpdateDialog();
            }
        });

        logout_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });



        getNotificationColor();
        initToolbar();
        getCurrentUserData();
        fetcWatchData();
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(),NewDashbord.class));
        finish();
    }
    private Toolbar toolbar;

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#040507'>My Profile</font>"));
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


    private void refer_earn(String referid) {

        ImageButton cancel_dialog,copy_button;

        TextView refer_id;
        TextInputEditText refer_edit_text;
        MaterialButton refer_submit_button;


        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.refer_and_earn_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        refer_edit_text = dialog.findViewById(R.id.enter_refer_id);
        refer_id = dialog.findViewById(R.id.refer_id);
        copy_button = dialog.findViewById(R.id.copy_icon);
        refer_submit_button = dialog.findViewById(R.id.submit_refer_id);

        refer_id.setText(referid);

        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("refer id",refer_id.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(NewProfileActivity.this, "Copy clip board "+refer_id.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        refer_submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!refer_edit_text.getText().toString().isEmpty()) {
                    if (!refer_edit_text.getText().toString().equals(getOUid())) {

                        checkReferClass(refer_edit_text.getText().toString());
                       // dialog.cancel();
                    } else {
                        Toast.makeText(NewProfileActivity.this, "You cant copy same user id", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Enter something", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
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
                        Toast.makeText(NewProfileActivity.this, "You already referd by him", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(NewProfileActivity.this, "done", Toast.LENGTH_SHORT).show();



                                        getReferIdUserData(referbyid);
                                    }
                                });
                            }
                            else{
                                Toast.makeText(NewProfileActivity.this, "Multiple ref not allowed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(NewProfileActivity.this, "wrong ref id", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(NewProfileActivity.this, "referencing done succesfully", Toast.LENGTH_SHORT).show();

                        getCurrentUserData();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    void getCurrentUserData()
    {
        DatabaseReference profileref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());

        profileref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);

                if(ud != null)
                {
                    username.setText(ud.getUsername());
                    useremail.setText(ud.getMail());
                    long p = (long)Double.parseDouble(ud.points);
                    points.setText(String.valueOf(p));
                }
                else{
                    Toast.makeText(NewProfileActivity.this, "Data Not Found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Toast.makeText(getApplicationContext(), "Please eneter some text!", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
                    dref.child("mail").setValue(mail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            getCurrentUserData();
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "email updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        dialog.show();
    }


    int video_c = 0,nots_c = 0;


    void fetcWatchData()
    {
        DatabaseReference watchref = FirebaseDatabase.getInstance().getReference("TopicCount").child(getOUid());

        watchref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    TopicCount tc = ds.getValue(TopicCount.class);
                    if(tc.getType().equalsIgnoreCase("video"))
                    {
                        video_c++;
                    }
                    else{
                        nots_c++;
                    }
                }

                video.setText(String.valueOf(video_c));
                nots.setText(String.valueOf(nots_c));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}