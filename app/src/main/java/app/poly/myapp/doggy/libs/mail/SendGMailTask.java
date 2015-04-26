package app.poly.myapp.doggy.libs.mail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import app.poly.myapp.doggy.R;
import app.poly.myapp.doggy.libs.views.dialogs.Boast;

public class SendGMailTask extends AsyncTask<String, Void, Void>{
    private ProgressDialog progressDialog;
    private Context context;
    private GMailSender sender;
    private String[] messages;

    public SendGMailTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, null, context.getString(R.string.login_forgot_password_wait), true, false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(progressDialog != null) progressDialog.dismiss();
        if(messages != null && messages.length == 3) {
            Boast.makeText(context, String.format(context.getResources().getString(R.string.login_send_mail_complete), messages[2])).show();
        }
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
            this.messages = messages;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
