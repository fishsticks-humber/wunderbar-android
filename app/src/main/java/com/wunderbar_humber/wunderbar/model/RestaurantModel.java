package com.wunderbar_humber.wunderbar.model;

import android.util.Log;

import com.wunderbar_humber.wunderbar.model.bookmark.Bookmark;
import com.wunderbar_humber.wunderbar.webservice.yelp.YelpGetBusinessTask;
import com.wunderbar_humber.wunderbar.webservice.yelp.YelpInitializeApiTask;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Reviews;

import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/**
 * Created by ceppe on 2018-01-02.
 */

public class RestaurantModel {

    private Business restaurant;
    private Reviews review;
    private YelpFusionApi yelp;
    private Bookmark bookmark;

    public RestaurantModel(String restaurantId) {
        // create a yelp api instance
        try {
            yelp = new YelpInitializeApiTask().execute().get();
        } catch (InterruptedException e) {
            Log.e("Yelp Initialization", "Yelp API interrupted", e);
        } catch (ExecutionException e) {
            Log.e("Yelp Initialization", "Exception while initializing Yelp API", e);
        }

        // get the restaurant from yelp
        Call<Business> call = yelp.getBusiness(restaurantId);
        YelpGetBusinessTask task = new YelpGetBusinessTask();
        try {
            restaurant = task.execute(call).get();
        } catch (InterruptedException e) {
            Log.e("Yelp Business", "Yelp API interrupted", e);
        } catch (ExecutionException e) {
            Log.e("Yelp Business", "Exception while getting business from Yelp API", e);
        }

        // get the restaurant REVIEWS from yelp
//        Call<Reviews> call2 = yelp.getBusinessReviews(restaurantId); //review id???
//        YelpGetReviews task2 = new YelpGetReviews();
//        try {
//            review = task2.execute(call2).get();
//        } catch (InterruptedException e) {
//            Log.e("Yelp Business REViews", "Yelp API interrupted", e);
//        } catch (ExecutionException e) {
//            Log.e("Yelp Business REViews", "Exception while getting business REViews from Yelp API", e);
//        }
    }

    /**
     * Checks if this restaurant is bookmarked in the given list of bookmarks
     *
     * @param bookmarks list of bookmarks to check in
     * @return true if bookmarked
     */
    public boolean isBookmarked(List<Bookmark> bookmarks) {
        boolean isBookmarked = false;
        for (Bookmark bookmark : bookmarks) {
            if (bookmark.getRestaurantId().equals(this.restaurant.getId())) {
                this.bookmark = bookmark;
                isBookmarked = true;
            }
        }
        return isBookmarked;
    }

    /**
     * Create bookmark of the current restaurant
     *
     * @return created bookmark
     */
    public Bookmark createBookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.setRestaurantId(restaurant.getId());
        bookmark.setName(restaurant.getName());
        bookmark.setImage(restaurant.getImageUrl());
        bookmark.setPrice(restaurant.getPrice());
        return bookmark;
    }

    public Business getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Business restaurant) {
        this.restaurant = restaurant;
    }

    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }
}
