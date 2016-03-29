package com.helukable.quickwork.modle;

import android.content.Context;

import com.helukable.quickwork.util.Helper;

import jxl.Sheet;

/**
 * Created by zouyong on 2016/3/28.
 */
public class Company {
    private int id;
    private String name;
    private String type;
    private String city;
    private String county;
    private String address;
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static Company createBySheet(Sheet sheet, int row){
        int id = Helper.getCellInt(sheet, row, 1);
        if (id == 0) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        company.setName(Helper.getCellString(sheet,row,0));
        company.setRemarks(Helper.getCellString(sheet,row,2));
        company.setType(Helper.getCellString(sheet,row,3));
        company.setCity(Helper.getCellString(sheet,row,4));
        company.setCounty(Helper.getCellString(sheet,row,5));
        company.setAddress(Helper.getCellString(sheet,row,6));
        return company;
    }
}
