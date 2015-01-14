package com.media2359.jktmalls.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.media2359.jktmalls.R;
import com.media2359.jktmalls.repository.CategoriesRepository;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.TenantsRepository;
import com.media2359.jktmalls.repository.item.Category;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.repository.item.Tenant;
import com.media2359.jktmalls.tools.FileTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by randiwaranugraha on 12/8/14.
 */
public class SplashActivity extends BaseActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);
        setupLayout(R.layout.activity_splash);

        new LoadData().execute();

        timer = new CountDownTimer(3000, 100) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);

                finish();
            }
        };
        timer.start();
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        return null;
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            loadCategories();
            loadMalls();
            loadTenants();

            return null;
        }
    }

    private void loadCategories() {
        String categories = FileTools.loadStringfromAsset(getApplicationContext(), getString(R.string.categories_asset));
        if(categories == null) {
            return;
        }

        try {
            CategoriesRepository categoriesRepo = new CategoriesRepository(getApplicationContext());

            JSONArray categoryArray = new JSONArray(categories);
            for(int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryObject = categoryArray.getJSONObject(i);
                Category category = new Category(categoryObject);
                categoriesRepo.save(category);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadMalls() {
        String malls = FileTools.loadStringfromAsset(getApplicationContext(), getString(R.string.malls_asset));
        if (malls == null) {
            return;
        }

        try {
            MallsRepository mallsRepo = new MallsRepository(getApplicationContext());

            JSONArray mallsArray = new JSONArray(malls);
            for (int i = 0; i < mallsArray.length(); i++) {
                JSONObject mallsObject = mallsArray.getJSONObject(i);
                Mall mall = new Mall(mallsObject);
                mallsRepo.save(mall);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadTenants() {
        int[] tenant_assets = new int[] {
                R.string.tenants_asset,
                R.string.tenants_asset_2,
                R.string.tenants_asset_3,
                R.string.tenants_asset_4,
                R.string.tenants_asset_5
        };
        for(int j = 0; j < tenant_assets.length; j++) {
            String tenants = FileTools.loadStringfromAsset(getApplicationContext(), getString(tenant_assets[j]));
            if (tenants == null) {
                continue;
            }

            try {
                TenantsRepository tenantRepo = new TenantsRepository(getApplicationContext());

                JSONArray tenantArray = new JSONArray(tenants);
                for (int i = 0; i < tenantArray.length(); i++) {
                    JSONObject tenantObject = tenantArray.getJSONObject(i);
                    Tenant tenant = new Tenant(tenantObject);
                    tenantRepo.save(tenant);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}