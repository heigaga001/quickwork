package com.helukable.quickwork.dragger.component;

import android.app.Application;

import com.helukable.quickwork.QuickWorkApplication;
import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBMateriel;
import com.helukable.quickwork.dragger.module.QuickWorkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zouyong on 2016/3/23.
 */
/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = QuickWorkModule.class)
public interface QuickWorkComponent {

    void inject(QuickWorkApplication application);

    Application application();

    DBMateriel dbMateriel();

    DBCompany dbCompany();

    DBCustomer dbCustomer();


}
