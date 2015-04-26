package app.poly.myapp.doggy.libs.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import org.json.JSONException;
import org.json.JSONObject;

import app.poly.myapp.doggy.LoginActivity;
import app.poly.myapp.doggy.R;
import app.poly.myapp.doggy.keys.KeyAccount;
import app.poly.myapp.doggy.keys.KeyGlobal;
import app.poly.myapp.doggy.libs.session.SessionLocal;
import app.poly.myapp.doggy.libs.views.dialogs.Boast;
import app.poly.myapp.doggy.models.History;

public class GlobalUtil {

    public static final String SMS_BUNDLE = "pdus";

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void setDefaultSMS(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            String myPackageName = context.getPackageName();
            if (!Telephony.Sms.getDefaultSmsPackage(context).equals(myPackageName)) {
                Boast.makeText(context, R.string.sms_warning).show();
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                context.startActivity(intent);
            }
        }
    }

    public static boolean isDefaultSMS(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            String myPackageName = context.getPackageName();
            if (Telephony.Sms.getDefaultSmsPackage(context).equals(myPackageName)) {
                return true;
            }else{
                return false;
            }
        }

        return true;
    }

    public static void sendSMS(String telephone, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telephone, null, message, null, null);
    }

    public static void receiveSMS(Context context, Intent intent) throws JSONException {
        SessionLocal sessionLocal = SessionLocal.getInstance(context);
        if(sessionLocal==null || !sessionLocal.hasKey(KeyAccount.ROLE_ID)) return;

        int roleid = Integer.valueOf(String.valueOf(sessionLocal.get(KeyAccount.ROLE_ID)));
        if(roleid > 1) return;

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
            String lng = "";

            for (int i = 0; i < sms.length; i++) {

                keyapp = "";
                name = "";
                deviceid = "";
                datetime = "";
                lat = "";
                lng = "";
                smsMessageStr.setLength(0);

                smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                String smsBody = smsMessage.getMessageBody().toString();
                String telephone = smsMessage.getOriginatingAddress();

                historySms = new JSONObject(smsBody);
                if(!historySms.has(KeyGlobal.SMS_KEYAPP) || !KeyGlobal.KEY_APP.equals(historySms.getString(KeyGlobal.SMS_KEYAPP))) continue;

                telephone = replacePhoneStr(telephone);
                name = historySms.getString(KeyGlobal.SMS_NAME);
                deviceid = historySms.getString(KeyGlobal.SMS_DEVIVE_ID);
                datetime = historySms.getString(KeyGlobal.SMS_DATETIME);
                lat = historySms.getString(KeyGlobal.SMS_LAT);
                lng = historySms.getString(KeyGlobal.SMS_LNG);

                History history = new History();
                history.setDate(datetime);
                history.setName(name);
                history.setDeviceid(deviceid);
                history.setTelephone(telephone);
                history.setLat(lat);
                history.setLng(lng);
                history.save();

                pushNotification(context, history);
            }

        }
    }

    private static void pushNotification(Context context, History history) {

        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(KeyGlobal.SMS_ID, history.getId());
        intent.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.SMS_RECEIVER);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification  = new Notification.Builder(context)
                .setContentTitle(String.format("%s out form area.", history.getName()))
                .setContentText(String.format("SMS From %s", history.getTelephone()))
                .setSmallIcon(R.drawable.nearly2)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public static String replacePhoneStr(String telephone){
        telephone = telephone.replace("+66", "0");
        return telephone;
    }
}
