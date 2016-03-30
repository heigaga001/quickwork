package com.helukable.quickwork.db.model;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;

import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;

/**
 * Created by zouyong on 2016/3/29.
 */
public class DBQuotation extends DBModel{

    public static final int QUOTATION = 1;

    public static final int QUOTATION_ID = 2;

    public static final int QUOTATION_CUSTOMER_ID = 3;

    private static final String TABLE_NAME = "quotation";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_BASE_URI, TABLE_NAME);

    private static DBQuotation instance;

    public static DBQuotation getInstance() {
        if (instance == null) {
            instance = new DBQuotation();
        }
        return instance;
    }

    public static String getTable() {
        return TABLE_NAME;
    }

    public interface Columns {
        String ID = "_id";
        String COEFFICIENT = "coefficient";
        String DELCUVALUE = "delCuValue";
        String NIVALUE = "niValue";
        String ALUVALUE = "aluValue";
        String MESSINGBRASSFIX = "messingBrassfix";
        String COPPERMK = "copperMk";
        String CUSTOMERID = "customer";
        String COMPANYID = "company";
        String CREATEAT = "createAt";
    }

    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID +" INTEGER PRIMARY KEY, "
                + Columns.COEFFICIENT +" FLOAT, "
                + Columns.COMPANYID +" INTEGER, "
                + Columns.DELCUVALUE +" FLOAT, "
                + Columns.NIVALUE +" FLOAT, "
                + Columns.ALUVALUE +" FLOAT, "
                + Columns.MESSINGBRASSFIX +" FLOAT, "
                + Columns.COPPERMK +" FLOAT, "
                + Columns.CUSTOMERID +" TEXT, "
                + Columns.CREATEAT +" TEXT);"
                ;
        return sql;
    }

    @Override
    public String getTable(int code) {
        return TABLE_NAME;
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[] {
                new DBContract.PathInfo(TABLE_NAME, QUOTATION),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, QUOTATION_ID),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_COMPANY,
                        QUOTATION_CUSTOMER_ID) };
    }

    @Override
    public Uri getUri(int code, int id) {
        switch (code) {
            case QUOTATION:
                return CONTENT_URI;
            case QUOTATION_ID:
                return ContentUris.withAppendedId(CONTENT_URI, id);
            case QUOTATION_CUSTOMER_ID:
                return Uri.withAppendedPath(CONTENT_URI, CODE_CUSTORM_COMPANY);
            default:
                return null;
        }
    }

    @Override
    public void notifyChange(Context context, Uri uri, int code) {
        notifyUri(context, uri, false);
        if(code == QUOTATION_CUSTOMER_ID){
            notifyUri(context,getUri(QUOTATION,0),false);
        }
    }

    @Override
    public SelectionBuilder buildSelection(Uri uri, int code) {
        return null;
    }

    public void insert(){

    }
}
