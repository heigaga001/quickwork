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
 * Created by zouyong on 2016/3/28.
 */
public class DBStock extends DBModel {

    public static final int STOCK = 1;

    public static final int STOCK_ID = 2;

    private static final String TABLE_NAME = "stock";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_BASE_URI, TABLE_NAME);

    private static DBStock instance;

    public static DBStock getInstance() {
        if (instance == null) {
            instance = new DBStock();
        }
        return instance;
    }

    public static String getColumn(String colum) {
        return getTable() + "." + colum;
    }

    public static String[] getColumns() {
        return new String[]{getTable()+"."+Columns.STOCK};
    }

    public interface Columns {
        String ID = "_id";
        String STOCK = "stock";
    }

    public static String getTable() {
        return TABLE_NAME;
    }

    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID + " INTEGER PRIMARY KEY, "
                + Columns.STOCK + " INTEGER)";
        return sql;
    }

    @Override
    public String getTable(int code) {
        return TABLE_NAME;
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[]{
                new DBContract.PathInfo(TABLE_NAME, STOCK),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, STOCK_ID)};
    }

    @Override
    public Uri getUri(int code, int id) {
        switch (code) {
            case STOCK:
                return CONTENT_URI;
            case STOCK_ID:
                return ContentUris.withAppendedId(CONTENT_URI, id);
            default:
                return null;
        }
    }

    @Override
    public void notifyChange(Context context, Uri uri, int code) {
        notifyUri(context, uri, false);
    }

    @Override
    public SelectionBuilder buildSelection(Uri uri, int code) {
        SelectionBuilder builder = new SelectionBuilder();
        switch (code) {
            case STOCK:
                return builder.table(getTable(code));
            case STOCK_ID:
                return builder.table(getTable(code)).where(BaseColumns._ID + "=?",
                        String.valueOf(ContentUris.parseId(uri)));
            default:
                throw new UnsupportedOperationException("Unknown uri code: " + code);
        }
    }

    public void insertStock(Context context, int id, int stock) {
        if (id == 0) {
            return;
        }
        if (id > 10000000) {
            id = id - 10000000;
        }
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.ID, id);
        values.put(Columns.STOCK, stock);
        resolver.insert(getUri(STOCK, 0), values);
    }

    public void deleteData(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(getUri(STOCK, 0), "", null);
    }

}
