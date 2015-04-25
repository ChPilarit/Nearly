package app.ch.pilarit.nearly.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.gps.GPS;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.utils.DateUtil;
import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.models.TrackSetting;

/**
 * Created by ch_pilarit on 4/5/15 AD.
 */
public class GPSTracking extends Service implements LocationListener{

    public final static String SMS_TO = "SMS_TO";
    public final static String SMS_MSG = "SMS_MSG";

    private SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
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
        validateTracking(location);
    }

    private void validateTracking(Location location) {
        //int daysOfweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        List<TrackSetting> trackSettingList = TrackSetting.find(TrackSetting.class, "active=1");
        List<LatLng> polygon = null;
        for(TrackSetting trackSetting : trackSettingList){
            int starttime = DateUtil.timeToInt(trackSetting.getStarttime());
            int endtime = DateUtil.timeToInt(trackSetting.getEndtime());
            Log.e("GPSTracking", "starttime : " + starttime);
            Log.e("GPSTracking", "endtime : " + endtime);
            if(!DateUtil.isBetweenTime(starttime, endtime)) continue;

            polygon = Map.stringPolygonToPolygon(trackSetting.getPolygon());
            if(!Map.isPointInPolygon(currentLatLng, polygon)){
                Boast.makeText(this, trackSetting.getName() + " Out Polygon").show();
                Log.e("GPSTracking", "---------- Out Polygon ----------");
                trackSetting.setActive(false);
                trackSetting.save();
                try {
                    JSONObject smsJson = new JSONObject();
                    smsJson.put(KeyGlobal.SMS_DEVIVE_ID , GlobalUtil.getDeviceId(getApplicationContext()));
                    smsJson.put(KeyGlobal.SMS_NAME, trackSetting.getName());
                    smsJson.put(KeyGlobal.SMS_DATETIME, datetimeFormat.format(Calendar.getInstance().getTime()));
                    smsJson.put(KeyGlobal.SMS_LAT, location.getLatitude());
                    smsJson.put(KeyGlobal.SMS_LNG, location.getLongitude());
                    smsJson.put(KeyGlobal.SMS_KEYAPP, KeyGlobal.KEY_APP);
                    Log.e("GPSTracking", smsJson.toString());

                    Boast.makeText(getApplicationContext(), "Send SMS to " + trackSetting.getTelephone()).show();
                    GlobalUtil.sendSMS(trackSetting.getTelephone(), smsJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else{
                Boast.makeText(this, trackSetting.getName() + " In Polygon ").show();
                Log.e("GPSTracking", "---------- In Polygon ----------");
            }

            Log.e("GPSTracking", "Id : " + trackSetting.getId());
            Log.e("GPSTracking", "Name : " + trackSetting.getName());
            Log.e("GPSTracking", "Telephone : " + trackSetting.getTelephone());
            Log.e("GPSTracking", "DateTime : " + datetimeFormat.format(Calendar.getInstance().getTime()));
            Log.e("GPSTracking", "StartTime : " + trackSetting.getStarttime());
            Log.e("GPSTracking", "EndTime : " + trackSetting.getEndtime());
            Log.e("GPSTracking", "lat :" + location.getLatitude());
            Log.e("GPSTracking", "lng :" + location.getLongitude());
            Log.e("GPSTracking", "Device Id :" + GlobalUtil.getDeviceId(getApplicationContext()));
        }
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
