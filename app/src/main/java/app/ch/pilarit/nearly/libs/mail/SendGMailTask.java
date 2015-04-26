package app.ch.pilarit.nearly.libs.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by ch_pilarit on 4/26/15 AD.
 */
public class SendGMailTask extends AsyncTask<String, Void, Void>{
    private ProgressDialog progressDialog;
    private Context context;
    private GMailSender sender;

    public SendGMailTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(context, "Please wait", "Sending mail", true, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //progressDialog.dismiss();
    }

    /*
     * subject email = messages[0]
     * body email = messages[1]
     * to email messages[2]
     */
    @Override
    protected Void doInBackground(String... messages) {
        if(messages == null) return null;
        if(messages.length != 3) return null;

        try {
            sender = new GMailSender();
            sender.sendMail(messages[0], messages[1], messages[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
