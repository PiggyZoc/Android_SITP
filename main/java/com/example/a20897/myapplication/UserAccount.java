package com.example.a20897.myapplication;

import com.example.a20897.myapplication.models.UserModel;

import java.util.Date;

/**
 * Created by 20897 on 2017/12/27.
 */

public class UserAccount {
    private static UserAccount instance;
    private static UserModel currentUser;
    private static boolean state;  // 0 - no 1 - yes
    private static Date loginTime;

    static public UserAccount getInstance()
    {
        if(instance == null)
        {
            instance = new UserAccount();
            state = false;
        }
        return  instance;
    }
    // Login
    public void setUser(UserModel user)
    {
        currentUser = user;
        state = true;
        loginTime = new Date();
        return;
    }

    public UserModel getUser()
    {
        return currentUser;
    }

    public boolean getState()
    {
        return state;
    }

    public void logout()
    {
        currentUser = null;
        state = false;
        return;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }
}
