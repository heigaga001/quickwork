package com.helukable.quickwork.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.Sheet;

/**
 * Created by zouyong on 2016/3/24.
 */
public class Helper {
    public static int dp2px(Context ctx, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                ctx.getResources().getDisplayMetrics());
    }

    public static String getCellString(Sheet sheet, int row, int column) {
        try{
            String content = sheet.getCell(column, row).getContents();
            if (content == null) {
                content = "";
            }
            return content;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static float getCellFloat(Sheet sheet, int row, int column) {
        String content = getCellString(sheet, row, column);
        if (TextUtils.isEmpty(content)) {
            return 0f;
        }
        float value = 0f;
        try {
            value = Float.parseFloat(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int getCellInt(Sheet sheet, int row, int column) {
        String content = getCellString(sheet, row, column);
        if (TextUtils.isEmpty(content)) {
            return 0;
        }
        int value = 0;
        try {
            value = Integer.parseInt(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");

    public static String getData(String time){
        try{
            long t = Long.parseLong(time);
            return format.format(new Date(t));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getTodayData(){
        try{
            return format2.format(new Date(System.currentTimeMillis()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

}
