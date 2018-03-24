package com.example.a20897.myapplication;

import com.example.a20897.myapplication.models.BlogModel;
import com.example.a20897.myapplication.models.ParaModel;
import com.example.a20897.myapplication.models.UserModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/29.
 */

public class CurrentEditBlog {
    private static CurrentEditBlog instance;
    private static BlogModel currentBlog;


    static public  CurrentEditBlog getInstance(){
        {
            if(instance == null)
            {
                instance = new CurrentEditBlog();
            }
            return  instance;
        }
    }
    public BlogModel getBlogModel(){
        return currentBlog;
    }
    public void setBlogModel(BlogModel blogModel){
        currentBlog=blogModel;
    }

}
