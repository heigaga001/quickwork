package com.helukable.quickwork.db.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Settings;

import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;
import com.helukable.quickwork.modle.Quotation;

/**
 * Created by zouyong on 2016/3/29.
 */
public class DBQuotation extends DBModel {

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
        String TIP = "tip";
        String TYPE = "type";
        String CREATEAT = "createAt";
    }

    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID + " INTEGER PRIMARY KEY, "
                + Columns.COEFFICIENT + " FLOAT, "
                + Columns.COMPANYID + " INTEGER, "
                + Columns.DELCUVALUE + " FLOAT, "
                + Columns.NIVALUE + " FLOAT, "
                + Columns.ALUVALUE + " FLOAT, "
                + Columns.MESSINGBRASSFIX + " FLOAT, "
                + Columns.COPPERMK + " FLOAT, "
                + Columns.CUSTOMERID + " TEXT, "
                + Columns.TIP + " TEXT, "
                + Columns.TYPE + " INTEGER, "
                + Columns.CREATEAT + " TEXT);";
        return sql;
    }

    public static String getColumn(String colum) {
        return getTable() + "." + colum;
    }

    public static String[] getColumns(int code) {
        switch (code) {
            case QUOTATION:
            case QUOTATION_ID:
                return new String[]{"*"};
            case QUOTATION_CUSTOMER_ID:
                String[] colums = new String[]{getColumn(Columns.ID), getColumn(Columns.ALUVALUE), getColumn(Columns.COMPANYID),
                        getColumn(Columns.CUSTOMERID), getColumn(Columns.COEFFICIENT), getColumn(Columns.COPPERMK),
                        getColumn(Columns.CREATEAT), getColumn(Columns.DELCUVALUE), getColumn(Columns.MESSINGBRASSFIX),
                        getColumn(Columns.NIVALUE), getColumn(Columns.TYPE), getColumn(Columns.TIP)};
                return concat(concat(colums, DBCustomer.getColumns(DBCustomer.QUOTATION_CUSTOMER_ID)), DBCompany.getColumns());
            default:
                return null;
        }
    }

    @Override
    public String getTable(int code) {
        switch (code) {
            case QUOTATION:
            case QUOTATION_ID:
                return TABLE_NAME;
            case QUOTATION_CUSTOMER_ID:
                return getJoinTable(TABLE_NAME, Columns.CUSTOMERID, Columns.COMPANYID, DBCustomer.getTable(), DBCustomer.Columns.ID, DBCompany.getTable(), DBCompany.Columns.ID);
        }
        return null;
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[]{
                new DBContract.PathInfo(TABLE_NAME, QUOTATION),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, QUOTATION_ID),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_CUSTORM_COMPANY,
                        QUOTATION_CUSTOMER_ID)};
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
        if (code == QUOTATION_CUSTOMER_ID) {
            notifyUri(context, getUri(QUOTATION, 0), false);
        }
    }

    @Override
    public SelectionBuilder buildSelection(Uri uri, int code) {
        SelectionBuilder builder = new SelectionBuilder();
        switch (code) {
            case QUOTATION:
                return builder.table(getTable(code));
            case QUOTATION_ID:
                return builder.table(getTable(code)).where(BaseColumns._ID + "=?",
                        String.valueOf(ContentUris.parseId(uri)));
            case QUOTATION_CUSTOMER_ID:
                return builder.table(getTable(code));
            default:
                throw new UnsupportedOperationException("Unknown uri code: " + code);
        }
    }

    public void insert(Context context, Quotation qutation) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.ALUVALUE, qutation.getAluValue());
        values.put(Columns.COEFFICIENT, qutation.getCoefficient());
        values.put(Columns.COMPANYID, qutation.getCompanyId());
        values.put(Columns.COPPERMK, qutation.getCopperMk());
        values.put(Columns.CUSTOMERID, qutation.getCustormerID());
        values.put(Columns.DELCUVALUE, qutation.getDelCuValue());
        values.put(Columns.MESSINGBRASSFIX, qutation.getMessingBrassfix());
        values.put(Columns.NIVALUE, qutation.getNiValue());
        values.put(Columns.TIP, qutation.getTip());
        values.put(Columns.TYPE, qutation.getType());
        values.put(Columns.CREATEAT, qutation.getCreateAt() == null ? "" + System.currentTimeMillis() : qutation.getCreateAt());
        if (qutation.getId() != 0) {
            resolver.update(getUri(QUOTATION, 0), values, Columns.ID + " = ? ", new String[]{String.valueOf(qutation.getId())});
            return;
        }
        Uri uri = resolver.insert(getUri(QUOTATION_ID, 0), values);
        if (uri != null) {
            int id = (int) ContentUris.parseId(uri);
            qutation.setId(id);
        }

    }
}

