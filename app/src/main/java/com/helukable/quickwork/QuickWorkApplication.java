package com.helukable.quickwork;

import android.app.Application;

import com.helukable.quickwork.dragger.component.DaggerQuickWorkComponent;
import com.helukable.quickwork.dragger.component.QuickWorkComponent;
import com.helukable.quickwork.dragger.module.QuickWorkModule;

/**
 * Created by zouyong on 2016/3/22.
 */
public class QuickWorkApplication extends Application{
    private QuickWorkComponent quickWorkComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        quickWorkComponent = DaggerQuickWorkComponent.builder().quickWorkModule(new QuickWorkModule(this)).build();
    }

    public QuickWorkComponent getQuickWorkComponent(){
        return quickWorkComponent;
    }

}
