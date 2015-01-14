package com.media2359.jktmalls.view.widget.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AbsListView;
import android.widget.SectionIndexer;

import com.media2359.jktmalls.view.adapter.BaseCursorAdapter;
import com.media2359.jktmalls.view.widget.CoolListView;
import com.media2359.jktmalls.view.widget.PinnedHeader;

/**
 * Created by randiwaranugraha on 12/16/14.
 */
public abstract class CoolCursorAdapter extends BaseCursorAdapter implements SectionIndexer, AbsListView.OnScrollListener, PinnedHeader {

    private static final String TAG = CoolCursorAdapter.class.getSimpleName();

    public CoolCursorAdapter(Context context) {
        super(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        bindCoolView(view, context, cursor);

        int position = cursor.getPosition();
        final int section = getSectionForPosition(position);
        boolean displaySectionHeader = (getPositionForSection(section) == position);
        bindSectionHeader(view, position, displaySectionHeader);
    }

    public abstract void bindCoolView(View view, Context context, Cursor cursor);

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof CoolListView) {
            ((CoolListView) view).configureHeaderView(firstVisibleItem);
        }
    }

    @Override
    public int getPinnedHeaderState(int position) {
        if (position < 0 || getCount() == 0) {
            return HEADER_GONE;
        }

        // The header should get pushed up if the top item shown
        // is the last item in a section for a particular letter.
        int section = getSectionForPosition(position);
        int nextSectionPosition = getPositionForSection(section + 1);
        if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
            return HEADER_PUSHED_UP;
        }

        return HEADER_VISIBLE;
    }
}