package app.poly.myapp.doggy.libs.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private static StringBuffer stringBuffer;
    private static List<LatLng> poligonList;

    public static String polygonToStringPolygon(List<LatLng> poligonList){
        if(stringBuffer == null) stringBuffer = new StringBuffer();
        stringBuffer.setLength(0);

        String lat = "";
        String lng = "";
        for (LatLng latLng : poligonList){
            lat = String.valueOf(latLng.latitude);
            lng = String.valueOf(latLng.longitude);
            stringBuffer.append(String.format("%s,%s/", lat, lng));
        }

        return stringBuffer.toString();
    }



    public static List<LatLng> stringPolygonToPolygon(String polygonStr){
        if(poligonList == null) poligonList = new ArrayList<LatLng>();
        poligonList.clear();

        if(polygonStr == null) return poligonList;
        if(!polygonStr.contains("/")) return poligonList;
        String[] splitPolygon = polygonStr.split("/");

        String[] splitLatLng;
        for (String latlngStr : splitPolygon){
            if(!latlngStr.contains(",")) continue;

            splitLatLng = latlngStr.split(",");
            LatLng latlng = new LatLng(Double.valueOf(splitLatLng[0]), Double.valueOf(splitLatLng[1]));
            poligonList.add(latlng);
        }

        return poligonList;
    }

    public static boolean isPointInPolygon(LatLng currentLatLng, List<LatLng> polygon) {
        int intersectCount = 0;
        for (int j = 0; j < polygon.size() - 1; j++) {
            if (rayCastIntersect(currentLatLng, polygon.get(j), polygon.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    public static boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY) || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    public static LatLng centroid(List<LatLng> points) {
        double[] centroid = { 0.0, 0.0 };

        for (int i = 0; i < points.size(); i++) {
            centroid[0] += points.get(i).latitude;
            centroid[1] += points.get(i).longitude;
        }

        int totalPoints = points.size();
        centroid[0] = centroid[0] / totalPoints;
        centroid[1] = centroid[1] / totalPoints;

        return new LatLng(centroid[0], centroid[1]);
    }

    public static LatLng convertLocationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

}
