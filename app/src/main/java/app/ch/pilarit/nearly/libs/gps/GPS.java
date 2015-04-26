package app.ch.pilarit.nearly.libs.gps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import app.ch.pilarit.nearly.libs.KeyService;
import app.ch.pilarit.nearly.libs.session.SessionLocal;

/**
 * Created by ch_pilarit on 4/5/15 AD.
 */
public class GPS{

    private static long MIN_TIME = 10000;
    private static float MIN_DISTANCE = 100;

    private static LocationManager locationManager;

    private static GPS instance;

    private GPS(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static GPS getInstance(Context context) {
        if(instance == null || locationManager == null){
            instance = new GPS(context);
        }
        return instance;
    }

    public void setLocationListener(Context context, LocationListener locationListener) {

        String gpsProvider = LocationManager.PASSIVE_PROVIDER;

        Log.e("GPS", "start gps listener : " + gpsProvider);

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            gpsProvider = LocationManager.NETWORK_PROVIDER;
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gpsProvider = LocationManager.GPS_PROVIDER;
        }

        SessionLocal sessionLocal = SessionLocal.getInstance(context);
        if(sessionLocal.hasKey(KeyService.GPS_MIN_TIME)) {
            MIN_TIME = Long.parseLong(String.valueOf(sessionLocal.get(KeyService.GPS_MIN_TIME)));
        }

        if(sessionLocal.hasKey(KeyService.GPS_MIN_DISTANCE)) {
            MIN_DISTANCE = Float.parseFloat(String.valueOf(sessionLocal.get(KeyService.GPS_MIN_DISTANCE)));
        }

        this.locationManager.requestLocationUpdates(gpsProvider, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    public boolean isGPSEnabled(Context context) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkGPSEnabled(Context context) {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void turnGPSOn(Context context){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(canToggleGPS(context)) {
            if(!provider.contains("gps")){ //if gps is disabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        }else{
            Log.e("GPSTracking", "can not Toggle GPS");
        }

    }

    public void turnGPSOff(Context context){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(canToggleGPS(context)) {
            if(provider.contains("gps")){ //if gps is enabled
                final Intent poke = new Intent();
                poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
                poke.setData(Uri.parse("3"));
                context.sendBroadcast(poke);
            }
        }else{
            Log.e("GPSTracking", "can not Toggle GPS");
        }
    }

    public boolean canToggleGPS(Context context) {
        PackageManager pacman = context.getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if(pacInfo != null){
            for(ActivityInfo actInfo : pacInfo.receivers){
                //test if recevier is exported. if so, we can toggle GPS.
                if(actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported){
                    return true;
                }
            }
        }

        return false; //default
    }
}
