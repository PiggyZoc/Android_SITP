package com.example.a20897.myapplication.activities;

import android.app.Activity;

import com.example.a20897.myapplication.MyActivity;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by 20897 on 2017/12/26.
 */

public class ActivityManager {
    private static Stack<MyActivity> activityStack;
    private static ActivityManager instance;
    private ActivityManager(){}

    public static ActivityManager getActivityManager()
    {
        if(instance == null)
        {
            instance = new ActivityManager();
        }
        return instance;
    }

    public void addActivity(MyActivity activity)
    {
        if(activityStack == null)
        {
            activityStack = new Stack<MyActivity>();
        }
        if(activity == null)
            return;
        activityStack.push(activity);
    }

    public Activity currentActivity()
    {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    public void finishActivity(Activity activity)
    {
        if(activity != null)
        {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    public void finishAllActivity()
    {
        for(Iterator<MyActivity> iterator = activityStack.iterator(); iterator.hasNext();)
        {
            Activity a = iterator.next();
            if(a != null)
                a.finish();
        }
        activityStack.clear();
    }

    public void popActivity()
    {
        if(activityStack.size() != 0)
            activityStack.pop();
        return;
    }
}
