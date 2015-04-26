package app.poly.myapp.doggy;

import android.os.Bundle;

import app.poly.myapp.doggy.activity.BaseActivity;
import app.poly.myapp.doggy.libs.views.dialogs.Boast;

public class ComposeSmsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Boast.makeText(this, "This app is not support send sms.").show();
        finish();
    }
}