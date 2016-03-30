package com.helukable.quickwork.db.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.helukable.quickwork.base.BaseActivity;
import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;
import com.helukable.quickwork.modle.Company;

/**
 * Created by zouyong on 2016/3/22.
 */
public class DBCompany extends DBModel{

    public static final int COMPANY = 1;

    public static final int COMPANY_ID = 2;

    private static final String TABLE_NAME = "company";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_BASE_URI, TABLE_NAME);

    private static DBCompany instance;

    public static DBCompany getInstance() {
        if (instance == null) {
            instance = new DBCompany();
        }
        return instance;
    }

    public static String getColumn(String colum) {
        return getTable() + "." + colum;
    }

    public static String[] getColumns() {
        return new String[]{getColumn(Columns.NAME),getColumn(Columns.TYPE),getColumn(Columns.CITY),getColumn(Columns.COUNTY),getColumn(Columns.ADDRESS),getColumn(Columns.REMARKS)};
    }


    public interface Columns {
        String ID = "_id";
        String NAME = "name";
        String TYPE = "type";
        String CITY = "city";
        String COUNTY = "county";
        String ADDRESS = "address";
        String REMARKS = "remarks";
    }

    public static String getTable() {
        return TABLE_NAME;
    }
    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID + " INTEGER PRIMARY KEY, "
                + Columns.NAME +" TEXT,"
                + Columns.TYPE +" TEXT,"
                + Columns.CITY +" TEXT,"
                + Columns.COUNTY +" TEXT,"
                + Columns.ADDRESS +" TEXT,"
                + Columns.REMARKS + " TEXT)";
        return sql;
    }

    @Override
    public String getTable(int code) {
        return TABLE_NAME;
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[]{
                new DBContract.PathInfo(TABLE_NAME, COMPANY),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, COMPANY_ID)};
    }

    @Override
    public Uri getUri(int code, int id) {
        switch (code) {
            case COMPANY:
                return CONTENT_URI;
            case COMPANY_ID:
                return ContentUris.withAppendedId(CONTENT_URI, id);
            default:
                return null;
        }
    }

    @Override
    public SelectionBuilder buildSelection(Uri uri, int code) {
        SelectionBuilder builder = new SelectionBuilder();
        switch (code) {
            case COMPANY:
                return builder.table(getTable(code));
            case COMPANY_ID:
                return builder.table(getTable(code)).where(BaseColumns._ID + "=?",
                        String.valueOf(ContentUris.parseId(uri)));
            default:
                throw new UnsupportedOperationException("Unknown uri code: " + code);
        }
    }

    @Override
    public void notifyChange(Context context, Uri uri, int code) {
        notifyUri(context, uri, false);
    }

    public void insertCompany(Context context, Company company) {
        if(company == null){
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.ID, company.getId());
        values.put(Columns.NAME, company.getName());
        values.put(Columns.TYPE, company.getType());
        values.put(Columns.CITY, company.getCity());
        values.put(Columns.COUNTY, company.getCounty());
        values.put(Columns.ADDRESS, company.getAddress());
        values.put(Columns.REMARKS, company.getRemarks());
        resolver.insert(getUri(COMPANY, 0), values);
    }

    public void deleteData(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(getUri(COMPANY, 0), "", null);
    }
}
