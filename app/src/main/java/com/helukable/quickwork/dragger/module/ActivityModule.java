package com.helukable.quickwork.dragger.module;

import android.app.Activity;

import com.helukable.quickwork.dragger.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zouyong on 2016/3/24.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    Activity activity() {
        return activity;
    }
}
