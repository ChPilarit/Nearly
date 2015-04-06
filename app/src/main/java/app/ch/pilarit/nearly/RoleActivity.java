package app.ch.pilarit.nearly;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.libs.views.dialogs.DialogComfirm;
import app.ch.pilarit.nearly.libs.views.dialogs.DialogInterface;
import app.ch.pilarit.nearly.services.GPSTracking;
import app.ch.pilarit.nearly.utils.AccountSessionUtil;


public class RoleActivity extends BaseActivity implements View.OnClickListener{

    private Button roleBtnFolower;
    private Button roleBtnTraker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if(AccountSessionUtil.hasTracker(this)) {
            Intent gpsTracking = new Intent(this, GPSTracking.class);
            this.startService(gpsTracking);
        }*/
        getActionBar().hide();
        setContentView(R.layout.activity_role);
        initView();
    }

    private void initView() {
        roleBtnFolower = (Button) findViewById(R.id.role_btn_follower);
        roleBtnTraker = (Button) findViewById(R.id.role_btn_tracker);

        roleBtnFolower.setOnClickListener(this);
        roleBtnTraker.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.role_btn_follower:{
                doConfirmRole(KeyAccount.ROLE_FOLLOWER);
                break;
            }

            case R.id.role_btn_tracker:{
                doConfirmRole(KeyAccount.ROLE_TRACKER);
                break;
            }
        }

    }

    private void doConfirmRole(final int roleid) {
        String title = getResources().getString(R.string.role_confirm);

        String question = "";
        if(roleid == KeyAccount.ROLE_FOLLOWER){
            question = getResources().getString(R.string.role_question_follower);
        }else if(roleid == KeyAccount.ROLE_TRACKER){
            question = getResources().getString(R.string.role_question_tracker);
        }

        new DialogComfirm(RoleActivity.this).show(title, R.drawable.ic_launcher, question, new DialogInterface() {
            @Override
            public void ok(Dialog dialog) {
                gotoAccountSetting(roleid);
                dialog.dismiss();
            }

            @Override
            public void cancel(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    private void gotoAccountSetting(int roleid) {
        Intent gotoAccountSetting = new Intent(RoleActivity.this, AccountActivity.class);
        gotoAccountSetting.putExtra(KeyAccount.ROLE_ID, roleid);
        startActivity(gotoAccountSetting);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_role, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
