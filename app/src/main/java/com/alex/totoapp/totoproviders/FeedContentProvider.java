package com.alex.totoapp.totoproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/**
 * This a implementation of a ContentProvider made for
 * the feeds.
 */
public class FeedContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.alex.totoapp.Feeds";
    public static final String URL = "content://" + PROVIDER_NAME + "/feeds";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String FEED_ID = "_id";
    public static final String FEED_TITLE = "title";
    public static final String FEED_URL = "url";

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Feeds";
    private static final String FEEDS_TABLE_NAME = "Feeds";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_DB_TABLE =
            " CREATE TABLE " + FEEDS_TABLE_NAME + " ( " +
                    FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FEED_TITLE + " TEXT NOT NULL, " +
                    FEED_URL + " TEXT NOT NULL " + ");";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FEEDS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(FEEDS_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            if (_uri != null) {
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            }
            return null;
        }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FEEDS_TABLE_NAME);

        // ???? //
        HashMap<String, String> FEEDS_PROJECTION_MAP = new HashMap<String, String>();
        qb.setProjectionMap(FEEDS_PROJECTION_MAP);

        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = FEED_TITLE;
        }

        try {
            Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        try {
            count = db.delete(FEEDS_TABLE_NAME, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        try {
            count = db.update(FEEDS_TABLE_NAME, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return count;
    }


    @Override
    public String getType(Uri uri) {
        try {
            return "vnd.android.cursor.dir/vnd.alex.totoapp.feeds";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
