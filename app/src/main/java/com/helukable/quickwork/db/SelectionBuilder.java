package com.helukable.quickwork.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;

public class SelectionBuilder {

	private String mTable = null;
	private StringBuilder mSelection = new StringBuilder();
    private ArrayList<String> mSelectionArgs = new ArrayList<String>();
    
    public SelectionBuilder reset() {
        mTable = null;
        mSelection.setLength(0);
        mSelectionArgs.clear();
        return this;
    }
    
    public SelectionBuilder where(String selection, String... selectionArgs) {
    	if (TextUtils.isEmpty(selection)) {
            if (selectionArgs != null && selectionArgs.length > 0) {
                throw new IllegalArgumentException(
                        "Valid selection required when including arguments=");
            }

            return this;
        }
    	
    	if (mSelection.length() > 0) {
            mSelection.append(" AND ");
        }
    	
    	mSelection.append("(").append(selection).append(")");
    	if (selectionArgs != null) {
            Collections.addAll(mSelectionArgs, selectionArgs);
        }
    	return this;
    }
    
    public SelectionBuilder table(String table) {
    	mTable = table;
    	return this;
    }
    
    private void assertTable() {
    	if (mTable == null) {
    		throw new IllegalStateException("Table not specified");
    	}
    }
    
    public String getSelection() {
    	return mSelection.toString();
    }
    
    public String[] getSelectionArgs() {
    	return mSelectionArgs.toArray(new String[mSelectionArgs.size()]);
    }
    
    public Cursor query(SQLiteDatabase db, String[] columns, String orderBy) {
    	return query(db, columns, null, null, orderBy, null);
    }
    
    public Cursor query(SQLiteDatabase db, String[] columns, String groupBy, String having, String orderBy, String limit) {
    	assertTable();
    	return db.query(mTable, columns, getSelection(), 
    			getSelectionArgs(), groupBy, having, orderBy, limit);
    }
    
    public int update(SQLiteDatabase db, ContentValues values) {
    	assertTable();
    	return db.update(mTable, values, getSelection(), getSelectionArgs());
    }
    
    public int delete(SQLiteDatabase db) {
    	assertTable();
    	return db.delete(mTable, getSelection(), getSelectionArgs());
    }
	
}
