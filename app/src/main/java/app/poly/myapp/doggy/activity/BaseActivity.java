package app.poly.myapp.doggy.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import app.poly.myapp.doggy.R;

public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
        overridePendingTransition(R.anim.in_trans_right_left, R.anim.out_trans_left_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_trans_left_right, R.anim.out_trans_right_left);
        finish();
    }
}
