package app.poly.myapp.doggy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.json.JSONException;

import app.poly.myapp.doggy.libs.utils.GlobalUtil;

public class SmsReceiverKitKat extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            GlobalUtil.receiveSMS(context, intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}