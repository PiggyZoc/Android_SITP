package com.example.a20897.myapplication.activities;


import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.a20897.myapplication.MyActivity;
import com.example.a20897.myapplication.QueryManager;
import com.example.a20897.myapplication.R;
import com.example.a20897.myapplication.UserAccount;
import com.example.a20897.myapplication.models.UserModel;

import java.util.ArrayList;

/**
 * Created by 20897 on 2017/12/26.
 */

public class RegisterActivity extends MyActivity {
    private EditText txtId;
    private EditText txtPsw;
    private EditText txtUname;
    private EditText txtGender;
    private Button btn;
    private MyActivity ma;
    private UserModel um;

    @Override
    public void goingOn(ArrayList<String> arrayList) {

        ArrayList<String> result = arrayList;
        String methodName = result.get(0);
        Intent intent = new Intent();
        try {
            switch (methodName) {
                case "insertUserInfo":
                    String str = result.get(1);
                    if (!str.equals("false")) {
                        um.user_id = txtId.getText().toString();
                        um.password = txtPsw.getText().toString();
                        um.user_name = txtUname.getText().toString();
                        um.gender = txtGender.getText().toString();
                    } else {
                        um = null;
                        return;
                    }
                    UserAccount.getInstance().setUser(um);
                    //  Fan Hui Main
                    intent.putExtra("ShowToast", true);
                    setResult(301, intent);
                     finish();
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
        name = "ActivityRegister";
        setContentView(R.layout.register_layout);
        ma = this;
        btn = findViewById(R.id.btn2);
        txtId = findViewById(R.id.id);
        txtPsw = findViewById(R.id.psw);
        txtUname = findViewById(R.id.uname);
        txtGender = findViewById(R.id.gender);
        um=new UserModel();
        btn.setOnClickListener(mClickListenser);
    }

    private View.OnClickListener mClickListenser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String id = txtId.getText().toString();
            String password = txtPsw.getText().toString();
            String uname = txtUname.getText().toString();
            String gender = txtGender.getText().toString();
            QueryManager qm = new QueryManager(ma);
           // QueryManager qm1=new QueryManager(ma);

            qm.execute("insertUserInfo", "user_id", id, "password", password, "uname", uname, "sex", gender);
            //qm1.execute("createDirectory","")
            // ma.goingOn();

            //  ma.goingOn();
            return;
        }
    };
}
