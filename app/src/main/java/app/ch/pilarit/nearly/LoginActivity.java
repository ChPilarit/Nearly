package app.ch.pilarit.nearly;

import android.accounts.Account;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.libs.authen.AuthenLocal;
import app.ch.pilarit.nearly.libs.session.SessionLocal;


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
    private Button loginButtonDelete;
    private Button loginButtonOk;
    private EditText loginPassword;

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
        loginButtonDelete = (Button) findViewById(R.id.login_button_delete);
        loginButtonOk = (Button) findViewById(R.id.login_button_ok);


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

                }else {
                Intent gotoHome = new Intent(this,HomeActivity.class);
                 startActivity(gotoHome);
                 finish();


                }
                break;
            }
        }

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
