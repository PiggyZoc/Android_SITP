package com.example.a20897.myapplication.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.view.View;

import com.example.a20897.myapplication.CircleImageView;
import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.UriUtils;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.BitmapManager;
import com.example.a20897.myapplication.models.UserModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static com.example.a20897.myapplication.activities.WriteBlogActivity.REQUEST_CODE_LOCAL;

public class UserDetailActivity extends MyActivity {

    private UserModel um;

    private View mProgressView;
    private View user_detail_layout;
    private CircleImageView user_photo_view;
    private View set_photo_view;

    private Bitmap user_photo;

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int SMALL_CAPTURE = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        mProgressView = findViewById(R.id.user_detail_progress);
        user_detail_layout = findViewById(R.id.user_detail_layout);
        user_photo_view = (CircleImageView) findViewById(R.id.user_detail_photo);
        set_photo_view = findViewById(R.id.user_detail_edit_photo);

        set_photo_view.setOnClickListener(set_user_photo_listner);
        user_photo_view.setOnClickListener(set_user_photo_listner);

        um = UserAccount.getInstance().getUser();

        QueryManager qm = new QueryManager(this);
        qm.execute("getAvatarById",
                "user_id",um.user_id);
        showProgress(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        um = UserAccount.getInstance().getUser();
        showToast(um.user_id);
    }


    private View.OnClickListener set_user_photo_listner= v -> {
        if(um.user_id != null){
            if(isReadPermitted()){
                selectPicFromLocal();
            }
        }
        else {
            showToast("获取用户信息错误");
        }
    };
    private boolean isReadPermitted() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPicFromLocal();
            }
            else {
                showToast("没有读取文件权限");
            }
        }
    }

    protected void selectPicFromLocal() {Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_LOCAL);


//        Intent intent =  new Intent();
//        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, REQUEST_CODE_LOCAL);
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

            user_detail_layout.setVisibility(show ? View.GONE : View.VISIBLE);
            user_detail_layout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    user_detail_layout.setVisibility(show ? View.GONE : View.VISIBLE);
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
            user_detail_layout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setUser_photo_view(){
        if (user_photo == null)
            return;
        user_photo_view.setImageBitmap(user_photo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SMALL_CAPTURE && resultCode == RESULT_OK) {
//            String result;
//            Uri contentURI = data.getData();
//            Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//            if (cursor == null) {
//                // Source is Dropbox or other similar local file path
//                result = contentURI.getPath();
//            } else {
//                cursor.moveToFirst();
//                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//                result = cursor.getString(idx);
//                cursor.close();
//            }
//
//            Bitmap bitmap = BitmapFactory.decodeFile(result);
////                File file =  new File(result);
////
////                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider",file);
////                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
//            user_photo_view.setImageBitmap(bitmap);
//
//        }

        if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
            if (data != null) {
                Uri selectedImage = data.getData();
                String imageurl = UriUtils.getImageAbsolutePath(this, selectedImage);
                //System.out.println("=================="+imageurl);
                Uri imgUriSel = data.getData();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //相册会返回一个由相册安全策略定义的Uri，app使用这个Uri直接放入裁剪程序会不识别，抛出[暂不支持此类型：华为7.0]
                    //formatUri会返回根据Uri解析出的真实路径
                    imageurl = UriUtils.formatUri(this, selectedImage);
                    //根据真实路径转成File,然后通过应用程序重新安全化，再放入裁剪程序中才可以识别
//                    imgUriSel = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(imageurl));
                }

                //System.out.println(imageurl);
                String[] Names=imageurl.split("/");
                final String imageName=Names[Names.length-1];
                Bitmap bitmap = BitmapFactory.decodeFile(imageurl);
                user_photo = bitmap;
//                setUser_photo_view();
                String base64 = BitmapManager.encode(user_photo);

                QueryManager qm = new QueryManager(this);
                qm.execute("insertAvatarById",
                        "user_id",um.user_id,
                        "base64string",base64,
                        "file_name",imageName);
                showProgress(true);

            }
        }

    }

    @Override
    public void goingOn(ArrayList<String> arrayList) {
        showProgress(false);
        String methodName = arrayList.get(0);
        Intent intent = new Intent();
        boolean have_error = false;
        try {
            switch (methodName) {
                case "insertAvatarById":
                    have_error = true;
                    if (arrayList.size() > 1) {
                        String rs = arrayList.get(1);
                        if (rs.equals("true")) {
                            setUser_photo_view();
                            have_error = false;
                        }
                    }
                    if (have_error) {
                        showToast("上传失败");
                        return;
                    }
                    //
                    return;
                case "getAvatarById":
                    if (arrayList.size() <= 1) {
                        showToast("server error:getAvatarById-size");
                        return;
                    }
                    String rs = arrayList.get(1);
                    if (rs.isEmpty()) {
                        showToast("server error:getAvatarById-empty");
                        return;
                    }
                    Bitmap bitmapTmp = BitmapManager.decode(rs);
                    if (bitmapTmp != null){
                        user_photo = bitmapTmp;
                        setUser_photo_view();
                    }
                    //
                    return;
                case "Error":

//                    UserAccount.getInstance().setUser(um);
//                    intent.setClassName(getApplicationContext(),"com.example.a20897.myapplication.MainActivity");
//                    startActivity(intent);
//                    finish();
                    //
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
