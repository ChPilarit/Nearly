package app.ch.pilarit.nearly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.ch.pilarit.nearly.services.GPSTracking;
import app.ch.pilarit.nearly.utils.AccountSessionUtil;

/**
 * Created by ch_pilarit on 4/5/15 AD.
 */
public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!AccountSessionUtil.hasTracker(context)) return;

        Intent gpsTracking = new Intent(context, GPSTracking.class);
        context.startService(gpsTracking);
        Log.e("AutoStart", "Auto Start Service");
    }
}
