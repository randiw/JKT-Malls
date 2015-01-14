package com.media2359.jktmalls.model;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by randiwaranugraha on 12/18/14.
 */
public class LocationManager {

    public static final String TAG = LocationManager.class.getSimpleName();

    private static LocationManager instance = null;

    public static void init(Context context) {
        instance = new LocationManager(context);
    }

    public static boolean hasInit() {
        if (instance == null) {
            return false;
        }

        return true;
    }

    public static LocationManager getInstance() {
        if (hasInit()) {
            return instance;
        }
        return null;
    }

    private ReactiveLocationProvider locationProvider;
    private Location lastLocation;
    private ArrayList<LocationListener> listeners;

    private LocationManager(Context context) {
        locationProvider = new ReactiveLocationProvider(context);
        locationProvider.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                lastLocation = location;
            }
        });
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void forceUpdate() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setExpirationDuration(TimeUnit.SECONDS.toMillis(4))
                .setInterval(1000);

        Observable<Location> goodEnoughObservables = locationProvider.getUpdatedLocation(locationRequest)
                .filter(new Func1<Location, Boolean>() {
                    @Override
                    public Boolean call(Location location) {
                        return true;
                    }
                })
                .timeout(4, TimeUnit.SECONDS, Observable.just((Location) null), AndroidSchedulers.mainThread())
                .first()
                .observeOn(AndroidSchedulers.mainThread());

        goodEnoughObservables.subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                if (location == null) {
                    return;
                }

                lastLocation = location;
                if (listeners != null && listeners.size() > 0) {
                    for (LocationListener listener : listeners) {
                        listener.onLocationUpdate(location);
                    }
                }
            }
        });
    }

    public void addListener(LocationListener locationListener) {
        if (listeners == null) {
            listeners = new ArrayList<LocationListener>();
        }
        if (!listeners.contains(locationListener)) {
            listeners.add(locationListener);
        }
    }

    public void removeListener(LocationListener locationListener) {
        if (listeners == null) {
            return;
        }
        if (listeners.contains(locationListener)) {
            listeners.remove(locationListener);
        }
    }

    public interface LocationListener {
        public void onLocationUpdate(Location location);
    }
}