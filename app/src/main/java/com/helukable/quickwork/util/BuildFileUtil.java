package com.helukable.quickwork.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by zouyong on 2016/4/1.
 */
public class BuildFileUtil {
    public static boolean createXLSFile(File file,String[][] data){
        if(data==null||data.length==0){
            return false;
        }
        WritableWorkbook book;
        try {
            if(file.exists()){
                file.delete();
            }
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("报价", 0);

            sheet.getSettings().setShowGridLines(false);
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 35);
            sheet.setColumnView(2, 35);
            sheet.setColumnView(3, 15);
            sheet.setColumnView(4, 15);
            sheet.setColumnView(5, 15);
            String[] header = data[0];
            Label tempLabel = null;
            // 循环写入表头内容
            for (int i = 0; i < header.length; i++) {

                tempLabel = new Label(i, 0, header[i],
                        getHeaderCellStyle());
                sheet.addCell(tempLabel);
            }
            for(int i=1;i<data.length;i++){
                String[] body = data[i];
                for (int j=0;j<body.length;j++){
                    tempLabel = new Label(j, i, body[j],
                            getBodyCellStyle());
                    sheet.addCell(tempLabel);
                }
            }
            book.write();
            book.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static WritableCellFormat getHeaderCellStyle() {
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
                WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat headerFormat = new WritableCellFormat(
                NumberFormats.TEXT);
        try {
            // 添加字体设置
            headerFormat.setFont(font);
            // 设置单元格背景色：表头为黄色
            headerFormat.setBackground(Colour.LIGHT_BLUE);
            // 设置表头表格边框样式
            // 整个表格线为粗线、黑色
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);
            // 表头内容水平居中显示
            headerFormat.setAlignment(Alignment.CENTRE);
            headerFormat.setShrinkToFit(false);
            headerFormat.setWrap(true);
        } catch (WriteException e) {
            System.out.println("表头单元格样式设置失败！");
        }
        return headerFormat;
    }

    private static WritableCellFormat getBodyCellStyle() {
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10,
                WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat headerFormat = new WritableCellFormat(
                NumberFormats.TEXT);
        try {
            // 添加字体设置
            headerFormat.setFont(font);
            // 设置单元格背景色：表头为黄色
            headerFormat.setBackground(Colour.WHITE);
            // 设置表头表格边框样式
            // 整个表格线为粗线、黑色
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN,
                    Colour.BLACK);
            // 表头内容水平居中显示
            headerFormat.setAlignment(Alignment.CENTRE);
        } catch (WriteException e) {
            System.out.println("表头单元格样式设置失败！");
        }
        return headerFormat;
    }

    public static void createImageFile(File file,String [][] data){
        Bitmap bitmap = Bitmap.createBitmap(100,200, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.RED);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {

        }
    }
}
