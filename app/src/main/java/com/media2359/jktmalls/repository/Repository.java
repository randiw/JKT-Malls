package com.media2359.jktmalls.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
public abstract class Repository {

    protected SQLiteDatabase database;
    protected static ContentResolver resolver;
    private Context context;

    public Repository(Context context) {
        this.context = context;
        resolver = context.getContentResolver();
    }

    public static String createTable(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName;
    }

    public static String primary(String str) {
        String primary = "(" + str;
        return primary;
    }

    public static String primaryAutoID(String field_name) {
        String primaryAutoID = field_name + " INTEGER PRIMARY KEY AUTOINCREMENT";
        return primary(primaryAutoID);
    }

    public static String field(String str) {
        String field = ", " + str;
        return field;
    }

    public static String fieldVarchar(String str) {
        String fieldVarchar = str + " VARCHAR";
        return field(fieldVarchar);
    }

    public static String fieldInteger(String field_name) {
        String fieldInteger = field_name + " INTEGER";
        return field(fieldInteger);
    }

    public static String fieldDouble(String field_name) {
        String fieldDouble = field_name + " DOUBLE";
        return field(fieldDouble);
    }

    public static String close() {
        return ");";
    }

    public Context getContext() {
        return context;
    }
}