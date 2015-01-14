package com.media2359.jktmalls.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.media2359.jktmalls.R;
import com.media2359.jktmalls.fragment.MallDirectoryFragment;
import com.media2359.jktmalls.fragment.MallInfoFragment;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.viewpagerindicator.TitlePageIndicator;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by randiwaranugraha on 12/15/14.
 */
public class MallActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = MallActivity.class.getSimpleName();

    private TextView title;
    private MaterialMenuView materialMenu;

    @InjectView(R.id.view_pager) ViewPager viewPager;
    @InjectView(R.id.tab) TitlePageIndicator titlePageIndicator;

    private Mall mall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_mall);

        int mallId = getIntent().getIntExtra("mall_id", 0);
        MallsRepository mallsRepo = new MallsRepository(getApplicationContext());
        mall = mallsRepo.find(mallId);

        title.setText(mall.getName());

        MallDetailAdapter mallDetailAdapter = new MallDetailAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mallDetailAdapter);
        titlePageIndicator.setViewPager(viewPager);
    }

    @Override
    protected View createActionBar(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.actionbar_base, null);

        title = ButterKnife.findById(view, R.id.title);

        materialMenu = ButterKnife.findById(view, R.id.menu);
        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        materialMenu.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                onBackPressed();
                break;
        }
    }

    class MallDetailAdapter extends FragmentPagerAdapter {

        private MallDirectoryFragment directoryFragment;
        private MallInfoFragment infoFragment;

        MallDetailAdapter(FragmentManager fm) {
            super(fm);
            int mall_id = mall.getMallId();
            directoryFragment = MallDirectoryFragment.newInstance(mall_id);
            infoFragment = MallInfoFragment.newInstance(mall_id);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return infoFragment;

                default:
                    return directoryFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return "Info";

                default:
                    return "Directory";
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
