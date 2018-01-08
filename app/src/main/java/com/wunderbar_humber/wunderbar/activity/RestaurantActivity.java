package com.wunderbar_humber.wunderbar.activity;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wunderbar_humber.wunderbar.R;
import com.wunderbar_humber.wunderbar.model.RestaurantModel;
import com.wunderbar_humber.wunderbar.model.bookmark.Bookmark;
import com.wunderbar_humber.wunderbar.model.db.AppDatabase;
import com.yelp.fusion.client.models.Review;

import java.net.URLEncoder;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    private RestaurantModel restaurantModel;
    private AppDatabase database;
    private FloatingActionButton bookmarkButton;
    private List<Bookmark> bookmarks;
    private RatingBar ratingBar;
    private TextView reviewCounter;
    private TextView address;
    private TextView hours;
    private TextView price;
    private TextView phone;
    private ImageView mainImage;
    private AppBarLayout header;


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

        //fill the restaurant data from API

        CollapsingToolbarLayout toolbarLayout = findViewById(R.id.toolbar_layout);
        String restaurantNameString = restaurantModel.getRestaurant().getName();
        toolbarLayout.setTitle(restaurantNameString);

        double restaurantRate = restaurantModel.getRestaurant().getRating();
        float floatRestaurantRate = (float) restaurantRate;
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(floatRestaurantRate);
        ratingBar.setStepSize(floatRestaurantRate);

        String reviewCounterString = String.valueOf(restaurantModel.getRestaurant().getReviewCount());
        TextView reviewCounter = findViewById(R.id.reviewCounter);
        reviewCounter.setText("(" + reviewCounterString + " Reviews)");

        String addressString = restaurantModel.getRestaurant().getLocation().getAddress1();
        TextView address = findViewById(R.id.addressTextView);
        address.setText(addressString);

        //in yelp API hours field is an array of data, fix if time available.
//        String hoursString = String.valueOf(restaurantModel.getRestaurant().getHours());
//        TextView hours = findViewById(R.id.hoursTextView);
//        hours.setText(hoursString);

        String priceString = restaurantModel.getRestaurant().getPrice();
        TextView price = findViewById(R.id.orderTextView);
        price.setText(priceString);

        String phoneString = restaurantModel.getRestaurant().getPhone();
        TextView phone = findViewById(R.id.phoneTextView);
        phone.setText(phoneString);

//        String imageUrl = restaurantModel.getRestaurant().getImageUrl();
//        ImageView image = findViewById(R.id.imageView);
//        image.setImageURI(Uri.parse(imageUrl));

        ImageView image = findViewById(R.id.imageView2);
        String imageUrl = restaurantModel.getRestaurant().getImageUrl();
        Glide.with(this).load(imageUrl).into(image);


        //REVIEWS load from yelp

//        String name1String = restaurantModel.getRestaurant().getRe();
//        TextView price = findViewById(R.id.orderTextView);
//        price.setText(priceString);

        Review review1 = restaurantModel.getReviews().getReviews().get(0);
        TextView name1 = findViewById(R.id.nameLabel1);
        name1.setText(review1.getUser().getName());
        TextView date1 = findViewById(R.id.dateLabel1);
        date1.setText(review1.getTimeCreated());
        RatingBar ratebar1 = findViewById(R.id.ratingBar1);
        ratebar1.setRating(review1.getRating());
        TextView comment1 = findViewById(R.id.commentTextLabel1);
        comment1.setText(review1.getText());

        Review review2 = restaurantModel.getReviews().getReviews().get(1);
        TextView name2 = findViewById(R.id.nameLabel2);
        name2.setText(review2.getUser().getName());
        TextView date2 = findViewById(R.id.dateLabel2);
        date2.setText(review2.getTimeCreated());
        RatingBar ratebar2 = findViewById(R.id.ratingBar2);
        ratebar2.setRating(review2.getRating());
        TextView comment2 = findViewById(R.id.commentTextLabel2);
        comment2.setText(review2.getText());


        Review review3 = restaurantModel.getReviews().getReviews().get(2);
        TextView name3 = findViewById(R.id.nameLabel3);
        name3.setText(review3.getUser().getName());
        TextView date3 = findViewById(R.id.dateLabel3);
        date3.setText(review3.getTimeCreated());
        RatingBar ratebar3 = findViewById(R.id.ratingBar3);
        ratebar3.setRating(review3.getRating());
        TextView comment3 = findViewById(R.id.commentTextLabel3);
        comment3.setText(review3.getText());
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

    public void openYelp(View view) {
        String yelpUrl = "https://www.yelp.com/biz/"
                + restaurantModel.getRestaurant().getId();
        Uri uri = Uri.parse(yelpUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
