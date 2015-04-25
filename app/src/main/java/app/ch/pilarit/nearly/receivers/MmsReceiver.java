package app.ch.pilarit.nearly.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.ch.pilarit.nearly.libs.views.dialogs.Boast;

/**
 * Created by ch_pilarit on 4/24/15 AD.
 */
public class MmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Boast.makeText(context, "This app is not support receive mms.").show();
    }
}
