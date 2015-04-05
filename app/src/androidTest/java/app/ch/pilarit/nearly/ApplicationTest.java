package app.ch.pilarit.nearly;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.List;

import app.ch.pilarit.nearly.models.TrackSetting;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Log.e("trackSetting","\n====> start test <====");

        if(!TrackSetting.hasName("toomtam")){
            Log.e("trackSetting","\n====> save <====");
            TrackSetting trackSetting = new TrackSetting();
            trackSetting.setName("toomtam");
            trackSetting.setTelephone("123456678");
            trackSetting.save();

        }else{
            TrackSetting.deleteAll(TrackSetting.class);
        }

        List<TrackSetting> trackSettings = TrackSetting.listAll(TrackSetting.class);

        for (TrackSetting trackSetting1 : trackSettings){
            Log.e("trackSetting","====> id: " + trackSetting1.getId());
            Log.e("trackSetting","====> name: " + trackSetting1.getName());
            Log.e("trackSetting","====> telephone: " + trackSetting1.getTelephone());
        }

        Log.e("trackSetting","====> end test <====\n");

    }
}