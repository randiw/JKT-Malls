package com.media2359.jktmalls.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.media2359.jktmalls.R;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.repository.item.Tenant;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by randiwaranugraha on 12/10/14.
 */
public class ShopsCursorAdapter extends BaseCursorAdapter {

    public static final String TAG = ShopsCursorAdapter.class.getSimpleName();

    private MallsRepository mallsRepo;

    public ShopsCursorAdapter(Context context) {
        super(context);
        mallsRepo = new MallsRepository(context);
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
        View view = inflater.inflate(R.layout.item_shop, parent, false);

        ViewHolder holder = new ViewHolder(view);
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Tenant tenant = new Tenant(cursor);

        Mall mall = mallsRepo.find(tenant.getMallId());

        int startIndex = indexOfSearchQuery(tenant.getName());
        if (startIndex == -1) {
            holder.name.setText(tenant.getName());
        } else {
            SpannableString spannableString = new SpannableString(tenant.getName());
            spannableString.setSpan(getSearchSpan(), startIndex, startIndex + getSearchTermLength(), 0);
            holder.name.setText(spannableString);
        }
        holder.mallName.setText("at " + mall.getName());
    }

    static class ViewHolder {
        @InjectView(R.id.name) TextView name;
        @InjectView(R.id.mall_name) TextView mallName;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}