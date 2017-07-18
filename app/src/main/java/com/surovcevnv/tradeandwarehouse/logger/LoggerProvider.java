package com.surovcevnv.tradeandwarehouse.logger;

/**
 * Created by surovcevnv on 08.07.17.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class LoggerProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://ru.dubki.logger/logs");

    private SQLiteDatabase db;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete("logs", selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert("logs", "", values);
        if (rowID > 0) {
            uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(uri, null);
            return uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor = db.query("logs", projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update("logs", values, selection, selectionArgs);
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "log.db", null, 8);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE logs (id integer primary key autoincrement, dttm TEXT, app TEXT, file TEXT, function TEXT, typeEvent TEXT, msg TEXT, isLoaded INTEGER, uid TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS logs");
            onCreate(db);
        }
    }
}
