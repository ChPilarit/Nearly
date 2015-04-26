package app.poly.myapp.doggy.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.poly.myapp.doggy.libs.views.dialogs.Boast;

public class MmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Boast.makeText(context, "This app is not support receive mms.").show();
    }
}
