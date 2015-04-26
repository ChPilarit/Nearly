package app.ch.pilarit.nearly;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.software.shell.fab.ActionButton;

import java.util.List;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.adapters.TrackListAdapter;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.utils.GPSUtil;
import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.libs.utils.NetworkUtils;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.models.TrackSetting;
import app.ch.pilarit.nearly.utils.AccountSessionUtil;


public class HomeActivity extends BaseActivity implements View.OnClickListener{

    public final static String TRACKER_SETTING_ID = "TRACKER_SETTING_ID";

   //private DrawerLayout homeDrawerLayout;
   //private ActionBarDrawerToggle mDrawerToggle;
    private ActionButton homeImvAdd;
    private ListView homeLvTracklist;
    private TrackListAdapter trackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AccountSessionUtil.hasTracker(this)) {
            GPSUtil.startGPSTrackingService(this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        //setUpDrawerLayout();
        homeLvTracklist = (ListView) findViewById(R.id.home_lv_tracklist);
        homeLvTracklist.setEmptyView(findViewById(R.id.home_tv_empty));
        setUpTrackListAdapter();

        homeImvAdd = (ActionButton) findViewById(R.id.home_imv_add);
        homeImvAdd.setOnClickListener(this);

        GlobalUtil.setDefaultSMS(this);
    }

    private void setUpTrackListAdapter() {
        List<TrackSetting> trackSettingList = TrackSetting.listAll(TrackSetting.class);
        trackListAdapter = new TrackListAdapter(this, trackSettingList);
        homeLvTracklist.setAdapter(trackListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_imv_add:{
                if(NetworkUtils.isNetworkAvailable(this)) {
                    gotoMapSetting();
                }else{
                    Boast.makeText(this, R.string.net_warning).show();
                }
                break;
            }
        }
    }

    private void gotoMapSetting() {
        Intent gotoMap = new Intent(this, MapActivity.class);
        gotoMap.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.HOME_ACTIVITY);
        startActivity(gotoMap);
    }

    private void setUpDrawerLayout() {
        /*homeDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout,
                R.drawable.ic_action_reorder, R.string.hello_world, R.string.tracker) {

            *//** Called when a drawer has settled in a completely closed state. *//*
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        homeDrawerLayout.setDrawerListener(mDrawerToggle);*/

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpTrackListAdapter();
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            Intent gotoAccountSetting = new Intent(this, AccountActivity.class);
            gotoAccountSetting.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.HOME_HISTORY_ACTIVITY);
            startActivity(gotoAccountSetting);
            return true;
        }

        /*if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

}
