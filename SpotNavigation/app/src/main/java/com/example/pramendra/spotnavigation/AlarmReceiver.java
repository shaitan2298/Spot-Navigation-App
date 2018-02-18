package com.example.pramendra.spotnavigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            AppLocationService service=new AppLocationService(context);
            Location location=  service.getLocation(LocationManager.NETWORK_PROVIDER);
            SimpleDateFormat fmt=new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            String curTime=fmt.format(new java.util.Date());
            String dtWrite=String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude())+","+curTime+"@";
            NavigationUtility.writeLocation("SpotLocation.txt",dtWrite);
        }
        catch(Exception ex){
            Log.e("AlarmBroadCast",ex.toString());
        }
    }
}
