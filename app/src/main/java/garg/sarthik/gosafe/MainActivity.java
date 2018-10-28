package garg.sarthik.gosafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements  LocationListener {

    public static final int requestCodeForPermission = 12345;
    public static final String CHANNEL_ID = "420";
    double latitude;
    double longitude;


    LocationManager locationManager;
    NotificationManager notificationManager;

    ArrayList<AccidentData> accidentDataArrayList = new ArrayList<>();

    TextView tvCity;
    TextView tvAddress;
    TextView tvCountry;
    TextView tvPostalCOde;
    TextView tvState;
    TextView tvKnownName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAddress = findViewById(R.id.tvAddress);
        tvCity = findViewById(R.id.tvCity);
        tvCountry = findViewById(R.id.tvCountry);
        tvKnownName = findViewById(R.id.tvKnownName);
        tvPostalCOde = findViewById(R.id.tvPostalCOde);
        tvState = findViewById(R.id.tvState);

        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Dilshad Garden",28.68,77.31, 20));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Connaught Place",28.63,77.21, 23));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Rithala",28.73,77.11, 25));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Chandni Chowk",28.65,77.22, 55));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Indrapuram",28.63,77.36, 30));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Vaishali",28.64,77.33, 20));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Mohan Nagar",28.67,77.39, 51));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeForPermission);

        } else {
            Toast.makeText(this, "Already have the Permission", Toast.LENGTH_SHORT).show();
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Location is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                locationSuccess();
            }
        }
        ViewPager vp = findViewById(R.id.vpFrags);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vp);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Dashboard";
                case 1:
                    return "Emergency Contacts";
                case 2:
                    return "Near Places";
            }
            return "";
        }
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new Frag_Dashboard();
                case 1:
                    return new Frag_EmergencyContact();
                case 2:
                    return new Frag_NearPlaces();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("TAG", "long: " + location.getLongitude());
        Log.e("TAG", "lat " + location.getLatitude());

        latitude = Double.valueOf((""+location.getLatitude()).substring(0,5));
        longitude = Double.valueOf((""+location.getLongitude()).substring(0,5));

        Log.e("TAG", "long: " + longitude);
        Log.e("TAG", "lat " + latitude);

    //    tvPostalCOde.setText(""+latitude);
    //    tvKnownName.setText(""+longitude);

        for(AccidentData data: accidentDataArrayList)
        {
            Log.e("TAG", "for lat: " + data.getLocality());
            if(longitude == data.getLongitude() && latitude == data.getLatitude())
            {
//                tvAddress.setText(""+data.getPercentage());
//                tvCity.setText(data.getCity());
//                tvState.setText(data.getState());
//                tvCountry.setText(data.getLocality());

                if(data.getPercentage() >= 50) {
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                                "Default Channel",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivities(MainActivity.this, 123, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_warning)
                            .setContentTitle("DRIVE SAFE")
                            .setContentText("You are at " + data.getLocality() + " where the accident rate is " + data.getPercentage() + "%.")
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
                    notificationManager.notify(101, notification);
                }
                break;
            }
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Thank you for the permission", Toast.LENGTH_SHORT).show();
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Location is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                locationSuccess();
            }
        } else {

            Toast.makeText(this,"App Need Permissions to work!!!",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    public void locationSuccess() {

        Log.e("TAG", "locationSuccess: ");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }
}
