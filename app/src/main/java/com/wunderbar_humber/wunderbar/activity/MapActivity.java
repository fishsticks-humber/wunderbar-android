package com.wunderbar_humber.wunderbar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wunderbar_humber.wunderbar.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapLoadedCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String restaurantName;
    private String directionsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // get latitude and longitude from intent
        latitude = getIntent().getDoubleExtra("latitude", -34);
        longitude = getIntent().getDoubleExtra("longitude", 151);
        restaurantName = getIntent().getStringExtra("restaurant");
        directionsUrl = "https://www.google.com/maps/dir/?api=1&destination=" + latitude + "," + longitude;
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
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLoadedCallback(this);
    }

    @Override
    public void onMapLoaded() {
        LatLng restaurantLocation = new LatLng(latitude, longitude);

        Marker restaurant = mMap.addMarker(new MarkerOptions().position(restaurantLocation).title(restaurantName).snippet("Click to get Directions"));
        restaurant.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantLocation));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Uri uri = Uri.parse(directionsUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
