package com.example.a20897.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.ArrayList;

public class MainActivity extends MyActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ImageView me_icon;
    private TextView textView;
    private MyActivity ma;
    @Override
    protected void onResume(){
        super.onResume();
        boolean state = UserAccount.getInstance().getState();
        if(state)
        {
            textView.setText("Hello:  "+UserAccount.getInstance().getUser().user_name);

        }
        else
        {
            textView.setText("Welcome, please login");
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "MainActivity";
        ma = this;
        setContentView(R.layout.activity_main);
       btn1=findViewById(R.id.btn1);
        btn2=findViewById(R.id.btn2);
        btn3=findViewById(R.id.btn3);
        btn4=findViewById(R.id.btn4);
        me_icon=findViewById(R.id.me_icon);
        textView=findViewById(R.id.textView);
        btn1.setOnClickListener(mClickLisener1);
        btn2.setOnClickListener(mClickLisener2);
        btn3.setOnClickListener(mClickListener3);
        btn4.setOnClickListener(mClickLisener4);
        me_icon.setOnClickListener(mClickLisener5);

    }
    private View.OnClickListener mClickLisener1=(v)->{
        QueryManager qm = new QueryManager(ma);
        qm.execute("getManyBlogs");
    };
    private View.OnClickListener mClickLisener5=(v)->{
          me_icon.setImageResource(R.drawable.people_fill);
    };
    private View.OnClickListener mClickListener3= v -> {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),LoginActivityYu2.class);
        startActivityForResult(intent,102);
    };


    private View.OnClickListener mClickLisener2= v -> {
        boolean state=UserAccount.getInstance().getState();
        if(state){
            Intent intent = new Intent();
            intent.setClassName(getApplicationContext(),"com.example.a20897.myapplication.activities.WriteBlogActivity");
            startActivity(intent);
        }
    };
    private View.OnClickListener mClickLisener4= v -> {
        boolean state=UserAccount.getInstance().getState();
        if(state){
            Intent intent = new Intent();
            intent.setClassName(getApplicationContext(),"com.example.a20897.myapplication.activities.MyBlogsActivity");
            startActivity(intent);
        }
    };

    @Override
    public void goingOn(ArrayList<String> arrayList) {
        String methodName = arrayList.get(0);
        try{
            switch(methodName){
                case "getManyBlogs":
                    if(arrayList.size()>1){
                        String rs=arrayList.get(1);
                        ArrayList<BlogModel> models = ResultParser.parseHotBlogs(rs);

                        System.out.println(models);
                    }
        }
        }catch (Exception e){}
    }
}
