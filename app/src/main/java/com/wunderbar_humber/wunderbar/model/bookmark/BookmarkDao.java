package com.wunderbar_humber.wunderbar.model.bookmark;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by mohit on 2018-01-04.
 */

@Dao
public interface BookmarkDao {

    @Query("SELECT * from Bookmark")
    List<Bookmark> getAll();

    @Insert
    void insertAll(Bookmark... bookmarks);

    @Delete
    void delete(Bookmark bookmark);
}
