package garg.sarthik.gosafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    public static final int requestCodeForPermission = 12345;
    LocationManager locationManager;
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Dilshad Garden", 20));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Connaught Place", 23));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Rithala", 25));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Chandni Chowk", 55));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Indrapuram", 30));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Vaishali", 20));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Mohan Nagar", 51));

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {

        /*Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());*/

        Log.e("TAG", "long: " + location.getLongitude());
        Log.e("TAG", "lat " + location.getLatitude());
        tvAddress.setText("" + location.getLatitude());
        tvCity.setText("" + location.getLongitude());

       /* try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();


            tvState.setText(state);
            tvPostalCOde.setText(postalCode);
            tvKnownName.setText(knownName);
            tvCountry.setText(country);
            tvCity.setText(city);
            tvAddress.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e)
        {
            Log.e("TAG", "FUCK");
            e.printStackTrace();
        }*/

       /* for(AccidentData data: accidentDataArrayList)
        {

        }*/

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
