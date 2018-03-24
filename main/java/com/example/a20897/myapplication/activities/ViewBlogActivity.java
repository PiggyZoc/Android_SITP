package com.example.a20897.myapplication.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a20897.myapplication.CurrentEditBlog;
import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.models.ParaModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/30.
 */

public class ViewBlogActivity extends MyActivity {
    private MyActivity ma;
    private ArrayList<ParaModel> paraModel;
    private ArrayList<String> pos;
    private ArrayList<String> imgBase64;
    private String blog_id;
    private LinearLayout linearLayout;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "ActivityViewBlog";
        setContentView(R.layout.test_viewblog);
        ma = this;
        blog_id=String.valueOf(CurrentEditBlog.getInstance().getBlogModel().blog_id);
        ActivityManager.getActivityManager().addActivity(this);
         myWebView = findViewById(R.id.webview);
        //myWebView.loadUrl("http://wz66.top:86/demo.html");
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        loadBolg();
       // blog_id = String.valueOf(CurrentEditBlog.getInstance().getBlogModel().blog_id);
        //linearLayout = findViewById(R.id.linearLayout);
        // asynTask asynTask=new asynTask();
        //asynTask.execute();
        //ShowImgBase64String();
    }


    private void loadBolg() {

        QueryManager qm = new QueryManager(ma);

        qm.execute("getBlogURLByID", "blog_id", blog_id);

    }

    @Override
    public void goingOn(ArrayList<String> arrayList) {
        //   System.out.println(arrayList.get(1));

        String methodName = arrayList.get(0);
        try {
            String rs = arrayList.get(1);
            switch (methodName) {
                case "getBlogURLByID":
                    if (!rs.isEmpty()) {
                        myWebView.loadUrl(rs);

                    } else {
                        return;
                    }
                    return;





            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
