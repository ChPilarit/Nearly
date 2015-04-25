package app.ch.pilarit.nearly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import org.json.JSONException;

import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;

/**
 * Created by ch_pilarit on 4/24/15 AD.
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            GlobalUtil.receiveSMS(context, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
