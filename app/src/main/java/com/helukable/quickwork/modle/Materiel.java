package com.helukable.quickwork.modle;

import android.database.Cursor;
import android.text.TextUtils;

import com.helukable.quickwork.db.model.DBMateriel;
import com.helukable.quickwork.db.model.DBQuotationDetails;
import com.helukable.quickwork.db.model.DBStock;
import com.helukable.quickwork.util.Helper;

import java.math.BigDecimal;

import jxl.Sheet;

/**
 * Created by zouyong on 2016/3/24.
 */
public class Materiel {
    private int id;
    private String type;
    private String size;
    private String tgr;
    private float price;
    private int per;
    private String unit;
    private float weight;
    private float copperWeight;
    private float copperBasis;
    private float copperMkWeight;
    private float copperMkBasis;
    private float niWeight;
    private float niBasis;
    private float alWeight;
    private float alBasis;
    private String messingBrass;
    private float priceInEur;
    private float freightSer;
    private float freightSky;
    private int lanpuId;
    private String lappType;
    private int stock;
    private int stockImport;
    private int num;
    private int startPage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanpuId() {
        return lanpuId;
    }

    public void setLanpuId(int lanpuId) {
        this.lanpuId = lanpuId;
    }

    public String getLappType() {
        return lappType;
    }

    public void setLappType(String lappType) {
        this.lappType = lappType;
    }

    public float getFreightSer() {
        return freightSer;
    }

    public void setFreightSer(float freightSer) {
        this.freightSer = freightSer;
    }

    public float getFreightSky() {
        return freightSky;
    }

    public void setFreightSky(float freightSky) {
        this.freightSky = freightSky;
    }

    public float getPriceInEur() {
        return priceInEur;
    }

    public void setPriceInEur(float priceInEur) {
        this.priceInEur = priceInEur;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTgr() {
        return tgr;
    }

    public void setTgr(String tgr) {
        this.tgr = tgr;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getCopperWeight() {
        return copperWeight;
    }

    public void setCopperWeight(float copperWeight) {
        this.copperWeight = copperWeight;
    }

    public float getCopperBasis() {
        return copperBasis;
    }

    public void setCopperBasis(float copperBasis) {
        this.copperBasis = copperBasis;
    }

    public float getCopperMkWeight() {
        return copperMkWeight;
    }

    public void setCopperMkWeight(float copperMkWeight) {
        this.copperMkWeight = copperMkWeight;
    }

    public float getCopperMkBasis() {
        return copperMkBasis;
    }

    public void setCopperMkBasis(float copperMkBasis) {
        this.copperMkBasis = copperMkBasis;
    }

    public float getNiWeight() {
        return niWeight;
    }

    public void setNiWeight(float niWeight) {
        this.niWeight = niWeight;
    }

    public float getNiBasis() {
        return niBasis;
    }

    public void setNiBasis(float niBasis) {
        this.niBasis = niBasis;
    }

    public float getAlWeight() {
        return alWeight;
    }

    public void setAlWeight(float alWeight) {
        this.alWeight = alWeight;
    }

    public float getAlBasis() {
        return alBasis;
    }

    public void setAlBasis(float alBasis) {
        this.alBasis = alBasis;
    }

    public String getMessingBrass() {
        return messingBrass;
    }

    public void setMessingBrass(String messingBrass) {
        this.messingBrass = messingBrass;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getStockImport() {
        return stockImport;
    }

    public void setStockImport(int stockImport) {
        this.stockImport = stockImport;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public Materiel initPrice(float coefficient, Quotation variable) {
        float p = (variable.getDelCuValue() - copperBasis) * copperWeight / 1000 +
                (variable.getNiValue() - niBasis) * niWeight / 1000 +
                (variable.getAluValue() - alBasis) * alWeight / 1000 +
                (variable.getCopperMk() - copperMkBasis) * copperMkWeight / 1000 + price;
        BigDecimal b = new BigDecimal(p);
        priceInEur = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        b = new BigDecimal(priceInEur * coefficient / 100);
        freightSer = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        b = new BigDecimal(weight * coefficient / 1000 + freightSer);
        freightSky = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return this;
    }

    public static Materiel creatBySheet(Sheet sheet, int row) {
        Materiel materiel = new Materiel();
        int id = Helper.getCellInt(sheet, row, 0);
        if (id == 0) {
            return null;
        }
        materiel.setId(id);
        materiel.setType(Helper.getCellString(sheet, row, 1));
        materiel.setSize(Helper.getCellString(sheet, row, 2));
        materiel.setTgr(Helper.getCellString(sheet, row, 3));
        materiel.setPrice(Helper.getCellFloat(sheet, row, 4));
        materiel.setPer(Helper.getCellInt(sheet, row, 5));
        materiel.setUnit(Helper.getCellString(sheet, row, 6));
        materiel.setWeight(Helper.getCellFloat(sheet, row, 8));
        materiel.setCopperWeight(Helper.getCellFloat(sheet, row, 11));
        materiel.setCopperBasis(Helper.getCellFloat(sheet, row, 12));
        materiel.setCopperMkWeight(Helper.getCellFloat(sheet, row, 13));
        materiel.setCopperMkBasis(Helper.getCellFloat(sheet, row, 14));
        materiel.setNiWeight(Helper.getCellFloat(sheet, row, 15));
        materiel.setNiBasis(Helper.getCellFloat(sheet, row, 16));
        materiel.setAlWeight(Helper.getCellFloat(sheet, row, 17));
        materiel.setAlBasis(Helper.getCellFloat(sheet, row, 18));
        materiel.setMessingBrass(Helper.getCellString(sheet, row, 19));
        materiel.setLanpuId(Helper.getCellInt(sheet, row, 20));
        materiel.setLappType(Helper.getCellString(sheet, row, 21));
        materiel.setStartPage(Helper.getCellInt(sheet,row,22));
        return materiel;
    }

    public static Materiel createByCursor(Cursor cursor){
        Materiel materiel = null;
        if(cursor!=null){
            materiel = new Materiel();
            materiel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.MATERIELID))));
            materiel.setPer(cursor.getInt(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.PER))));
            materiel.setType(cursor.getString(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.TYPE))));
            materiel.setSize(cursor.getString(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.SIZE))));
            materiel.setTgr(cursor.getString(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.TGR))));
            materiel.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.UNIT))));
            materiel.setMessingBrass(cursor.getString(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.MESSING_BRASS))));
            materiel.setPrice(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.PRICE))));
            materiel.setWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.WEIGHT))));
            materiel.setCopperWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.COPPER_WEIGHT))));
            materiel.setCopperBasis(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.COPPER_BASIS))));
            materiel.setCopperMkWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.COPPER_MK_WEIGHT))));
            materiel.setCopperMkBasis(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.COPPER_MK_BASIS))));
            materiel.setNiWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.NI_WEIGHT))));
            materiel.setNiBasis(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.NI_BASIS))));
            materiel.setAlWeight(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.AL_WEIGHT))));
            materiel.setAlBasis(cursor.getFloat(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.AL_BASIS))));
            materiel.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DBStock.getColumn(DBStock.Columns.STOCK))));
            materiel.setStockImport(cursor.getInt(cursor.getColumnIndexOrThrow(DBStock.getColumn(DBStock.Columns.STOCKIMPORT))));
            materiel.setNum(cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotationDetails.getColumn(DBQuotationDetails.Columns.NUM))));
            materiel.setStartPage(cursor.getInt(cursor.getColumnIndexOrThrow(DBMateriel.getColumn(DBMateriel.Columns.STARTPAGE))));
        }
        return materiel;
    }

}
