package com.example.a20897.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.a20897.myapplication.activities.WriteBlogActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 20897 on 2017/12/28.
 */

public class RicherEditor extends android.support.v7.widget.AppCompatEditText {
    private final String TAG = "PATEditorView";
    private Context mContext;
    private WriteBlogActivity writeBlogActivity;



    private List<String> mContentList;

    public static final String mBitmapTag = "☆";
    private String mNewLineTag = "\n";

    public RicherEditor(Context context) {
        super(context);
        init(context);
    }

    public RicherEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RicherEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void attatchWriteBlogActivity(WriteBlogActivity activity){
        writeBlogActivity = activity;
    }

    private void init(Context context) {
        mContext = context;
        mContentList = getmContentList();
        insertData();
    }

    /**
     * 设置数据
     */
    private void insertData() {
        if (mContentList.size() > 0) {
            for (String str : mContentList) {
                if (str.contains(mBitmapTag)) {//判断是否是图片地址
                    String path = str.replace(mBitmapTag, "");//还原地址字符串
                    Bitmap bitmap = getSmallBitmap(path,480, 800);
                    bitmap.setWidth(100);
                    bitmap.setHeight(100);
                    //插入图片
                    insertBitmap(path, bitmap);
                } else {
                    //插入文字
                    SpannableString ss = new SpannableString(str);
                    append(ss);
                }
            }
        }
    }

    /**
     * 插入图片
     *
     * @param bitmap
     * @param path
     * @return
     */
    private SpannableString insertBitmap(String path, final Bitmap bitmap) {
        String[] Names=path.split("/");
        final String fileName=Names[Names.length-1];
        Editable edit_text = getEditableText();
        int index = getSelectionStart(); // 获取光标所在位置
        //插入换行符，使图片单独占一行
        ///System.out.println(index);
        SpannableString newLine = new SpannableString("\n");
        edit_text.insert(index, newLine);//插入图片前换行
        // 创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
        path = mBitmapTag + path + mBitmapTag;
        SpannableString spannableString = new SpannableString(path);
        // 根据Bitmap对象创建ImageSpan对象
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
        // 用ImageSpan对象替换你指定的字符串
        spannableString.setSpan(imageSpan, 0, path.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 将选择的图片追加到EditText中光标所在位置
        if (index < 0 || index >= edit_text.length()) {
            edit_text.append(spannableString);
        } else {

            edit_text.insert(index, spannableString);
            String blog_id=String.valueOf(CurrentEditBlog.getInstance().getBlogModel().blog_id);
            int pos=this.getmContentList().size();
            writeBlogActivity.executeQuery("saveImage",
                    "user_id",UserAccount.getInstance().getUser().user_id,
                    "blog_id",String.valueOf(CurrentEditBlog.getInstance().getBlogModel().blog_id),
                    "filename",fileName,
                    "base64string",encode(bitmap));

        }
        edit_text.insert(index, newLine);//插入图片后换行
        return spannableString;
    }


    /**
     * 插入图片
     *
     * @param path
     */
    public void insertBitmap(String path) {
        Bitmap bitmap = getSmallBitmap(path, 480,800 );
         insertBitmap(path, bitmap);
    }

    /**
     * 用集合的形式获取控件里的内容
     *
     * @return
     */
    public List<String> getmContentList() {
        if (mContentList == null) {
            mContentList = new ArrayList<String>();
        }
        String content = getText().toString().replaceAll(mNewLineTag, "");
        if (content.length() > 0 && content.contains(mBitmapTag)) {
            String[] split = content.split("☆");
            mContentList.clear();
            for (String str : split) {
                mContentList.add(str);
            }
        } else {
            mContentList.add(content);
        }

        return mContentList;
    }

    /**
     * 设置显示的内容集合
     *
     * @param contentList
     */
    public void setmContentList(List<String> contentList) {
        if (mContentList == null) {
            mContentList = new ArrayList<>();
        }
        this.mContentList.clear();
        this.mContentList.addAll(contentList);
        insertData();
    }

    float oldY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                requestFocus();
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                if (Math.abs(oldY - newY) > 20) {
                    clearFocus();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;



        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int w_width = w_screen;
        int b_width = bitmap.getWidth();
        int b_height = bitmap.getHeight();
        int w_height = w_width * b_height / b_width;
        bitmap = Bitmap.createScaledBitmap(bitmap, w_width, w_height, false);
        return bitmap;
    }


    private  Bitmap rotateBitmap(Bitmap bitmap, int rotate){
        if(bitmap == null)
            return null ;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    //计算图片的缩放值
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    private String encode(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        //base64 encode
        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        return new String(encode);
    }


}
