package app.ch.pilarit.nearly;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.software.shell.fab.ActionButton;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.session.SessionLocal;
import app.ch.pilarit.nearly.libs.utils.ImageUtil;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;

import static android.view.View.OnClickListener;


public class MapActivity extends FragmentActivity implements OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.SnapshotReadyCallback, GoogleMap.OnMyLocationChangeListener {

    public final static String KEY_POLYGON = "KEY_POLYGON";
    public final static String KEY_CACHE_MAP = "KEY_CACHE_MAP";
    public final static float DEFAULT_ZOOM = 17.0f;

    private ActionButton mapImvEdit;
    private ActionButton mapImvDelete;
    private Button mapSaveBtn;
    private GoogleMap googleMap;
    private PolygonOptions polygonOptions;
    private Polygon polygon;
    private Marker marker;
    private ActionButton mapImvMyLocation;
    private boolean hasFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasFocus = false;
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
        overridePendingTransition(R.anim.in_trans_right_left, R.anim.out_trans_left_right);
        setContentView(R.layout.activity_map);
        initView();
    }

    private void initView() {
        SessionLocal.getInstance(this).remove(KEY_CACHE_MAP);
        setUpMap();

        mapImvEdit = (ActionButton) findViewById(R.id.map_imv_edit);
        mapImvMyLocation = (ActionButton) findViewById(R.id.map_imv_mylocation);
        mapImvDelete = (ActionButton) findViewById(R.id.map_imv_delete);
        mapSaveBtn = (Button) findViewById(R.id.map_save_btn);

        mapImvEdit.setTag(false);
        mapImvEdit.setOnClickListener(this);
        mapImvMyLocation.setOnClickListener(this);
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
            case R.id.map_imv_mylocation:{
                doSetMyLocation();
                break;
            }
        }
    }

    private void doSetMyLocation() {
        if(googleMap.getMyLocation() == null) return;
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.convertLocationToLatLng(googleMap.getMyLocation()), DEFAULT_ZOOM));
    }

    private void doSave() {
        if(polygon == null){
            Boast.makeText(this, R.string.map_warn_no_data).show();
            return;
        }

        if(polygon.getPoints().size() < 4){
            Boast.makeText(this, R.string.map_warn_more_point).show();
            return;
        }

        if(marker != null) marker.setVisible(false);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Map.centroid(polygon.getPoints()), this.googleMap.getCameraPosition().zoom));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MapActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        googleMap.snapshot(MapActivity.this);
                    }
                });
            }
        }, 100);

    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        gotoTrackerSetting(bitmap);
    }

    private void gotoTrackerSetting(Bitmap bitmap) {
        Intent gotoTrackerSetting = new Intent(this, TrackerActivity.class);
        gotoTrackerSetting.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.MAP_ACTIVITY);
        gotoTrackerSetting.putExtra(KEY_POLYGON, Map.polygonToStringPolygon(polygon.getPoints()));
        String cachemapStr = ImageUtil.getStringBase64(ImageUtil.scaleDownBitmap(bitmap, 200, this));
        SessionLocal.getInstance(this).put(KEY_CACHE_MAP, cachemapStr);
        //gotoTrackerSetting.putExtra(KEY_CACHE_MAP, ImageUtil.scaleDownBitmap(bitmap, 200, this));

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
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setOnMyLocationChangeListener(this);

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

            final LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (i = 0; i < totalPoint-1; i++){
                latLng = latLngList.get(i);
                this.googleMap.clear();
                marker = googleMap.addMarker(new MarkerOptions().position(latLng));
                polygon = googleMap.addPolygon(polygonOptions.add(latLng));
                builder.include(latLng);
            }
            builder.include(latLngList.get(totalPoint-1));
            final LatLngBounds bounds = builder.build();

            this.googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 400, 800, 5));
            //this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Map.centroid(latLngList), this.googleMap.getCameraPosition().zoom));
            marker.setVisible(false);
        }
    }

    private void setUpStylePolygon() {
        polygon = null;
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
        if(marker != null) marker.remove();
        if(googleMap != null) googleMap.clear();
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

    @Override
    public void onMyLocationChange(Location location) {

        if(!hasFocus){
            String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
            if(!KeyGlobal.TRACKER_ACTIVITY.equals(fromActivity)) {
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.convertLocationToLatLng(location), DEFAULT_ZOOM));
                hasFocus = true;
            }
        }
    }
}
