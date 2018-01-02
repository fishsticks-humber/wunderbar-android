package com.wunderbar_humber.wunderbar.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.models.Business;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * {@link AsyncTask} to execute the business call to yelp api and get the business
 *
 * To form a call refer to
 * @see <a href="https://github.com/ranga543/yelp-fusion-android">Yelp Fusion API Android</a>
 */
public class YelpGetBusinessTask extends AsyncTask<Call<Business>, Void, Business> {

    Exception exception;

    /**
     * Gets the business from the yelp api
     * @param calls
     * @return
     */
    @Override
    protected Business doInBackground(Call<Business>[] calls) {
        Business business = null;
        try {
        Call<Business> businessCall = calls[0];

            Response<Business> businessResponse = businessCall.execute();
            business = businessResponse.body();
        } catch (IOException e) {
            Log.e("YELP BUSINESS", "Error while executing", e);
            exception = e;
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("YELP SEARCH", "You didn't supply the call parameter while executing. USAGE : new YelpSearchBusinessesTask().execute(<call to execute>)", e);
            exception = e;
        }
        return business;
    }
}
