package app.ch.pilarit.nearly.utils;

import android.content.Context;

import app.ch.pilarit.nearly.keys.KeyAccount;
import app.ch.pilarit.nearly.libs.session.SessionLocal;

/**
 * Created by ch_pilarit on 4/5/15 AD.
 */
public class AccountSessionUtil {

    public static boolean hasTracker(Context context) {

        SessionLocal sessionLocal = SessionLocal.getInstance(context);

        if(sessionLocal.hasKey(KeyAccount.ROLE_ID)){
            String roleidStr = String.valueOf(sessionLocal.get(KeyAccount.ROLE_ID));
            if(roleidStr.length()>0){
                int roleid = Integer.parseInt(roleidStr);
                if(roleid == KeyAccount.ROLE_FOLLOWER){
                    return false;
                }
            }
        }

        return true;
    }
}
