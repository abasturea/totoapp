package com.alex.totoapp.totoproviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * This a implementation of a ContentProvider made for
 * the feeds.
 */
public class FeedContentProvider extends ContentProvider implements BaseColumns {

    public static final String AUTHORITY = "com.alex.totoapp";

    public static final Uri FEED_ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/feeditems");

    public static final String FEED_ITEM_TITLE = "title";
    public static final String FEED_ITEM_URL = "url";

    public static final Uri RSS_ITEM_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/rssitems");

    public static final String RSS_ITEM_HEADLINE = "headline";
    public static final String RSS_ITEM_URL = "url";
    public static final String RSS_ITEM_IMAGE_LINK = "image";
    public static final String RSS_ITEM_DESCRIPTION = "description";
    public static final String RSS_ITEM_FEED_ID = "feed_id";

    private static HashMap<String, String> PROJECTION_MAP = new HashMap<String, String>();
    private static final int FEED_ITEMS = 1;
    private static final int FEED_ITEMS_ID = 2;
    private static final int RSS_ITEMS = 3;
    private static final int RSS_ITEMS_ID = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, "feeditems", FEED_ITEMS);
        uriMatcher.addURI(AUTHORITY, "feeditems/#", FEED_ITEMS_ID);
        uriMatcher.addURI(AUTHORITY, "rssitems", RSS_ITEMS);
        uriMatcher.addURI(AUTHORITY, "rssitems/#", RSS_ITEMS_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    private static final String FEED_ITEMS_TABLE_NAME = "FeedsItems";
    private static final String RSS_ITEMS_TABLE_NAME = "RssItems";

    private static final String CREATE_FEED_ITEMS_DB_TABLE =
            " CREATE TABLE " + FEED_ITEMS_TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FEED_ITEM_TITLE + " TEXT NOT NULL, " +
                    FEED_ITEM_URL + " TEXT NOT NULL " + ");";

    private static final String CREATE_RSS_ITEMS_DB_TABLE =
            " CREATE TABLE " + RSS_ITEMS_TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RSS_ITEM_HEADLINE + " TEXT NOT NULL, " +
                    RSS_ITEM_URL + " TEXT NOT NULL, " +
                    RSS_ITEM_IMAGE_LINK + " TEXT, " +
                    RSS_ITEM_DESCRIPTION + " TEXT, " +
                    RSS_ITEM_FEED_ID + " INTEGER " + ");";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "toto.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_FEED_ITEMS_DB_TABLE);
            db.execSQL(CREATE_RSS_ITEMS_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FEED_ITEMS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + RSS_ITEMS_TABLE_NAME);
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
        long rowID;

        switch (uriMatcher.match(uri)) {
            case FEED_ITEMS:
                rowID = db.insert(FEED_ITEMS_TABLE_NAME, "", values);

                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(FEED_ITEM_CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);

                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);

            case RSS_ITEMS:
                rowID = db.insert(RSS_ITEMS_TABLE_NAME, "", values);

                if (rowID > 0) {
                    Uri _uri = ContentUris.withAppendedId(RSS_ITEM_CONTENT_URI, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);

                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case FEED_ITEMS:
                qb.setProjectionMap(PROJECTION_MAP);
                qb.setTables(FEED_ITEMS_TABLE_NAME);
                break;
            case FEED_ITEMS_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                qb.setTables(FEED_ITEMS_TABLE_NAME);
                break;
            case RSS_ITEMS:
                qb.setProjectionMap(PROJECTION_MAP);
                qb.setTables(RSS_ITEMS_TABLE_NAME);
                break;
            case RSS_ITEMS_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                qb.setTables(RSS_ITEMS_TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = _ID;
        }

        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        String id;

        switch (uriMatcher.match(uri)) {
            case FEED_ITEMS:
                count = db.delete(FEED_ITEMS_TABLE_NAME, selection, selectionArgs);
                break;
            case FEED_ITEMS_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(FEED_ITEMS_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case RSS_ITEMS:
                count = db.delete(RSS_ITEMS_TABLE_NAME, selection, selectionArgs);
                break;
            case RSS_ITEMS_ID:
                id = uri.getPathSegments().get(1);
                count = db.delete(RSS_ITEMS_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;

        switch (uriMatcher.match(uri)) {
            case FEED_ITEMS:
                count = db.update(FEED_ITEMS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case FEED_ITEMS_ID:
                count = db.update(FEED_ITEMS_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            case RSS_ITEMS:
                count = db.update(RSS_ITEMS_TABLE_NAME, values, selection, selectionArgs);
                break;
            case RSS_ITEMS_ID:
                count = db.update(RSS_ITEMS_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case FEED_ITEMS:
                return "vnd.android.cursor.dir/vnd.alex.totoapp.feeds";
            case FEED_ITEMS_ID:
                return "vnd.android.cursor.item/vnd.alex.totoapp.feeds";
            case RSS_ITEMS:
                return "vnd.android.cursor.dir/vnd.alex.totoapp.rssitems";
            case RSS_ITEMS_ID:
                return "vnd.android.cursor.item/vnd.alex.totoapp.rssitems";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}