package com.wunderbar_humber.wunderbar.activity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
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
        LatLng location = new LatLng(latitude, longitude);

        if (restaurantName != null) { // restaurant location mode
            Marker restaurant = mMap.addMarker(new MarkerOptions().position(location).title(restaurantName).snippet("Click to get Directions"));
            restaurant.showInfoWindow();
        } else { // choose on map mode
            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title("Drag to set location").draggable(true));
            Snackbar snackbar = Snackbar.make(findViewById(R.id.map), "Choose Map Area to Search", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("DONE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();

                    LatLng center = visibleRegion.latLngBounds.getCenter();

                    LatLng farRight = visibleRegion.farRight;
                    LatLng farLeft = visibleRegion.farLeft;
                    LatLng nearRight = visibleRegion.nearRight;
                    LatLng nearLeft = visibleRegion.nearLeft;

                    float[] radius = new float[2];
                    Location.distanceBetween(
                            (farRight.latitude + nearRight.latitude) / 2,
                            (farRight.longitude + nearRight.longitude) / 2,
                            (farLeft.latitude + nearLeft.latitude) / 2,
                            (farLeft.longitude + nearLeft.longitude) / 2,
                            radius
                    );

                    Intent intent = new Intent();
                    intent.putExtra("latitude", String.valueOf(center.latitude));
                    intent.putExtra("longitude", String.valueOf(center.longitude));
                    intent.putExtra("radius", radius[0]);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            snackbar.show();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Uri uri = Uri.parse(directionsUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
