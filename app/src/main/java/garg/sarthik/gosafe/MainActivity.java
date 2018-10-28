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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    public static final int requestCodeForPermission = 12345;
    public static final String CHANNEL_ID = "420";

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvAddress = findViewById(R.id.tvAddress);
        tvCity = findViewById(R.id.tvCity);
        tvCountry = findViewById(R.id.tvCountry);
        tvKnownName = findViewById(R.id.tvKnownName);
        tvPostalCOde = findViewById(R.id.tvPostalCOde);
        tvState = findViewById(R.id.tvState);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_us) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            // Handle the camera action
        } else if (id == R.id.emergency_contacts) {

        } else if (id == R.id.near_by_places) {

        } else if (id == R.id.about_us){

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        double latitude = Double.valueOf((""+location.getLatitude()).substring(0,5));
        double longitude = Double.valueOf((""+location.getLongitude()).substring(0,5));

        Log.e("TAG", "long: " + longitude);
        Log.e("TAG", "lat " + latitude);

        tvPostalCOde.setText(""+latitude);
        tvKnownName.setText(""+longitude);

        for(AccidentData data: accidentDataArrayList)
        {
            Log.e("TAG", "for lat: " + data.getLocality());
            if(longitude == data.getLongitude() && latitude == data.getLatitude())
            {
                tvAddress.setText(""+data.getPercentage());
                tvCity.setText(data.getCity());
                tvState.setText(data.getState());
                tvCountry.setText(data.getLocality());

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
