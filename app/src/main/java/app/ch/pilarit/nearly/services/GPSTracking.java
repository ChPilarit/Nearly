package app.ch.pilarit.nearly.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import app.ch.pilarit.nearly.libs.gps.GPS;

/**
 * Created by ch_pilarit on 4/5/15 AD.
 */
public class GPSTracking extends Service implements LocationListener{

    private GPS gps;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("GPSTracking", "start GPS Tracking");
        gps = GPS.getInstance(getApplicationContext());
        gps.turnGPSOn(getApplicationContext());
        gps.setLocationListener(getApplicationContext(), this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("GPSTracking", "lat :" + location.getLatitude());
        Log.e("GPSTracking", "lng :" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e("GPSTracking", "onStatusChanged :" + provider);
        gps = GPS.getInstance(getApplicationContext());
        gps.setLocationListener(getApplicationContext(), this);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("GPSTracking", "onProviderEnabled :" + provider);
        gps = GPS.getInstance(getApplicationContext());
        gps.setLocationListener(getApplicationContext(), this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.e("GPSTracking", "onProviderDisabled :" + provider);
        gps = GPS.getInstance(getApplicationContext());
        gps.turnGPSOn(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
