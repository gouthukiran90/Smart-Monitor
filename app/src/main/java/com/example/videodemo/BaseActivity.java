package com.example.videodemo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class BaseActivity extends AppCompatActivity {



    protected SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MyPrefs", 0);
    }

    public void showSnackBar(String msg, int color,View mCoordinatorLayout) {
        try {
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, msg, Snackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, color));
            snackbar.show();
        } catch (Exception e) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
//
    }

//    private static boolean isForeground(Context context) {
//        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
//        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//        return componentInfo.getPackageName().equals(Constants.ApplicationPackage);
//    }
}
