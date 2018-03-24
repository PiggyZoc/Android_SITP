package com.example.a20897.myapplication.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.ParaModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/30.
 */

public class ViewBlogActivity extends MyActivity {
    private MyActivity ma;

    private String blog_id;
    private String user_id;
   private TextView addlike;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "ActivityViewBlog";
        setContentView(R.layout.test_viewblog);
        ma = this;
        blog_id=String.valueOf(CurrentEditBlog.getInstance().getBlogModel().blog_id);
        user_id= UserAccount.getInstance().getUser().user_id;
        addlike=findViewById(R.id.add_like);
        addlike.setOnClickListener(mClickLisener1);
        myWebView = findViewById(R.id.webview);
        myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        loadBolg();
    }
    private View.OnClickListener mClickLisener1=v->{
        QueryManager qm = new QueryManager(ma);
        qm.execute("addLikes","blog_id",blog_id,"user_id",user_id);
    };

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
