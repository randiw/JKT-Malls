package com.media2359.jktmalls.repository.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.media2359.jktmalls.repository.DatabaseHelper;
import com.media2359.jktmalls.repository.TenantsRepository;

/**
 * Created by randiwaranugraha on 12/10/14.
 */
public class TenantsProvider extends ContentProvider {

    public static final String TAG = TenantsProvider.class.getSimpleName();

    private DatabaseHelper databaseHelper;

    public static final String AUTHORITY = "com.media2359.jktmalls.repository.provider.TenantsProvider";
    private static final String TABLE_NAME = TenantsRepository.TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    public static final int TENANT = 0;
    public static final int TENANT_ID = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME, TENANT);
        URI_MATCHER.addURI(AUTHORITY, TABLE_NAME + "/#", TENANT_ID);
    }

    public TenantsProvider() {
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE_NAME);

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TENANT:
                break;

            case TENANT_ID:
                queryBuilder.appendWhere(TenantsRepository.ID + "=" + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Cursor cursor = null;
        cursor = queryBuilder.query(databaseHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        long id = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TENANT:
                id = database.insert(TABLE_NAME, null, contentValues);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TABLE_NAME + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsDeleted = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TENANT:
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;

            case TENANT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.delete(TABLE_NAME, TenantsRepository.ID + "=" + id, null);
                } else {
                    rowsDeleted = database.delete(TABLE_NAME, TenantsRepository.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsUpdated = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TENANT:
                rowsUpdated = database.update(TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            case TENANT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(TABLE_NAME, contentValues, TenantsRepository.ID + "=" + id, selectionArgs);
                } else {
                    rowsUpdated = database.update(TABLE_NAME, contentValues, TenantsRepository.ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}