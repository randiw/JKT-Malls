package com.media2359.jktmalls.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.media2359.jktmalls.repository.item.Tenant;
import com.media2359.jktmalls.repository.provider.TenantsProvider;
import com.media2359.jktmalls.tools.RepoTools;

/**
 * Created by randiwaranugraha on 12/10/14.
 */
public class TenantsRepository extends Repository {

    public static final String TAG = TenantsRepository.class.getSimpleName();
    public static final String TABLE_NAME = "tenants";
    public static final Uri CONTENT_URI = TenantsProvider.CONTENT_URI;

    public static final String ID = "_id";
    public static final String TENANT_ID = "tenant_id";
    public static final String MALL_ID = "mall_id";
    public static final String NAME = "name";
    public static final String CATEGORY = "category";
    public static final String CATEGORY_ID = "category_id";
    public static final String UNIT = "unit";
    public static final String FLOOR = "floor";
    public static final String PHONE = "phone";

    public TenantsRepository(Context context) {
        super(context);
    }

    public void save(Tenant tenant) {
        ContentValues tenantValues = tenant.toContentValues();

        if(has(tenant)) {
            String selection = TENANT_ID + "='" + tenant.getTenantId() + "'";
            resolver.update(CONTENT_URI, tenantValues, selection, null);
        } else {
            resolver.insert(CONTENT_URI, tenantValues);
        }
    }

    public Tenant find(int tenant_id) {
        String selection = TENANT_ID + "='" + tenant_id + "'";
        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if(!RepoTools.isRowAvailable(cursor)) {
            return null;
        }

        Tenant tenant = new Tenant(cursor);
        cursor.close();
        return tenant;
    }

    public boolean has(Tenant tenant) {
        return has(tenant.getTenantId());
    }

    public boolean has(int tenant_id) {
        String selection = TENANT_ID + "='" + tenant_id + "'";
        Cursor cursor = resolver.query(CONTENT_URI, null, selection, null, null);
        if(!RepoTools.isRowAvailable(cursor)) {
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

        query.append(fieldInteger(TENANT_ID));
        query.append(fieldInteger(MALL_ID));
        query.append(fieldVarchar(NAME));
        query.append(fieldVarchar(CATEGORY));
        query.append(fieldInteger(CATEGORY_ID));
        query.append(fieldVarchar(UNIT));
        query.append(fieldVarchar(FLOOR));
        query.append(fieldVarchar(PHONE));

        query.append(close());
        return query.toString();
    }
}