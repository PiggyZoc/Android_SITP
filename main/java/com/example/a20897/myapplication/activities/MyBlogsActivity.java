package com.example.a20897.myapplication.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 20897 on 2017/12/30.
 */

public class MyBlogsActivity extends MyActivity {
    private MyActivity ma;
    private ListView listView;
    private List<Map<String, Object>> mData;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        name = "ActivityMyBlogs";
        setContentView(R.layout.myblogs_layout);
        ma = this;
        ActivityManager.getActivityManager().addActivity(this);
        listView= findViewById(R.id.lv);
        ShowList();

    }

    private void ShowList() {
        String user_id= UserAccount.getInstance().getUser().user_id;
        QueryManager qm=new QueryManager(ma);
        qm.execute("selectAllBlogsById","user_id",user_id);
    }



    @Override
    public void goingOn(ArrayList<String> arrayList) {
        String methodName = arrayList.get(0);

        try {
            switch (methodName) {
                case "selectAllBlogsById":
                    String rs = arrayList.get(1);
                    if (!rs.isEmpty()) {
                        ArrayList<BlogModel> bModel=ResultParser.parseBlogs(rs);
                        mData= new ArrayList<>();
                        for (int i=0;i<bModel.size();i++){
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("title", bModel.get(i).title);
                            map.put("info", bModel.get(i).blog_id);
                            mData.add(map);
                        }

                        ListAdapter adapter = new ListAdapter();
                        listView.setAdapter(adapter);//设置适配器
                    } else {
                       
                        return;
                    }
                   
                   // finish();
                    //
                    return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
