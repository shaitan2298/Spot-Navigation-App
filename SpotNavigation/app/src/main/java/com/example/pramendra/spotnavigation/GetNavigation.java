package com.example.pramendra.spotnavigation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class GetNavigation extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        SearchView.OnQueryTextListener {
    private GoogleMap mMap;
    ActionBar actBar;
    LocationManager locMgr;
    Context context;
    DrawerLayout dLayout;
    private MyLocation myLocation;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavigationUtility.showToast(context,"Press Back Again To Exit",0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_navigation);
        Intent locIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 234324243, locIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService
                (Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 1000*60*5, pendingIntent);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blackTrans));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.slidemenu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(
                getResources().getColor(R.color.smokyWhite)));
        getSupportActionBar().setTitle(Html.fromHtml(
                "<font color='#000000' size='12px'>Spot Navigation </font>"));
        setNavigationDrawer();
    }
    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null;
                int itemId = menuItem.getItemId();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().
                            beginTransaction();
                    transaction.commit();
                    dLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.appbar,menu);
        SearchView sview=(SearchView)menu.findItem(R.id.mnsearch).getActionView();
        sview.setOnQueryTextListener(this);
        sview.setIconifiedByDefault(false);
        SearchManager searchManager =(SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        sview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            NavigationUtility.showToast(context,query,0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            dLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if(locMgr==null){
            locMgr=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        }
        if(locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            try {
                Location lstLoc=locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                mMap.setOnMapClickListener(this);
                mMap.setOnMapLongClickListener(this);
                locMgr.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
                String add="";
                String currentAddress="";
                Geocoder coder=new Geocoder(getApplicationContext(), Locale.getDefault());
                List<android.location.Address> details=
                        coder.getFromLocation(lstLoc.getLatitude(),lstLoc.getLongitude(),1);
                android.location.Address obj=details.get(0);
                add=obj.getAddressLine(0);
                currentAddress = obj.getSubAdminArea() + ","
                        + obj.getAdminArea();
                LatLng curLoc=new LatLng(lstLoc.getLatitude(),lstLoc.getLongitude());
                BitmapDescriptor marIcon= BitmapDescriptorFactory.fromResource(R.drawable.tmarker6);
                mMap.addMarker(new MarkerOptions().position(curLoc).title(add).
                        snippet(currentAddress).draggable(true).icon(marIcon)).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLoc,15));
            }
            catch(SecurityException ex){
                ex.printStackTrace();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    LocationListener locationListenerNetwork=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mMap.clear();
            String add="";
            String currentAddress="";
            Geocoder coder=new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<android.location.Address> details=
                        coder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                android.location.Address obj=details.get(0);
                add=obj.getAddressLine(0);
                currentAddress = obj.getSubAdminArea() + ","
                        + obj.getAdminArea();
                LatLng curLoc=new LatLng(location.getLatitude(),location.getLongitude());
                BitmapDescriptor marIcon=
                        BitmapDescriptorFactory.fromResource(R.drawable.tmarker6);
                mMap.addMarker(new MarkerOptions().position(curLoc).title(add).
                        snippet(currentAddress).draggable(true).icon(marIcon)).showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLoc,15));
            } catch (IOException e) {
                Log.e("Exception",e.toString());
            }
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
    };
    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        mMap.clear();
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0));
        String add="";
        String currentAddress="";
        Geocoder coder=new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> details=coder.getFromLocation(arg0.latitude,arg0.longitude,1);
            android.location.Address obj=details.get(0);
            add=obj.getAddressLine(0);
            currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            BitmapDescriptor marIcon= BitmapDescriptorFactory.fromResource(R.drawable.tmarker6);
            mMap.addMarker(new MarkerOptions().position(arg0).title(add).snippet(currentAddress).draggable(true).icon(marIcon)).showInfoWindow();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0,15));
        } catch (IOException e) {
            e.printStackTrace();
        }
        NavigationUtility.showToast(context,"Map Clicked",1);
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        NavigationUtility.showToast(context,"Long Clicked!!!",1);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        NavigationUtility.showToast(context,newText,0);
        return false;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
