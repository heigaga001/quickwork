package com.helukable.quickwork.modle;

import android.os.Parcel;
import android.os.Parcelable;

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

    String createAt;

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
}
