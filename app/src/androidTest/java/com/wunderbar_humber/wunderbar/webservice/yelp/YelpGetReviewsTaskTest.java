package com.wunderbar_humber.wunderbar.webservice.yelp;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.models.Reviews;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Call;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by mohit on 2018-01-01.
 */
public class YelpGetReviewsTaskTest {
    private YelpFusionApi api;

    @Before
    public void setUp() throws Exception {
        api = new YelpInitializeApiTask().execute().get();
    }

    @Test
    public void getBusiness() throws Exception {

        YelpGetReviewsTask task = new YelpGetReviewsTask();
        Call<Reviews> call = api.getBusinessReviews("osmows-toronto-4", "en_CA");
        Reviews reviews = task.execute(call).get();

        assertNull("There should be no exceptions", task.exception);
        assertNotNull(reviews);
    }
}