package app.ch.pilarit.nearly.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import app.ch.pilarit.nearly.LoginActivity;
import app.ch.pilarit.nearly.R;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.authen.AuthenLocal;

/**
 * Created by ch_pilarit on 4/6/15 AD.
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
        overridePendingTransition(R.anim.in_trans_right_left, R.anim.out_trans_left_right);
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.in_trans_left_right, R.anim.out_trans_right_left);
        finish();
    }
}
