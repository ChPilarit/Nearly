package app.ch.pilarit.nearly;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.authen.AuthenLocal;
import app.ch.pilarit.nearly.libs.session.SessionLocal;
import app.ch.pilarit.nearly.libs.validate.EmailValidator;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;


public class AccountActivity extends BaseActivity implements View.OnClickListener{

    private EditText accountEdtUsername;
    private EditText accountEdtPassword;
    private EditText accountEdtRepassword;
    private Button accountBtnSave;
    private EditText accountEdtEmail;
    private EmailValidator emailValidator;
    private SessionLocal sessionLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        loadData();
        initView();
    }

    private void loadData() {
        String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.HOME_ACTIVITY.equals(fromActivity) || KeyGlobal.HOME_HISTORY_ACTIVITY.equals(fromActivity)){
            getActionBar().setDisplayHomeAsUpEnabled(true);
            sessionLocal = SessionLocal.getInstance(this);
        }
    }

    private void initView() {
        accountEdtUsername = (EditText) findViewById(R.id.account_edt_username);
        accountEdtPassword = (EditText) findViewById(R.id.account_edt_password);
        accountEdtRepassword = (EditText) findViewById(R.id.account_edt_repassword);
        accountEdtEmail = (EditText) findViewById(R.id.account_edt_email);
        accountBtnSave = (Button) findViewById(R.id.account_btn_save);
        accountBtnSave.setOnClickListener(this);

        if(sessionLocal!=null && sessionLocal.hasSession()){
            accountEdtUsername.setText(String.valueOf(sessionLocal.get(KeyAccount.AUTHEN_USERNAME)));
            accountEdtPassword.setText(String.valueOf(sessionLocal.get(KeyAccount.AUTHEN_PASSWORD)));
            accountEdtRepassword.setText(String.valueOf(sessionLocal.get(KeyAccount.AUTHEN_PASSWORD)));
            accountEdtEmail.setText(String.valueOf(sessionLocal.get(KeyAccount.AUTHEN_EMAIL)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_btn_save:{
                if(validate()){
                    doRegisterAccount();
                    gotoLogInActivity();
                }
                break;
            }
        }
    }

    private void gotoLogInActivity() {
        String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.ROLE_ACTIVITY.equals(fromActivity)){
            Intent gotoLogin = new Intent(this, LoginActivity.class);
            gotoLogin.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.ROLE_ACTIVITY);
            startActivity(gotoLogin);
            finish();
        }else{
            onBackPressed();
        }
    }

    private void doRegisterAccount() {
        String username = accountEdtUsername.getText().toString();
        String password = accountEdtPassword.getText().toString();
        String repassword = accountEdtRepassword.getText().toString();
        String email = accountEdtEmail.getText().toString();
        int roleId = 0;
        if(sessionLocal != null && sessionLocal.hasSession()){
            roleId = Integer.valueOf(String.valueOf(sessionLocal.get(KeyAccount.ROLE_ID)));
        }else{
            roleId = getIntent().getIntExtra(KeyAccount.ROLE_ID, 0);
        }

        Map accountMap = new HashMap<String, Object>();
        accountMap.put(KeyAccount.AUTHEN_USERNAME, username);
        accountMap.put(KeyAccount.AUTHEN_PASSWORD, password);
        accountMap.put(KeyAccount.ROLE_ID, roleId);
        accountMap.put(KeyAccount.AUTHEN_EMAIL, email);

        AuthenLocal.register(AccountActivity.this, accountMap);
    }

    private boolean validate() {
        String username = accountEdtUsername.getText().toString();
        String password = accountEdtPassword.getText().toString();
        String repassword = accountEdtRepassword.getText().toString();
        String email = accountEdtEmail.getText().toString();

        if(username.length() < 4){
            Boast.makeText(AccountActivity.this, R.string.account_warn_username_lenght, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(password.length() < 4){
            Boast.makeText(AccountActivity.this, R.string.account_warn_password_lenght, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!password.equals(repassword)){
            Boast.makeText(AccountActivity.this, R.string.account_warn_password_invalid, Toast.LENGTH_SHORT).show();
            return false;
        }

        if(emailValidator==null) emailValidator = new EmailValidator();

        if(email == null || !emailValidator.validate(email)){
            Boast.makeText(AccountActivity.this, R.string.account_warn_email_invalid, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
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

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
