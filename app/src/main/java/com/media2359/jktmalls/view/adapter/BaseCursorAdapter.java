package com.media2359.jktmalls.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.jktmalls.R;
import com.norbsoft.typefacehelper.TypefaceHelper;

import java.util.Locale;

/**
 * Created by randiwaranugraha on 12/14/14.
 */
public abstract class BaseCursorAdapter extends CursorAdapter {

    private String searchTerm;
    private TextAppearanceSpan searchSpan;

    private Context context;
    private LayoutInflater inflater;

    public BaseCursorAdapter(Context context) {
        super(context, null, false);
        this.context = context;
        inflater = LayoutInflater.from(context);
        searchSpan = new TextAppearanceSpan(context, R.style.search_span);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = createNewView(inflater, cursor, parent);
        TypefaceHelper.typeface(view);
        return view;
    }

    protected abstract View createNewView(LayoutInflater inflater, Cursor cursor, ViewGroup parent);

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getSearchTermLength() {
        if(searchTerm == null) {
            return 0;
        }

        return searchTerm.length();
    }

    public TextAppearanceSpan getSearchSpan() {
        return searchSpan;
    }

    public int indexOfSearchQuery(String displayName) {
        if (!TextUtils.isEmpty(searchTerm)) {
            return displayName.toLowerCase(Locale.getDefault()).indexOf(searchTerm.toLowerCase(Locale.getDefault()));
        }
        return -1;
    }

    public Context getContext() {
        return context;
    }
}