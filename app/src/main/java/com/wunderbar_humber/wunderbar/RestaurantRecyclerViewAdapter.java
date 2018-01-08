package com.wunderbar_humber.wunderbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wunderbar_humber.wunderbar.RestaurantFragment.OnListFragmentInteractionListener;
import com.wunderbar_humber.wunderbar.activity.MapActivity;
import com.wunderbar_humber.wunderbar.activity.RestaurantActivity;
import com.wunderbar_humber.wunderbar.model.HomeModel;
import com.yelp.fusion.client.models.Business;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Business} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder> {

    private final OnListFragmentInteractionListener mListener;
    private final Context context;
    private List<Business> mValues;


    public RestaurantRecyclerViewAdapter(HomeModel model, OnListFragmentInteractionListener listener, Context context) {
        mValues = model.getBusinessList();
        mListener = listener;
        this.context = context;
    }

    /**
     * Updates the Recycler View with the new data
     *
     * @param businesses the new data
     */
    public void updateData(List<Business> businesses) {
        this.mValues = businesses;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Business business = mValues.get(position);

        holder.mItem = business;
        holder.mIdView.setText(business.getName());
        holder.mContentView.setText(business.getPrice());

        String imageUrl = business.getImageUrl();
        if (StringUtils.isNotEmpty(imageUrl)) {
            Picasso.with(context).load(imageUrl).into(holder.imageView);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RestaurantActivity.class);
                intent.putExtra("restaurantId", business.getId());
                view.getContext().startActivity(intent);
            }
        });

        holder.mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                intent.putExtra("latitude", business.getCoordinates().getLatitude());
                intent.putExtra("longitude", business.getCoordinates().getLongitude());
                intent.putExtra("restaurant", business.getName());
                view.getContext().startActivity(intent);
            }
        });

        holder.uberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uberUrl = "https://m.uber.com/ul/"
                        + "?client_id=HAb_WWbX4pVCH7Ugd8FuLHnhRR0cnSEQ"
                        + "&action=setPickup"
                        + "&pickup=my_location"
                        + "&dropoff[latitude]=" + business.getCoordinates().getLatitude()
                        + "&dropoff[longitude]=" + business.getCoordinates().getLongitude()
                        + "&dropoff[nickname]=" + URLEncoder.encode(business.getName());

                Uri uri = Uri.parse(uberUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imageView;
        public final Button uberButton;
        public final Button mapButton;

        public Business mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
            imageView = view.findViewById(R.id.restaurantImage);
            uberButton = view.findViewById(R.id.uberButton);
            mapButton = view.findViewById(R.id.mapsButton);
        }


        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
