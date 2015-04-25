package app.ch.pilarit.nearly.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orm.SugarRecord;

import java.util.List;

import app.ch.pilarit.nearly.R;
import app.ch.pilarit.nearly.libs.views.dialogs.DialogComfirm;
import app.ch.pilarit.nearly.libs.views.dialogs.DialogInterface;
import app.ch.pilarit.nearly.models.History;


/**
 * Created by Emokidz on 4/24/15 AD.
 */
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

            view.setTag(viewItemHolder);
        }else {
            viewItemHolder = (ViewItemHolder) view.getTag();
        }
        initView(i,history,viewItemHolder);



        return view;
    }

    private void initView(final int position, final History history, ViewItemHolder viewItemHolder) {

        viewItemHolder.historyItemName.setText(history.getName());
        viewItemHolder.historyItemTelephone.setText(history.getTelephone());
        viewItemHolder.historyItemDate.setText(history.getDate());
        viewItemHolder.historyItemCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
