package com.media2359.jktmalls.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.util.Log;

import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.repository.provider.MallsProvider;
import com.media2359.jktmalls.tools.LocationTools;
import com.media2359.jktmalls.tools.RepoTools;

import java.util.ArrayList;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
public class MallsRepository extends Repository {

    public static final String TAG = MallsRepository.class.getSimpleName();
    public static final String TABLE_NAME = "malls";
    public static final Uri CONTENT_URI = MallsProvider.CONTENT_URI;

    public static final String ID = "_id";
    public static final String MALL_ID = "mall_id";
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String AREA = "area";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String OPEN_HOUR = "open_hour";
    public static final String CLOSE_HOUR = "close_hour";
    public static final String PHONE = "phone";

    public MallsRepository(Context context) {
        super(context);
    }

    public void save(Mall mall) {
        ContentValues mallValues = mall.toContentValues();

        if (has(mall)) {
            String selection = MALL_ID + "='" + mall.getMallId() + "'";
            resolver.update(CONTENT_URI, mallValues, selection, null);
        } else {
            resolver.insert(CONTENT_URI, mallValues);
        }
    }

    public Mall find(int mall_id) {
        String selection = MALL_ID + "='" + mall_id + "'";
        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if (!RepoTools.isRowAvailable(cursor)) {
            return null;
        }

        Mall mall = new Mall(cursor);
        cursor.close();
        return mall;
    }

    public ArrayList<Mall> findByPosition(float lat, float lon, int distance, String searchTerm) {
        PointF center = new PointF(lat, lon);
        final double mult = 1;
        PointF p1 = LocationTools.calculateDerivedPosition(center, mult * distance, 0);
        PointF p2 = LocationTools.calculateDerivedPosition(center, mult * distance, 90);
        PointF p3 = LocationTools.calculateDerivedPosition(center, mult * distance, 180);
        PointF p4 = LocationTools.calculateDerivedPosition(center, mult * distance, 270);

        String selection = LATITUDE + " <> '0' AND " + LONGITUDE + " <> '0'";
        selection += " AND (" + LATITUDE + " > '" + String.valueOf(p3.x) + "' AND " +
                LATITUDE + " < '" + String.valueOf(p1.x) + "' AND " +
                LONGITUDE + " < '" + String.valueOf(p2.y) + "' AND " +
                LONGITUDE + " > '" + String.valueOf(p4.y) + "')";
        if(searchTerm != null) {
            selection += " AND " + NAME + " LIKE '%" + searchTerm + "%'";
        }

        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if (!RepoTools.isRowAvailable(cursor)) {
            return null;
        }

        ArrayList<Mall> malls = new ArrayList<Mall>();
        do {
            Mall mall = new Mall(cursor);
            malls.add(mall);
        } while(cursor.moveToNext());

        cursor.close();
        return malls;
    }

    public boolean has(int mall_id) {
        String selection = MALL_ID + "='" + mall_id + "'";
        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if (!RepoTools.isRowAvailable(cursor)) {
            return false;
        }

        cursor.close();
        return true;
    }

    public boolean has(Mall mall) {
        return has(mall.getMallId());
    }

    public static String dropTable() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return query;
    }

    public static String createTable() {
        StringBuilder query = new StringBuilder();
        query.append(createTable(TABLE_NAME));
        query.append(primaryAutoID(ID));

        query.append(fieldInteger(MALL_ID));
        query.append(fieldVarchar(NAME));
        query.append(fieldVarchar(ADDRESS));
        query.append(fieldVarchar(AREA));
        query.append(fieldDouble(LATITUDE));
        query.append(fieldDouble(LONGITUDE));
        query.append(fieldVarchar(OPEN_HOUR));
        query.append(fieldVarchar(CLOSE_HOUR));
        query.append(fieldVarchar(PHONE));

        query.append(close());
        return query.toString();
    }
}