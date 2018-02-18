package com.example.pramendra.spotnavigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Pramendra on 3/28/2017.
 */

public class DataEnabled extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NavigationUtility.showToast(context,"Data Enabled",0);
            if (NetworkUtility.getConnectivityStatus(context) == 2) {
                String lcnts = NavigationUtility.readLocation(context, "SpotLocation.txt");
                String[] lines = lcnts.split("@");
                NavigationUtility.showToast(context,String.valueOf(lines),0);
                double lat;
                double lngt;
                String curTime;
                StringBuffer allLocation = new StringBuffer();
                for (String line : lines) {
                    String []cords=line.split(",");
                    lat=Double.valueOf(cords[0]);
                    lngt=Double.valueOf(cords[1]);
                    curTime=cords[2];
                    String locName=NavigationUtility.getLocationName(context,lat,lngt);
                    allLocation.append(locName+" "+curTime+"<br><br>");
                }
                EmailSender sender = new EmailSender();
                sender.sendEmail(context, "Your Locations", allLocation.toString());
                NavigationUtility.clearLocation("SpotLocation.txt");
            }
        }
        catch(Exception ex){
            Log.e("DataEnabled",ex.toString());
        }
    }
}
