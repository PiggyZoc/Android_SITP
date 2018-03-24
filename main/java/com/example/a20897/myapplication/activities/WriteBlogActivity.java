package com.example.a20897.myapplication.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.a20897.myapplication.CurrentEditBlog;
import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.RicherEditor;
import com.example.a20897.myapplication.UriUtils;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.BlogModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 20897 on 2017/12/27.
 */


public class WriteBlogActivity extends MyActivity  {
    private MyActivity ma;
    private Button btn;
    private Button mBtn;
    private EditText txtTitle;
    private RicherEditor mEditText;
    private BlogModel bm;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    @Override
    public void goingOn(ArrayList<String> arrayList) {
        ArrayList<String> result = arrayList;
        String methodName = result.get(0);
        Intent intent = new Intent();
        try {
            switch (methodName) {
                case "insertBlogWithoutTitle":
                    String str = result.get(1);
                    if (!str.equals("false")) {
                        bm.blog_id=Integer.parseInt(str);
                        CurrentEditBlog.getInstance().setBlogModel(bm);
                        QueryManager queryManager=new QueryManager(new MyActivity() {
                            @Override
                            public void goingOn(ArrayList<String> arrayList) {

                            }
                        });
                        queryManager.execute("createDirectoryOfBlog","user_id",String.valueOf(UserAccount.getInstance().getUser().user_id),"blog_id",String.valueOf(bm.blog_id));
                       // System.out.println("部落格ID++++++++++++++"+blogId);
                        /*intent.putExtra("ShowToast", true);
                        setResult(301, intent);
                        finish();*/

                    } else {

                        return;
                    }

                    //  Fan Hui Main

                    //
                    return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "ActivityWriteBlog";
        setContentView(R.layout.wrireblog_layout);
        ma = this;
        ActivityManager.getActivityManager().addActivity(this);
        this.InsertBlogWithoutTitle();
        bm=new BlogModel();
        btn=findViewById(R.id.btnPublish);
        mBtn=findViewById(R.id.button_add_picture);
        txtTitle=findViewById(R.id.title);
        mEditText=findViewById(R.id.edit_text);
        btn.setOnClickListener(mListenser);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicFromLocal();//获取手机本地图片的代码，大家可以自行实现
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("EditActivity",mEditText.getmContentList().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void InsertBlogWithoutTitle(){
        String writer_id=UserAccount.getInstance().getUser().user_id;
        QueryManager qm=new QueryManager(ma);

        qm.execute("insertBlogWithoutTitle","writer_id",writer_id);
        return;
    }


    private void insertTitle(){
        String title=txtTitle.getText().toString();
        QueryManager qm=new QueryManager(new MyActivity() {
            @Override
            public void goingOn(ArrayList<String> arrayList){

            }
        });
        int blog_id=CurrentEditBlog.getInstance().getBlogModel().blog_id;
        String user_id=String.valueOf(UserAccount.getInstance().getUser().user_id);
        qm.execute("insertTitle","user_id",user_id,"blog_id",String.valueOf(blog_id),"title",title);
    }
    private View.OnClickListener mListenser= new OnClickListener() {
        @Override
        public void onClick(View v) {
            List<String> ls=  mEditText.getmContentList();
            String title=txtTitle.getText().toString();
            if(title.trim().isEmpty()) return;
            int blog_id=CurrentEditBlog.getInstance().getBlogModel().blog_id;
            for (int i=0;i<ls.size();i++){
                if(!ls.get(i).contains("/")) {

                    QueryManager qm = new QueryManager(new MyActivity() {
                        @Override
                        public void goingOn(ArrayList<String> arrayList) {

                        }
                    });
                    qm.execute("insertText", "blog_id", String.valueOf(blog_id), "pos", String.valueOf(i+1), "content", ls.get(i),"flag",String.valueOf(2));
                }
                else{
                    String[] arr=ls.get(i).split("/");
                    QueryManager qm = new QueryManager(new MyActivity() {
                        @Override
                        public void goingOn(ArrayList<String> arrayList) {

                        }
                    });
                    qm.execute("insertText", "blog_id", String.valueOf(blog_id), "pos", String.valueOf(i+1), "content", arr[arr.length-1],"flag",String.valueOf(1));

                }
            }
            insertTitle();



        }
    };

    protected void selectPicFromLocal() {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    String imageurl = UriUtils.getImageAbsolutePath(this, selectedImage);
                    //System.out.println("=================="+imageurl);


                    mEditText.insertBitmap(imageurl);

                }
            }

        }
    }




