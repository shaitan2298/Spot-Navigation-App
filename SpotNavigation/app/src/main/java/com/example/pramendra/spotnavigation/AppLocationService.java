package com.example.pramendra.spotnavigation;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class AppLocationService extends Service implements LocationListener {
    protected LocationManager locationManager;
    Location location;
    Context context;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public AppLocationService() {
    }
    public AppLocationService(Context context){
        this.context=context;
        locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }
    public Location getLocation(String provider){
        try {
            if (locationManager.isProviderEnabled(provider)) {
                locationManager.requestLocationUpdates(provider, 0, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider);
                    return location;
                }
            }
        }
        catch(SecurityException ex){
            Log.e("Security Ex",ex.toString());
        }
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
