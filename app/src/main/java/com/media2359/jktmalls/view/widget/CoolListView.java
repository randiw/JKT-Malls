package com.media2359.jktmalls.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.media2359.jktmalls.view.widget.adapter.CoolCursorAdapter;

/**
 * Created by randiwaranugraha on 12/16/14.
 */
public class CoolListView extends ListView {

    public static final String TAG = CoolListView.class.getSimpleName();

    private View headerView;
    private boolean headerViewVisible;

    private int headerViewWidth;
    private int headerViewHeight;

    private PinnedHeader pinnedHeader;
    private ListAdapter adapter;

    public CoolListView(Context context) {
        super(context);
    }

    public CoolListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoolListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPinnedHeaderView(View view) {
        headerView = view;
        if (headerView != null) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (!(adapter instanceof PinnedHeader)) {
            throw new IllegalArgumentException(CoolListView.class.getSimpleName() + " must use adapter of type " + PinnedHeader.class.getSimpleName());
        }

        pinnedHeader = (PinnedHeader) adapter;
        if (adapter instanceof CoolCursorAdapter) {
            this.adapter = adapter;
            setOnScrollListener((CoolCursorAdapter) adapter);
        }

        View dummy = new View(getContext());
        super.addFooterView(dummy);
        super.setAdapter(adapter);
        super.removeFooterView(dummy);
    }

    @Override
    public ListAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (headerView != null) {
            measureChild(headerView, widthMeasureSpec, heightMeasureSpec);
            headerViewWidth = headerView.getMeasuredWidth();
            headerViewHeight = headerView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (headerView != null) {
            headerView.layout(0, 0, headerViewWidth, headerViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    public void configureHeaderView(int position) {
        if (headerView == null) {
            return;
        }

        int state = pinnedHeader.getPinnedHeaderState(position);
        switch (state) {
            case PinnedHeader.HEADER_GONE: {
                headerViewVisible = false;
                break;
            }

            case PinnedHeader.HEADER_VISIBLE: {
                pinnedHeader.configurePinnedHeader(headerView, position, 255);
                if (headerView.getTop() != 0) {
                    headerView.layout(0, 0, headerViewWidth, headerViewHeight);
                }
                headerViewVisible = true;
                break;
            }

            case PinnedHeader.HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                if (firstView != null) {
                    int bottom = firstView.getBottom();
                    int headerHeight = headerView.getHeight();
                    int y;
                    int alpha;
                    if (bottom < headerHeight) {
                        y = (bottom - headerHeight);
                        alpha = 255 * (headerHeight + y) / headerHeight;
                    } else {
                        y = 0;
                        alpha = 255;
                    }
                    pinnedHeader.configurePinnedHeader(headerView, position, alpha);
                    if (headerView.getTop() != y) {
                        headerView.layout(0, y, headerViewWidth, headerViewHeight + y);
                    }
                    headerViewVisible = true;
                }
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (headerViewVisible) {
            drawChild(canvas, headerView, getDrawingTime());
        }
    }
}