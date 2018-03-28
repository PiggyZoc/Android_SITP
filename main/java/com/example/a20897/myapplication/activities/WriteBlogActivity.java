package com.example.a20897.myapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class WriteBlogActivity extends MyActivity {
    private MyActivity ma;
    private Button btn;
    private Button mBtn;
    private EditText txtTitle;
    private RicherEditor mEditText;
    private BlogModel bm;
    private Boolean isCreated;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    private View mProgressView;
    private View writeLayout;

    private int querySize = 0;
    private boolean createSuccess = true;

    @Override
    public void goingOn(ArrayList<String> arrayList) {
        ArrayList<String> result = arrayList;
        String methodName = result.get(0);
        Intent intent = new Intent();
        try {
            switch (methodName) {
                case "createDirectoryOfBlog":
                    showProgress(false);
                    return;
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
                        queryManager.execute("createDirectoryOfBlog",
                                "user_id",String.valueOf(UserAccount.getInstance().getUser().user_id),
                                "blog_id",String.valueOf(bm.blog_id));
                       // System.out.println("部落格ID++++++++++++++"+blogId);
                        /*intent.putExtra("ShowToast", true);
                        setResult(301, intent);
                        finish();*/

                    } else {

                        return;
                    }
                    return;

                    //  Fan Hui Main

                case "insertTitle":
                    str = result.get(1);
                    if (--querySize==0) {
                        if (str.equals("true") && createSuccess)
                            Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
                        this.finish();
                        showProgress(false);
                    }
                    if (str.equals("false")) {
                        Toast.makeText(this, "创建标题失败", Toast.LENGTH_SHORT).show();
                        createSuccess = false;
                    }
                    return;

                case "insertText":

                    str = result.get(1);
                    if (--querySize==0) {
                        if (str.equals("true") && createSuccess)
                            Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
                        this.finish();
                        showProgress(false);
                    }
                    if (str.equals("false")) {
                        Toast.makeText(this, "创建内容失败", Toast.LENGTH_SHORT).show();
                        createSuccess = false;
                    }
                    return;

                case "saveImage":

                    str = result.get(1);
                    if (str.equals("true")) {
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                    if (str.equals("false")) {
                        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                    showProgress(false);
                    return;

                default:
                    showProgress(false);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(this, "上传错误", Toast.LENGTH_SHORT).show();
            showProgress(false);
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = "ActivityWriteBlog";
        setContentView(R.layout.wrireblog_layout);
        ma = this;

        bm=new BlogModel();
        btn=findViewById(R.id.btnPublish);
        mBtn=findViewById(R.id.button_add_picture);
        txtTitle=findViewById(R.id.title);
        mEditText=findViewById(R.id.edit_text);
        mEditText.attatchWriteBlogActivity(this);
        mProgressView = findViewById(R.id.write_progress);
        writeLayout = findViewById(R.id.write_layout);
        btn.setOnClickListener(mListenser);
        isCreated=false;
        mBtn.setOnClickListener(v -> {
            if(!isCreated){
                this.InsertBlogWithoutTitle();
                isCreated=true;
            }
            selectPicFromLocal();//获取手机本地图片的代码，大家可以自行实现
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("EditActivity",mEditText.getmContentList().toString());
                if(!isCreated){
                    InsertBlogWithoutTitle();
                    isCreated=true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            writeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            writeLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    writeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            writeLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void executeQuery(String... para){
        QueryManager qm=new QueryManager(ma);
        qm.execute(para);
        showProgress(true);
    }

    private void InsertBlogWithoutTitle(){
        String writer_id=UserAccount.getInstance().getUser().user_id;
        QueryManager qm=new QueryManager(ma);

        qm.execute("insertBlogWithoutTitle","writer_id",writer_id);
        showProgress(true);
        return;
    }


    private void insertTitle(){
        String title=txtTitle.getText().toString();
        QueryManager qm=new QueryManager(ma);
        int blog_id=CurrentEditBlog.getInstance().getBlogModel().blog_id;
        String user_id=String.valueOf(UserAccount.getInstance().getUser().user_id);
        qm.execute("insertTitle","user_id",user_id,"blog_id",String.valueOf(blog_id),"title",title);
        showProgress(true);
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

                    QueryManager qm = new QueryManager(ma);
                    qm.execute("insertText", "blog_id", String.valueOf(blog_id), "pos", String.valueOf(i+1), "content", ls.get(i),"flag",String.valueOf(2));
                }
                else{
                    String[] arr=ls.get(i).split("/");
                    QueryManager qm = new QueryManager(ma);
                    qm.execute("insertText", "blog_id", String.valueOf(blog_id), "pos", String.valueOf(i+1), "content", arr[arr.length-1],"flag",String.valueOf(1));

                }
                querySize = ls.size()+1;
            }
            insertTitle();
            showProgress(true);


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




