package app.ch.pilarit.nearly.libs.utils;

import android.content.Context;
import android.content.Intent;

import app.ch.pilarit.nearly.R;
import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.libs.gps.GPS;
import app.ch.pilarit.nearly.libs.session.SessionLocal;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.services.GPSTracking;

/**
 * Created by ch_pilarit on 4/26/15 AD.
 */
public class GPSUtil {
    public static void startGPSTrackingService(Context context){
        SessionLocal sessionLocal = SessionLocal.getInstance(context);
        if(sessionLocal!=null && sessionLocal.hasKey(KeyAccount.ROLE_ID) && sessionLocal.get(KeyAccount.ROLE_ID) != null) {
            String roleStr = String.valueOf(sessionLocal.get(KeyAccount.ROLE_ID));
            int roleid = Integer.valueOf(roleStr);
            if(roleid == KeyAccount.ROLE_TRACKER) {
                Intent gpsTracking = new Intent(context, GPSTracking.class);
                context.startService(gpsTracking);
            }
        }
    }

    public static boolean isEnableGPS(Context context){

        if(GPS.getInstance(context).canToggleGPS(context)){
            GPS.getInstance(context).turnGPSOn(context);
            startGPSTrackingService(context);
            return true;
        }

        if(!GPS.getInstance(context).isGPSEnabled(context)){
            Boast.makeText(context, R.string.gps_warning).show();
            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return false;
        }

        if(!GPS.getInstance(context).isNetworkGPSEnabled(context)){
            Boast.makeText(context, R.string.gps_network_warning).show();
            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return false;
        }

        startGPSTrackingService(context);

        return true;
    }
}
