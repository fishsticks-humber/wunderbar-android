package com.wunderbar_humber.wunderbar.webservice;

import android.util.Log;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static org.junit.Assert.*;

/**
 * Created by mohit on 2018-01-01.
 */
public class YelpSearchBusinessesTaskTest {

    private YelpFusionApi api;

    @Before
    public void setUp() throws Exception {
        api = new YelpInitializeApiTask().execute().get();
    }

    @Test
    public void searchBusiness() throws Exception {
        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "indian food");
        params.put("latitude", "40.581140");
        params.put("longitude", "-111.914184");

        YelpSearchBusinessesTask task = new YelpSearchBusinessesTask();
        Call<SearchResponse> call = api.getBusinessSearch(params);
        List<Business> businessList = task.execute(call).get();

        assertNull("There should be no exceptions", task.exception);
        assertNotEquals(0, businessList.size());

    }
}