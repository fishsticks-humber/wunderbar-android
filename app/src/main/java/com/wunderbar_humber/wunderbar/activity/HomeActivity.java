package com.wunderbar_humber.wunderbar.activity;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.wunderbar_humber.wunderbar.R;
import com.wunderbar_humber.wunderbar.RestaurantFragment;
import com.wunderbar_humber.wunderbar.RestaurantRecyclerViewAdapter;
import com.wunderbar_humber.wunderbar.model.HomeModel;
import com.wunderbar_humber.wunderbar.model.bookmark.Bookmark;
import com.wunderbar_humber.wunderbar.model.db.AppDatabase;
import com.yelp.fusion.client.models.Business;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int LOCATION_PERMISSION = 1;
    private boolean locationPermission = false;

    private RecyclerView mainRecyclerView;
    private RestaurantRecyclerViewAdapter restaurantAdapter;
    private FusedLocationProviderClient locationProviderClient;
    private HomeModel homeModel;
    private String city;
    private AppDatabase db;
    private List<Bookmark> bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // get database and bookmarks
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "bookmark-database").allowMainThreadQueries().build();

        // populate the recycler view using the adapter
        mainRecyclerView = findViewById(R.id.list);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeModel = new HomeModel("43.7289", "-79.6073");

        restaurantAdapter = new RestaurantRecyclerViewAdapter(homeModel, new RestaurantFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Business business) {

            }
        }, this.getApplicationContext());
        mainRecyclerView.setAdapter(restaurantAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set location for the home model and update the view
        //
        // Check if location permission is granted by the user and then set the home content location
        //
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
        } else {
            Task<Location> lastLocationTask = locationProviderClient.getLastLocation();

            lastLocationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        homeModel.setLocation(location);
                        restaurantAdapter.updateData(homeModel.getBusinessList());

                        city = getCity(location.getLatitude(), location.getLongitude());
                        if (city != null) {
                            getSupportActionBar().setTitle(city);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Add search functionality to the application
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                homeModel.setSearchTerm(query);
                homeModel.searchRestaurants();
                restaurantAdapter.updateData(homeModel.getBusinessList());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeModel.setSearchTerm(newText);
                homeModel.searchRestaurants();
                restaurantAdapter.updateData(homeModel.getBusinessList());
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.filter_restaurants) {
            homeModel.clearSearchTerm();
            homeModel.setCategoryToOnlyRestaurants();
            restaurantAdapter.updateData(homeModel.getBusinessList());
        } else if (id == R.id.filter_nightlife) {
            homeModel.clearSearchTerm();
            homeModel.setCategoryToOnlyRestaurants();
            restaurantAdapter.updateData(homeModel.getBusinessList());
        } else if (id == R.id.map_location_choose) {

        } else if (id == R.id.bookmarks) {
            List<Business> bookmarkBusinessList = new ArrayList<>();
            for (Bookmark bookmark :
                    db.bookmarkDao().getAll()) {
                Business business = new Business();
                business.setId(bookmark.getRestaurantId());
                business.setImageUrl(bookmark.getImage());
                business.setPrice(bookmark.getPrice());
                business.setName(bookmark.getName());
                bookmarkBusinessList.add(business);
            }
            restaurantAdapter.updateData(bookmarkBusinessList);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission = true;
                }
                return;
        }
    }


    public String getCity(double latitude, double longitude) {
        String city = null;
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault()); //it is Geocoder
        try {
            List<Address> addressList = geoCoder.getFromLocation(latitude, longitude, 1);
            Address address = addressList.get(0);
            if (address != null) {
                city = address.getSubLocality();
            }
        } catch (IOException e) {
        } catch (NullPointerException e) {
        }
        return city;
    }
}
