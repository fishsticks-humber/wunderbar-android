package com.wunderbar_humber.wunderbar.model.bookmark;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by mohit on 2018-01-04.
 */

@Entity
public class Bookmark {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "restaurantid")
    @NonNull
    private String restaurantId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private String price;

    @ColumnInfo(name = "image")
    private String image;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bookmark bookmark = (Bookmark) o;

        if (uid != bookmark.uid) return false;
        return restaurantId.equals(bookmark.restaurantId);
    }

    @Override
    public int hashCode() {
        int result = uid;
        result = 31 * result + restaurantId.hashCode();
        return result;
    }
}
