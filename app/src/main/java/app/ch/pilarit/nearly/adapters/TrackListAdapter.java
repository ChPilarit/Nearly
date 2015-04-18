package app.ch.pilarit.nearly.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import app.ch.pilarit.nearly.HomeActivity;
import app.ch.pilarit.nearly.R;
import app.ch.pilarit.nearly.TrackerActivity;
import app.ch.pilarit.nearly.keys.KeyGlobal;
import app.ch.pilarit.nearly.libs.db.DbBitmapUtility;
import app.ch.pilarit.nearly.libs.map.Map;
import app.ch.pilarit.nearly.libs.utils.ImageUtil;
import app.ch.pilarit.nearly.models.TrackSetting;

/**
 * Created by ch_pilarit on 4/14/15 AD.
 */
public class TrackListAdapter extends BaseAdapter {

    private List<TrackSetting> trackSettingList;
    private Context context;

    public TrackListAdapter(Context context, List<TrackSetting> trackSettingList) {
        this.context = context;
        this.trackSettingList = trackSettingList;
    }

    @Override
    public int getCount() {
        return trackSettingList.size();
    }

    @Override
    public Object getItem(int i) {
        return trackSettingList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return trackSettingList.indexOf(getItem(i));
    }

    class ViewItemHolder{
        ImageView tracklistItemPhoto;
        Switch tracklistItemActive;
        TextView tracklistItemName;
        TextView tracklistItemStartTime;
        TextView tracklistItemEndTime;
        CardView tracklistItemCardview;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TrackSetting trackSetting = (TrackSetting) getItem(i);
        ViewItemHolder viewItemHolder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.home_lv_tracklist_item, viewGroup, false);

            viewItemHolder = new ViewItemHolder();
            viewItemHolder.tracklistItemActive = (Switch) view.findViewById(R.id.tracklist_item_active);
            viewItemHolder.tracklistItemName = (TextView) view.findViewById(R.id.tracklist_item_name);
            viewItemHolder.tracklistItemPhoto = (ImageView) view.findViewById(R.id.tracklist_item_photo);
            viewItemHolder.tracklistItemStartTime = (TextView) view.findViewById(R.id.tracklist_item_starttime);
            viewItemHolder.tracklistItemEndTime = (TextView) view.findViewById(R.id.tracklist_item_endtime);
            viewItemHolder.tracklistItemCardview = (CardView) view.findViewById(R.id.tracklist_item_cardview);

            view.setTag(viewItemHolder);
        }else{
            viewItemHolder = (ViewItemHolder) view.getTag();
        }

        initView(i, trackSetting, viewItemHolder);

        return view;
    }

    private void initView(int i, final TrackSetting trackSetting, ViewItemHolder viewItemHolder) {

        viewItemHolder.tracklistItemActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                trackSetting.setActive(checked);
                trackSetting.save();
            }
        });
        viewItemHolder.tracklistItemActive.setChecked(trackSetting.isActive());
        viewItemHolder.tracklistItemName.setText(trackSetting.getName());
        viewItemHolder.tracklistItemStartTime.setText(trackSetting.getStarttime());
        viewItemHolder.tracklistItemEndTime.setText(trackSetting.getEndtime());
        viewItemHolder.tracklistItemPhoto.setImageBitmap(DbBitmapUtility.getBitmapBase64(trackSetting.getMapphoto()));
        viewItemHolder.tracklistItemCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoTrackerSetting(trackSetting);
            }
        });
    }

    private void gotoTrackerSetting(TrackSetting trackSetting) {
        Intent gotoTrackerSetting = new Intent(context, TrackerActivity.class);
        gotoTrackerSetting.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.HOME_ACTIVITY);
        gotoTrackerSetting.putExtra(HomeActivity.TRACKER_SETTING_ID, trackSetting.getId());
        context.startActivity(gotoTrackerSetting);
    }
}
