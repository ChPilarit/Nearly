package app.ch.pilarit.nearly.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;

/**
 * Created by ch_pilarit on 4/24/15 AD.
 */
public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Boast.makeText(getApplicationContext(), "This app is not support send sms.").show();
        return super.onStartCommand(intent, flags, startId);
    }
}
