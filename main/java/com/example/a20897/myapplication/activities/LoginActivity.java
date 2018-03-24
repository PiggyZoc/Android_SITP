package com.example.a20897.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.ResultParser;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.UserModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/27.
 */

public class LoginActivity extends MyActivity {
    private EditText txtId;
    private EditText txtPsw;
    private Button btn;
    private MyActivity ma;
    private UserModel um;
    @Override
    public void goingOn(ArrayList<String> arrayList) {
        String methodName = arrayList.get(0);
        Intent intent = new Intent();
        try {
            switch (methodName) {
                case "UserLogin":
                    String rs = arrayList.get(1);
                    if (!rs.isEmpty()) {
                        um=ResultParser.parseUser(rs);
                    } else {
                        um = null;
                        return;
                    }
                    UserAccount.getInstance().setUser(um);
                    //  Fan Hui Main
                    intent.putExtra("ShowToast", true);
                    setResult(201, intent);
                    finish();
                    //
                    return;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        name = "ActivityLogin";
        setContentView(R.layout.login_layout);
        ma = this;
        ActivityManager.getActivityManager().addActivity(this);
        btn=findViewById(R.id.btnlogin);
        txtId=findViewById(R.id.idlogin);
        txtPsw=findViewById(R.id.pswlogin);
        btn.setOnClickListener(mOnClickListener);

    }
    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String idLogin=txtId.getText().toString();
            String pswLogin=txtPsw.getText().toString();
            QueryManager qm=new QueryManager(ma);

            qm.execute("UserLogin","User_id",idLogin,"password",pswLogin);
            return;
        }
    };
}
