package com.wunderbar_humber.wunderbar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wunderbar_humber.wunderbar.R;
import com.wunderbar_humber.wunderbar.model.RestaurantModel;

import java.net.URLEncoder;

public class RestaurantActivity extends AppCompatActivity {

    private RestaurantModel restaurantModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String restaurantId = getIntent().getStringExtra("restaurantId");
        restaurantModel = new RestaurantModel(restaurantId);
    }

    public void openMap(View view) {
        Intent intent = new Intent(view.getContext(), MapActivity.class);
        intent.putExtra("latitude", restaurantModel.getRestaurant().getCoordinates().getLatitude());
        intent.putExtra("longitude", restaurantModel.getRestaurant().getCoordinates().getLongitude());
        intent.putExtra("restaurant", restaurantModel.getRestaurant().getName());
        view.getContext().startActivity(intent);
    }

    public void requestUber(View view) {
        String uberUrl = "https://m.uber.com/ul/"
                + "?client_id=HAb_WWbX4pVCH7Ugd8FuLHnhRR0cnSEQ"
                + "&action=setPickup"
                + "&pickup=my_location"
                + "&dropoff[latitude]=" + restaurantModel.getRestaurant().getCoordinates().getLatitude()
                + "&dropoff[longitude]=" + restaurantModel.getRestaurant().getCoordinates().getLongitude()
                + "&dropoff[nickname]=" + URLEncoder.encode(restaurantModel.getRestaurant().getName());

        Uri uri = Uri.parse(uberUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
