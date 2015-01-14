package com.media2359.jktmalls.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.media2359.jktmalls.R;
import com.media2359.jktmalls.repository.CategoriesRepository;
import com.media2359.jktmalls.repository.TenantsRepository;
import com.media2359.jktmalls.repository.item.Category;
import com.media2359.jktmalls.repository.item.Tenant;
import com.media2359.jktmalls.tools.RepoTools;
import com.media2359.jktmalls.view.widget.adapter.CoolCursorAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by randiwaranugraha on 12/15/14.
 */
public class TenantsCursorAdapter extends CoolCursorAdapter {

    public static final String TAG = TenantsCursorAdapter.class.getSimpleName();
    private CategoriesRepository categoriesRepo;

    public TenantsCursorAdapter(Context context) {
        super(context);
        categoriesRepo = new CategoriesRepository(context);
    }

    @Override
    public Tenant getItem(int position) {
        Cursor cursor = getCursor();
        if (!cursor.moveToPosition(position)) {
            return null;
        }

        Tenant tenant = new Tenant(cursor);
        return tenant;
    }

    @Override
    protected View createNewView(LayoutInflater inflater, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_tenant, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindCoolView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Tenant tenant = new Tenant(cursor);

        int startIndex = indexOfSearchQuery(tenant.getName());
        if (startIndex == -1) {
            holder.name.setText(tenant.getName());
        } else {
            SpannableString spannableString = new SpannableString(tenant.getName());
            spannableString.setSpan(getSearchSpan(), startIndex, startIndex + getSearchTermLength(), 0);
            holder.name.setText(spannableString);
        }
        holder.unit.setText(tenant.getUnit());
    }

    @Override
    public void configurePinnedHeader(View header, int position, int alpha) {
        int sectionIndex = getSectionForPosition(position);

        Category category = categoriesRepo.find(sectionIndex);

        TextView sectionHeader = ButterKnife.findById(header, R.id.section_header);
        sectionHeader.setText(category.getName());

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

            Category category = categoriesRepo.find(sectionIndex);

            holder.section.setVisibility(View.VISIBLE);
            holder.section.setBackgroundColor(getContext().getResources().getColor(color));
            holder.section.setText(category.getName());
        } else {
            holder.section.setVisibility(View.GONE);
        }
    }

    @Override
    public String[] getSections() {
        return categoriesRepo.getAllToStringArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return 0;
        }
        if (sectionIndex == 0) {
            return 0;
        }
        if (!cursor.moveToFirst()) {
            return 0;
        }

        do {
            int category_id = RepoTools.getInt(cursor, TenantsRepository.CATEGORY_ID);
            if (sectionIndex == category_id) {
                int position = cursor.getPosition();
                return position;
            }
        } while (cursor.moveToNext());

        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return 0;
        }
        if (position >= cursor.getCount()) {
            cursor.moveToLast();
            int category_id = RepoTools.getInt(cursor, TenantsRepository.CATEGORY_ID);
            return category_id;
        }
        if (cursor.moveToPosition(position)) {
            int category_id = RepoTools.getInt(cursor, TenantsRepository.CATEGORY_ID);
            return category_id;
        }

        return 0;
    }

    static class ViewHolder {
        @InjectView(R.id.section_header) TextView section;
        @InjectView(R.id.name) TextView name;
        @InjectView(R.id.unit) TextView unit;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}