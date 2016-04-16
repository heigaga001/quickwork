package com.helukable.quickwork.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

public class DBProvider extends ContentProvider {

    private static DBHelper mDBHelper = null;

    public static void openDatabase(Context context) {
        mDBHelper = new DBHelper(context);
    }

    public static void closeDatabase() {
        if (mDBHelper != null) {
            mDBHelper.close();
            mDBHelper = null;
        }
    }

    @Override
    public boolean onCreate() {
    	openDatabase(getContext());
    	return true;
    }

    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        if (mDBHelper != null) {
            for(String s:projection){
                System.out.println("========="+s);
            }

            DBContract.ModelInfo modelInfo = DBContract.getModelInfo(uri);
            if (modelInfo != null) {
                SelectionBuilder builder = modelInfo.model.buildSelection(uri, modelInfo.code);
                if (builder != null) {
                    final SQLiteDatabase db = mDBHelper.getReadableDatabase();
                    Cursor cursor = builder.where(selection, selectionArgs).query(db, projection, sortOrder);
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursor;
                }
                Cursor cursor = mDBHelper.getReadableDatabase().query(
                        modelInfo.model.getTable(modelInfo.code), projection,
                        selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),
                        uri);
                return cursor;
            }
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mDBHelper != null) {
            DBContract.ModelInfo modelInfo = DBContract.getModelInfo(uri);
            if (modelInfo != null) {
                long id = -1;
                try {
                    id = mDBHelper.getWritableDatabase().insertOrThrow(
                            modelInfo.model.getTable(modelInfo.code), null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                if (id != -1) {
                    modelInfo.model.notifyChange(getContext(), uri,
                            modelInfo.code);
                    return modelInfo.model.getUri(modelInfo.code, (int)id);
                }
            }
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (mDBHelper != null) {
            DBContract.ModelInfo modelInfo = DBContract.getModelInfo(uri);
            if (modelInfo != null) {
                SelectionBuilder builder = modelInfo.model.buildSelection(uri, modelInfo.code);
                if (builder != null) {
                    final SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    int retVal = builder.where(selection, selectionArgs).delete(db);
                    modelInfo.model.notifyChange(getContext(), uri, modelInfo.code);
                    return retVal;
                }
                int id = mDBHelper.getWritableDatabase().delete(
                        modelInfo.model.getTable(modelInfo.code), selection,
                        selectionArgs);
                modelInfo.model.notifyChange(getContext(), uri, modelInfo.code);
                return id;
            }
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        if (mDBHelper != null) {
            DBContract.ModelInfo modelInfo = DBContract.getModelInfo(uri);
            if (modelInfo != null) {
                SelectionBuilder builder = modelInfo.model.buildSelection(uri, modelInfo.code);
                if (builder != null) {
                    final SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    int retVal = builder.where(selection, selectionArgs).update(db, values);
                    modelInfo.model.notifyChange(getContext(), uri, modelInfo.code);
                    return retVal;
                }
                int id = mDBHelper.getWritableDatabase().update(
                        modelInfo.model.getTable(modelInfo.code), values,
                        selection, selectionArgs);
                modelInfo.model.notifyChange(getContext(), uri, modelInfo.code);
                return id;
            }
        }
        return 0;
    }
    
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (mDBHelper != null) {
            DBContract.ModelInfo modelInfo = DBContract.getModelInfo(uri);
            if (modelInfo != null) {
                SQLiteDatabase db = mDBHelper.getWritableDatabase();
                db.beginTransaction();
                for (int i = 0; i < values.length; i++) {
                    ContentValues value = values[i];
                    db.replace(modelInfo.model.getTable(modelInfo.code), null, value);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
                modelInfo.model.notifyChange(getContext(), uri,
                        modelInfo.code);
                return values.length;
            }
        }
        return 0;
    }
}
