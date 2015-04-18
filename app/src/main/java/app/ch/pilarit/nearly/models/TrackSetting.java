package app.ch.pilarit.nearly.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ch_pilarit on 4/4/15 AD.
 */
public class TrackSetting extends SugarRecord<TrackSetting>{

    String name;
    String mapphoto;
    String polygon;
    String starttime;
    String endtime;
    String telephone;
    boolean repeate;
    String dayofweek;
    String date;
    boolean active;

    int status;

    public TrackSetting() {

    }

    public static boolean hasName(String name) {
        List<TrackSetting> trackSetting = TrackSetting.find(TrackSetting.class, "name = ?", name);
        if(trackSetting == null || trackSetting.isEmpty()) return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMapphoto() {
        return mapphoto;
    }

    public void setMapphoto(String mapphoto) {
        this.mapphoto = mapphoto;
    }

    public String getPolygon() {
        return polygon;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isRepeate() {
        return repeate;
    }

    public void setRepeate(boolean repeate) {
        this.repeate = repeate;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public void setDayofweek(String dayofweek) {
        this.dayofweek = dayofweek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
