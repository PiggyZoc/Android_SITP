package com.example.a20897.myapplication.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.adapter.InitAdapter;
import com.example.a20897.myapplication.models.BlogModel;
import com.example.a20897.myapplication.models.UserModel;

import java.util.ArrayList;

public class MainActivity extends MyActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ImageView me_icon;
    private TextView textView;
    private ListView hotblogs;
    ArrayList<BlogModel> models;
    private MyActivity ma;

    private SQLiteDatabase DB = null;
    @Override
    protected void onResume(){
        super.onResume();
        DBSetUp();
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
    protected void onStop(){
        super.onStop();
        DB.close();
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
        hotblogs=findViewById(R.id.hotblogs);
        models=new ArrayList<>();
        initUI();
        btn1.setOnClickListener(mClickLisener1);
        btn2.setOnClickListener(mClickLisener2);
        btn3.setOnClickListener(mClickListener3);
        btn4.setOnClickListener(mClickLisener4);
        me_icon.setOnClickListener(mClickLisener5);

    }

    private void initUI() {
        QueryManager qm = new QueryManager(ma);
        qm.execute("getManyBlogs");
    }

    private View.OnClickListener mClickLisener1=(v)->{
        QueryManager qm = new QueryManager(ma);
        qm.execute("getManyBlogs");
    };
    private View.OnClickListener mClickLisener5=(v)->{
        me_icon.setImageResource(R.drawable.people_fill);
        boolean state=UserAccount.getInstance().getState();
        if(state){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),UserDetailActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),LoginActivityYu2.class);
            startActivityForResult(intent,102);
        }
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
        else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),LoginActivityYu2.class);
            startActivityForResult(intent,102);
        }
    };
    private View.OnClickListener mClickLisener4= v -> {
        boolean state=UserAccount.getInstance().getState();
        if(state){
            Intent intent = new Intent();
            intent.setClassName(getApplicationContext(),"com.example.a20897.myapplication.activities.MyBlogsActivity");
            startActivity(intent);
        }
        else {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),LoginActivityYu2.class);
            startActivityForResult(intent,102);
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
                        models= ResultParser.parseHotBlogs(rs);
                        InitAdapter initAdapter=new InitAdapter(this,models);
                        hotblogs.setAdapter(initAdapter);
                    }
            }
        }catch (Exception e){}
    }
    private void DBSetUp() {
        DB = this.openOrCreateDatabase("USER",MODE_PRIVATE,null);

        boolean isTableExist=true;
        Cursor cursorTable=DB.rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='USER'", null);
        cursorTable.moveToFirst();
        if (cursorTable.getInt(0)==0) {
            isTableExist=false;
        }
        cursorTable.close();

        if(!isTableExist){
            createUserTable();
        }

        UserModel DBUser = null;
        UserModel currentUser = UserAccount.getInstance().getUser();
        Cursor cursor=DB.rawQuery("SELECT * FROM USER limit 1", null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            DBUser = new UserModel();
            DBUser.user_id=cursor.getString(0);
            DBUser.user_name=cursor.getString(1);
            DBUser.password=cursor.getString(2);
            DBUser.phone=cursor.getString(3);
            DBUser.email=cursor.getString(4);
        }
        cursor.close();

        if (DBUser != null && currentUser != null){
            DB.execSQL("DROP TABLE USER");
            createUserTable();
            DBUser = null;
        }
        if (DBUser == null && currentUser != null){
            String insertSql = "insert into user(user_id,user_name,password,phone,email) values(?,?,?,?,?)";
            DB.execSQL(insertSql, new String[] {
                    currentUser.user_id,
                    currentUser.user_name,
                    currentUser.password,
                    currentUser.phone,
                    currentUser.email });
        }
        if (DBUser != null && currentUser == null){
            UserAccount.getInstance().setUser(DBUser);
        }
    }

    private void UserLogout(){
        DB.execSQL("DROP TABLE USER");
        UserAccount.getInstance().logout();
        this.onResume();
    }

    private void createUserTable(){
        String createSql = "create table USER(" +
                "ID INTEGER primary key autoincrement," +
                "user_id text," +
                "user_name text," +
                "password text," +
                "phone text," +
                "email text)";
        DB.execSQL(createSql);
    }

}
