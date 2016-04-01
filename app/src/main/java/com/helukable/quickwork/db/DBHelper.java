package com.helukable.quickwork.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBQuotationDetails;
import com.helukable.quickwork.db.model.DBStock;


public class DBHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 3;
	
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
		switch (oldVersion){
			case 1:
				if(newVersion ==2){
					db.beginTransaction();
					try{
						db.execSQL("ALTER TABLE " + DBQuotationDetails.getTable() + " ADD COLUMN " + DBQuotationDetails.Columns.NUM + " INTEGER DEFAULT 100");
                        db.execSQL("ALTER TABLE " + DBCustomer.getTable() + " ADD COLUMN " + DBCustomer.Columns.EMAIL + " TEXT");
					    db.setTransactionSuccessful();
                    }catch (Exception e){
                        e.printStackTrace();
					}finally {
						db.endTransaction();
					}
					break;
				}
            case 2:
                if(newVersion ==3){
                    db.beginTransaction();
                    try{
                        db.execSQL("ALTER TABLE " + DBStock.getTable() + " ADD COLUMN " + DBStock.Columns.STOCKIMPORT + " INTEGER DEFAULT 0");
                        db.setTransactionSuccessful();
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        db.endTransaction();
                    }
                    break;
                }

			default:
				dropAll(db);
				onCreate(db);
				break;
		}
	}

    private void dropAll(SQLiteDatabase db) {
        for (int i = 0; i < DBContract.mModels.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + DBContract.mModels[i].getTable(-1));
        }
    }
}
