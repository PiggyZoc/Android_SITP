package com.example.a20897.myapplication.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.adapter.InitAdapter;
import com.example.a20897.myapplication.models.BlogModel;
import com.example.a20897.myapplication.models.UserModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends MyActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private ImageView me_icon;
    private TextView textView;
    private InitAdapter initAdapter;
    private Bitmap bitmap;
    ArrayList<BlogModel> models;
    private MyActivity ma;
   // private PullToRefreshListView pullToRefreshListView;
   private SwipeRefreshLayout swiper;
    private ListView mListView;
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
        //hotblogs=findViewById(R.id.hotblogs);
        //pullToRefreshListView = findViewById(R.id.pull_refresh_list);
        swiper = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        //为SwipeRefreshLayout设置监听事件
        swiper.setOnRefreshListener(this);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //初始化ListView
        mListView = findViewById(R.id.list_view);
        //setIamge();
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
//        swiper.setRefreshing(true);
    }
    private void setIamge()
    {
        String url = "http://wz66.top:86/UserDirectories/1234/1/NOTE20180116004003.png";
        OkHttpClient client = new OkHttpClient();
        //构造Request对象
        //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
        Request request = new Request.Builder().get().url(url).build();
        Call call = client.newCall(request);
        //异步调用并设置回调函数
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //final String responseStr = response.body().string();
                final InputStream inputStream = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                for(int i=0;i<models.size();i++)
                    models.get(i).Writer_Avatar_String = bitmap;
                MainActivity.this.runOnUiThread(() -> {
                    //Handle UI here
                    initAdapter.notifyDataSetChanged();
                   /// findViewById(R.id.loading).setVisibility(View.GONE);
                });

            }
        });

    }



    private View.OnClickListener mClickLisener1=(v)->{

        Intent intent=new Intent();
        intent.setClass(this,MyLikesActivity.class);
        startActivity(intent);
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
                        initAdapter =new InitAdapter(this,models);
                        mListView.setAdapter(initAdapter);
                        initAdapter.notifyDataSetChanged();
                        swiper.setRefreshing(false);
                        setIamge();
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
            DBUser.user_id=cursor.getString(1);
            DBUser.user_name=cursor.getString(2);
            DBUser.password=cursor.getString(3);
            DBUser.phone=cursor.getString(4);
            DBUser.email=cursor.getString(5);
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

    @Override
    public void onRefresh() {
        for (int i = 0 ; i < models.size(); i ++){
            models.get(i).recycle();
        }
        models = null;
        models = new ArrayList<BlogModel>();
        initUI();
//        QueryManager qm = new QueryManager(ma);
//        qm.execute("getManyBlogs");
//        swiper.setRefreshing(false);
    }
}
