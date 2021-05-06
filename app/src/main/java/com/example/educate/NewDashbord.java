package com.example.educate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewDashbord extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout layout_dots;
    private AdapterImageSlider adapterImageSlider;
    private Runnable runnable = null;
    private Handler handler = new Handler();
    FloatingActionButton whatsapp, fb, youtube, insta;
    SQLiteDatabase db;

    DatabaseReference noticeref, noticelinkref;

    TextView new_name;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int ACCESS_COARSE_LOCATION = 102;
    private static final int REQUEST_LOCATION = 123;

    LinearLayout learning, setting, points, noti, logout;

    LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserData();


        getNotificationColor();


    }




    private void initComponent() {


        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<String>());

        final List<String> items = new ArrayList<>();
        //for (int i = 0; i < 4; i++) {
        //obj.imageDrw = getResources().getDrawable(obj.image);
        items.add("https://inclusive-solutions.com/wp-content/uploads/2020/08/75729614.cms_.jpeg");
        items.add("https://1.cms.s81c.com/sites/default/files/2018-09-13/education.jpg");
        items.add("https://www.orfonline.org/wp-content/uploads/2020/12/Education.jpg");
        items.add("https://images.pexels.com/photos/5088017/pexels-photo-5088017.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        items.add("https://images.indianexpress.com/2020/03/classroom.jpg");

        //}

        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAutoSlider(adapterImageSlider.getCount());
    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
        }
    }

    private void startAutoSlider(final int count) {
        runnable = new Runnable() {
            @Override
            public void run() {
                int pos = viewPager.getCurrentItem();
                pos = pos + 1;
                if (pos >= count) pos = 0;
                viewPager.setCurrentItem(pos);
                handler.postDelayed(runnable, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //  finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//
//        Geocoder geocoder;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//
//            AddressClass ac = new AddressClass(addresses.get(0).getAddressLine(0),addresses.get(0).getAddressLine(0));
//            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Address").child(getOUid());
//            dref.child("address").setValue(ac);
//
//
//      //      Toast.makeText(this, ""+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//

    private static class AdapterImageSlider extends PagerAdapter {

        private Activity act;
        private List<String> items;

        private AdapterImageSlider.OnItemClickListener onItemClickListener;

        private interface OnItemClickListener {
            void onItemClick(View view, String obj);
        }

        public void setOnItemClickListener(AdapterImageSlider.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        // constructor
        private AdapterImageSlider(Activity activity, List<String> items) {
            this.act = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return this.items.size();
        }

        public String getItem(int pos) {
            return items.get(pos);
        }

        public void setItems(List<String> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final String o = items.get(position);
            LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.item_slider_image, container, false);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);

            Picasso.get().load(o).into(image);

            //Tools.displayImageOriginal(act, image, o.image);
            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, o);
                    }
                }
            });

            ((ViewPager) container).addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);

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

    void getNotificationColor()
    {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.sky_blue));
        }
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
                dialog.cancel();
                finish();
            }
        });

        dialog.show();
    }


    DatabaseReference name_ref;
    void fetchData()
    {
        name_ref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());

        name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData ud = snapshot.getValue(UserData.class);
                new_name.setText(ud.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

            if(requestCode == REQUEST_LOCATION) {

                @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                Geocoder geocoder;
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    AddressClass ac = new AddressClass(addresses.get(0).getAddressLine(0), addresses.get(0).getAddressLine(0));
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Address").child(getOUid());
                    dref.child("address").setValue(ac);


                    //      Toast.makeText(this, ""+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }




        }
    }


    void getUserData(){

        try {
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserData").child(getOUid());
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot != null) {

                        UserData ud =  snapshot.getValue(UserData.class);

                        if(ud != null) {
                            if (ud.getActivation().equalsIgnoreCase("true") || ud.getActivation().equalsIgnoreCase("active") || ud.getActivation().equalsIgnoreCase("notnotice")) {

                                setContentView(R.layout.activity_new_dashbord);


                                insta = findViewById(R.id.insta);

                                insta.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri uriUrl = Uri.parse("https://www.graphicsera.org/dm/");
                                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                        startActivity(launchBrowser);
                                    }
                                });

                                FirebaseMessaging.getInstance().subscribeToTopic("all");


                                initComponent();


                                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||

                                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||

                                        ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(NewDashbord.this,

                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,

                                                    Manifest.permission.ACCESS_COARSE_LOCATION,

                                                    Manifest.permission.CAMERA,

                                                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_LOCATION);

                                } else {

                                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                                    Geocoder geocoder;
                                    geocoder = new Geocoder(NewDashbord.this, Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                                        AddressClass ac = new AddressClass(addresses.get(0).getAddressLine(0),addresses.get(0).getAddressLine(0));
                                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Address").child(getOUid());
                                        dref.child("address").setValue(ac);


                                        //      Toast.makeText(this, ""+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                    //    System.out.println("Location permissions available, starting location");

                                }



                                new_name = findViewById(R.id.new_username);
                                learning = findViewById(R.id.learning);
                                setting = findViewById(R.id.setting);
                                points = findViewById(R.id.points);
                                noti = findViewById(R.id.notice);
                                logout = findViewById(R.id.logout);

                                fetchData();
                                learning.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getApplicationContext(), SubjectActivity.class));
                                    }
                                });

                                setting.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getApplicationContext(), NewProfileActivity.class));
                                        finish();
                                    }
                                });

                                noti.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getApplicationContext(), Notification.class));
                                    }
                                });

                                logout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        logout();
                                    }
                                });

                                points.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                FcmNotificationsSender fns = new FcmNotificationsSender("/topics/all","title","sahil hii",getApplicationContext(),NewDashbord.this);
//
//                fns.SendNotifications();
                                        startActivity(new Intent(getApplicationContext(), PointsActivity.class));
                                    }
                                });


                            } else {

                                //System.exit(0);
                                Toast.makeText(NewDashbord.this, "you are block by the admin, for more information please contact to admin", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
                            db.execSQL("drop table userdata;");

                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           // dialog.cancel();
                            finish();

                            Toast.makeText(NewDashbord.this, "Your account is deleted from server, please call to admin", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        db = openOrCreateDatabase("UserData", MODE_PRIVATE, null);
                        db.execSQL("drop table userdata;");

                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                        dialog.cancel();
                        finish();

                        Toast.makeText(NewDashbord.this, "Your account is deleted from server, please call to admin", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception ex)
        {

        }
    }
}