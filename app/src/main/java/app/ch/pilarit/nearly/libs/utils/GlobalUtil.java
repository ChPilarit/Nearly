package app.ch.pilarit.nearly.libs.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.SmsManager;

/**
 * Created by ch_pilarit on 4/17/15 AD.
 */
public class GlobalUtil {

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void sendSMS(String telephone, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telephone, null, message, null, null);
    }
}
