package com.example.a20897.myapplication;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/26.
 */

public abstract class MyActivity extends Activity {
    protected String name;
    public abstract void goingOn(ArrayList<String> arrayList) ;
    public String getName()
    {
        return name;
    }

    public void showToast(String toast){
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

}
