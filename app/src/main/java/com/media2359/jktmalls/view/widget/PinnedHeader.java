package com.media2359.jktmalls.view.widget;

import android.view.View;

/**
 * Created by randiwaranugraha on 12/16/14.
 */
public interface PinnedHeader {

    public static final String TAG = PinnedHeader.class.getSimpleName();
	/**
	 * Pinned header state: don't show the header.
	 */
	public static final int HEADER_GONE = 0;

	/**
	 * Pinned header state: show the header at the top of the list.
	 */
	public static final int HEADER_VISIBLE = 1;

	/**
	 * Pinned header state: show the header. If the header extends beyond the
	 * bottom of the first shown element, push it up and clip.
	 */
	public static final int HEADER_PUSHED_UP = 2;

	/**
	 * Computes the desired state of the pinned header for the given position of
	 * the first visible list item. Allowed return values are
	 * {@link #HEADER_GONE}, {@link #HEADER_VISIBLE} or
	 * {@link #HEADER_PUSHED_UP}.
	 */
	public int getPinnedHeaderState(int position);

	/**
	 * Configures the pinned header view to match the first visible list item.
	 * 
	 * @param header
	 *            pinned header view.
	 * @param position
	 *            position of the first visible list item.
	 * @param alpha
	 *            fading of the header view, between 0 and 255.
	 */
	public void configurePinnedHeader(View header, int position, int alpha);

	/**
	 * Configure the view (a listview item) to display headers or not based on
	 * displaySectionHeader (e.g. if displaySectionHeader
	 * header.setVisibility(VISIBLE) else header.setVisibility(GONE)).
	 */
	public void bindSectionHeader(View view, int position, boolean displaySectionHeader);
}