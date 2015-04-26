package app.poly.myapp.doggy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import app.poly.myapp.doggy.services.GPSTracking;
import app.poly.myapp.doggy.utils.AccountSessionUtil;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!AccountSessionUtil.hasTracker(context)) return;

        Intent gpsTracking = new Intent(context, GPSTracking.class);
        context.startService(gpsTracking);
        Log.e("AutoStart", "Auto Start Service");
    }
}
