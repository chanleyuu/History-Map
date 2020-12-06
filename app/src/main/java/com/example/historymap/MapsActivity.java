package com.example.historymap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    final private int REQUEST_COARSE_ACCESS = 123;
    boolean permissionGranted = false;
    LocationManager lm;
    LocationListener locationListener;
    DatabaseHelper db;
    CustomInfoWindowAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_COARSE_ACCESS);
            return;
        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                } else {
                    permissionGranted = false;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        db = new DatabaseHelper(this);

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },REQUEST_COARSE_ACCESS);
            return;

        } else {
            permissionGranted = true;
        }
        if (permissionGranted) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, locationListener);
        }

        //double locs[] = { 0.0, 0.0 };
        //boolean e = db.addlocation(35.0, -112.2, "Desert Location");
        //if (e) {

       // LatLng p = new LatLng(locs[0], locs[1]);
       // mMap.addMarker(new MarkerOptions().position(p).title("Delivery Location")
          //      .icon(BitmapDescriptorFactory
            //            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        location loc = new location();
        loc.name = "The invention of history";
        loc.locx = 54.993505;
        loc.locy = -7.33697;
        loc.description = "It was at this place that" +
                " Johnathan Swift, the inventor of the Swift programming language" +
                " got the idea to write" +
                " things that happened down";
        loc.image = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Jonathan_Swift_by_Charles_Jervas_detail.jpg/330px-Jonathan_Swift_by_Charles_Jervas_detail.jpg";
        //  loc = db.getlocation(location.getLatitude(), location.getLongitude());
        //            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        location loc2 = new location();
        loc2.name = "The death of Cu Culainn ";
        loc2.locx = 53.974505;
        loc2.locy = 6.465919;
        loc2.description = "It was at this aproximate place that" +
                " The hero Cu Chulainn died after being struck " +
                "by one of three spears each fortold to slay a king " +
                "he tied himself to a rock so that he may die standing up.";
        loc2.image = "https://upload.wikimedia.org/wikipedia/commons/4/4a/Cuchulainn%27s_death%2C_illustration_by_Stephen_Reid_1904.jpg";
        //  }
        //  }
        //  for (int i = 0; i < locs.size(); i++){
        LatLng q = new LatLng(loc.locx, loc.locy);
        LatLng r = new LatLng(loc2.locx, loc2.locy);
        MarkerOptions markeropt = new MarkerOptions();
        MarkerOptions markeropt2 = new MarkerOptions();
        markeropt = markeropt.position(q).title(loc.name)
                .snippet(loc.description)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
               /*    Marker marker = mMap.addMarker(new MarkerOptions().position(q).title(loc.name)
                           .snippet(loc.description)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))); */
        markeropt2 = markeropt2.position(r).title(loc2.name)
                .snippet(loc2.description)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        adapter = new CustomInfoWindowAdapter(this, loc.image);
        //CustomInfoWindowAdapter adapter2 = new CustomInfoWindowAdapter(this, loc2.image);

        mMap.setInfoWindowAdapter(adapter);
        //mMap.setInfoWindowAdapter(adapter2);

        mMap.addMarker(markeropt).showInfoWindow();
        mMap.addMarker(markeropt2);

    }

    @Override
    public boolean onMarkerClick(Marker marker){
        System.out.println(marker.getId());
        if(marker.getId().equals("m0")){
            try {
                adapter.SetImage("https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Jonathan_Swift_by_Charles_Jervas_detail.jpg/330px-Jonathan_Swift_by_Charles_Jervas_detail.jpg");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(marker.getId().equals("m1")){
            try {
                adapter.SetImage("https://upload.wikimedia.org/wikipedia/commons/4/4a/Cuchulainn%27s_death%2C_illustration_by_Stephen_Reid_1904.jpg");
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        marker.showInfoWindow();
        return true;
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                Toast.makeText(getBaseContext(),
                        "Current Location : Lat: " + location.getLatitude() +
                                " Lng: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                LatLng p = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(p).title("Current Location")
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(p, 12.0f));
                ArrayList<location> locs = new ArrayList<location>();
             //   for (int i = -10; i < 10; i++) {
             //       for(int e = -10; e < 10; e++) {
                /*
                        location loc = new location();
                        loc.name = "The invention of history";
                        loc.locx = 54.993505;
                        loc.locy = -7.33697;
                        loc.description = "It was at this place that" +
                                " Johnathan Swift inventor of the Swift programming language" +
                                " got the idea to write" +
                                " things that happened down";
                      //  loc = db.getlocation(location.getLatitude(), location.getLongitude());
                        if (loc != null) {
                            locs.add(loc);
                        }
                  //  }
              //  }
              //  for (int i = 0; i < locs.size(); i++){
                    LatLng q = new LatLng(loc.locx, loc.locy);
                       MarkerOptions markeropt = new MarkerOptions();
                       markeropt = markeropt.position(q).title(loc.name)
                               .snippet(loc.description)
                               .icon(BitmapDescriptorFactory
                                       .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                   Marker marker = mMap.addMarker(new MarkerOptions().position(q).title(loc.name)
                           .snippet(loc.description)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(this);
                mMap.setInfoWindowAdapter(adapter);

                mMap.addMarker(markeropt).showInfoWindow(); */
                }
           // }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }


    }
}