package com.helukable.quickwork.modle;

import android.content.Context;

import com.helukable.quickwork.util.ShareUtil;

/**
 * Created by zouyong on 2016/3/25.
 */
public class Variable {
    public static final String SDELCUVALUE = "delCuValue";
    public static final String SNIVALUE = "niValue";
    public static final String SALUVALUE = "aluValue";
    public static final String SMESSINGBRASSFIX = "messingBrassfix";
    public static final String SCOPPERMK = "copperMk";
    float delCuValue;
    float niValue;
    float aluValue;
    float messingBrassfix;
    float copperMk;
    private static Variable instance;

    public static Variable getInstance(Context context){
        if(instance == null){
            instance = new Variable(context);
        }
        return instance;
    }

    private Variable(Context context){
        delCuValue = ShareUtil.read(context,SDELCUVALUE,429.00f);
        niValue = ShareUtil.read(context,SNIVALUE,1655.99f);
        aluValue = ShareUtil.read(context,SALUVALUE,250.00f);
        messingBrassfix = ShareUtil.read(context,SMESSINGBRASSFIX,280f);
        copperMk = ShareUtil.read(context,SCOPPERMK,750f);
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
}
