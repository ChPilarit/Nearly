package app.ch.pilarit.nearly;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.libs.authen.AuthenLocal;
import app.ch.pilarit.nearly.libs.session.SessionLocal;


public class LoginActivity extends BaseActivity {

    private TextView loginTevUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        String username = String.valueOf(SessionLocal.getInstance(this).get(KeyAccount.AUTHEN_USERNAME));
        loginTevUserName = (TextView) findViewById(R.id.login_tev_username);

        loginTevUserName.setText(username);
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
