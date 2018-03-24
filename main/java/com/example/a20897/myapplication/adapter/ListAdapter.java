package com.example.a20897.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.a20897.myapplication.CurrentEditBlog;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.activities.MyBlogsActivity;
import com.example.a20897.myapplication.activities.ViewBlogActivity;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.List;
import java.util.Map;

/**
 * Created by 20897 on 2018/3/24.
 */

public class ListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private Context context;
    public ListAdapter(Context context,List<Map<String, Object>> data) {
        super();
        this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.mData=data;
    }
    @Override
    public int getCount() {
        return mData.size();
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

            convertView = mInflater.inflate(R.layout.vlist, null);
            holder.title=convertView.findViewById(R.id.title);
            holder.viewBtn = convertView.findViewById(R.id.view_btn);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText((String)mData.get(position).get("title"));
        holder.viewBtn.setTag(position);
        //给Button添加单击事件 添加Button之后ListView将失去焦点 需要的直接把Button的焦点去掉
        holder.viewBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BlogModel bm=new BlogModel();
                bm.blog_id=(Integer) mData.get(position).get("info");
                bm.title=(String)mData.get(position).get("title");
                CurrentEditBlog.getInstance().setBlogModel(bm);
                Intent intent=new Intent();
                intent.setClass(context, ViewBlogActivity.class);
                context.startActivity(intent);
            }
        });

        //holder.viewBtn.setOnClickListener(MyListener(position));

        return convertView;

    }
    private class ViewHolder {
        public TextView title;
        public Button viewBtn;
    }
}

