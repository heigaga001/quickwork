package com.helukable.quickwork.db.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;
import com.helukable.quickwork.modle.Customer;

/**
 * Created by zouyong on 2016/3/22.
 */
public class DBCustomer extends DBModel{

    public static final int CUSTOMER = 1;

    public static final int CUSTOMER_ID = 2;

    public static final int CUSTOMER_COMPANY_ID = 3;

    public static final int QUOTATION_CUSTOMER_ID = 4;

    private static final String TABLE_NAME = "customer";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_BASE_URI, TABLE_NAME);

    private static DBCustomer instance;

    public static DBCustomer getInstance() {
        if (instance == null) {
            instance = new DBCustomer();
        }
        return instance;
    }

    public interface Columns {
        String ID = "_id";
        String NAME = "cname";
        String COMPANYID = "companyid";
        String JOB = "job";
        String PHONE1 = "phone1";
        String PHONE2 = "phone2";
        String MOBILE_PHONE = "mobilephone";
        String EMAIL = "email";
        String FAX = "fax";
    }

    public static String getTable() {
        return TABLE_NAME;
    }
    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID + " TEXT UNIQUE, "
                + Columns.NAME +" TEXT,"
                + Columns.COMPANYID +" INTEGER,"
                + Columns.JOB +" TEXT,"
                + Columns.PHONE1 +" TEXT,"
                + Columns.PHONE2 +" TEXT,"
                + Columns.MOBILE_PHONE +" TEXT,"
                + Columns.EMAIL +" TEXT,"
                + Columns.FAX + " TEXT)";
        return sql;
    }

    @Override
    public String getTable(int code) {
        switch (code) {
            case CUSTOMER:
            case CUSTOMER_ID:
                return TABLE_NAME;
            case CUSTOMER_COMPANY_ID:
                return getJoinTable(TABLE_NAME, Columns.COMPANYID, DBCompany.getTable(),
                        DBCompany.Columns.ID);
            default:
                return TABLE_NAME;
        }
    }

    public static String getColumn(String colum) {
        return getTable() + "." + colum;
    }

    public static String[] getColumns(int code) {
        switch (code) {
            case CUSTOMER:
            case CUSTOMER_ID:
                return new String[] { "*" };
            case CUSTOMER_COMPANY_ID:
                String[] colums = new String[] { getColumn(Columns.ID), getColumn(Columns.NAME),getColumn(Columns.COMPANYID),
                        getColumn(Columns.JOB), getColumn(Columns.PHONE1),
                        getColumn(Columns.PHONE2), getColumn(Columns.MOBILE_PHONE),
                        getColumn(Columns.FAX),getColumn(Columns.EMAIL) };
                return concat(colums, DBCompany.getColumns());
            case QUOTATION_CUSTOMER_ID:
                return new String[]{getColumn(Columns.NAME),
                        getColumn(Columns.JOB), getColumn(Columns.PHONE1),
                        getColumn(Columns.PHONE2), getColumn(Columns.MOBILE_PHONE),
                        getColumn(Columns.FAX),getColumn(Columns.EMAIL)};
            default:
                return null;
        }
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[] {
                new DBContract.PathInfo(TABLE_NAME, CUSTOMER),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, CUSTOMER_ID),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_COMPANY,
                CUSTOMER_COMPANY_ID) };
    }

    @Override
    public Uri getUri(int code, int id) {
        switch (code) {
            case CUSTOMER:
                return CONTENT_URI;
            case CUSTOMER_ID:
                return ContentUris.withAppendedId(CONTENT_URI, id);
            case CUSTOMER_COMPANY_ID:
                return Uri.withAppendedPath(CONTENT_URI, CODE_COMPANY);
            default:
                return null;
        }
    }

    @Override
    public SelectionBuilder buildSelection(Uri uri, int code) {
        SelectionBuilder builder = new SelectionBuilder();
        switch (code) {
            case CUSTOMER:
                return builder.table(getTable(code));
            case CUSTOMER_ID:
                return builder.table(getTable(code)).where(BaseColumns._ID + "=?",
                        String.valueOf(ContentUris.parseId(uri)));
            case CUSTOMER_COMPANY_ID:
                return builder.table(getTable(code));
            default:
                throw new UnsupportedOperationException("Unknown uri code: " + code);
        }
    }

    @Override
    public void notifyChange(Context context, Uri uri, int code) {
        notifyUri(context, uri, false);
        if(code == CUSTOMER_COMPANY_ID){
            notifyUri(context,getUri(CUSTOMER,0),false);
        }
    }

    public void insertCustomer(Context context, Customer customer){
        if(customer == null){
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.ID,customer.getId());
        values.put(Columns.COMPANYID, customer.getCompany().getId());
        values.put(Columns.NAME,customer.getName());
        values.put(Columns.JOB,customer.getJob());
        values.put(Columns.PHONE1,customer.getPhone1());
        values.put(Columns.PHONE2,customer.getPhone2());
        values.put(Columns.MOBILE_PHONE,customer.getMobile_phone());
        values.put(Columns.FAX,customer.getFax());
        values.put(Columns.EMAIL,customer.getEmail());
        resolver.insert(getUri(CUSTOMER, 0), values);
    }
    public void deleteData(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(getUri(CUSTOMER, 0), "", null);
    }
}
