package com.media2359.jktmalls.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.github.johnkil.print.PrintView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.media2359.jktmalls.R;
import com.media2359.jktmalls.fragment.BaseFragment;
import com.media2359.jktmalls.fragment.MallListFragment;
import com.media2359.jktmalls.fragment.MallMapFragment;
import com.media2359.jktmalls.fragment.MenuFragment;
import com.media2359.jktmalls.fragment.ShopListFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.tabak.fragmentswitcher.FragmentStateArrayPagerAdapter;
import me.tabak.fragmentswitcher.FragmentSwitcher;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
@SuppressLint("ResourceAsColor")
public class MainActivity extends BaseActivity implements View.OnClickListener, MenuFragment.OnMenuClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView title;
    private PrintView searchOrLocate;
    private MaterialMenuView materialMenu;
    private EditText searchInput;

    private SlidingMenu slidingMenu;
    private FragmentStateArrayPagerAdapter<BaseFragment> fragmentAdapter;
    private MallListFragment mallListFragment;
    private ShopListFragment shopListFragment;
    private MallMapFragment mallMapFragment;

    private enum SearchIconState {
        SEARCH, LOCATION_INACTIVE, LOCATION_ACTIVE
    }

    private SearchIconState searchIconState;

    @InjectView(R.id.content) FragmentSwitcher fragmentSwitcher;

    private InputMethodManager inputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);
        initSlidingMenu();

        fragmentAdapter = new FragmentStateArrayPagerAdapter<BaseFragment>(getSupportFragmentManager());
        fragmentSwitcher.setAdapter(fragmentAdapter);

        mallListFragment = MallListFragment.newInstance();
        fragmentAdapter.add(mallListFragment);
        searchInput.addTextChangedListener(mallListFragment);

        shopListFragment = ShopListFragment.newInstance();
        fragmentAdapter.add(shopListFragment);
        searchInput.addTextChangedListener(shopListFragment);

        mallMapFragment = MallMapFragment.newInstance();
        fragmentAdapter.add(mallMapFragment);
        searchInput.addTextChangedListener(mallMapFragment);

        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.fragment_menu);
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                materialMenu.animateState(MaterialMenuDrawable.IconState.ARROW);
            }
        });
        slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
            }
        });
    }

    private void enableSlide() {
        if (slidingMenu != null) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        }
    }

    private void disableSlide() {
        if (slidingMenu != null) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.actionbar_main, null);

        title = ButterKnife.findById(view, R.id.title);
        title.setVisibility(View.VISIBLE);

        materialMenu = ButterKnife.findById(view, R.id.menu);
        materialMenu.setOnClickListener(this);

        searchOrLocate = ButterKnife.findById(view, R.id.search_icon);
        searchOrLocate.setOnClickListener(this);
        searchIconState = SearchIconState.SEARCH;

        searchInput = ButterKnife.findById(view, R.id.search_input);
        searchInput.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_icon:
                if (slidingMenu.isMenuShowing()) {
                    return;
                }

                if (SearchIconState.SEARCH == searchIconState) {
                    openSearch();
                } else if (SearchIconState.LOCATION_INACTIVE == searchIconState) {
                    enableLocation();
                } else if (SearchIconState.LOCATION_ACTIVE == searchIconState) {
                    disableLocation();
                }
                break;

            case R.id.menu:
                if (MaterialMenuDrawable.IconState.BURGER == materialMenu.getState()) {
                    slidingMenu.toggle(true);
                } else if (MaterialMenuDrawable.IconState.ARROW == materialMenu.getState()) {
                    closeSearch();
                    if (slidingMenu.isMenuShowing()) {
                        slidingMenu.toggle(true);
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (MaterialMenuDrawable.IconState.ARROW == materialMenu.getState()) {
            closeSearch();
            if (slidingMenu.isMenuShowing()) {
                slidingMenu.toggle(true);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMenuClick(int id) {
        slidingMenu.toggle(true);
        switch (id) {
            case R.id.malls:
                fragmentSwitcher.setCurrentItem(0);
                break;

            case R.id.shops:
                fragmentSwitcher.setCurrentItem(1);
                break;
        }
    }

    private void openSearch() {
        disableSlide();

        title.setVisibility(View.INVISIBLE);
        searchInput.setVisibility(View.VISIBLE);
        searchInput.requestFocus();
        openKeyboard();

        searchOrLocate.setIconText(R.string.ic_material_location);
        searchIconState = SearchIconState.LOCATION_INACTIVE;

        materialMenu.animateState(MaterialMenuDrawable.IconState.ARROW);
    }

    private void closeSearch() {
        enableSlide();

        searchInput.setText(null);
        searchInput.setVisibility(View.INVISIBLE);
        searchInput.clearFocus();
        title.setVisibility(View.VISIBLE);
        closeKeyboard();

        searchOrLocate.setIconText(R.string.ic_material_search);
        searchOrLocate.setIconColor(android.R.color.white);
        searchIconState = SearchIconState.SEARCH;

        materialMenu.animateState(MaterialMenuDrawable.IconState.BURGER);
    }

    private void enableLocation() {
        searchIconState = SearchIconState.LOCATION_ACTIVE;
        searchOrLocate.setIconColor(R.color.blue_dodger);
        closeKeyboard();
        fragmentSwitcher.setCurrentItem(2);
    }

    private void disableLocation() {
        searchIconState = SearchIconState.LOCATION_INACTIVE;
        searchOrLocate.setIconColor(android.R.color.white);

        fragmentSwitcher.setCurrentItem(0);
    }

    private void openKeyboard() {
        if (inputManager != null) {
            inputManager.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void closeKeyboard() {
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
        }
    }
}