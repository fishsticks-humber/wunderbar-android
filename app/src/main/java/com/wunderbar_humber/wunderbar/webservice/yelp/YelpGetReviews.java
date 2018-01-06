package com.wunderbar_humber.wunderbar.webservice.yelp;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.models.Reviews;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * {@link AsyncTask} to execute the business call to yelp api and get the business
 * <p>
 * To form a call refer to
 *
 * @see <a href="https://github.com/ranga543/yelp-fusion-android">Yelp Fusion API Android</a>
 */
public class YelpGetReviews extends AsyncTask<Call<Reviews>, Void, Reviews> {

    Exception exception;

    /**
     * Gets the business from the yelp api
     *
     * @param calls
     * @return
     */
    @Override
    protected Reviews doInBackground(Call<Reviews>[] calls) {
        Reviews reviews = null;
        try {
            Call<Reviews> reviewsCall = calls[0];
            Response<Reviews> reviewsResponse = reviewsCall.execute();
            reviews = reviewsResponse.body();
        } catch (IOException e) {
            Log.e("YELP REVIEWS", "Error while executing", e);
            exception = e;
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("YELP SEARCH", "You didn't supply the call parameter while executing. USAGE : new YelpSearchBusinessesTask().execute(<call to execute>)", e);
            exception = e;
        }
        return reviews;
    }
}
