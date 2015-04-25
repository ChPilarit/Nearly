package app.ch.pilarit.nearly.libs.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.services.GPSTracking;

/**
 * Created by ch_pilarit on 4/17/15 AD.
 */
public class GlobalUtil {

    public static final String SMS_BUNDLE = "pdus";

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void sendSMS(String telephone, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telephone, null, message, null, null);
    }

    public static void receiveSMS(Context context, Intent intent) throws JSONException {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            StringBuffer smsMessageStr = new StringBuffer();
            SmsMessage smsMessage = null;
            JSONObject historySms = null;
            String keyapp = "";
            String name = "";
            String deviceid = "";
            String datetime = "";
            String lat = "";
            String lmg = "";

            for (int i = 0; i < sms.length; i++) {

                keyapp = "";
                name = "";
                deviceid = "";
                datetime = "";
                lat = "";
                lmg = "";
                smsMessageStr.setLength(0);

                smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String telephone = smsMessage.getOriginatingAddress();

                historySms = new JSONObject(smsBody);
                if(!historySms.has(KeyGlobal.SMS_KEYAPP) || !KeyGlobal.KEY_APP.equals(historySms.getString(KeyGlobal.SMS_KEYAPP))) continue;

                name = historySms.getString(KeyGlobal.SMS_NAME);
                deviceid = historySms.getString(KeyGlobal.SMS_DEVIVE_ID);
                datetime = historySms.getString(KeyGlobal.SMS_DATETIME);
                lat = historySms.getString(KeyGlobal.SMS_LAT);
                lmg = historySms.getString(KeyGlobal.SMS_LNG);

                smsMessageStr.append("SMS From: " + telephone + "\n");
                smsMessageStr.append(smsBody + "\n");

                Boast.makeText(context, smsMessageStr.toString()).show();
            }

        }
    }
}
