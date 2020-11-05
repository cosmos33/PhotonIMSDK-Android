package com.momo.demo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.cosmos.photonim.imbase.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ActivityRecorder implements Application.ActivityLifecycleCallbacks {
    private Map<Integer, Stack<Activity>> activityStacks;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activityStacks == null) {
            activityStacks = new HashMap<>();
        }
        int taskId = activity.getTaskId();
        Stack<Activity> activities = activityStacks.get(taskId);
        if (activities == null) {
            activities = new Stack<>();
            activityStacks.put(taskId, activities);
        }
        activities.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        int taskId = activity.getTaskId();
        Stack<Activity> activities = activityStacks.get(taskId);
        if (activities == null) {
            LogUtils.log("activityStacks pop failed");
        } else {
            activities.pop();
        }
    }
}
