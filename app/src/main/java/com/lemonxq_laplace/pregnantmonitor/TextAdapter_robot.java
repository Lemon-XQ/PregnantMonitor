package com.lemonxq_laplace.pregnantmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class TextAdapter_robot extends BaseAdapter {
    private List<ListData_robot> lists;
    private Context mContext;

    private RelativeLayout layout;

    public TextAdapter_robot(List<ListData_robot> lists, Context mContext) {
        super();
        this.lists = lists;
        this.mContext = mContext;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return lists == null ? 0 : lists.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lists.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (lists.get(position).getFlag() == ListData_robot.RECEIVER) {
            layout = (RelativeLayout) inflater.inflate(
                    R.layout.activity_leftitem_robot, null);
        }
        if (lists.get(position).getFlag() == ListData_robot.SEND) {
            layout = (RelativeLayout) inflater.inflate(
                    R.layout.activity_rightitem_robot, null);
        }
        TextView time = (TextView) layout.findViewById(R.id.tvtime);
        TextView tv = (TextView) layout.findViewById(R.id.tv);
        tv.setText(lists.get(position).getContent());
        time.setText(lists.get(position).getTime());

        return layout;
    }

}
