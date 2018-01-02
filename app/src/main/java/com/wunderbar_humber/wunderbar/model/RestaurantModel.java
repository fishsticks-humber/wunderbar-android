package com.wunderbar_humber.wunderbar.model;

import android.util.Log;

import com.wunderbar_humber.wunderbar.webservice.yelp.YelpGetBusinessTask;
import com.wunderbar_humber.wunderbar.webservice.yelp.YelpInitializeApiTask;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;

/**
 * Created by ceppe on 2018-01-02.
 */

public class RestaurantModel {

    private Business restaurant;
    private YelpFusionApi yelp;

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
    }

    public Business getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Business restaurant) {
        this.restaurant = restaurant;
    }
}
