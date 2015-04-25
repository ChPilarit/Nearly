package app.ch.pilarit.nearly.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Emokidz on 4/24/15 AD.
 */
public class History extends SugarRecord<History>{
    String name;
    String mapphoto;
    String telephone;
    String lat;
    String lng;
    String date;
    String deviceid;

    String address;


    public History(){

    }



    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMapphoto(){
        return mapphoto;

    }
    public void setMapphoto(String mapphoto){
        this.mapphoto = mapphoto;

    }

    public String getTelephone(){
        return telephone;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

    public String getLat(){
        return lat;
    }
    public void setLat(String lat){
        this.lat = lat;
    }

    public String getLng(){
        return lng;
    }

    public void setLng(String lng){
        this.lng = lng;
    }

    public  String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDeviceid(){
        return deviceid;
    }

    public void setDeviceid(String deviceid){
        this.deviceid = deviceid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
