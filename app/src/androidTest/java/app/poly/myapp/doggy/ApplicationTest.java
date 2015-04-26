package app.poly.myapp.doggy;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import app.poly.myapp.doggy.libs.mail.SendGMailTask;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Log.e("trackSetting","\n====> start test <====");

        SendGMailTask sendGMailTask = new SendGMailTask(getContext());
        sendGMailTask.execute(new String[]{"test","test", "somkaw@gmail.com"});

        Log.e("trackSetting","====> end test <====\n");

    }
}