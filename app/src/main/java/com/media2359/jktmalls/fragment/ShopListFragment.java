package com.media2359.jktmalls.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.media2359.jktmalls.R;
import com.media2359.jktmalls.activities.TenantDetailActivity;
import com.media2359.jktmalls.repository.TenantsRepository;
import com.media2359.jktmalls.repository.item.Tenant;
import com.media2359.jktmalls.view.adapter.ShopsCursorAdapter;

import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by randiwaranugraha on 12/14/14.
 */
public class ShopListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher {

    public static final String TAG = ShopListFragment.class.getSimpleName();
    private static final int QUERY_SHOPS = 2;

    @InjectView(R.id.list) ListView shops;

    private ShopsCursorAdapter shopsCursorAdapter;
    private LoaderManager loaderManager;

    private String searchTerm;

    public static ShopListFragment newInstance() {
        ShopListFragment shopListFragment = new ShopListFragment();
        return shopListFragment;
    }

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_list_shop, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shopsCursorAdapter = new ShopsCursorAdapter(getActivity().getApplicationContext());
        shops.setAdapter(shopsCursorAdapter);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(QUERY_SHOPS, null, this);
    }

    @OnItemClick(R.id.list)
    public void seeShop(int position) {
        Tenant tenant = shopsCursorAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), TenantDetailActivity.class);
        intent.putExtra("tenant_id", tenant.getTenantId());
        getActivity().startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if(isAdded()) {
            String searchFilter = !TextUtils.isEmpty(charSequence) ? charSequence.toString() : null;
            searchTerm = searchFilter;
            shopsCursorAdapter.setSearchTerm(searchTerm);
            loaderManager.restartLoader(QUERY_SHOPS, null, this);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(QUERY_SHOPS == id) {
            String selection = null;
            if(searchTerm != null) {
                selection = TenantsRepository.NAME + " LIKE '%" + searchTerm + "%'";
            }
            return new CursorLoader(getActivity().getApplicationContext(), TenantsRepository.CONTENT_URI, null, selection, null, TenantsRepository.NAME);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
        if(QUERY_SHOPS == loader.getId()) {
            shopsCursorAdapter.swapCursor(newCursor);
            shopsCursorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(QUERY_SHOPS == loader.getId()) {
            shopsCursorAdapter.swapCursor(null);
            shopsCursorAdapter.notifyDataSetChanged();
        }
    }
}