package com.helukable.quickwork.modle;

import android.text.TextUtils;

import com.helukable.quickwork.util.Helper;

import jxl.Sheet;

/**
 * Created by zouyong on 2016/3/28.
 */
public class Customer {
    private String id;
    private String name;
    private Company company;
    private String job;
    private String phone1 ;
    private String phone2;
    private String mobile_phone;
    private String fax;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public static Customer createBySheet(Sheet sheet, int row){
        int id = Helper.getCellInt(sheet, row, 1);
        if (id == 0) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        Customer customer = new Customer();

        customer.setCompany(company);
        String customerId = Helper.getCellString(sheet,row,2);
        if(TextUtils.isEmpty(customerId)){
            return null;
        }
        customer.setId(customerId);
        customer.setName(Helper.getCellString(sheet,row,3));
        customer.setJob(Helper.getCellString(sheet,row,4));
        customer.setPhone1(Helper.getCellString(sheet,row,5));
        customer.setPhone2(Helper.getCellString(sheet,row,6));
        customer.setMobile_phone(Helper.getCellString(sheet,row,7));
        customer.setFax(Helper.getCellString(sheet,row,8));
        return customer;
    }

}
