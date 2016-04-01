package com.helukable.quickwork.db.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

import com.helukable.quickwork.db.DBContract;
import com.helukable.quickwork.db.SelectionBuilder;
import com.helukable.quickwork.modle.Materiel;

/**
 * Created by zouyong on 2016/3/22.
 */
public class DBMateriel extends DBModel{

    public static final int MATERIEL = 1;

    public static final int MATERIEL_ID = 2;

    private static final String TABLE_NAME = "materiel";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(
            CONTENT_BASE_URI, TABLE_NAME);

    private static DBMateriel instance;

    public static DBMateriel getInstance() {
        if(instance == null){
            instance = new DBMateriel();
        }
        return instance;
    }

    public static String getColumn(String colum) {
        return getTable() + "." + colum;
    }

    public static String[] getColumns() {
        return new String[]{getColumn(Columns.TYPE),getColumn(Columns.SIZE),getColumn(Columns.TGR),getColumn(Columns.PRICE),getColumn(Columns.PER),
                getColumn(Columns.UNIT),getColumn(Columns.WEIGHT),getColumn(Columns.COPPER_WEIGHT),getColumn(Columns.COPPER_BASIS),getColumn(Columns.COPPER_MK_WEIGHT),
                getColumn(Columns.COPPER_MK_BASIS),getColumn(Columns.NI_WEIGHT),getColumn(Columns.NI_BASIS),getColumn(Columns.AL_WEIGHT),getColumn(Columns.AL_BASIS),getColumn(Columns.MESSING_BRASS)};
    }

    public interface Columns {
        String ID = "_id";
        String TYPE = "type";
        String SIZE = "size";
        String TGR = "tgr";
        String PRICE = "price";
        String PER = "per";
        String UNIT = "unit";
        String WEIGHT = "weight";
        String COPPER_WEIGHT = "Copper_weight";
        String COPPER_BASIS  = "Copper_Basis";
        String COPPER_MK_WEIGHT  = "Copper_MK_weight";
        String COPPER_MK_BASIS  = "Copper_MK_Basis";
        String NI_WEIGHT = "Ni_weight";
        String NI_BASIS = "Ni_Basis";
        String AL_WEIGHT = "Al_weight";
        String AL_BASIS = "Al_Basis";
        String MESSING_BRASS = "Messing_Brass";
        String LANPUID = "lanpuid";
        String LANPUTYPE = "lanputype";

    }

    public static String getTable() {
        return TABLE_NAME;
    }

    @Override
    public String getCreateTableSql() {
        String sql = "CREATE TABLE " + getTable() + " ("
                + Columns.ID +" INTEGER PRIMARY KEY, "
                + Columns.LANPUID +" INTEGER, "
                + Columns.LANPUTYPE +" TEXT, "
                + Columns.TYPE +" TEXT, "
                + Columns.SIZE +" TEXT, "
                + Columns.TGR +" TEXT, "
                + Columns.PRICE +" FLOAT, "
                + Columns.PER +" INTEGER, "
                + Columns.UNIT +" TEXT, "
                + Columns.WEIGHT +" FLOAT, "
                + Columns.COPPER_WEIGHT +" FLOAT, "
                + Columns.COPPER_BASIS +" FLOAT, "
                + Columns.COPPER_MK_WEIGHT +" FLOAT, "
                + Columns.COPPER_MK_BASIS +" FLOAT, "
                + Columns.NI_WEIGHT +" FLOAT, "
                + Columns.NI_BASIS +" FLOAT, "
                + Columns.AL_WEIGHT +" FLOAT, "
                + Columns.AL_BASIS +" FLOAT, "
                + Columns.MESSING_BRASS +" TEXT);"
                ;
        return sql;
    }

    @Override
    public String getTable(int code) {
        return TABLE_NAME;
    }

    @Override
    public DBContract.PathInfo[] getPathInfo() {
        return new DBContract.PathInfo[]{
                new DBContract.PathInfo(TABLE_NAME, MATERIEL),
                new DBContract.PathInfo(TABLE_NAME + "/" + CODE_ID, MATERIEL_ID)};
    }

    @Override
    public Uri getUri(int code, int id) {
        switch (code) {
            case MATERIEL:
                return CONTENT_URI;
            case MATERIEL_ID:
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
            case MATERIEL:
                return builder.table(getTable(code));
            case MATERIEL_ID:
                return builder.table(getTable(code)).where(BaseColumns._ID + "=?",
                        String.valueOf(ContentUris.parseId(uri)));
            default:
                throw new UnsupportedOperationException("Unknown uri code: " + code);
        }
    }

    public void insertMateriel(Context context, Materiel materiel){
        if(materiel == null){
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Columns.ID,materiel.getId());
        values.put(Columns.TYPE,materiel.getType());
        values.put(Columns.SIZE,materiel.getSize());
        values.put(Columns.TGR,materiel.getTgr());
        values.put(Columns.PRICE,materiel.getPrice());
        values.put(Columns.PER,materiel.getPer());
        values.put(Columns.UNIT,materiel.getUnit());
        values.put(Columns.WEIGHT,materiel.getWeight());
        values.put(Columns.COPPER_WEIGHT,materiel.getCopperWeight());
        values.put(Columns.COPPER_BASIS,materiel.getCopperBasis());
        values.put(Columns.COPPER_MK_WEIGHT,materiel.getCopperMkWeight());
        values.put(Columns.COPPER_MK_BASIS,materiel.getCopperMkBasis());
        values.put(Columns.NI_WEIGHT,materiel.getNiWeight());
        values.put(Columns.NI_BASIS,materiel.getNiBasis());
        values.put(Columns.AL_WEIGHT,materiel.getAlWeight());
        values.put(Columns.AL_BASIS,materiel.getAlBasis());
        values.put(Columns.MESSING_BRASS,materiel.getMessingBrass());
        values.put(Columns.LANPUID,materiel.getLanpuId());
        values.put(Columns.LANPUTYPE,materiel.getLappType());
        resolver.insert(getUri(MATERIEL,0),values);
    }
    public void deleteData(Context context) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(getUri(MATERIEL, 0), "", null);
    }
}
