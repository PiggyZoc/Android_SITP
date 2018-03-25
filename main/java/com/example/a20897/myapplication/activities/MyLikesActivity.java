package com.example.a20897.myapplication.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.adapter.InitAdapter;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2018/3/25.
 */

public class MyLikesActivity extends MyActivity {


    private MyActivity ma;
    private ListView mylikes;
    private String user_id;
    ArrayList<BlogModel> models;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "ActivityMyLikes";
        setContentView(R.layout.mylikes_layout);
        ma = this;
        mylikes=findViewById(R.id.mylikeblogs);
        user_id= UserAccount.getInstance().getUser().user_id;
        initUI();
    }

    private void initUI() {
        QueryManager qm=new QueryManager(ma);
        qm.execute("getMyLikesBlogs","user_id",user_id);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void goingOn(ArrayList<String> arrayList) {
        String methodName = arrayList.get(0);
        try{
            switch (methodName) {
                case "getMyLikesBlogs":
                    if (arrayList.size() > 1) {
                        String rs = arrayList.get(1);
                        models = ResultParser.parseHotBlogs(rs);
                        InitAdapter initAdapter = new InitAdapter(this, models);
                        mylikes.setAdapter(initAdapter);
                    }
            }
        }catch (Exception e){}

    }
}
