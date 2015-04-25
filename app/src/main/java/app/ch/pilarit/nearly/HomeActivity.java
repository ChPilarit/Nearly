package app.ch.pilarit.nearly;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.software.shell.fab.ActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.adapters.TrackListAdapter;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.models.TrackSetting;
import app.ch.pilarit.nearly.services.GPSTracking;
import app.ch.pilarit.nearly.utils.AccountSessionUtil;


public class HomeActivity extends BaseActivity implements View.OnClickListener{

    public final static String TRACKER_SETTING_ID = "TRACKER_SETTING_ID";

    private DrawerLayout homeDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionButton homeImvAdd;
    private ListView homeLvTracklist;
    private TrackListAdapter trackListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AccountSessionUtil.hasTracker(this)) {
            Intent gpsTracking = new Intent(this, GPSTracking.class);
            this.startService(gpsTracking);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        setUpDrawerLayout();

        homeLvTracklist = (ListView) findViewById(R.id.home_lv_tracklist);
        setUpTrackListAdapter();

        homeImvAdd = (ActionButton) findViewById(R.id.home_imv_add);
        homeImvAdd.setOnClickListener(this);

        setDefaultSMS();
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
                gotoMapSetting();
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
        homeDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, homeDrawerLayout,
                R.drawable.ic_action_reorder, R.string.hello_world, R.string.tracker) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        homeDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(trackListAdapter!=null) {
            trackListAdapter.notifyDataSetChanged();
        }
    }

    private void setDefaultSMS() {
        // Android 4.4 and up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            final String myPackageName = getPackageName();
            if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
                // App is not default.
                // Show the "not currently set as the default SMS app" interface
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


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
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
