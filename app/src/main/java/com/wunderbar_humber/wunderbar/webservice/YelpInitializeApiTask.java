package com.wunderbar_humber.wunderbar.webservice;

import android.os.AsyncTask;
import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;

import java.io.IOException;

/**
 * Created by mohit on 2018-01-01.
 */

public class YelpInitializeApiTask extends AsyncTask<Void, Void, YelpFusionApi> {

    private static final String YELP_CLIENT_ID = "1MYkC01ehtuQzF5hzSPL-A";
    private static final String YELP_CLIENT_SECRET = "nR5AJp4yyIsEVxVw7jcCUQK1bIWEeHQ22v7g6XeLRzobqSOTs9iqaeLnr3uanV0e";

    @Override
    protected YelpFusionApi doInBackground(Void... voids) {
        YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
        YelpFusionApi api = null;
        try {
            api = apiFactory.createAPI(YELP_CLIENT_ID, YELP_CLIENT_SECRET);
        } catch (IOException e) {
            Log.e("Yelp API Task", "Something went wrong while creating the api object", e);
        }
        return api;
    }
}
