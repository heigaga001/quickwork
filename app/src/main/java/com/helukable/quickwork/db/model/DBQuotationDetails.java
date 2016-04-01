package com.helukable.quickwork.db.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;

/**
 * Created by zouyong on 2016/3/29.
 */
public class DBQuotationDetails extends DBModel{

    public static final int QUOTATIONDETAIL = 1;

    public static final int QUOTATIONDETAIL_ID = 2;

    public static final int QUOTATIONDETAIL_MATERIEL_STOCKS_ID = 3;

    private static final String TABLE_NAME = "quotationdetail";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_BASE_URI, TABLE_NAME);

    private static DBQuotationDetails instance;

    public static DBQuotationDetails getInstance() {
        if (instance == null) {
            instance = new DBQuotationDetails();
        }
        return instance;
    }

    public static String getTable() {
        return TABLE_NAME;
    }

    public interface Columns {
        String ID = "_id";
        String QUOTATIONID = "quotationid";
        String MATERIELID = "materielid";
        String NUM = "num";
    }
    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID +" INTEGER PRIMARY KEY, "
                + Columns.QUOTATIONID +" INTEGER, "
                + Columns.NUM +" INTEGER, "
                + Columns.MATERIELID +" INTEGER);"
                ;
        return sql;
    }

    @Override
    public String getTable(int code) {
        switch (code) {
            case QUOTATIONDETAIL:
            case QUOTATIONDETAIL_ID:
                return TABLE_NAME;
            case QUOTATIONDETAIL_MATERIEL_STOCKS_ID:
                return getJoinTable(TABLE_NAME, Columns.MATERIELID, DBMateriel.getTable(),
                        DBMateriel.Columns.ID,DBStock.getTable(),DBStock.Columns.ID);
            default:
                return TABLE_NAME;
        }
    }

    public static String getColumn(String colum) {
        return getTable() + "." + colum;
    }

    public static String[] getColumns(int code) {
        switch (code) {
            case QUOTATIONDETAIL:
            case QUOTATIONDETAIL_ID:
                return new String[] { "*" };
            case QUOTATIONDETAIL_MATERIEL_STOCKS_ID:
                String[] colums = new String[] { getColumn(Columns.ID), getColumn(Columns.MATERIELID),getColumn(Columns.NUM)};
                return concat(concat(colums, DBMateriel.getColumns()),DBStock.getColumns());
            default:
                return null;
        }
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[] {
                new DBContract.PathInfo(TABLE_NAME, QUOTATIONDETAIL),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, QUOTATIONDETAIL_ID),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_STORK,
                        QUOTATIONDETAIL_MATERIEL_STOCKS_ID)};
    }

    @Override
    public Uri getUri(int code, int id) {
        switch (code) {
            case QUOTATIONDETAIL:
                return CONTENT_URI;
            case QUOTATIONDETAIL_ID:
                return ContentUris.withAppendedId(CONTENT_URI, id);
            case QUOTATIONDETAIL_MATERIEL_STOCKS_ID:
                return Uri.withAppendedPath(CONTENT_URI, CODE_STORK);
            default:
                return null;
        }
    }

    @Override
    public void notifyChange(Context context, Uri uri, int code) {
        notifyUri(context, uri, false);
        if(code == QUOTATIONDETAIL_MATERIEL_STOCKS_ID){
            notifyUri(context,getUri(QUOTATIONDETAIL,0),false);
        }
    }

    @Override
    public SelectionBuilder buildSelection(Uri uri, int code) {
        SelectionBuilder builder = new SelectionBuilder();
        switch (code) {
            case QUOTATIONDETAIL:
                return builder.table(getTable(code));
            case QUOTATIONDETAIL_ID:
                return builder.table(getTable(code)).where(BaseColumns._ID + "=?",
                        String.valueOf(ContentUris.parseId(uri)));
            case QUOTATIONDETAIL_MATERIEL_STOCKS_ID:
                return builder.table(getTable(code));
            default:
                throw new UnsupportedOperationException("Unknown uri code: " + code);
        }
    }

    public void insert(Context context,int quotationId,int marterielId,int num){
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.QUOTATIONID, quotationId);
        values.put(Columns.MATERIELID, marterielId);
        values.put(Columns.NUM, num);
        resolver.insert(getUri(QUOTATIONDETAIL,0),values);
    }
    public void updateMarteriel(Context context,int id,int marterielId){
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.MATERIELID, marterielId);
        resolver.update(getUri(QUOTATIONDETAIL,0),values,Columns.ID+" = ? ",new String[]{String.valueOf(id)});
    }
    public void updateNum(Context context,int id,int num){
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.NUM, num);
        resolver.update(getUri(QUOTATIONDETAIL,0),values,Columns.ID+" = ? ",new String[]{String.valueOf(id)});
    }

    public void delete(Context context,int id){
        context.getContentResolver().delete(getUri(QUOTATIONDETAIL,0),Columns.ID+" = ? ",new String[]{String.valueOf(id)});
    }
}
