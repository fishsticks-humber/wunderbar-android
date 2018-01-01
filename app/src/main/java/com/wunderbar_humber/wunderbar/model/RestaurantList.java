package com.wunderbar_humber.wunderbar.model;

import android.media.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RestaurantList {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Restaurant> ITEMS = new ArrayList<Restaurant>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Restaurant> ITEM_MAP = new HashMap<String, Restaurant>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Restaurant item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Restaurant createDummyItem(int position) {
        String restaurantName,address,category;
        Double price;
        int phone;
        Image imageLink = null;

        return new Restaurant(String.valueOf(position), "Item " + position, makeDetails(position), "something", "something", "something", 123-1231111, 123.123, imageLink);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Restaurant {
        public final String id;
        public final String content;
        public final String restaurantName;
        public final String address;
        public final String category;
        public final int phone;
        public final double price;
        public final Image image;

        public Restaurant(String id, String content, String details, String restaurantName, String address, String category, int phone, double price, Image image) {
            this.id = id;
            this.content = content;
            this.restaurantName = restaurantName;
            this.address = address;
            this.category = category;
            this.phone = phone;
            this.price = price;
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public String getAddress() {
            return address;
        }

        public String getCategory() {
            return category;
        }

        public int getPhone() {
            return phone;
        }

        public double getPrice() {
            return price;
        }

        public Image getImage() {
            return image;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
