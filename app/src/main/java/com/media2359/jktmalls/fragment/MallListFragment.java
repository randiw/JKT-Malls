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

import com.media2359.jktmalls.R;
import com.media2359.jktmalls.activities.MallActivity;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.view.adapter.MallsCursorAdapter;
import com.media2359.jktmalls.view.widget.CoolListView;
import com.norbsoft.typefacehelper.TypefaceHelper;

import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by randiwaranugraha on 12/13/14.
 */
public class MallListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, TextWatcher {

    public static final String TAG = MallListFragment.class.getSimpleName();
    private static final int QUERY_MALLS = 1;

    @InjectView(R.id.list) CoolListView malls;

    private MallsCursorAdapter mallsAdapter;
    private LoaderManager loaderManager;

    private String searchTerm;

    public static MallListFragment newInstance() {
        MallListFragment mallListFragment = new MallListFragment();
        return mallListFragment;
    }

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_list_mall, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.item_section, malls, false);
        TypefaceHelper.typeface(headerView);

        mallsAdapter = new MallsCursorAdapter(getActivity().getApplicationContext());
        malls.setAdapter(mallsAdapter);
        malls.setPinnedHeaderView(headerView);

        loaderManager = getLoaderManager();
        loaderManager.initLoader(QUERY_MALLS, null, this);
    }

    @OnItemClick(R.id.list)
    public void seeMall(int position) {
        Mall mall = mallsAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), MallActivity.class);
        intent.putExtra("mall_id", mall.getMallId());
        getActivity().startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (QUERY_MALLS == id) {
            String selection = null;
            if(searchTerm != null) {
                selection = MallsRepository.NAME + " LIKE '%" + searchTerm + "%'";
            }
            return new CursorLoader(getActivity().getApplicationContext(), MallsRepository.CONTENT_URI, null, selection, null, MallsRepository.NAME);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (QUERY_MALLS == loader.getId()) {
            mallsAdapter.swapCursor(data);
            mallsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (QUERY_MALLS == loader.getId()) {
            mallsAdapter.swapCursor(null);
            mallsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if(isAdded()) {
            String searchFilter = !TextUtils.isEmpty(charSequence) ? charSequence.toString() : null;
            searchTerm = searchFilter;
            mallsAdapter.setSearchTerm(searchTerm);
            loaderManager.restartLoader(QUERY_MALLS, null, this);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}