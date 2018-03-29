package com.example.a20897.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a20897.myapplication.BitmapManager;
import com.example.a20897.myapplication.CurrentEditBlog;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.activities.ViewBlogActivity;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.List;

/**
 * Created by 20897 on 2018/3/24.
 */

public class InitAdapter extends BaseAdapter {
    private List<?> mData;
    private LayoutInflater mInflater;
    private Context context;
    public InitAdapter(Context context,List<?> data){
        super();
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
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

            convertView = mInflater.inflate(R.layout.initlistitem, null);
            holder.item_view=convertView.findViewById(R.id.item_id);
            holder.avatar = convertView.findViewById(R.id.blog_writer_photo);
            holder.title=convertView.findViewById(R.id.title_id);
            holder.paragraph = convertView.findViewById(R.id.paragraph);
            holder.author=convertView.findViewById(R.id.author_name);
            holder.create_time=convertView.findViewById(R.id.create_time);
            holder.likes=convertView.findViewById(R.id.blog_likes);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Object mObject = mData.get(position);
        if(mObject!=null&&mObject instanceof BlogModel){
            holder.avatar.setImageBitmap(((BlogModel) mObject).Writer_Avatar_String);
            holder.title.setText(((BlogModel) mObject).title);
            holder.author.setText(((BlogModel) mObject).Writer_name);
            holder.paragraph.setText(((BlogModel) mObject).Paragraph);
            holder.likes.setText(((BlogModel) mObject).Likes);
            holder.create_time.setText(((BlogModel) mObject).Create_time);
            holder.item_view.setOnClickListener(v -> {
                BlogModel bm=new BlogModel();
                bm.blog_id=((BlogModel) mObject).blog_id;
                bm.title=((BlogModel) mObject).title;
                CurrentEditBlog.getInstance().setBlogModel(bm);
                Intent intent=new Intent();
                intent.setClass(context, ViewBlogActivity.class);
                context.startActivity(intent);
            });
        }
        return  convertView;
    }

    private class ViewHolder {
        public View item_view;
        public ImageView avatar;
        public TextView title;
        public TextView author;
        public TextView paragraph;
        public TextView likes;
        public TextView create_time;
    }
}
