package com.wunderbar_humber.wunderbar.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.wunderbar_humber.wunderbar.model.bookmark.Bookmark;
import com.wunderbar_humber.wunderbar.model.bookmark.BookmarkDao;

/**
 * Created by mohit on 2018-01-04.
 */
@Database(entities = {Bookmark.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookmarkDao bookmarkDao();

}
