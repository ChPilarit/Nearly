package app.poly.myapp.doggy.libs.views.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.poly.myapp.doggy.R;

/**
 * Created by ch_pilarit on 4/6/15 AD.
 */
public class DialogComfirm implements View.OnClickListener{

    private Context context;
    private TextView dialogComfirmTevTitle;
    private ImageView dialogComfirmImvIcon;
    private Dialog dialog;
    private TextView dialogComfirmTevQuestion;
    private Button dialogComfirmBtnOk;
    private Button dialogComfirmBtnCancel;
    private DialogInterface dialogInterface;
    private ImageView dialogComfirmImvClose;

    public DialogComfirm(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_comfirm);

        dialogComfirmTevTitle = (TextView) dialog.findViewById(R.id.dialog_comfirm_tev_title);
        dialogComfirmImvIcon = (ImageView) dialog.findViewById(R.id.dialog_comfirm_imv_icon);
        dialogComfirmImvClose = (ImageView) dialog.findViewById(R.id.dialog_comfirm_imv_close);
        dialogComfirmTevQuestion = (TextView) dialog.findViewById(R.id.dialog_comfirm_tev_question);
        dialogComfirmBtnOk = (Button) dialog.findViewById(R.id.dialog_comfirm_btn_ok);
        dialogComfirmBtnCancel = (Button) dialog.findViewById(R.id.dialog_comfirm_btn_cancel);
    }

    public void show(String title, int iconId, String question, DialogInterface dialogInterface){
        dialogComfirmTevTitle.setText(title);
        dialogComfirmImvIcon.setImageResource(iconId);
        dialogComfirmTevQuestion.setText(String.format("\n%s\n",question));
        this.dialogInterface = dialogInterface;
        dialogComfirmBtnOk.setOnClickListener(this);
        dialogComfirmBtnCancel.setOnClickListener(this);
        dialogComfirmImvClose.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_comfirm_imv_close:{
                dialog.dismiss();
                break;
            }

            case R.id.dialog_comfirm_btn_cancel:{
                dialogInterface.cancel(dialog);
                break;
            }

            case R.id.dialog_comfirm_btn_ok:{
                dialogInterface.ok(dialog);
                break;
            }
        }
    }
}
