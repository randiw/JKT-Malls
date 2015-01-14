package com.media2359.jktmalls.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.media2359.jktmalls.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by randiwaranugraha on 12/13/14.
 */
public class MenuFragment extends BaseFragment {

    public static final String TAG = MenuFragment.class.getSimpleName();

    @InjectView(R.id.malls) TextView malls;
    @InjectView(R.id.shops) TextView shops;

    private OnMenuClickListener menuClickListener;

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_menu_content, container, false);
        return view;
    }

    @OnClick({R.id.malls, R.id.shops})
    public void menuClick(View view) {
        if(menuClickListener != null) {
            menuClickListener.onMenuClick(view.getId());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnMenuClickListener) {
            menuClickListener = (OnMenuClickListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        menuClickListener = null;
    }

    public interface OnMenuClickListener {
        public void onMenuClick(int id);
    }
}