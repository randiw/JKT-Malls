package com.media2359.jktmalls.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.TextView;

import com.media2359.jktmalls.R;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.view.widget.adapter.CoolCursorAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by randiwaranugraha on 12/8/14.
 */
public class MallsCursorAdapter extends CoolCursorAdapter {

    public static final String TAG = MallsCursorAdapter.class.getSimpleName();

    private AlphabetIndexer alphabetIndexer;

    public MallsCursorAdapter(Context context) {
        super(context);
        final String alphabet = context.getString(R.string.alphabet);
        alphabetIndexer = new AlphabetIndexer(null, 2, alphabet);
    }

    @Override
    public Mall getItem(int position) {
        Cursor cursor = getCursor();
        if (!cursor.moveToPosition(position)) {
            return null;
        }

        Mall mall = new Mall(cursor);
        return mall;
    }

    @Override
    protected View createNewView(LayoutInflater inflater, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_mall, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindCoolView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Mall mall = new Mall(cursor);

        int startIndex = indexOfSearchQuery(mall.getName());
        if (startIndex == -1) {
            holder.name.setText(mall.getName());
        } else {
            SpannableString spannableString = new SpannableString(mall.getName());
            spannableString.setSpan(getSearchSpan(), startIndex, startIndex + getSearchTermLength(), 0);
            holder.name.setText(spannableString);
        }
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        int sectionIndex = getSectionForPosition(position);

        TextView sectionHeader = ButterKnife.findById(header, R.id.section_header);
        sectionHeader.setText(getSections()[sectionIndex]);

        int color = getContext().getResources().getColor(R.color.green_mountain_meadow);
        sectionHeader.setBackgroundColor(alpha << 24 | (color));
        sectionHeader.setTextColor(alpha << 24 | (0xFFFFFF));
    }

    @Override
    public void bindSectionHeader(View view, int position, boolean displaySectionHeader) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (displaySectionHeader) {
            int sectionIndex = getSectionForPosition(position);
            int color = R.color.green_mountain_meadow;

            holder.section.setVisibility(View.VISIBLE);
            holder.section.setBackgroundColor(getContext().getResources().getColor(color));
            holder.section.setText(getSections()[sectionIndex]);
        } else {
            holder.section.setVisibility(View.GONE);
        }
    }

    @Override
    public String[] getSections() {
        return (String[]) alphabetIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if (getCursor() == null) {
            return 0;
        }
        return alphabetIndexer.getPositionForSection(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (getCursor() == null) {
            return 0;
        }

        if (position >= getCursor().getCount()) {
            return 0;
        }

        return alphabetIndexer.getSectionForPosition(position);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        alphabetIndexer.setCursor(newCursor);
        return super.swapCursor(newCursor);
    }

    static class ViewHolder {
        @InjectView(R.id.section_header) TextView section;
        @InjectView(R.id.name) TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}