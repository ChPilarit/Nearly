package app.ch.pilarit.nearly;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.software.shell.fab.ActionButton;

import java.util.Timer;
import java.util.TimerTask;

import app.ch.pilarit.nearly.R;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.libs.utils.ImageUtil;
import app.ch.pilarit.nearly.models.History;

public class MapHistoryActivity extends FragmentActivity implements OnMapReadyCallback , View.OnClickListener, GoogleMap.SnapshotReadyCallback, GoogleMap.OnMyLocationChangeListener {

    private GoogleMap googleMap;
    private ActionButton mapImvMyLocation;
    private LatLng latLng = null;
    private History historySms = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));
        overridePendingTransition(R.anim.in_trans_right_left, R.anim.out_trans_left_right);
        setContentView(R.layout.activity_map_history);
        loadData();
        initView();
    }

    private void loadData() {
        long historyid = getIntent().getLongExtra(KeyGlobal.SMS_ID, 0);
        if(historyid == 0) return;
        historySms = History.findById(History.class, historyid);
        latLng = new LatLng(Double.valueOf(historySms.getLat()), Double.valueOf(historySms.getLng()));
    }

    private void initView() {
        setUpMap();
        progressDialog = ProgressDialog.show(this, null, getResources().getString(R.string.histroy_dialog_progess));
        progressDialog.setCancelable(false);
        mapImvMyLocation = (ActionButton) findViewById(R.id.map_imv_mylocation);
        mapImvMyLocation.setOnClickListener(this);
        mapImvMyLocation.setEnabled(false);
    }

    private void setUpMap() {
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.googlemap, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.clear();
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setOnMyLocationChangeListener(this);

        if(latLng == null) return;
        MarkerOptions marker = new MarkerOptions().position(latLng).title(historySms.getName()).snippet(GlobalUtil.replacePhoneStr(historySms.getTelephone()));
        this.googleMap.addMarker(marker).showInfoWindow();
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapActivity.DEFAULT_ZOOM));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MapHistoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MapHistoryActivity.this.googleMap.snapshot(MapHistoryActivity.this);
                    }
                });
            }
        }, 1500);
    }

    @Override
    public void onMyLocationChange(Location location) {}

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        mapImvMyLocation.setEnabled(true);
        progressDialog.dismiss();

        if(historySms == null) return;
        //if(historySms.getMapphoto()!=null && historySms.getMapphoto().length() > 0) return;

        bitmap = ImageUtil.scaleDownBitmap(bitmap, 200, this);
        historySms.setMapphoto(ImageUtil.getStringBase64(bitmap));
        historySms.save();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.map_imv_mylocation:{
                doSetMyLocation();
                break;
            }
        }
    }

    private void doSetMyLocation() {
        if(googleMap.getMyLocation() == null) return;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(Map.convertLocationToLatLng(googleMap.getMyLocation())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_history, menu);
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
