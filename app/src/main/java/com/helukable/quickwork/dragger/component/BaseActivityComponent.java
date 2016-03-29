package com.helukable.quickwork.dragger.component;

import android.app.Activity;


import com.helukable.quickwork.dragger.module.ActivityModule;
import com.helukable.quickwork.dragger.scope.PerActivity;

import dagger.Component;

/**
 * Created by zouyong on 2016/3/24.
 */
@PerActivity
@Component(dependencies = QuickWorkComponent.class,modules = ActivityModule.class)
public interface BaseActivityComponent {
    Activity activity();
}
