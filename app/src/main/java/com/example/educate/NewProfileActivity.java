package com.example.educate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.FoldingCube;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class NewProfileActivity extends AppCompatActivity {

    CardView refer_earn_card,logout_card,comment_feedback;

    TextView username,useremail,points;
    TextView video,nots;
    CircularImageView profile_image;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        refer_earn_card = findViewById(R.id.refer_earn);

        logout_card = findViewById(R.id.logout_card);
        username = findViewById(R.id.new_profile_user_name);
        useremail = findViewById(R.id.new_profile_user_email);
        points = findViewById(R.id.new_profile_user_points);

        comment_feedback = findViewById(R.id.comment_feedback);
        profile_image = findViewById(R.id.profile_image);

        video = findViewById(R.id.video_c);
        nots = findViewById(R.id.nots_c);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        FoldingCube foldingCube = new FoldingCube();
        progressBar.setIndeterminateDrawable(foldingCube);

        progressBar.setVisibility(View.INVISIBLE);



        comment_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment();

            }
        });

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

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
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


    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private void refer_earn(String referid) {

        ImageButton cancel_dialog,copy_button;

        TextView refer_id,scan_code;
        TextInputEditText refer_edit_text;
        MaterialButton refer_submit_button;
        ImageView refer_qr;



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
        refer_qr = dialog.findViewById(R.id.refer_qr);
        scan_code = dialog.findViewById(R.id.scan_qr_code);


        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        // getting width and
        // height of a point
        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(referid, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            refer_qr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }




        refer_id.setText(referid);





        scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScanneQRCode.class));
                finish();
            }
        });



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

                    if(!ud.getProfile().equalsIgnoreCase("url")){
                        Picasso.get().load(ud.getProfile()).into(profile_image);
                    }
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


    private void comment() {

        ImageButton cancel_dialog;
        MaterialButton update;
        TextInputEditText comment;


        //    CircularImageView profile;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.comment_review);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        cancel_dialog = dialog.findViewById(R.id.cancel_dialog);
        update = dialog.findViewById(R.id.submit);

        comment = dialog.findViewById(R.id.comment);

        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Comment c = new Comment(getOUid(),comment.getText().toString(),"time","date","true");

                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Comment").child(getOUid());
                dref.setValue(c);

                dialog.cancel();
                Toast.makeText(NewProfileActivity.this, "comment posted", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.show();
    }

    Uri filepath=null;
    StorageReference sr;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    Integer REQUEST_CAMERA=1,SELECT_FILE=0;


    void selectImage()
    {
      /*  Intent i = new Intent();
        i.setType("Image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select an Image"),ChooseImage);
    */

        final CharSequence[] items={"Camera","Gallery","Cancel"};

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera"))
                {
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                    }
                    else{
                        ActivityCompat.requestPermissions(NewProfileActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA);

                    }
                }else if(items[which].equals("Gallery")){
                    Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Select File"),SELECT_FILE);

                }else if(items[which].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        }).create().show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    filepath = data.getData();
                    Bundle bundle = data.getExtras();
                    final Bitmap bitmap = (Bitmap) bundle.get("data");
                    profile_image.setImageBitmap(bitmap);

                    filepath = (Uri) getImageUri(NewProfileActivity.this, bitmap);

                    uploadImage();
                } else if (requestCode == SELECT_FILE) {
                    filepath = data.getData();
                    profile_image.setImageURI(filepath);
                    uploadImage();
                   // Toast.makeText(this, ""+filepath, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri getImageUri(NewProfileActivity applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        String path= MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(),bitmap,"Title",null);
        return Uri.parse(path);

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void uploadImage()
    {
        progressBar.setVisibility(View.VISIBLE);
        if(filepath != null) {
            //S111111111111111torageReference riversRef = sr.child("images/"+filepath.getLastPathSegment());
            // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));

            sr = FirebaseStorage.getInstance().getReference();

            StorageReference riversRef = sr.child("images/" + filepath.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(filepath);


// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(NewProfileActivity.this, "some thing wrong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressBar.setVisibility(View.INVISIBLE);

                    //Uri u = taskSnapshot.getUploadSessionUri();
                    //String s = u.toString();
                    //pname.setText(s);

                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.


                    // ...
                    final String[] s = {""};
                    sr.child("images/" + filepath.getLastPathSegment()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            s[0] = task.getResult().toString();

                            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid()).child("profile");
                            dref.setValue(s[0]);

                          //  sendDataToFirebase(s[0]);

                            //prsnap = s[0];
                            //pname.setText(s[0]);

                            //ProductClass pc = new ProductClass(prname,prid,prdiscription,prtype,prrice,prquantity,prdate,prtime,prdiscount,prsnap,practivation);
                            //dref.child(prid).setValue(pc);

                        }
                    });

                    StorageMetadata downloadUri = taskSnapshot.getMetadata();


                }
            });
        }
        else if(filepath == null)
        {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "select file first", Toast.LENGTH_SHORT).show();
//            if(activity.equalsIgnoreCase("edit"))
//            {
//               // sendDataToFirebase(teacherClassval.getTeachersnap());
//            }
//            else
//            {
//               // sendDataToFirebase("snap");
//            }
        }
    }

}