package com.example.a20897.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.List;

/**
 * Created by 20897 on 2018/3/24.
 */

public class InitAdapter extends BaseAdapter {
    private List<?> mData;
    private LayoutInflater mInflater;
    public InitAdapter(Context context,List<?> data){
        super();
        this.mInflater=LayoutInflater.from(context);
        this.mData=data;
    }
    @Override
    public int getCount() {
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            holder= new ViewHolder();

            //可以理解为从vlist获取view 之后把view返回给ListView

            convertView = mInflater.inflate(R.layout.initlistitem, null);
            holder.item_view=convertView.findViewById(R.id.item_id);
            holder.title=convertView.findViewById(R.id.title_id);
            holder.author=convertView.findViewById(R.id.author_name);
            holder.create_time=convertView.findViewById(R.id.create_time);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Object mObject = mData.get(position);
        if(mObject!=null&&mObject instanceof BlogModel){
            holder.title.setText(((BlogModel) mObject).title);
            holder.author.setText(((BlogModel) mObject).Writer_name);
            holder.create_time.setText(((BlogModel) mObject).Create_time);
        }
        return  convertView;
    }

    private class ViewHolder {
        public View item_view;
        public TextView title;
        public TextView author;
        public TextView create_time;
    }
}
