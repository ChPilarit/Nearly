package app.ch.pilarit.nearly;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.adapters.HistoryListAdapter;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.models.History;

public class HomeHistoryActivity extends BaseActivity {

    private DrawerLayout historyDrawerLayout;
    private ListView historyLvTracklist;
    private HistoryListAdapter historyListAdapter;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_history);
        initView();
    }

    private void initView() {
        setUpDrawerLayout();
        historyLvTracklist = (ListView)findViewById(R.id.history_lv_tracklist);
        setUpHistoryListAdapter();
        checkSMSReceive();
    }

    private void setUpHistoryListAdapter() {
        List<History> historyList = History.listAll(History.class);
        historyListAdapter = new HistoryListAdapter(this,historyList);
        historyLvTracklist.setAdapter(historyListAdapter);
    }

    private void checkSMSReceive() {
        String from = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.SMS_RECEIVER.equals(from)) {
            Intent gotoMapHistory = new Intent(this, MapHistoryActivity.class);
            gotoMapHistory.putExtra(KeyGlobal.SMS_ID, getIntent().getLongExtra(KeyGlobal.SMS_ID, 0));
            gotoMapHistory.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.HOME_HISTORY_ACTIVITY);
            startActivity(gotoMapHistory);
        }else{
            GlobalUtil.setDefaultSMS(this);
        }
    }

    private void setUpDrawerLayout() {
        historyDrawerLayout = (DrawerLayout) findViewById(R.id.history_drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this,historyDrawerLayout,
                R.drawable.ic_action_reorder,R.string.hello_world, R.string.tracker ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        historyDrawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpHistoryListAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
