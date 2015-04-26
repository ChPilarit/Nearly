package app.poly.myapp.doggy.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import app.poly.myapp.doggy.libs.views.dialogs.Boast;

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
