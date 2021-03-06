package app.poly.myapp.doggy;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import app.poly.myapp.doggy.activity.BaseActivity;
import app.poly.myapp.doggy.keys.KeyAccount;
import app.poly.myapp.doggy.keys.KeyGlobal;
import app.poly.myapp.doggy.libs.authen.AuthenLocal;
import app.poly.myapp.doggy.libs.views.dialogs.DialogComfirm;
import app.poly.myapp.doggy.libs.views.dialogs.DialogInterface;


public class RoleActivity extends BaseActivity implements View.OnClickListener{

    private Button roleBtnFolower;
    private Button roleBtnTraker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validateActivity();
        getActionBar().hide();
        setContentView(R.layout.activity_role);
        initView();
    }

    private void validateActivity() {
        if(AuthenLocal.hasRegister(this)){
            Intent gotoLogin = new Intent(this, LoginActivity.class);
            gotoLogin.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.ROLE_ACTIVITY);
            startActivity(gotoLogin);
            finish();
        }
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

        new DialogComfirm(RoleActivity.this).show(title, R.drawable.ic_action_tick, question, new DialogInterface() {
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
        gotoAccountSetting.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.ROLE_ACTIVITY);
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
