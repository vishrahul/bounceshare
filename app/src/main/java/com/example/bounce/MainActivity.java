package com.example.bounce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bounce.database.constraint;
import com.example.bounce.database.firstmanager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    double current_lat, current_lang;
    String current_time;

    Marker mCurrLocationMarker;
    ArrayList<lat_lang_class> arrayList;
    lat_lang_class lat_lang = new lat_lang_class();

    FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    LocationRequest locationRequest;

    ListView recyclerview;


    //************************ Sqlite Database ************************//
    SQLiteDatabase sb;


    ArrayList<lat_lang_class> viewlist;
    lat_lang_class vw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                1);

        firstmanager cm = new firstmanager(MainActivity.this);

        //************************ Open Sqlite Database ************************//
        sb = cm.openDB();


        arrayList = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerview);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(8000);
        locationRequest.setFastestInterval(4000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {

                    System.out.println("getLatitude== " + location.getLatitude());

                    lat_lang = new lat_lang_class();
                    lat_lang.setLat(String.valueOf(location.getLatitude()));
                    lat_lang.setLang(String.valueOf(location.getLongitude()));
                    arrayList.add(lat_lang);

                    current_lat = location.getLatitude();
                    current_lang = location.getLongitude();


                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MainActivity.this);


                    Date currentTime = Calendar.getInstance().getTime();
                    System.out.println("currentTime = " + currentTime);

                    current_time = String.valueOf(currentTime);
                    System.out.println("currentTime1111 = " + currentTime);


                    //************************* save data in to SQlite

                    try {

                        ContentValues cv = new ContentValues();
                        cv.put(constraint.COL_LAT, current_lat);
                        cv.put(constraint.COL_LANG, current_lang);
                        cv.put(constraint.COL_TIME, String.valueOf(currentTime));


                        long l = sb.insert(constraint.TBL_NAME, null, cv);
                        System.out.println("save sqlite" + l);

                        if (l > 0) {

                            System.out.println("save sqlite successfully");
                            get_data();

                        }


                    } catch (Exception e) {


                        e.printStackTrace();
                    } finally {


                    }


                }
            }
        });


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {


                    System.out.println("sdsddsfdf2222 " + location.getLongitude());


                    lat_lang.setLat(String.valueOf(location.getLatitude()));
                    lat_lang.setLang(String.valueOf(location.getLongitude()));
                    arrayList.add(lat_lang);

                    current_lat=location.getLatitude();
                    current_lang=location.getLongitude();

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MainActivity.this);

                    Date currentTime = Calendar.getInstance().getTime();
                    System.out.println("currentTime = " + currentTime);

                    current_time = String.valueOf(currentTime);
                    System.out.println("currentTime222 = " + currentTime);

                    //************************* save data in to SQlite

                    try {

                        ContentValues cv = new ContentValues();
                        cv.put(constraint.COL_LAT, current_lat);
                        cv.put(constraint.COL_LANG, current_lang);
                        cv.put(constraint.COL_TIME, String.valueOf(currentTime));


                        long l = sb.insert(constraint.TBL_NAME, null, cv);
                        System.out.println("save sqlite" + l);

                        if (l > 0) {

                            System.out.println("save sqlite successfully");
                            get_data();

                        }


                    } catch (Exception e) {


                        e.printStackTrace();
                    } finally {


                    }

                }
            }
        };


        //******************************* fetch data sqlite
        get_data();


        //******************************* oncreate closing here................
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMapLoadedCallback(() -> {

            System.out.println("inside currentTime = " + current_time);
            System.out.println("inside onMapReady current_lat = " + current_lat);

            LatLng origin = new LatLng(current_lat, current_lang);
            LatLng zoom = new LatLng(current_lat, current_lang);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(origin);
            markerOptions.title("BounceShare");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.clear();
            mCurrLocationMarker = mMap.addMarker(markerOptions);


//            LatLng sydney = new LatLng(current_lat, current_lang);
//            googleMap.addMarker(new MarkerOptions()
//                    .position(sydney)
//                    .title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        });


    }


    public void get_data() {

        viewlist = new ArrayList<>();

        viewlist.clear();

        Cursor c = sb.query(constraint.TBL_NAME, null, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            do {
                String lat = c.getString(c.getColumnIndex(constraint.COL_LAT));
                String lang = c.getString(c.getColumnIndex(constraint.COL_LANG));
                String time = c.getString(c.getColumnIndex(constraint.COL_TIME));


                vw = new lat_lang_class();
                vw.setLat(lat);
                vw.setLang(lang);
                vw.setTime(time);


                viewlist.add(vw);
            } while (c.moveToNext());
            c.close();
        } else {

        }


        myadapter md = new myadapter(MainActivity.this, viewlist);
        recyclerview.setAdapter(md);

    }


    @Override
    protected void onResume() {
        super.onResume();

        startLocationUpdates();

    }

    @Override
    protected void onPause() {
        super.onPause();

        startLocationUpdates();

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync( MainActivity.this);


        }catch (Exception e){

        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {





                } else {


                    Toast.makeText(MainActivity.this, "Permission denied!!", Toast.LENGTH_SHORT).show();
                }


                return;


            }



        }


    }




}