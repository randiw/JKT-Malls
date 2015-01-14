package com.media2359.jktmalls.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.media2359.jktmalls.repository.item.Category;
import com.media2359.jktmalls.repository.provider.CategoriesProvider;
import com.media2359.jktmalls.tools.RepoTools;

/**
 * Created by randiwaranugraha on 12/16/14.
 */
public class CategoriesRepository extends Repository {

    public static final String TAG = CategoriesRepository.class.getSimpleName();
    public static final String TABLE_NAME = "categories";
    public static final Uri CONTENT_URI = CategoriesProvider.CONTENT_URI;

    public static final String ID = "_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String NAME = "name";
    public static final String ORDER = "sort";

    public CategoriesRepository(Context context) {
        super(context);
    }

    public void save(Category category) {
        ContentValues values = category.toContentValues();

        if (has(category)) {
            String selection = CATEGORY_ID + "='" + category.getCategoryId() + "'";
            resolver.update(CONTENT_URI, values, selection, null);
        } else {
            resolver.insert(CONTENT_URI, values);
        }
    }

    public Category find(int category_id) {
        String selection = CATEGORY_ID + "='" + category_id + "'";
        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if (!RepoTools.isRowAvailable(cursor)) {
            return null;
        }

        Category category = new Category(cursor);
        cursor.close();
        return category;
    }

    public String[] getAllToStringArray() {
        Cursor cursor = resolver.query(CONTENT_URI, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String[] categories = new String[cursor.getCount()];
        int i = 0;
        do {
            Category category = new Category(cursor);
            categories[i] = category.getName();
            i++;
        } while (cursor.moveToNext());

        cursor.close();
        return categories;
    }

    public boolean has(Category category) {
        return has(category.getCategoryId());
    }

    public boolean has(int category_id) {
        String selection = CATEGORY_ID + "='" + category_id + "'";
        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if (!RepoTools.isRowAvailable(cursor)) {
            return false;
        }

        cursor.close();
        return true;
    }

    public static String dropTable() {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return query;
    }

    public static String createTable() {
        StringBuilder query = new StringBuilder();
        query.append(createTable(TABLE_NAME));
        query.append(primaryAutoID(ID));

        query.append(fieldInteger(CATEGORY_ID));
        query.append(fieldVarchar(NAME));
        query.append(fieldInteger(ORDER));

        query.append(close());
        return query.toString();
    }
}
