package com.media2359.jktmalls.repository.item;

import android.content.ContentValues;
import android.database.Cursor;

import com.media2359.jktmalls.repository.TenantsRepository;
import com.media2359.jktmalls.tools.RepoTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by randiwaranugraha on 12/10/14.
 */
public class Tenant {

    public static final String TAG = Tenant.class.getSimpleName();

    private int _id;
    private int tenantId;
    private int mallId;
    private String name;
    private String category;
    private int category_id;
    private String unit;
    private String phone;
    private String floor;

    public Tenant() {
    }

    public Tenant(Cursor cursor) {
        int id = RepoTools.getInt(cursor, TenantsRepository.ID);
        setId(id);

        int tenant_id = RepoTools.getInt(cursor, TenantsRepository.TENANT_ID);
        setTenantId(tenant_id);

        int mall_id = RepoTools.getInt(cursor, TenantsRepository.MALL_ID);
        setMallId(mall_id);

        String name = RepoTools.getString(cursor, TenantsRepository.NAME);
        setName(name);

        String category = RepoTools.getString(cursor, TenantsRepository.CATEGORY);
        setCategory(category);

        int category_id = RepoTools.getInt(cursor, TenantsRepository.CATEGORY_ID);
        setCategoryId(category_id);

        String unit = RepoTools.getString(cursor, TenantsRepository.UNIT);
        setUnit(unit);

        String phone = RepoTools.getString(cursor, TenantsRepository.PHONE);
        setPhone(phone);

        String floor = RepoTools.getString(cursor, TenantsRepository.FLOOR);
        setFloor(floor);
    }

    public Tenant(JSONObject json) {
        try {
            int tenant_id = json.getInt("id");
            setTenantId(tenant_id);

            int mall_id = json.getInt("mall_id");
            setMallId(mall_id);

            String name = json.getString("name");
            setName(name);

            String category = json.getString("category");
            setCategory(category);

            int category_id = json.getInt("category_id");
            setCategoryId(category_id);

            String unit = json.getString("unit");
            setUnit(unit);

            String phone = json.getString("phone");
            setPhone(phone);

            String floor = json.getString("floor");
            setFloor(floor);
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

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getMallId() {
        return mallId;
    }

    public void setMallId(int mallId) {
        this.mallId = mallId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return category_id;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(TenantsRepository.TENANT_ID, getTenantId());
        values.put(TenantsRepository.MALL_ID, getMallId());
        values.put(TenantsRepository.NAME, getName());
        values.put(TenantsRepository.CATEGORY, getCategory());
        values.put(TenantsRepository.CATEGORY_ID, getCategoryId());
        values.put(TenantsRepository.UNIT, getUnit());
        values.put(TenantsRepository.PHONE, getPhone());
        values.put(TenantsRepository.FLOOR, getFloor());

        return values;
    }
}