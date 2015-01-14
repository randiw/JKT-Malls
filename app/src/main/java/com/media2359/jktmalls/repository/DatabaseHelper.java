package com.media2359.jktmalls.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "jktmalls";
    private static final int DATABASE_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MallsRepository.createTable());
        db.execSQL(TenantsRepository.createTable());
        db.execSQL(CategoriesRepository.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TenantsRepository.dropTable());
        db.execSQL(MallsRepository.dropTable());
        db.execSQL(CategoriesRepository.dropTable());
        onCreate(db);
    }
}