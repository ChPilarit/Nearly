package app.ch.pilarit.nearly;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyGlobal;


public class AccountActivity extends BaseActivity implements View.OnClickListener{

    private EditText accountEdtUsername;
    private EditText accountEdtPassword;
    private EditText accountEdtRepassword;
    private Button accountBtnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initView();
    }

    private void initView() {
        accountEdtUsername = (EditText) findViewById(R.id.account_edt_username);
        accountEdtPassword = (EditText) findViewById(R.id.account_edt_password);
        accountEdtRepassword = (EditText) findViewById(R.id.account_edt_repassword);
        accountBtnSave = (Button) findViewById(R.id.account_btn_save);
        accountBtnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.account_btn_save:{
                doValidate();
                break;
            }
        }
    }

    private void doValidate() {
        String username = accountEdtUsername.getText().toString();
        String password = accountEdtPassword.getText().toString();
        String repassword = accountEdtRepassword.getText().toString();

        if(username.length() < 4){
            return;
        }

        if(password.length() < 4){
            return;
        }

        if(!password.equals(repassword)){
            return;
        }

        String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.ROLE_ACTIVITY.equals(fromActivity)){
            Intent gotoLogin = new Intent(this, LoginActivity.class);
            gotoLogin.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.LOGIN_ACTIVITY);
            startActivity(gotoLogin);
        }
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

        return super.onOptionsItemSelected(item);
    }

}
