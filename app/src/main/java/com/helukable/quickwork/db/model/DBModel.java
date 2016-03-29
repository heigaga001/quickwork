package com.helukable.quickwork.db.model;

import android.content.Context;
import android.net.Uri;

import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;


public abstract class DBModel {
    
    public static final String CONTENT_HOST = "com.helukable.quickwork";
    public static final Uri CONTENT_BASE_URI = Uri.parse("content://"+CONTENT_HOST);
    
    protected static final String CODE_ID = "#";
    protected static final String CODE_COMPANY = "company";
    protected static final String CODE_STORK = "stock";
//    protected static final String CODE_AVATAR_GROUP = "avatar_group";
    
    public abstract String getCreateTableSql();
    
    public abstract String getTable(int code);
    
    public abstract DBContract.PathInfo[] getPathInfo();
    
    public abstract Uri getUri(int code, int id);
    
    public abstract void notifyChange(Context context, Uri uri, int code);
    
    protected void notifyUri(Context context, Uri uri, boolean syncToNetwork) {
        context.getContentResolver().notifyChange(uri, null, syncToNetwork);
    }
    
    protected String getJoinTable(String table1, String table1Colums, String table2, String table2Colums) {
        return String.format("%s left join %s on %s.%s = %s.%s", table1, table2, table1, table1Colums, table2, table2Colums);
    }
    
    protected static String[] concat(String[] a, String[] b) {
        String[] c= new String[a.length+b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public SelectionBuilder buildSelection(Uri uri, int code) {
        return null;
    }
}
