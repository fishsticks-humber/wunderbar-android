package com.wunderbar_humber.wunderbar.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wunderbar_humber.wunderbar.R;
import com.wunderbar_humber.wunderbar.model.RestaurantModel;
import com.wunderbar_humber.wunderbar.model.bookmark.Bookmark;
import com.wunderbar_humber.wunderbar.model.db.AppDatabase;

import java.net.URLEncoder;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    private RestaurantModel restaurantModel;
    private AppDatabase database;
    private FloatingActionButton bookmarkButton;
    private List<Bookmark> bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get database and bookmarks
        database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "bookmark-database").allowMainThreadQueries().build();
        bookmarks = database.bookmarkDao().getAll();

        // set up bookmark button and click listener
        bookmarkButton = findViewById(R.id.bookmark_button);
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restaurantModel.isBookmarked(bookmarks)) {
                    Bookmark bookmark = restaurantModel.getBookmark();
                    database.bookmarkDao().delete(bookmark);
                    bookmarkButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                } else {
                    Bookmark bookmark = restaurantModel.createBookmark();
                    database.bookmarkDao().insertAll(bookmark);
                    bookmarkButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String restaurantId = getIntent().getStringExtra("restaurantId");
        restaurantModel = new RestaurantModel(restaurantId);

        if (restaurantModel.isBookmarked(bookmarks)) {
            bookmarkButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
        }
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
