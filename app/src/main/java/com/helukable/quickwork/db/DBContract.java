package com.helukable.quickwork.db;

import android.content.UriMatcher;
import android.net.Uri;

import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBMateriel;
import com.helukable.quickwork.db.model.DBModel;
import com.helukable.quickwork.db.model.DBQuotation;
import com.helukable.quickwork.db.model.DBQuotationDetails;
import com.helukable.quickwork.db.model.DBStock;


public class DBContract {
	
    public static DBModel[] mModels = new DBModel[] {
            DBMateriel.getInstance(),
            DBStock.getInstance(),
            DBCompany.getInstance(),
            DBCustomer.getInstance(),
            DBQuotation.getInstance(),
            DBQuotationDetails.getInstance()
    };
    
    private static UriMatcher sUriMatcher = buildMatcher();
    
    public static UriMatcher buildMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String host = DBModel.CONTENT_HOST;
        for (int i = 0; i < mModels.length; i++) {
            PathInfo[] paths = mModels[i].getPathInfo();
            for (PathInfo path : paths) {
                int code = i << 16 | path.code;
                matcher.addURI(host, path.path, code);
            }
        }
        return matcher;
    }
    
    public static ModelInfo getModelInfo(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match != UriMatcher.NO_MATCH) {
            int index = match >> 16 & 0xff;
            int code = match & 0xff;
            if (index >= 0 && index < mModels.length) {
                return new ModelInfo(mModels[index], code);
            }
        }
        return null;
    }
    
    public static class PathInfo {

        String path;
        int code;
        
        public PathInfo(String path, int code) {
            this.path = path;
            this.code = code;
        }
    }
    
    public static class ModelInfo {

        DBModel model;
        int code;
        
        public ModelInfo(DBModel model, int code) {
            this.model = model;
            this.code = code;
        }
    }
}
