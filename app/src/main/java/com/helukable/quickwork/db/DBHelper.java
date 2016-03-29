package com.helukable.quickwork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	
	public DBHelper(Context context) {
		super(context,"quickwork.db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    for (int i = 0; i < DBContract.mModels.length; i++) {
			System.out.println(""+DBContract.mModels[i].getCreateTableSql());
	        db.execSQL(DBContract.mModels[i].getCreateTableSql());
        }
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAll(db);
        onCreate(db);
	}

    private void dropAll(SQLiteDatabase db) {
        for (int i = 0; i < DBContract.mModels.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.mModels[i].getTable(-1));
        }
    }
}
