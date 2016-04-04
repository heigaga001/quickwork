package com.helukable.quickwork.modle;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.helukable.quickwork.db.model.DBCompany;
import com.helukable.quickwork.db.model.DBCustomer;
import com.helukable.quickwork.db.model.DBQuotation;

/**
 * Created by zouyong on 2016/3/30.
 */
public class Quotation implements Parcelable {

    private int id;

    private String custormerID;

    private int companyId;

    float delCuValue;

    float niValue;

    float aluValue;

    float messingBrassfix;

    float copperMk;

    float coefficient;

    String name;

    String companyName;

    String createAt;

    String phone1;

    String phone2;

    String fax;

    String telephone;

    String email;

    String tip;

    int type;

    protected Quotation(Parcel in) {
        id = in.readInt();
        custormerID = in.readString();
        companyId = in.readInt();
        delCuValue = in.readFloat();
        niValue = in.readFloat();
        aluValue = in.readFloat();
        messingBrassfix = in.readFloat();
        copperMk = in.readFloat();
        coefficient = in.readFloat();
        createAt = in.readString();
        name = in.readString();
        phone1 = in.readString();
        phone2 = in.readString();
        telephone = in.readString();
        fax = in.readString();
        email = in.readString();
        companyName = in.readString();
        tip = in.readString();
        type = in.readInt();
    }

    public Quotation(){

    }

    public static final Creator<Quotation> CREATOR = new Creator<Quotation>() {
        @Override
        public Quotation createFromParcel(Parcel in) {
            return new Quotation(in);
        }

        @Override
        public Quotation[] newArray(int size) {
            return new Quotation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(custormerID);
        dest.writeInt(companyId);
        dest.writeFloat(delCuValue);
        dest.writeFloat(niValue);
        dest.writeFloat(aluValue);
        dest.writeFloat(messingBrassfix);
        dest.writeFloat(copperMk);
        dest.writeFloat(coefficient);
        dest.writeString(createAt);
        dest.writeString(name);
        dest.writeString(phone1);
        dest.writeString(phone2);
        dest.writeString(telephone);
        dest.writeString(fax);
        dest.writeString(email);
        dest.writeString(companyName);
        dest.writeString(tip);
        dest.writeInt(type);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustormerID() {
        return custormerID;
    }

    public void setCustormerID(String custormerID) {
        this.custormerID = custormerID;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public float getDelCuValue() {
        return delCuValue;
    }

    public void setDelCuValue(float delCuValue) {
        this.delCuValue = delCuValue;
    }

    public float getNiValue() {
        return niValue;
    }

    public void setNiValue(float niValue) {
        this.niValue = niValue;
    }

    public float getAluValue() {
        return aluValue;
    }

    public void setAluValue(float aluValue) {
        this.aluValue = aluValue;
    }

    public float getMessingBrassfix() {
        return messingBrassfix;
    }

    public void setMessingBrassfix(float messingBrassfix) {
        this.messingBrassfix = messingBrassfix;
    }

    public float getCopperMk() {
        return copperMk;
    }

    public void setCopperMk(float copperMk) {
        this.copperMk = copperMk;
    }

    public float getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(float coefficient) {
        this.coefficient = coefficient;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static Quotation createFromCursor(Cursor cursor){
        Quotation quotation = new Quotation();
        quotation.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.ID))));
        quotation.setCompanyId(cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.COMPANYID))));
        quotation.setNiValue(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.NIVALUE))));
        quotation.setDelCuValue(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.DELCUVALUE))));
        quotation.setAluValue(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.ALUVALUE))));
        quotation.setMessingBrassfix(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.MESSINGBRASSFIX))));
        quotation.setCopperMk(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.COPPERMK))));
        quotation.setCoefficient(cursor.getFloat(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.COEFFICIENT))));
        quotation.setCreateAt(cursor.getString(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.CREATEAT))));
        quotation.setCustormerID(cursor.getString(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.CUSTOMERID))));
        quotation.setName(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.NAME)))+"("+
                cursor.getString(cursor.getColumnIndexOrThrow(DBCompany.getColumn(DBCompany.Columns.NAME)))+")");
        quotation.setCompanyName(cursor.getString(cursor.getColumnIndexOrThrow(DBCompany.getColumn(DBCompany.Columns.NAME))));
        quotation.setPhone1(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.PHONE1))));
        quotation.setPhone2(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.PHONE2))));
        quotation.setTelephone(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.MOBILE_PHONE))));
        quotation.setFax(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.FAX))));
        quotation.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DBCustomer.getColumn(DBCustomer.Columns.EMAIL))));
        quotation.setTip(cursor.getString(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.TIP))));
        quotation.setType(cursor.getInt(cursor.getColumnIndexOrThrow(DBQuotation.getColumn(DBQuotation.Columns.TYPE))));
        return quotation;
    }
}
