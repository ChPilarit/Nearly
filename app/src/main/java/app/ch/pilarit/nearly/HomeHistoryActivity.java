package app.ch.pilarit.nearly;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.adapters.HistoryListAdapter;
import app.ch.pilarit.nearly.models.History;

public class HomeHistoryActivity extends BaseActivity {

    private DrawerLayout historyDrawerLayout;
    private ListView historyLvTracklist;
    private HistoryListAdapter historyListAdapter;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
    }

    private void initView() {
        setUpDrawerLayout();

        List<History> historyList;
        historyList = History.listAll(History.class);


        historyLvTracklist = (ListView)findViewById(R.id.history_lv_tracklist);
        historyListAdapter = new HistoryListAdapter(this,historyList);
        historyLvTracklist.setAdapter(historyListAdapter);

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

        return super.onOptionsItemSelected(item);
    }
}
