package app.ch.pilarit.nearly;

import android.os.Bundle;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;

/**
 * Created by ch_pilarit on 4/24/15 AD.
 */
public class ComposeSmsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boast.makeText(this, "This app is not support send sms.").show();
        finish();
    }
}