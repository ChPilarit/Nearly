package app.ch.pilarit.nearly;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.software.shell.fab.ActionButton;

import java.util.ArrayList;
import java.util.List;

import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.utils.ImageUtil;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;

import static android.view.View.OnClickListener;


public class MapActivity extends FragmentActivity implements OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.SnapshotReadyCallback {

    public final static String KEY_POLYGON = "KEY_POLYGON";
    public final static String KEY_CACHE_MAP = "KEY_CACHE_MAP";

    private ActionButton mapImvEdit;
    private ActionButton mapImvDelete;
    private Button mapSaveBtn;
    private GoogleMap googleMap;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
        overridePendingTransition(R.anim.in_trans_right_left, R.anim.out_trans_left_right);
        setContentView(R.layout.activity_map);
        initView();
    }

    private void initView() {
        setUpMap();

        mapImvEdit = (ActionButton) findViewById(R.id.map_imv_edit);
        mapImvDelete = (ActionButton) findViewById(R.id.map_imv_delete);
        mapSaveBtn = (Button) findViewById(R.id.map_save_btn);

        mapImvEdit.setTag(false);
        mapImvEdit.setOnClickListener(this);
        mapImvDelete.setOnClickListener(this);
        mapSaveBtn.setOnClickListener(this);
    }

    private void setUpMap() {
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.googlemap, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.map_imv_edit:{
                doEditSelected(view);
                break;
            }
            case R.id.map_imv_delete:{
                doDelete();
                break;
            }
            case R.id.map_save_btn:{
                doSave();
                break;
            }
        }
    }

    private void doSave() {
        if(polygon == null){
            Boast.makeText(this, R.string.map_warn_no_data).show();
            return;
        }

        if(marker != null) marker.setVisible(false);
        googleMap.snapshot(this);
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        gotoTrackerSetting(bitmap);
    }

    private void gotoTrackerSetting(Bitmap bitmap) {
        Intent gotoTrackerSetting = new Intent(this, TrackerActivity.class);
        gotoTrackerSetting.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.MAP_ACTIVITY);
        gotoTrackerSetting.putExtra(KEY_POLYGON, Map.polygonToStringPolygon(polygon.getPoints()));
        gotoTrackerSetting.putExtra(KEY_CACHE_MAP, ImageUtil.scaleDownBitmap(bitmap, 200, this));

        String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.TRACKER_ACTIVITY.equals(fromActivity)){
            setResult(RESULT_OK, gotoTrackerSetting);
        }else{
            startActivity(gotoTrackerSetting);
        }

        onBackPressed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initGoogleMap(googleMap);
    }

    private void initGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.clear();
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);

        setUpStylePolygon();
        checkFromTrackerActivity();

    }

    private void checkFromTrackerActivity() {
        String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.TRACKER_ACTIVITY.equals(fromActivity)){
            String polygonStr = getIntent().getStringExtra(KEY_POLYGON);
            List<LatLng> latLngList = Map.stringPolygonToPolygon(polygonStr);
            if (latLngList==null || latLngList.size() < 2){
                return;
            }

            int totalPoint = latLngList.size();
            int i = 0;
            LatLng latLng;
            for (i = 0; i < totalPoint-1; i++){
                latLng = latLngList.get(i);
                this.googleMap.clear();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng));
                polygon = googleMap.addPolygon(polygonOptions.add(latLng));
            }
            marker.setVisible(false);
        }
    }

    private void setUpStylePolygon() {
        polygonOptions = new PolygonOptions();
        polygonOptions.strokeColor(Color.RED);
        polygonOptions.fillColor(Color.argb(33, 255, 0, 0));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(!Boolean.valueOf(mapImvEdit.getTag().toString())) return;

        googleMap.clear();
        marker = googleMap.addMarker(new MarkerOptions().position(latLng));
        polygon = googleMap.addPolygon(polygonOptions.add(latLng));
    }

    private void doEditSelected(View view) {

        boolean selected = Boolean.valueOf(view.getTag().toString());
        if(!selected) {
            mapImvEdit.setImageResource(R.drawable.ic_action_edit_selected);
            mapImvEdit.setTag(true);

            if(marker != null) marker.setVisible(true);
        }else{
            mapImvEdit.setImageResource(R.drawable.ic_action_edit);
            mapImvEdit.setTag(false);

            if(marker != null) marker.setVisible(false);
        }

    }

    private void doDelete() {
        polygon = null;
        marker.remove();
        googleMap.clear();
        setUpStylePolygon();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in_trans_left_right, R.anim.out_trans_right_left);
        finish();
    }

}
