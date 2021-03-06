package app.poly.myapp.doggy.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.poly.myapp.doggy.MapHistoryActivity;
import app.poly.myapp.doggy.R;
import app.poly.myapp.doggy.keys.KeyGlobal;
import app.poly.myapp.doggy.libs.utils.DateUtil;
import app.poly.myapp.doggy.libs.utils.GlobalUtil;
import app.poly.myapp.doggy.libs.utils.ImageUtil;
import app.poly.myapp.doggy.libs.utils.NetworkUtils;
import app.poly.myapp.doggy.libs.views.dialogs.Boast;
import app.poly.myapp.doggy.libs.views.dialogs.DialogComfirm;
import app.poly.myapp.doggy.libs.views.dialogs.DialogInterface;
import app.poly.myapp.doggy.models.History;

public class HistoryListAdapter extends BaseAdapter{

    private List<History> historyList;
    private Context context;


    public HistoryListAdapter(Context context, List<History> historyList){
        this.context = context;
        this.historyList = historyList;

    }
    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return historyList.indexOf(getItem(i));
    }
    class ViewItemHolder {
        ImageView historyItemPhoto;
        TextView  historyItemName;
        TextView  historyItemTelephone;
        TextView  historyItemDate;
        CardView  historyItemCardview;
        TextView  historyItemAddress;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        History history = (History)getItem(i);
        ViewItemHolder viewItemHolder = null;
        if (view ==null){

            view = LayoutInflater.from(context).inflate(R.layout.history_lv_tracklist_item,viewGroup,false);

            viewItemHolder = new ViewItemHolder();
            viewItemHolder.historyItemPhoto = (ImageView)view.findViewById(R.id.history_item_photo);
            viewItemHolder.historyItemName = (TextView)view.findViewById(R.id.history_item_name);
            viewItemHolder.historyItemTelephone = (TextView)view.findViewById(R.id.history_telephone);
            viewItemHolder.historyItemDate = (TextView)view.findViewById(R.id.history_date);
            viewItemHolder.historyItemCardview = (CardView)view.findViewById(R.id.history_item_cardview);
            viewItemHolder.historyItemAddress = (TextView)view.findViewById(R.id.history_item_address);

            view.setTag(viewItemHolder);
        }else {
            viewItemHolder = (ViewItemHolder) view.getTag();
        }
        initView(i,history,viewItemHolder);



        return view;
    }

    private void initView(final int position, final History history, ViewItemHolder viewItemHolder) {
        if(history.getMapphoto() != null && history.getMapphoto().length() > 0) {
            viewItemHolder.historyItemPhoto.setImageBitmap(ImageUtil.getBitmapBase64(history.getMapphoto()));
        }

        if(history.getAddress() != null && history.getAddress().length() > 0) {
            viewItemHolder.historyItemAddress.setText(history.getAddress());
        }

        viewItemHolder.historyItemName.setText(history.getName());
        viewItemHolder.historyItemTelephone.setText(GlobalUtil.replacePhoneStr(history.getTelephone()));
        viewItemHolder.historyItemDate.setText(DateUtil.dateFormat(history.getDate()));
        viewItemHolder.historyItemCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(NetworkUtils.isNetworkAvailable(context)) {
                gotoMapHistory(history);
            }else{
                Boast.makeText(context, R.string.net_warning).show();
            }
            }
        });

        viewItemHolder.historyItemCardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                doConfirmDelete(position,history);
                return false;
            }
        });
    }

    private void gotoMapHistory(History history){
        Intent gotoMapHistory = new Intent(context, MapHistoryActivity.class);
        gotoMapHistory.putExtra(KeyGlobal.SMS_ID, history.getId());
        gotoMapHistory.putExtra(KeyGlobal.FROM_ACTIVITY, KeyGlobal.HOME_HISTORY_ACTIVITY);
        context.startActivity(gotoMapHistory);
    }

    private void doConfirmDelete(final int position, final History history) {
        String title = context.getResources().getString(R.string.history_dialog_title);
        String question = context.getResources().getString(R.string.histroy_dialog_question);
        new DialogComfirm(context).show(title,R.drawable.ic_action_tick,question,new DialogInterface() {
            @Override
            public void ok(Dialog dialog) {
                history.delete();
                historyList.remove(position);
                notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void cancel(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }
}
