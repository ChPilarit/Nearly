package app.ch.pilarit.nearly;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.software.shell.fab.ActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.utils.GlobalUtil;
import app.ch.pilarit.nearly.libs.utils.ImageUtil;
import app.ch.pilarit.nearly.models.History;

public class MapHistoryActivity extends FragmentActivity implements OnMapReadyCallback , View.OnClickListener, GoogleMap.SnapshotReadyCallback, GoogleMap.OnMyLocationChangeListener, GoogleMap.OnMapLoadedCallback, GoogleMap.InfoWindowAdapter {

    private GoogleMap googleMap;
    private ActionButton mapImvMyLocation;
    private LatLng latLng = null;
    private History historySms = null;
    private Address address = null;
    private ProgressDialog progressDialog;
    private StringBuffer addressBuffer;

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

        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) address = addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        this.googleMap.setOnMapLoadedCallback(this);
        if(latLng == null) return;

        addressBuffer = new StringBuffer();
        if(address != null){
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressBuffer.append(address.getAddressLine(i));
                addressBuffer.append("\n");
            }
        }

        MarkerOptions marker = new MarkerOptions()
                .position(latLng)
                .title(String.format("%s : %s", historySms.getName(), GlobalUtil.replacePhoneStr(historySms.getTelephone())))
                .snippet(addressBuffer.toString());

        this.googleMap.setInfoWindowAdapter(this);
        this.googleMap.addMarker(marker).showInfoWindow();
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MapActivity.DEFAULT_ZOOM));
    }

    @Override
    public void onMapLoaded() {
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
        if(addressBuffer != null && addressBuffer.length() > 0) historySms.setAddress(addressBuffer.toString());
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
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Map.convertLocationToLatLng(googleMap.getMyLocation()), MapActivity.DEFAULT_ZOOM));
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

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = getLayoutInflater().inflate(R.layout.marker_info, null);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView info = (TextView) v.findViewById(R.id.info);
        title.setText(marker.getTitle());
        info.setText(marker.getSnippet());
        return v;
    }
}
