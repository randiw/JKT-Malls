package com.media2359.jktmalls.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by randiwaranugraha on 12/19/14.
 */
public class MallClusterItem implements ClusterItem {

    private LatLng position;

    public MallClusterItem(double lat, double lng) {
        position = new LatLng(lat, lng);
    }

    public MallClusterItem(LatLng position) {
        this.position = position;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }
}