package com.media2359.jktmalls.repository.item;

import android.content.ContentValues;
import android.database.Cursor;

import com.media2359.jktmalls.repository.CategoriesRepository;
import com.media2359.jktmalls.tools.RepoTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by randiwaranugraha on 12/16/14.
 */
public class Category {

    public static final String TAG = Category.class.getSimpleName();

    private int _id;
    private int categoryId;
    private String name;
    private int order;

    public Category() {
    }

    public Category(Cursor cursor) {
        int id = RepoTools.getInt(cursor, CategoriesRepository.ID);
        setId(id);

        int category_id = RepoTools.getInt(cursor, CategoriesRepository.CATEGORY_ID);
        setCategoryId(category_id);

        String name = RepoTools.getString(cursor, CategoriesRepository.NAME);
        setName(name);

        int order = RepoTools.getInt(cursor, CategoriesRepository.ORDER);
        setOrder(order);
    }

    public Category(JSONObject json) {
        try {
            int category_id = json.getInt("id");
            setCategoryId(category_id);

            String name = json.getString("name");
            setName(name);

            int order = json.getInt("order");
            setOrder(order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(CategoriesRepository.CATEGORY_ID, getCategoryId());
        values.put(CategoriesRepository.NAME, getName());
        values.put(CategoriesRepository.ORDER, getOrder());

        return values;
    }
}