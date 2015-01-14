package com.media2359.jktmalls.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.ClusterManager;
import com.media2359.jktmalls.R;
import com.media2359.jktmalls.activities.MainActivity;
import com.media2359.jktmalls.model.LocationManager;
import com.media2359.jktmalls.model.MallClusterItem;
import com.media2359.jktmalls.repository.MallsRepository;
import com.media2359.jktmalls.repository.item.Mall;
import com.media2359.jktmalls.tools.LocationTools;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by randiwaranugraha on 12/18/14.
 */
public class MallMapFragment extends BaseFragment implements LocationManager.LocationListener, TextWatcher {

    public static final String TAG = MallMapFragment.class.getSimpleName();

    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker userMarker;

    private float zoom;
    private String searchTerm;
    private Bundle searchBundle;

    public static MallMapFragment newInstance() {
        MallMapFragment mallMapFragment = new MallMapFragment();
        return mallMapFragment;
    }

    @Override
    protected View setupLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mall_map, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = getChildFragmentManager();
        googleMap = ((SupportMapFragment) fragmentManager.findFragmentById(R.id.map)).getMap();

        MapsInitializer.initialize(getActivity().getApplicationContext());
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext()) == ConnectionResult.SUCCESS) {
            initLocation();
            populatingMalls();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager.addListener(this);
        locationManager.forceUpdate();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeListener(this);
    }

    private void initLocation() {
        googleMap.setOnCameraChangeListener(cameraChange);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(true);

        locationManager = LocationManager.getInstance();
        Location lastLocation = locationManager.getLastLocation();

        if(lastLocation != null) {
            zoom = 14.0f;
            LatLng position = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
            setUserMarker(position.latitude, position.longitude, true);
        }
    }

    private void populatingMalls() {
        Location lastLocation = locationManager.getLastLocation();
        LatLng latLon = null;
        if(lastLocation == null) {
            latLon = new LatLng(-6.2341111, 106.821174);
        } else {
            latLon = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        searchMalls(latLon.latitude, latLon.longitude, 2000);
    }

    private void searchMalls(double lat, double lon, int distance) {
        if(searchBundle == null) {
            searchBundle = new Bundle();
        }

        if(lat == 0) {
            lat = searchBundle.getDouble("latitude", 0);
        } else {
            searchBundle.putDouble("latitude", lat);
        }
        if(lon == 0) {
            lon = searchBundle.getDouble("longitude", 0);
        } else {
            searchBundle.putDouble("longitude", lon);
        }
        if(distance == 0) {
            distance = searchBundle.getInt("distance", 0);
        } else {
            searchBundle.putInt("distance", distance);
        }

        googleMap.clear();
        Location lastLocation = locationManager.getLastLocation();
        if(lastLocation != null) {
            setUserMarker(lastLocation.getLatitude(), lastLocation.getLongitude(), true);
        }

        float x = LocationTools.convert(lat);
        float y = LocationTools.convert(lon);

        MallsRepository mallsRepo = new MallsRepository(getActivity().getApplicationContext());
        ArrayList<Mall> malls = mallsRepo.findByPosition(x, y, distance, searchTerm);

        if(malls == null) {
            return;
        }

        for(Mall mall : malls) {
            LatLng latLng = new LatLng(mall.getPosition().latitude, mall.getPosition().longitude);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title(mall.getName())
                    .snippet(mall.getAddress());

            googleMap.addMarker(markerOptions);
        }
    }

    private GoogleMap.OnCameraChangeListener cameraChange = new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            float oldZoom = zoom;
            zoom = cameraPosition.zoom;

            VisibleRegion visibleRegion = googleMap.getProjection().getVisibleRegion();
            LatLng center = visibleRegion.latLngBounds.getCenter();

            if(zoom < oldZoom) {
                // zoom out, radius bigger, fetch more item
                double left = visibleRegion.latLngBounds.southwest.longitude;

                Location centerLocation = new Location("center");
                centerLocation.setLatitude(center.latitude);
                centerLocation.setLongitude(center.longitude);

                Location centerLeftLocation = new Location("centerLeft");
                centerLeftLocation.setLatitude(center.latitude);
                centerLeftLocation.setLongitude(left);

                float distanceMeter = centerLocation.distanceTo(centerLeftLocation);
//                float distanceKM = Math.round(distanceMeter / 100) / 10;
//                double doubleDistance = distanceKM;

                searchMalls(center.latitude, center.longitude, (int) distanceMeter);
            } else if(zoom > oldZoom) {

            } else {

            }
        }
    };

    private void moveCenterTo(double lat, double lng) {
        LatLng centerPoint = new LatLng(lat, lng);
        zoom = 15.0f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(centerPoint, zoom);
        googleMap.animateCamera(cameraUpdate);
    }

    @Override
    public void onLocationUpdate(Location location) {
        if(location == null) {
            return;
        }
        moveCenterTo(location.getLatitude(), location.getLongitude());
        setUserMarker(location.getLatitude(), location.getLongitude(), false);
    }

    private void setUserMarker(double lat, double lon, boolean clear) {
        if(clear) {
            userMarker = null;
        }

        LatLng position = new LatLng(lat, lon);
        if(userMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_user));

            userMarker = googleMap.addMarker(markerOptions);
        }
        userMarker.setPosition(position);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if(isAdded()) {
            String searchFilter = !TextUtils.isEmpty(charSequence) ? charSequence.toString() : null;
            searchTerm = searchFilter;
            searchMalls(0,0,0);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}