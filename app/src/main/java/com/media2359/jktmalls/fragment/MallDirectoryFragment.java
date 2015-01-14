package com.media2359.jktmalls.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.media2359.jktmalls.R;
import com.media2359.jktmalls.activities.TenantDetailActivity;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.TenantsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.repository.item.Tenant;
import com.media2359.jktmalls.view.adapter.TenantsCursorAdapter;
import com.media2359.jktmalls.view.widget.CoolListView;
import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by randiwaranugraha on 12/15/14.
 */
public class MallDirectoryFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = MallDirectoryFragment.class.getSimpleName();
    private static final int QUERY_TENANT = 3;

    private LoaderManager loaderManager;
    private TenantsCursorAdapter tenantsAdapter;

    @InjectView(R.id.search) EditText search;
    @InjectView(R.id.list) CoolListView tenants;

    private Mall mall;
    private String searchTerm;

    public static MallDirectoryFragment newInstance(int mall_id) {
        MallDirectoryFragment mallDirectoryFragment = new MallDirectoryFragment();

        Bundle data = new Bundle();
        data.putInt("mall_id", mall_id);
        mallDirectoryFragment.setArguments(data);

        return mallDirectoryFragment;
    }

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mall_directory, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle data = getArguments();
        int mallId = data.getInt("mall_id");

        MallsRepository mallsRepo = new MallsRepository(getActivity().getApplicationContext());
        mall = mallsRepo.find(mallId);

        search.addTextChangedListener(textWatcher);

        View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.item_section, tenants, false);
        TypefaceHelper.typeface(headerView);

        tenantsAdapter = new TenantsCursorAdapter(getActivity().getApplicationContext());
        tenants.setAdapter(tenantsAdapter);
        tenants.setPinnedHeaderView(headerView);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(QUERY_TENANT, null, this);
    }

    @OnItemClick(R.id.list)
    public void openTenant(int position) {
        Tenant tenant = tenantsAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), TenantDetailActivity.class);
        intent.putExtra("tenant_id", tenant.getTenantId());
        getActivity().startActivity(intent);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if(isAdded()) {
                String searchFilter = !TextUtils.isEmpty(charSequence) ? charSequence.toString() : null;
                searchTerm = searchFilter;
                tenantsAdapter.setSearchTerm(searchTerm);
                loaderManager.restartLoader(QUERY_TENANT, null, MallDirectoryFragment.this);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(QUERY_TENANT == id) {
            String selection = TenantsRepository.MALL_ID + "='" + mall.getMallId() + "'";
            if(searchTerm != null) {
                selection += " AND " + TenantsRepository.NAME + " LIKE '%" + searchTerm + "%'";
            }
            return new CursorLoader(getActivity().getApplicationContext(), TenantsRepository.CONTENT_URI, null, selection, null, TenantsRepository.CATEGORY_ID + ", " + TenantsRepository.NAME);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
        if(QUERY_TENANT == loader.getId()) {
            tenantsAdapter.swapCursor(newCursor);
            tenantsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(QUERY_TENANT == loader.getId()) {
            tenantsAdapter.swapCursor(null);
            tenantsAdapter.notifyDataSetChanged();
        }
    }
}