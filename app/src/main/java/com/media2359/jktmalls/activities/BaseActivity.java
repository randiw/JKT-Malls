package com.media2359.jktmalls.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.media2359.jktmalls.model.LocationManager;
import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.ButterKnife;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocation();
        initActionBar();
    }

    protected void setupLayout(@LayoutRes int resource) {
        setContentView(resource);
        ButterKnife.inject(this);
        TypefaceHelper.typeface(this);
    }

    private void initActionBar() {
        View customActionBar = createActionBar(LayoutInflater.from(getApplicationContext()));
        TypefaceHelper.typeface(customActionBar);
        if(customActionBar != null) {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(customActionBar);
        }
    }

    protected abstract View createActionBar(LayoutInflater inflater);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    private void initLocation() {
        if(!LocationManager.hasInit()) {
            LocationManager.init(getApplicationContext());
        }
    }
}