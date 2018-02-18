package com.example.pramendra.spotnavigation;
/**
 * Created by Pramendra on 3/27/2017.
 */
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;

import  java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.*;
import android.util.Log;
import android.widget.Toast;

import static android.R.attr.data;

public class NavigationUtility {
    public static File storagePath,dir;
    static{
        storagePath= Environment.getExternalStorageDirectory();
        dir=new File(storagePath.getAbsolutePath()+"/SpotNavigation/");
        dir.mkdir();
    }
    public static void writeLocation(String fileName,String data){
            try{
                File file=new File(dir,fileName);
                FileOutputStream fileOut=new FileOutputStream(file,true);
                OutputStreamWriter fileWriter=new OutputStreamWriter(fileOut);
                fileWriter.write(data);
                fileWriter.close();
            }
            catch(Exception ioex){
                ioex.printStackTrace();
            }
    }
    public static void clearLocation(String fileName){
        try{
            File file=new File(dir,fileName);
            FileOutputStream fileOut=new FileOutputStream(file);
            fileOut.write(("").getBytes());
            fileOut.flush();
            fileOut.close();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public static String readLocation(Context context,String fileName)
    {
        String str="";
        try {
            File file = new File(dir, fileName);
            FileInputStream inFile = new FileInputStream(file);
            byte []data=new byte[(int)file.length()];
            inFile.read(data);
            inFile.close();
            str=new String(data,"UTF-8");

        }
        catch(Exception ex){
            Log.e("ReadException",ex.toString());
        }
        return str;
    }
    public static void showToast(Context context,String msg,int len){
        Toast.makeText(context,msg,len==1?Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show();
    }
    public static String getLocationName(Context context,Double lat,Double lont)
    {
        String locName="";
         String add="";
        String currentAddress="";
        try {
            Geocoder coder=new Geocoder(context, Locale.getDefault());
            List<Address> details=coder.getFromLocation(lat,lont,1);
            android.location.Address obj=details.get(0);
            add=obj.getAddressLine(0);
            currentAddress = obj.getSubAdminArea() + ","
                       + obj.getAdminArea()+","+obj.getLocality();
            locName=add+" "+currentAddress;

        } catch (IOException e) {
            Log.e("IOExcept",e.toString());
        }
        return locName;
    }
}
