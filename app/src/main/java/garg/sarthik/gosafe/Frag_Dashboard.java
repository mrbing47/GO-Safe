package garg.sarthik.gosafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Frag_Dashboard extends Fragment implements LocationListener{


    TextView tvCity;
    TextView tvAddress;
    TextView tvState;
    TextView tvLocality;
    TextView tvPostal;
    TextView tvKnown;
    TextView tvCountry;

    double longitude;
    double latitude;

    int currentTime;

    public static final int requestCodeForPermission = 12345;
    public static final String CHANNEL_ID = "420";

    ArrayList<AccidentData> accidentDataArrayList = new ArrayList<>();
    LocationManager locationManager;
    NotificationManager notificationManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_dashboard,container,false);

        tvAddress = view.findViewById(R.id.tvAddress);
        tvCity = view.findViewById(R.id.tvCity);
        tvKnown = view.findViewById(R.id.tvKnownName);
        tvLocality = view.findViewById(R.id.tvCountry);
        tvPostal = view.findViewById(R.id.tvPostalCOde);
        tvState = view.findViewById(R.id.tvState);
        tvCountry = view.findViewById(R.id.tvCountry);

        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Dilshad Garden",28.68,77.31, 20));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Connaught Place",28.63,77.21, 23));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Rithala",28.73,77.11, 25));
        accidentDataArrayList.add(new AccidentData("Delhi", "Delhi", "Chandni Chowk",28.65,77.22, 55));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Indrapuram",28.63,77.36, 30));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Vaishali",28.64,77.33, 20));
        accidentDataArrayList.add(new AccidentData("Uttar Pradesh", "Ghaziabad", "Mohan Nagar",28.67,77.39, 51));

        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeForPermission);

        } else {
            Toast.makeText(getContext(), "Already have the Permission", Toast.LENGTH_SHORT).show();
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getContext(), "Location is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                locationSuccess();
            }
        }


        return view;

    }

    public void onLocationChanged(android.location.Location location) {

        Log.e("TAG", "long: " + location.getLongitude());
        Log.e("TAG", "lat " + location.getLatitude());


        latitude = Double.valueOf((""+location.getLatitude()).substring(0,5));
        longitude = Double.valueOf((""+location.getLongitude()).substring(0,5));
        tvPostal.setText(""+latitude);
        tvKnown.setText(""+longitude);

        Log.e("TAG", "long: " + longitude);
        Log.e("TAG", "lat " + latitude);

        //    tvPostalCOde.setText(""+latitude);
        //    tvKnownName.setText(""+longitude);
        currentTime = Integer.parseInt(Calendar.getInstance().getTime().toString().substring(11,13));

        Log.e("TAG", "onLocationChanged: "+ (currentTime+5) );
        for(AccidentData data: accidentDataArrayList)
        {
            Log.e("TAG", "for lat: " + data.getLocality());
            if(longitude == data.getLongitude() && latitude == data.getLatitude())
            {
                int increaseRisk = 0;
                if(currentTime >= 18 && currentTime <= 7)
                    increaseRisk +=2;
                if(location.getSpeed() * 3.6 >= 60)
                    increaseRisk += 3;
                if(data.getPercentage() + increaseRisk >= 50) {

                    tvState.setText(data.getState());
                    tvCity.setText(data.getCity());
                    tvAddress.setText(""+data.getPercentage());
                    tvCountry.setText(data.getLocality());

                    notificationManager = (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                                "Default Channel",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }

                    Intent intent = new Intent(getContext(), MainActivity.class);

                    PendingIntent pendingIntent = PendingIntent.getActivities(getContext(), 123, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Thank you for the permission", Toast.LENGTH_SHORT).show();
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getContext(), "Location is not enabled", Toast.LENGTH_SHORT).show();
            } else {
                locationSuccess();
            }
        } else {

            Toast.makeText(getContext(),"App Need Permissions to work!!!",Toast.LENGTH_LONG).show();

        }
    }

    @SuppressLint("MissingPermission")
    public void locationSuccess() {

        Log.e("TAG", "locationSuccess: ");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }
}
