package com.media2359.jktmalls.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.ButterKnife;

/**
 * Created by randiwaranugraha on 12/14/14.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = setupLayout(inflater, container);
        ButterKnife.inject(this, view);
        TypefaceHelper.typeface(view);
        return view;
    }

    protected abstract View setupLayout(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}