package com.helukable.quickwork.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by zouyong on 2016/3/24.
 */
public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ProgressDialog progress;

    public void showProgress() {
        showProgress("加载中...");
    }

    public void showProgress(String msg) {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setCanceledOnTouchOutside(false);
        }
        progress.setMessage(msg);
        if (!progress.isShowing())
            progress.show();
    }
    public void dismissProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}
