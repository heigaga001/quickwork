package com.helukable.quickwork.dragger.module;

/**
 * Created by zouyong on 2016/3/23.
 */

import android.app.Application;

import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBMateriel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class QuickWorkModule {
    private final Application application;

    public QuickWorkModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton Application application() {
        return application;
    }

    @Provides
    @Singleton
    DBMateriel provideDbMateriel(){
        return DBMateriel.getInstance();
    }

    @Provides
    @Singleton
    DBCompany provideDbCompany(){
        return new DBCompany();
    }

    @Provides
    @Singleton
    DBCustomer provideDbCustomer(){
        return new DBCustomer();
    }

}
