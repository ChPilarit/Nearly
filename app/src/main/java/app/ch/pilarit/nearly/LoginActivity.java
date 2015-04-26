package app.ch.pilarit.nearly;

import android.accounts.Account;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.authen.AuthenLocal;
import app.ch.pilarit.nearly.libs.mail.SendGMailTask;
import app.ch.pilarit.nearly.libs.session.SessionLocal;
import app.ch.pilarit.nearly.libs.utils.NetworkUtils;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.libs.views.dialogs.DialogComfirm;
import app.ch.pilarit.nearly.libs.views.dialogs.DialogInterface;


public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView loginTevUserName;
    private Button loginButton1;
    private Button loginButton2;
    private Button loginButton3;
    private Button loginButton4;
    private Button loginButton5;
    private Button loginButton6;
    private Button loginButton7;
    private Button loginButton8;
    private Button loginButton9;
    private Button loginButton0;
    private ImageButton loginButtonDelete;
    private ImageButton loginButtonOk;
    private EditText loginPassword;
    private TextView loginForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        String username = String.valueOf(SessionLocal.getInstance(this).get(KeyAccount.AUTHEN_USERNAME));
        loginPassword = (EditText) findViewById(R.id.login_edt_password);
        loginTevUserName = (TextView) findViewById(R.id.login_tev_username);
        loginButton1 = (Button) findViewById(R.id.login_button1);
        loginButton2 = (Button) findViewById(R.id.login_button2);
        loginButton3 = (Button) findViewById(R.id.login_button3);
        loginButton4 = (Button) findViewById(R.id.login_button4);
        loginButton5 = (Button) findViewById(R.id.login_button5);
        loginButton6 = (Button) findViewById(R.id.login_button6);
        loginButton7 = (Button) findViewById(R.id.login_button7);
        loginButton8 = (Button) findViewById(R.id.login_button8);
        loginButton9 = (Button) findViewById(R.id.login_button9);
        loginButton0 = (Button) findViewById(R.id.login_button_0);
        loginButtonDelete = (ImageButton) findViewById(R.id.login_button_delete);
        loginButtonOk = (ImageButton) findViewById(R.id.login_button_ok);
        loginForgotPassword = (TextView) findViewById(R.id.login_forgot_password);

        loginTevUserName.setText(username);
        loginButton1.setOnClickListener(this);
        loginButton2.setOnClickListener(this);
        loginButton3.setOnClickListener(this);
        loginButton4.setOnClickListener(this);
        loginButton5.setOnClickListener(this);
        loginButton6.setOnClickListener(this);
        loginButton7.setOnClickListener(this);
        loginButton8.setOnClickListener(this);
        loginButton9.setOnClickListener(this);
        loginButton0.setOnClickListener(this);
        loginButtonDelete.setOnClickListener(this);
        loginButtonOk.setOnClickListener(this);
        loginForgotPassword.setOnClickListener(this);
        loginForgotPassword.setPaintFlags(loginForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button1: {
                loginPassword.append("1");
                break;
            }
            case R.id.login_button2: {
                loginPassword.append("2");
                break;
            }
            case R.id.login_button3: {
                loginPassword.append("3");
                break;
            }
            case R.id.login_button4: {
                loginPassword.append("4");
                break;
            }
            case R.id.login_button5: {
                loginPassword.append("5");
                break;
            }
            case R.id.login_button6: {
                loginPassword.append("6");
                break;
            }
            case R.id.login_button7: {
                loginPassword.append("7");
                break;
            }
            case R.id.login_button8: {
                loginPassword.append("8");
                break;
            }
            case R.id.login_button9: {
                loginPassword.append("9");
                break;
            }
            case R.id.login_button_0: {
                loginPassword.append("0");
                break;
            }
            case R.id.login_button_delete:{
                if (loginPassword.getText().length()>0){
                    loginPassword.getText().delete(loginPassword.getText().length()-1,loginPassword.getText().length());}
                break;
            }
            case R.id.login_button_ok:{
                SessionLocal session = AuthenLocal.login(this, loginPassword.getText().toString());
                if(session == null || !session.hasSession()){
                    loginPassword.setText("");
                    Boast.makeText(this, R.string.login_warn_password_invalid).show();
                }else {
                    gotoHome(session);
                }
                break;
            }
            case R.id.login_forgot_password:{
                if(NetworkUtils.isNetworkAvailable(this)){
                    doConfirmEmail();
                }else{
                    Boast.makeText(this, R.string.net_warning).show();
                }
                break;
            }

        }

    }

    private void doConfirmEmail() {

        String title = getResources().getString(R.string.home_dialog_forgotpassword);
        String question = getResources().getString(R.string.home_dialog_confirmEmail);
        final String email = String.valueOf(SessionLocal.getInstance(getApplicationContext()).get(KeyAccount.AUTHEN_EMAIL));
        final String password = String.valueOf(SessionLocal.getInstance(getApplicationContext()).get(KeyAccount.AUTHEN_PASSWORD));

        new DialogComfirm(LoginActivity.this).show(title, R.drawable.ic_action_tick,question+email,new DialogInterface() {
            @Override
            public void ok(Dialog dialog) {
                doSendEmail();
                dialog.dismiss();
            }

            @Override
            public void cancel(Dialog dialog) {
                dialog.dismiss();
            }
        });

    }

    private void doSendEmail() {
        String username = String.valueOf(SessionLocal.getInstance(this).get(KeyAccount.AUTHEN_USERNAME));
        String password = String.valueOf(SessionLocal.getInstance(getApplicationContext()).get(KeyAccount.AUTHEN_PASSWORD));
        String roleIdStr = String.valueOf(SessionLocal.getInstance(getApplicationContext()).get(KeyAccount.ROLE_ID));
        int roleId = Integer.valueOf(roleIdStr);
        String roleName = "";
        switch(roleId){
            case KeyAccount.ROLE_FOLLOWER:{
                roleName = getResources().getString(R.string.follower);
                break;
            }
            case KeyAccount.ROLE_TRACKER:{
                roleName = getResources().getString(R.string.tracker);
                break;
            }
        }

        StringBuffer bodyBuffer = new StringBuffer();
        bodyBuffer.append("\n");
        bodyBuffer.append(String.format("\t User Name : %s", username));
        bodyBuffer.append("\n");
        bodyBuffer.append(String.format("\t Password : %s", password));
        bodyBuffer.append("\n");
        bodyBuffer.append(String.format("\t Role Name : %s", roleName));
        bodyBuffer.append("\n");

        String subject = String.format("%s app : forgot password", getResources().getString(R.string.app_name));
        String body = bodyBuffer.toString();
        String email = String.valueOf(SessionLocal.getInstance(getApplicationContext()).get(KeyAccount.AUTHEN_EMAIL));

        SendGMailTask sendGMailTask = new SendGMailTask(LoginActivity.this);
        sendGMailTask.execute(new String[]{subject, body, email});
    }

    private void gotoHome(SessionLocal session) {
        if(!session.hasSession()) return;

        Intent gotoHome = null;
        int rolid = Integer.valueOf(String.valueOf(session.get(KeyAccount.ROLE_ID)));

        switch (rolid){
            case KeyAccount.ROLE_TRACKER:{
                gotoHome = new Intent(this, HomeActivity.class);
                gotoHome.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.LOGIN_ACTIVITY);
                break;
            }
            case KeyAccount.ROLE_FOLLOWER:{
                gotoHome = new Intent(this, HomeHistoryActivity.class);
                String from = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
                if(KeyGlobal.SMS_RECEIVER.equals(from)) {
                    gotoHome.putExtra(KeyGlobal.SMS_ID, getIntent().getLongExtra(KeyGlobal.SMS_ID, 0));
                    gotoHome.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.SMS_RECEIVER);
                }else{
                    gotoHome.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.LOGIN_ACTIVITY);
                }
                break;
            }
        }

        startActivity(gotoHome);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
