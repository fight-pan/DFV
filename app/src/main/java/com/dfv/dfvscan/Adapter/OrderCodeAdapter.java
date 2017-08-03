package com.dfv.dfvscan.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfv.dfvscan.R;
import com.dfv.dfvscan.bean.CodeList;

import java.util.ArrayList;

/**
 * Created by pan on 2016/11/4 0004.
 * >#
 * >#
 */
public class OrderCodeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CodeList> list;
    Handler handler;

    public OrderCodeAdapter(Context context, ArrayList<CodeList> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.ordercode_item, null);
        TextView idTv = (TextView) convertView.findViewById(R.id.id);
        TextView orderCode = (TextView) convertView.findViewById(R.id.order_code);
        ImageView removeIv = (ImageView) convertView.findViewById(R.id.remove_iv);

        idTv.setText(position + 1 + "");
        if (position == 0) {
            idTv.setTextColor(ContextCompat.getColor(context, R.color.blue));
            orderCode.setTextColor(ContextCompat.getColor(context, R.color.blue));
        }
        orderCode.setText(list.get(position).getCode_number());
        removeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 22;
                msg.arg1 = position;
                handler.sendMessage(msg);
            }
        });

        return convertView;
    }

}

