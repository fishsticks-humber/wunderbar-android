package com.wunderbar_humber.wunderbar.webservice.yelp;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * {@link AsyncTask} to execute the search call to yelp api and get the business list
 *
 * To form a call refer to
 * @see <a href="https://github.com/ranga543/yelp-fusion-android">Yelp Fusion API Android</a>
 */
public class YelpSearchBusinessesTask extends AsyncTask<Call<SearchResponse>, Void, List<Business>> {

    Exception exception;

    @Override
    protected List<Business> doInBackground(Call<SearchResponse>[] calls) {
        List<Business> businessList = null;
        try {
            Call<SearchResponse> call = calls[0];
            Response<SearchResponse> searchResponse = call.execute();
            businessList = searchResponse.body().getBusinesses();
        } catch (IOException e) {
            Log.e("YELP SEARCH", "Error while executing", e);
            exception = e;
        } catch (ArrayIndexOutOfBoundsException e) {
            exception = e;
            Log.e("YELP SEARCH", "You didn't supply the call parameter while executing. USAGE : new YelpSearchBusinessesTask().execute(<call to execute>)", e);
        }
        return businessList;
    }
}
