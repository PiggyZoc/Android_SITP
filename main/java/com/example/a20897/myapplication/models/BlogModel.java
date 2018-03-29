package com.example.a20897.myapplication.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.io.Writer;

/**
 * Created by 20897 on 2017/12/29.
 */

public class BlogModel implements Serializable {
    public int blog_id;
    public String title;
    public String Writer_id;
    public String Create_time;
    public String Writer_name;
    public String blog_url;
    public Bitmap Writer_Avatar_String;
    public String Paragraph;
    public BlogModel(String title, String name,String create_time){
        this.title = title;
        this.Writer_name = name;
        this.Create_time = create_time;
    }

    public BlogModel() {

    }
}
