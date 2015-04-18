package app.ch.pilarit.nearly;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.ch.pilarit.nearly.activity.BaseActivity;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.db.DbBitmapUtility;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.views.dialogs.Boast;
import app.ch.pilarit.nearly.models.TrackSetting;

import static android.widget.CompoundButton.OnCheckedChangeListener;


public class TrackerActivity extends BaseActivity implements View.OnClickListener, OnCheckedChangeListener{

    public final static String TIME_FORMAT = "HH:mm";
    public final static int REQUEST_CODE = 10000;

    private Switch trackerSwActive;
    private ImageView trackerImvMap;
    private EditText trackerEdtName;
    private TextView trackerTvStartTime;
    private TextView trackerTvEndTime;
    private EditText trackerEdtTelephone;
    private CheckBox trackerCbRepeat;
    private ToggleButton trackerTgSunday;
    private ToggleButton trackerTgMonday;
    private ToggleButton trackerTgTuesday;
    private ToggleButton trackerTgWednesday;
    private ToggleButton trackerTgThursday;
    private ToggleButton trackerTgFriday;
    private ToggleButton trackerTgSaturday;
    private Button trackerBtnSave;
    private List<LatLng> polygon;
    private Bitmap cachemap;
    private TrackSetting trackSetting;
    private LinearLayout trackerLayoutDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        doLoadTrackSetting();
        initView();
    }

    private void doLoadTrackSetting() {
        trackSetting = new TrackSetting();
        String fromActivity = getIntent().getStringExtra(KeyGlobal.FROM_ACTIVITY);
        if(KeyGlobal.MAP_ACTIVITY.equals(fromActivity)){
            String polygonStr = getIntent().getStringExtra(MapActivity.KEY_POLYGON);
            polygon = Map.stringPolygonToPolygon(polygonStr);
            cachemap = (Bitmap) getIntent().getParcelableExtra(MapActivity.KEY_CACHE_MAP);
        }else{
            long id = getIntent().getLongExtra(HomeActivity.TRACKER_SETTING_ID, 0);
            trackSetting = TrackSetting.findById(TrackSetting.class, id);
            cachemap = DbBitmapUtility.getBitmapBase64(trackSetting.getMapphoto());
            polygon = Map.stringPolygonToPolygon(trackSetting.getPolygon());
        }
    }

    private void initView() {
        trackerSwActive = (Switch) findViewById(R.id.tracker_sw_active);
        trackerImvMap = (ImageView) findViewById(R.id.tracker_imv_map);
        trackerEdtName = (EditText) findViewById(R.id.tracker_edt_name);
        trackerTvStartTime = (TextView) findViewById(R.id.tracker_tv_starttime);
        trackerTvEndTime = (TextView) findViewById(R.id.tracker_tv_endtime);
        trackerEdtTelephone = (EditText) findViewById(R.id.tracker_edt_telephone);
        trackerCbRepeat = (CheckBox) findViewById(R.id.tracker_cb_repeat);
        trackerLayoutDays = (LinearLayout) findViewById(R.id.tracker_layout_days);
        trackerTgSunday = (ToggleButton) findViewById(R.id.tracker_tg_sunday);
        trackerTgMonday = (ToggleButton) findViewById(R.id.tracker_tg_monday);
        trackerTgTuesday = (ToggleButton) findViewById(R.id.tracker_tg_tuesday);
        trackerTgWednesday = (ToggleButton) findViewById(R.id.tracker_tg_wednesday);
        trackerTgThursday = (ToggleButton) findViewById(R.id.tracker_tg_thursday);
        trackerTgFriday = (ToggleButton) findViewById(R.id.tracker_tg_friday);
        trackerTgSaturday = (ToggleButton) findViewById(R.id.tracker_tg_saturday);
        trackerBtnSave = (Button) findViewById(R.id.tracker_btn_save);

        trackerImvMap.setOnClickListener(this);
        trackerTvStartTime.setOnClickListener(this);
        trackerTvEndTime.setOnClickListener(this);
        trackerBtnSave.setOnClickListener(this);
        if(cachemap != null){
            trackerImvMap.setImageBitmap(cachemap);
        }

        trackerLayoutDays.setVisibility(View.GONE);
        if(trackSetting != null){
            try{
                if(trackSetting.getId() > 0) {
                    trackerSwActive.setChecked(trackSetting.isActive());
                    trackerEdtName.setText(trackSetting.getName());
                    trackerTvStartTime.setText(trackSetting.getStarttime());
                    trackerTvEndTime.setText(trackSetting.getEndtime());
                    trackerEdtTelephone.setText(trackSetting.getTelephone());
                    trackerCbRepeat.setChecked(trackSetting.isRepeate());
                    if(trackerCbRepeat.isChecked()){
                        trackerLayoutDays.setVisibility(View.VISIBLE);
                        setDaysOfWeek(trackSetting.getDayofweek());
                    }else{
                        trackerLayoutDays.setVisibility(View.GONE);
                        clearDaysOfWeek(false);
                    }
                }
            }catch (NullPointerException ex){ }

        }


        trackerCbRepeat.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()){
            case R.id.tracker_cb_repeat:{
                if(checked){
                    trackerLayoutDays.setVisibility(View.VISIBLE);
                }else{
                    trackerLayoutDays.setVisibility(View.GONE);

                }
                clearDaysOfWeek(false);
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tracker_btn_save:{
                if(validate()){
                    doSave();
                }
                break;
            }
            case R.id.tracker_imv_map:{
                gotoMapActivity();
                break;
            }
            case R.id.tracker_tv_starttime:{
                showTimePickerDialog(trackerTvStartTime);
                break;
            }
            case R.id.tracker_tv_endtime:{
                showTimePickerDialog(trackerTvEndTime);
                break;
            }
        }
    }

    private void doSave() {

        boolean active = trackerSwActive.isChecked();
        String polygonStr = Map.polygonToStringPolygon(polygon);
        String name = trackerEdtName.getText().toString().trim();
        String startTime = trackerTvStartTime.getText().toString().trim();
        String endTime = trackerTvEndTime.getText().toString().trim();
        String telephone = trackerEdtTelephone.getText().toString().trim();
        boolean repeat = trackerCbRepeat.isChecked();
        String daysOfweek = getDaysOfWeek();

        trackSetting.setActive(active);
        trackSetting.setPolygon(polygonStr);
        trackSetting.setName(name);
        trackSetting.setStarttime(startTime);
        trackSetting.setEndtime(endTime);
        trackSetting.setTelephone(telephone);
        trackSetting.setRepeate(repeat);
        trackSetting.setDayofweek(daysOfweek);

        if(cachemap != null) {
            trackSetting.setMapphoto(DbBitmapUtility.getStringBase64(cachemap));
        }else{
            trackSetting.setMapphoto("");
        }

        trackSetting.save();
        //Boast.makeText(this, "Save Complete " + trackSetting.getId()).show();
        onBackPressed();
    }

    public String getDaysOfWeek() {
        StringBuffer daysOfWeek = new StringBuffer();

        if(trackerTgSunday.isChecked()) daysOfWeek.append("0");
        if(trackerTgMonday.isChecked()) daysOfWeek.append("1");
        if(trackerTgTuesday.isChecked()) daysOfWeek.append("2");
        if(trackerTgWednesday.isChecked()) daysOfWeek.append("3");
        if(trackerTgThursday.isChecked()) daysOfWeek.append("4");
        if(trackerTgFriday.isChecked()) daysOfWeek.append("5");
        if(trackerTgSaturday.isChecked()) daysOfWeek.append("6");

        return daysOfWeek.toString();
    }

    public void setDaysOfWeek(String daysOfWeek) {

        trackerTgSunday.setChecked(false);
        trackerTgMonday.setChecked(false);
        trackerTgTuesday.setChecked(false);
        trackerTgWednesday.setChecked(false);
        trackerTgThursday.setChecked(false);
        trackerTgFriday.setChecked(false);
        trackerTgSaturday.setChecked(false);

        if(daysOfWeek == null) return;

        String[] daysOfWeekSplit = daysOfWeek.split("");
        if(daysOfWeekSplit == null || daysOfWeekSplit.length < 1){
            return;
        }

        for (String index : daysOfWeekSplit){
            int dayIndex = Integer.valueOf(index);

            switch (dayIndex){
                case 0 :{
                    trackerTgSunday.setChecked(true);
                    break;
                }
                case 1 :{
                    trackerTgMonday.setChecked(true);
                    break;
                }
                case 2 :{
                    trackerTgTuesday.setChecked(true);
                    break;
                }
                case 3 :{
                    trackerTgWednesday.setChecked(true);
                    break;
                }
                case 4 :{
                    trackerTgThursday.setChecked(true);
                    break;
                }
                case 5 :{
                    trackerTgFriday.setChecked(true);
                    break;
                }
                case 6 :{
                    trackerTgSaturday.setChecked(true);
                    break;
                }
            }

        }

    }

    public void clearDaysOfWeek(boolean checked) {

        trackerTgSunday.setChecked(checked);
        trackerTgMonday.setChecked(checked);
        trackerTgTuesday.setChecked(checked);
        trackerTgWednesday.setChecked(checked);
        trackerTgThursday.setChecked(checked);
        trackerTgFriday.setChecked(checked);
        trackerTgSaturday.setChecked(checked);

    }

    private boolean validate() {
        String name = trackerEdtName.getText().toString();
        if(name.trim().length() < 1){
            Boast.makeText(this, R.string.tracker_warn_enter_name).show();
            return false;
        }

        try {
            if (trackSetting.getId() < 1 && TrackSetting.hasName(name)) {
                Boast.makeText(this, R.string.tracker_warn_already_name).show();
                return false;
            }
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }

        if(polygon == null || polygon.size() < 1){
            Boast.makeText(this, R.string.tracker_warn_draw_map).show();
            return false;
        }

        String starttimeStr = trackerTvStartTime.getText().toString();
        String endtimeStr = trackerTvEndTime.getText().toString();
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
            Date starttime = timeFormat.parse(starttimeStr);
            Date endtime = timeFormat.parse(endtimeStr);

            if(starttime.after(endtime)){
                Boast.makeText(this, R.string.tracker_warn_time_invalid).show();
                return false;
            }
        } catch (ParseException e) {
            Boast.makeText(this, e.getMessage()).show();
            return false;
        }

        String telephone = trackerEdtTelephone.getText().toString();
        if(telephone.trim().length() < 1){
            Boast.makeText(this, R.string.tracker_warn_enter_telephone).show();
            return false;
        }

        if(trackerCbRepeat.isChecked()){

            boolean checked = true;

            if(trackerTgSunday.isChecked()) checked = false;
            else if(trackerTgMonday.isChecked()) checked = false;
            else if(trackerTgTuesday.isChecked()) checked = false;
            else if(trackerTgWednesday.isChecked()) checked = false;
            else if(trackerTgThursday.isChecked()) checked = false;
            else if(trackerTgFriday.isChecked()) checked = false;
            else if(trackerTgSaturday.isChecked()) checked = false;

            if(checked){
                Boast.makeText(this, R.string.tracker_warn_select_repeat, Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void gotoMapActivity() {
        Intent gotoMapActivity = new Intent(this, MapActivity.class);
        gotoMapActivity.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.TRACKER_ACTIVITY);
        gotoMapActivity.putExtra(MapActivity.KEY_POLYGON, Map.polygonToStringPolygon(polygon));
        startActivityForResult(gotoMapActivity, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            String fromActivity = data.getStringExtra(KeyGlobal.FROM_ACTIVITY);
            if(KeyGlobal.MAP_ACTIVITY.equals(fromActivity)){
                String polygonStr = data.getStringExtra(MapActivity.KEY_POLYGON);
                polygon = Map.stringPolygonToPolygon(polygonStr);
                cachemap = (Bitmap) data.getParcelableExtra(MapActivity.KEY_CACHE_MAP);
                trackerImvMap.setImageBitmap(cachemap);
            }
        }
    }

    private void showTimePickerDialog(final TextView trackerTvTime) {
        String[] time = trackerTvTime.getText().toString().split("\\:");
        int hour = Integer.valueOf(time[0]);
        int minute = Integer.valueOf(time[1]);
        new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        trackerTvTime.setText(sdf.format(calendar.getTime()));
                    }
                },
                hour,
                minute,
                true
        ).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tracker, menu);
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
